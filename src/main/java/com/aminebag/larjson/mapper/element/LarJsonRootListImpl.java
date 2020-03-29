package com.aminebag.larjson.mapper.element;

import com.aminebag.larjson.api.LarJsonPath;
import com.aminebag.larjson.api.LarJsonRootList;
import com.aminebag.larjson.blueprint.LarJsonBlueprintReader;
import com.aminebag.larjson.configuration.LarJsonTypedReadConfiguration;
import com.aminebag.larjson.configuration.PropertyResolver;
import com.aminebag.larjson.exception.LarJsonException;
import com.aminebag.larjson.mapper.cache.LarJsonCache;
import com.aminebag.larjson.mapper.cache.LarJsonValueCalculator;
import com.aminebag.larjson.mapper.propertymapper.LarJsonPropertyMapper;
import com.aminebag.larjson.mapper.valueoverwriter.ValueOverwriter;
import com.aminebag.larjson.resource.ResourcePool;
import com.aminebag.larjson.resource.SafeResourceCloser;
import com.aminebag.larjson.stream.ChannelCharacterStreamPool;
import com.aminebag.larjson.stream.CharacterStream;
import com.aminebag.larjson.utils.LongList;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Amine Bagdouri
 *
 * A root implementation of a typed LarJson array
 */
public class LarJsonRootListImpl<T> extends AbstractLarJsonList<T> implements LarJsonContext, LarJsonRootList<T> {

    private final LarJsonTypedReadConfiguration configuration;
    private final ResourcePool<? extends LarJsonBlueprintReader> blueprintReaderPool;
    private final ChannelCharacterStreamPool characterStreamPool;
    private final LarJsonCache larJsonCache;
    private final ValueOverwriter valueOverwriter;
    private final AtomicBoolean closed = new AtomicBoolean(false);
    private final PropertyResolver propertyResolver;
    private final Class<T> rootInterface;

    public LarJsonRootListImpl(LarJsonPropertyMapper<?> mapper, long jsonPosition, long blueprintPosition,
                               LongList keys, LarJsonTypedReadConfiguration configuration,
                               ResourcePool<? extends LarJsonBlueprintReader> blueprintReaderPool,
                               ChannelCharacterStreamPool characterStreamPool, PropertyResolver propertyResolver, Class<T> rootInterface) {
        super(mapper, jsonPosition, blueprintPosition, keys);
        this.configuration = configuration;
        this.blueprintReaderPool = blueprintReaderPool;
        this.characterStreamPool = characterStreamPool;
        this.larJsonCache = LarJsonCache.get(configuration.getCacheSize());
        this.valueOverwriter = ValueOverwriter.get(configuration.isMutable(), configuration.isThreadSafe());
        this.propertyResolver = propertyResolver;
        this.rootInterface = rootInterface;
    }

    @Override
    protected LarJsonContext getContext() {
        return this;
    }

    @Override
    public LarJsonTypedReadConfiguration getReadConfiguration() {
        return configuration;
    }

    @Override
    public LarJsonBlueprintReader getBlueprintReader() throws IOException {
        return blueprintReaderPool.get();
    }

    @Override
    public CharacterStream getCharacterStream(long jsonPosition) throws IOException {
        return characterStreamPool.getCharacterStream(jsonPosition);
    }

    @Override
    public <T> T getCachedValue(long jsonPosition, LarJsonValueCalculator<T> valueCalculator)
            throws IOException, LarJsonException {
        return larJsonCache.get(jsonPosition, valueCalculator);
    }

    @Override
    public ValueOverwriter getValueOverwriter() {
        return valueOverwriter;
    }

    @Override
    public PropertyResolver getPropertyResolver() {
        return propertyResolver;
    }

    @Override
    public void checkClosed() {
        if(closed.get()) {
            throw new IllegalStateException("The root array has been closed");
        }
    }

    @Override
    public Class<?> getRootInterface() {
        return rootInterface;
    }

    @Override
    String getPathElement() {
        return "$";
    }

    @Override
    public void close() throws IOException {
        if(closed.compareAndSet(false, true)) {
            new SafeResourceCloser()
                    .add(blueprintReaderPool)
                    .add(characterStreamPool)
                    .close();
        }
    }

    @Override
    public LarJsonPath getParentLarJsonPath() {
        getContext().checkClosed();
        return null;
    }
}
