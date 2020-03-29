package com.aminebag.larjson.mapper.element;

import com.aminebag.larjson.api.LarJsonPath;
import com.aminebag.larjson.mapper.propertymapper.LarJsonPropertyMapper;
import com.aminebag.larjson.mapper.valueoverwriter.ValueOverwriter;
import com.aminebag.larjson.utils.LongList;

/**
 * @author Amine Bagdouri
 *
 * A non-root implementation of a typed LarJson array
 */
public class LarJsonListImpl<T> extends AbstractLarJsonList<T> {

    private final LarJsonContext context;
    private final LarJsonPath parentPath;
    private final String pathElement;

    public LarJsonListImpl(LarJsonPropertyMapper<?> mapper, long jsonPosition, long blueprintPosition, LongList keys,
                           LarJsonContext context, LarJsonPath parentPath, String pathElement) {
        super(mapper, jsonPosition, blueprintPosition, keys);
        this.context = context;
        this.parentPath = parentPath;
        this.pathElement = pathElement;
    }

    @Override
    protected LarJsonContext getContext() {
        return context;
    }

    @Override
    protected ValueOverwriter getValueOverwriter() {
        return context.getValueOverwriter();
    }

    @Override
    String getPathElement() {
        return pathElement;
    }

    @Override
    public LarJsonPath getParentLarJsonPath() {
        getContext().checkClosed();
        return parentPath;
    }
}
