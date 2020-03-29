package com.aminebag.larjson.mapper.propertymapper;

import com.aminebag.larjson.api.LarJsonCloneable;
import com.aminebag.larjson.api.LarJsonPath;
import com.aminebag.larjson.blueprint.LarJsonBlueprintReader;
import com.aminebag.larjson.blueprint.LarJsonBlueprintWriter;
import com.aminebag.larjson.configuration.LarJsonTypedReadConfiguration;
import com.aminebag.larjson.configuration.LarJsonTypedWriteConfiguration;
import com.aminebag.larjson.configuration.PropertyResolver;
import com.aminebag.larjson.exception.LarJsonException;
import com.aminebag.larjson.mapper.LarJsonMapperUtils;
import com.aminebag.larjson.mapper.element.LarJsonContext;
import com.aminebag.larjson.mapper.element.LarJsonObject;
import com.aminebag.larjson.mapper.element.LarJsonRootListImpl;
import com.aminebag.larjson.mapper.element.LarJsonRootObject;
import com.aminebag.larjson.mapper.exception.LarJsonConstraintViolationException;
import com.aminebag.larjson.mapper.exception.LarJsonUnknownAttributeException;
import com.aminebag.larjson.mapper.valueoverwriter.ValueOverwriter;
import com.aminebag.larjson.parser.LarJsonParseException;
import com.aminebag.larjson.parser.LarJsonToken;
import com.aminebag.larjson.parser.LarJsonTokenParser;
import com.aminebag.larjson.resource.ResourcePool;
import com.aminebag.larjson.stream.ChannelCharacterStreamPool;
import com.aminebag.larjson.utils.MutableLongList;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Amine Bagdouri
 *
 * A mapper for model objects model properties
 */
public class ObjectLarJsonPropertyMapper<T> extends LarJsonPropertyMapper<T> {

    private final long[] nullArray;
    private final Class<T> type;
    private final Map<Method, LarJsonPropertyMapper<?>> propertyMapperByGetter;
    private final Map<Method, LarJsonPropertyMapper<?>> propertyMapperBySetter;
    private final LarJsonPropertyMapper<?>[] propertyMappersGetterArray;

    public ObjectLarJsonPropertyMapper(String name, Method getterMethod, Method setterMethod, int getterIndex,
                                       int setterIndex, boolean required, Class<T> type,
                                       Map<Method, LarJsonPropertyMapper<?>> propertyMapperByGetter,
                                       Map<Method, LarJsonPropertyMapper<?>> propertyMapperBySetter) {
        super(name, getterMethod, setterMethod, getterIndex, setterIndex, required);
        this.type = type;
        this.propertyMapperByGetter = propertyMapperByGetter;
        this.nullArray = new long[propertyMapperByGetter.size()];
        this.propertyMapperBySetter = propertyMapperBySetter;
        Arrays.fill(this.nullArray, -1);
        this.propertyMappersGetterArray = new LarJsonPropertyMapper[propertyMapperByGetter.size()];
        for(LarJsonPropertyMapper<?> mapper : propertyMapperByGetter.values()) {
            this.propertyMappersGetterArray[mapper.getGetterIndex()] = mapper;
        }
    }

    @Override
    public T calculateValue(LarJsonContext context, long key, long parentJsonPosition, long parentBlueprintPosition,
                            LarJsonPath parentPath , String pathElement) throws IOException, LarJsonException {
        LarJsonBlueprintReader blueprintReader = context.getBlueprintReader();
        blueprintReader.position(parentBlueprintPosition + key);
        long jsonPosition = parentJsonPosition + blueprintReader.get();
        return context.getCachedValue(jsonPosition,
                ()->calculateCacheableValue(context, jsonPosition, parentPath, pathElement));
    }

    private T calculateCacheableValue(
            LarJsonContext context, long jsonPosition, LarJsonPath parentPath, String pathElement)
            throws IOException {

        LarJsonBlueprintReader blueprintReader = context.getBlueprintReader();
        long blueprintLength = blueprintReader.get();
        long blueprintPosition = blueprintReader.position() - blueprintLength;

        long[] getters = new long[propertyMapperByGetter.size()];
        System.arraycopy(nullArray, 0, getters, 0, getters.length);

        int valueCount = (int) blueprintReader.get();
        for(int i=0; i<valueCount; i++) {
            int getterIndex = (int) blueprintReader.get();
            long subKey = blueprintReader.get();
            getters[getterIndex] = subKey;
        }
        return (T) Proxy.newProxyInstance(
                type.getClassLoader(),
                LarJsonMapperUtils.getProxiedInterfaces(type),
                new LarJsonObject(
                        this, getters, jsonPosition, blueprintPosition, context, parentPath, pathElement));
    }

    @Override
    public void write(
            LarJsonContext context, long key, long parentJsonPosition, long parentBlueprintPosition,
            JsonWriter jsonWriter, ValueOverwriter valueOverwriter, LarJsonTypedWriteConfiguration writeConfiguration,
            PropertyResolver propertyResolver)
            throws IOException, LarJsonException {

        jsonWriter.beginObject();
        LarJsonBlueprintReader blueprintReader = context.getBlueprintReader();
        blueprintReader.position(parentBlueprintPosition + key);
        long jsonPosition = parentJsonPosition + blueprintReader.get();
        long blueprintLength = blueprintReader.get();
        long blueprintPosition = blueprintReader.position() - blueprintLength;

        int keyCount = (int) blueprintReader.get();
        for(int i=0; i<keyCount; i++) {
            int getterIndex = (int) blueprintReader.get();
            long subKey = blueprintReader.get();
            LarJsonPropertyMapper<?> subMapper = propertyMappersGetterArray[getterIndex];
            if(LarJsonMapperUtils.isPropertyIgnored(
                    subMapper.getGetterMethod(), writeConfiguration.getPropertyConfigurationFactory(),
                    propertyResolver,
                    writeConfiguration.getAnnotationConfigurationFactory())) {
                continue;
            }
            long position = blueprintReader.position();
            jsonWriter.name(LarJsonMapperUtils.getPropertyName(
                    subMapper.getGetterMethod(), propertyResolver,
                    writeConfiguration.getPropertyConfigurationFactory(),
                    writeConfiguration.getAnnotationConfigurationFactory()));
            if(subMapper.hasSetter()) {
                Object value = valueOverwriter.getOverwrittenPropertyValue(jsonPosition, subMapper.getSetterIndex());
                if(valueOverwriter.isPropertyValueOverwritten(value)) {
                    LarJsonMapperUtils.writeValue(jsonWriter, value, writeConfiguration);
                    continue;
                }
            }
            subMapper.write(context, subKey, jsonPosition, blueprintPosition, jsonWriter, valueOverwriter,
                    writeConfiguration, propertyResolver);
            blueprintReader.position(position);
        }
        jsonWriter.endObject();
    }

    public void deepClone(
            LarJsonContext context, long key, long parentJsonPosition, long parentBlueprintPosition,
            ValueOverwriter sourceValueOverwriter, ValueOverwriter destValueOverwriter) throws IOException {

        if(key < 0L) {
            return;
        }
        LarJsonBlueprintReader blueprintReader = context.getBlueprintReader();
        blueprintReader.position(parentBlueprintPosition + key);
        long jsonPosition = parentJsonPosition + blueprintReader.get();
        long blueprintLength = blueprintReader.get();
        long blueprintPosition = blueprintReader.position() - blueprintLength;

        int keyCount = (int) blueprintReader.get();
        for(int i=0; i<keyCount; i++) {
            int getterIndex = (int) blueprintReader.get();
            long subKey = blueprintReader.get();
            LarJsonPropertyMapper<?> subMapper = propertyMappersGetterArray[getterIndex];
            long position = blueprintReader.position();
            cloneProperty(context, sourceValueOverwriter, destValueOverwriter, jsonPosition, blueprintPosition,
                    subKey, subMapper);
            blueprintReader.position(position);
        }
    }

    @Override
    public T nullValue() {
        return null;
    }

    public void cloneProperty(
            LarJsonContext context, ValueOverwriter sourceValueOverwriter, ValueOverwriter destValueOverwriter,
            long jsonPosition, long blueprintPosition, long subKey, LarJsonPropertyMapper<?> subMapper)
            throws IOException {

        if(!enrichValueOverwriter(subMapper, jsonPosition, sourceValueOverwriter, destValueOverwriter)) {
            subMapper.deepClone(context, subKey, jsonPosition, blueprintPosition, sourceValueOverwriter,
                    destValueOverwriter);
        }
    }

    private boolean enrichValueOverwriter(
            LarJsonPropertyMapper<?> subMapper, long jsonPosition, ValueOverwriter sourceValueOverwriter,
            ValueOverwriter destValueOverwriter) {

        if(subMapper.hasSetter()) {
            int setterIndex = subMapper.getSetterIndex();
            Object overwrittenValue = sourceValueOverwriter.getOverwrittenPropertyValue(jsonPosition, setterIndex);
            if(sourceValueOverwriter.isPropertyValueOverwritten(overwrittenValue)){
                if(overwrittenValue instanceof LarJsonCloneable) {
                    overwrittenValue = ((LarJsonCloneable)overwrittenValue).clone();
                }
                destValueOverwriter.overwritePropertyValue(jsonPosition, setterIndex,
                        getPropertyMapperBySetter().size(), overwrittenValue);
                return true;
            }
        }
        return false;
    }

    @Override
    public long enrichBlueprint(
            LarJsonContext context, LarJsonBlueprintWriter blueprintWriter, LarJsonTokenParser tokenParser,
            long parentJsonPosition, long parentBlueprintPosition) throws IOException, LarJsonException {

        long absoluteBlueprintPosition = blueprintWriter.position();
        LarJsonToken token = tokenParser.peek();
        if(token == LarJsonToken.BEGIN_OBJECT) {
            long absoluteJsonPosition = tokenParser.getCurrentPosition();
            tokenParser.beginObject();
            long relativeJsonPosition = absoluteJsonPosition - parentJsonPosition;
            long[] keys = new long[propertyMapperByGetter.size()];
            System.arraycopy(nullArray, 0, keys, 0, keys.length);
            processAttributes(context, blueprintWriter, tokenParser, absoluteBlueprintPosition, absoluteJsonPosition,
                    keys);
            int keyCount = 0;
            for(int i=0; i<keys.length ; i++) {
                long key = keys[i];
                if(key >= 0) {
                    blueprintWriter.put(key);
                    blueprintWriter.put(i);
                    keyCount++;
                }
            }
            blueprintWriter.put(keyCount);
            long blueprintEnd = blueprintWriter.position();
            blueprintWriter.put(blueprintEnd - absoluteBlueprintPosition);
            blueprintWriter.put(relativeJsonPosition);
            return blueprintWriter.position() - parentBlueprintPosition;
        } else {
            throw new LarJsonParseException(
                    String.format("Expected object, but found %s at byte position %d",
                            token.name(), tokenParser.getCurrentPosition()));
        }
    }

    public LarJsonRootObject buildRootObject(
            LarJsonTypedReadConfiguration configuration, LarJsonTokenParser tokenParser,
            LarJsonBlueprintWriter blueprintWriter, ChannelCharacterStreamPool characterStreamPool,
            PropertyResolver propertyResolver, Class<?> rootInterface)
            throws IOException, LarJsonException {

        LarJsonToken token = tokenParser.peek();
        if(token == LarJsonToken.BEGIN_OBJECT) {
            long absoluteJsonPosition = tokenParser.getCurrentPosition();
            tokenParser.beginObject();
            long absoluteBlueprintPosition = blueprintWriter.position();
            long[] keys = new long[propertyMapperByGetter.size()];
            System.arraycopy(nullArray, 0, keys, 0, keys.length);
            LarJsonRootObject root = new LarJsonRootObject(this, keys, absoluteJsonPosition,
                    configuration, new ResourcePool<>(blueprintWriter.getReaderFactory()), characterStreamPool,
                    propertyResolver, rootInterface);
            processAttributes(root, blueprintWriter, tokenParser, absoluteBlueprintPosition, absoluteJsonPosition,
                    keys);
            return root;
        } else {
            throw new LarJsonParseException(
                    String.format("Expected object, but found %s at byte position %d",
                            token.name(), tokenParser.getCurrentPosition()));
        }
    }

    public LarJsonRootListImpl<T> buildRootList(
            LarJsonTypedReadConfiguration configuration, LarJsonTokenParser tokenParser,
            LarJsonBlueprintWriter blueprintWriter, ChannelCharacterStreamPool characterStreamPool,
            PropertyResolver propertyResolver, Class<T> rootInterface)
            throws IOException, LarJsonException {

        LarJsonToken token = tokenParser.peek();
        if(token == LarJsonToken.BEGIN_ARRAY) {
            long absoluteJsonPosition = tokenParser.getCurrentPosition();
            tokenParser.beginArray();
            long absoluteBlueprintPosition = blueprintWriter.position();
            MutableLongList keys = new MutableLongList();
            LarJsonRootListImpl<T> root = new LarJsonRootListImpl<T>(this, absoluteJsonPosition,
                    absoluteBlueprintPosition, keys, configuration,
                    new ResourcePool<>(blueprintWriter.getReaderFactory()), characterStreamPool, propertyResolver,
                    rootInterface);
            while (tokenParser.peek() != LarJsonToken.END_ARRAY) {
                if(tokenParser.peek() == LarJsonToken.NULL) {
                    tokenParser.nextNull();
                    keys.add(0L);
                    continue;
                }
                long key = enrichBlueprint(root, blueprintWriter, tokenParser, absoluteJsonPosition,
                        absoluteBlueprintPosition) + 1;
                keys.add(key);
            }
            tokenParser.endArray();
            return root;
        } else {
            throw new LarJsonParseException(
                    String.format("Expected array, but found %s at byte position %d",
                            token.name(), tokenParser.getCurrentPosition()));
        }
    }

    private void processAttributes(
            LarJsonContext context, LarJsonBlueprintWriter blueprintWriter, LarJsonTokenParser tokenParser,
            long absoluteBlueprintPosition, long absoluteJsonPosition, long[] keys)
            throws IOException, LarJsonException {

        LarJsonPropertyMapperMethodSet candidates = new LarJsonPropertyMapperMethodSet(propertyMapperByGetter.values());
        while (tokenParser.peek() != LarJsonToken.END_OBJECT) {
            String name = tokenParser.nextName();
            processAttribute(tokenParser, blueprintWriter, absoluteBlueprintPosition, absoluteJsonPosition, keys,
                    context, candidates, name);
        }
        if (!candidates.isEmpty()) {
            Iterator<LarJsonPropertyMapper<?>> mapperIterator = candidates.mapperIterator();
            while (mapperIterator.hasNext()) {
                LarJsonPropertyMapper<?> mapper = mapperIterator.next();
                if (mapper.isRequired()) {
                    throw new LarJsonConstraintViolationException("Required property " + mapper.getName() +
                            " is missing at path " + tokenParser.getPath() + " at byte position " +
                            tokenParser.getCurrentPosition());
                }
            }
        }
        tokenParser.endObject();
    }

    private void processAttribute(LarJsonTokenParser tokenParser, LarJsonBlueprintWriter blueprintWriter,
                                  long absoluteBlueprintPosition, long absoluteJsonPosition, long[] keys,
                                  LarJsonContext context, LarJsonPropertyMapperMethodSet candidates, String name)
            throws IOException, LarJsonException {

        Method getter = context.getPropertyResolver().findGetter(name, candidates);
        if (getter != null) {
            LarJsonPropertyMapper mapper = propertyMapperByGetter.get(getter);
            candidates.removeMapper(mapper);
            if (tokenParser.peek() == LarJsonToken.NULL) {
                tokenParser.nextNull();
            } else {
                long key = mapper.enrichBlueprint(context, blueprintWriter, tokenParser,
                        absoluteJsonPosition, absoluteBlueprintPosition);
                keys[mapper.getGetterIndex()] = key;
            }
        } else if (!LarJsonMapperUtils.isUnknownAttributeAllowed(type, context.getReadConfiguration())) {
            throw new LarJsonUnknownAttributeException("JSON Attribute '" + name + "' is not recognized");
        } else {
            tokenParser.skipValue();
        }
    }

    public Map<Method, LarJsonPropertyMapper<?>> getPropertyMapperByGetter() {
        return propertyMapperByGetter;
    }

    public Map<Method, LarJsonPropertyMapper<?>> getPropertyMapperBySetter() {
        return propertyMapperBySetter;
    }

    public Class<T> getType() {
        return type;
    }
}
