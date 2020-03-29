package com.aminebag.larjson.mapper.valueoverwriter;

import java.util.List;
import java.util.Map;

abstract class AbstractValueOverwriter implements ValueOverwriter {
    protected final Object PROPERTY_NOT_OVERWRITTEN = new Object();
    protected final static Object NULL = new Object();
    protected final Map<Long, Object> overwrittenValues;

    protected AbstractValueOverwriter(Map<Long, Object> overwrittenValues) {
        this.overwrittenValues = overwrittenValues;
    }

    @Override
    public final boolean isPropertyValueOverwritten(Object value) {
        return value != PROPERTY_NOT_OVERWRITTEN;
    }

    @Override
    public final List<?> getListOrDefault(long jsonPosition, List<?> list){
        return (List<?>) overwrittenValues.getOrDefault(jsonPosition, list);
    }

    @Override
    public final boolean isEmpty() {
        return overwrittenValues.isEmpty();
    }
}
