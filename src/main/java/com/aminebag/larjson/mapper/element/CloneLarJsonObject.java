package com.aminebag.larjson.mapper.element;

import com.aminebag.larjson.api.LarJsonPath;
import com.aminebag.larjson.mapper.propertymapper.LarJsonPropertyMapper;
import com.aminebag.larjson.mapper.propertymapper.ObjectLarJsonPropertyMapper;
import com.aminebag.larjson.mapper.valueoverwriter.ValueOverwriter;

import java.io.IOException;

/**
 * @author Amine Bagdouri
 *
 * A clone of a LarJson model object
 */
class CloneLarJsonObject extends AbstractLarJsonObject {
    private final ValueOverwriter valueOverwriter;
    private final LarJsonContext context;
    private final long blueprintPosition;
    private final LarJsonPath parentPath;
    private final String pathElement;

    CloneLarJsonObject(ObjectLarJsonPropertyMapper<?> mapper, long[] attributes, long jsonPosition,
                       LarJsonContext context, long blueprintPosition, ValueOverwriter sourceValueOverwriter,
                       LarJsonPath parentPath, String pathElement)
            throws IOException {
        super(mapper, attributes, jsonPosition);
        this.valueOverwriter = sourceValueOverwriter.conceive();
        this.context = context;
        this.blueprintPosition = blueprintPosition;
        this.parentPath = parentPath;
        this.pathElement = pathElement;
        if(!sourceValueOverwriter.isEmpty()) {
            for(LarJsonPropertyMapper<?> subMapper : mapper.getPropertyMapperByGetter().values()) {
                mapper.cloneProperty(context, sourceValueOverwriter, this.valueOverwriter, jsonPosition,
                        blueprintPosition, attributes[subMapper.getGetterIndex()], subMapper);
            }
        }
    }

    @Override
    protected long getBlueprintPosition() throws IOException {
        return blueprintPosition;
    }

    @Override
    protected ValueOverwriter getValueOverwriter() {
        return valueOverwriter;
    }

    @Override
    protected LarJsonContext getContext() {
        return context;
    }

    @Override
    public LarJsonPath getParentLarJsonPath() {
        return parentPath;
    }

    @Override
    String getPathElement() {
        return pathElement;
    }
}
