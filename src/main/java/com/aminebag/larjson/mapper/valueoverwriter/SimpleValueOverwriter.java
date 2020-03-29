package com.aminebag.larjson.mapper.valueoverwriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class SimpleValueOverwriter extends AbstractValueOverwriter {

    public SimpleValueOverwriter() {
        super(new HashMap<>());
    }

    @Override
    public void overwritePropertyValue(long jsonPosition, int setterIndex, int setterCount, Object value){
        if(value == null) {
            value = NULL;
        }
        Object[] array = (Object[]) overwrittenValues.get(jsonPosition);
        if(array == null) {
            array = new Object[setterCount];
            overwrittenValues.put(jsonPosition, array);
        }
        array[setterIndex] = value;
    }

    @Override
    public Object getOverwrittenPropertyValue(long jsonPosition, int setterIndex){
        Object[] mapperValues = (Object[]) overwrittenValues.get(jsonPosition);
        if(mapperValues != null) {
            Object value = mapperValues[setterIndex];
            if(value == NULL) {
                return null;
            } else if(value != null) {
                return value;
            }
        }
        return PROPERTY_NOT_OVERWRITTEN;
    }

    @Override
    public final List<?> putListIfAbsent(long jsonPosition, List<?> list) {
        List<?> l = (List<?>) overwrittenValues.get(jsonPosition);
        if(l == null) {
            l = new ArrayList<>(list);
            overwrittenValues.put(jsonPosition, l);
        }
        return l;
    }

    @Override
    public ValueOverwriter conceive() {
        return new SimpleValueOverwriter();
    }
}
