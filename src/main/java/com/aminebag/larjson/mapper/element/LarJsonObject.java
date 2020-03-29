package com.aminebag.larjson.mapper.element;

import com.aminebag.larjson.api.LarJsonPath;
import com.aminebag.larjson.mapper.propertymapper.ObjectLarJsonPropertyMapper;
import com.aminebag.larjson.mapper.valueoverwriter.ValueOverwriter;

/**
 * @author Amine Bagdouri
 *
 * A non-root implementation of a typed LarJson object
 */
public class LarJsonObject extends AbstractLarJsonObject {

    private final LarJsonContext context;
    private final long blueprintPosition;
    private final LarJsonPath parentPath;
    private final String pathElement;

    public LarJsonObject(ObjectLarJsonPropertyMapper<?> mapper, long[] attributes, long jsonPosition,
                         long blueprintPosition, LarJsonContext context, LarJsonPath parentPath, String pathElement) {
        super(mapper, attributes, jsonPosition);
        this.context = context;
        this.blueprintPosition = blueprintPosition;
        this.parentPath = parentPath;
        this.pathElement = pathElement;
    }

    @Override
    protected long getBlueprintPosition() {
        return blueprintPosition;
    }

    @Override
    protected ValueOverwriter getValueOverwriter() {
        return context.getValueOverwriter();
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
