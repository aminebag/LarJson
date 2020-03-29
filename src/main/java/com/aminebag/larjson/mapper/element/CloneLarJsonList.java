package com.aminebag.larjson.mapper.element;

import com.aminebag.larjson.api.LarJsonPath;
import com.aminebag.larjson.mapper.propertymapper.LarJsonPropertyMapper;
import com.aminebag.larjson.mapper.propertymapper.ListLarJsonPropertyMapper;
import com.aminebag.larjson.mapper.valueoverwriter.ValueOverwriter;
import com.aminebag.larjson.utils.LongList;

import java.io.IOException;
import java.util.List;

/**
 * @author Amine Bagdouri
 *
 * A clone of a LarJson array
 */
public class CloneLarJsonList<T> extends AbstractLarJsonList<T> {
    private final ValueOverwriter valueOverwriter;
    private final LarJsonContext context;
    private final LarJsonPath parentPath;
    private final String pathElement;

    public CloneLarJsonList(LarJsonPropertyMapper<?> mapper, long jsonPosition, long blueprintPosition, LongList keys,
                            ValueOverwriter sourceValueOverwriter, LarJsonContext context, LarJsonPath parentPath,
                            String pathElement) throws IOException {
        super(mapper, jsonPosition, blueprintPosition, keys);
        this.valueOverwriter = sourceValueOverwriter.conceive();
        this.context = context;
        this.parentPath = parentPath;
        this.pathElement = pathElement;
        if(!sourceValueOverwriter.isEmpty()) {
            List<?> overwrittenList = sourceValueOverwriter.getListOrDefault(jsonPosition, null);
            if(overwrittenList != null) {
                ListLarJsonPropertyMapper.deepCloneOverwrittenList(this.valueOverwriter, jsonPosition, overwrittenList);
            } else {
                ListLarJsonPropertyMapper.deepCloneUnmodifiedList(context, sourceValueOverwriter, this.valueOverwriter,
                        jsonPosition, mapper, blueprintPosition, keys);
            }
        }
    }

    @Override
    protected LarJsonContext getContext() {
        return context;
    }

    @Override
    protected ValueOverwriter getValueOverwriter() {
        return valueOverwriter;
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
