package com.aminebag.larjson.mapper.valueoverwriter;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReferenceArray;

class ConcurrentValueOverwriter extends AbstractValueOverwriter {

    public ConcurrentValueOverwriter() {
        super(new ConcurrentHashMap<>());
    }

    @Override
    public void overwritePropertyValue(long jsonPosition, int setterIndex, int setterCount, Object value){
        if(value == null) {
            value = NULL;
        }
        AtomicReferenceArray<Object> array = (AtomicReferenceArray<Object>) overwrittenValues
                .computeIfAbsent(jsonPosition, (k) -> new AtomicReferenceArray<>(setterCount));
        array.set(setterIndex, value);
    }

    @Override
    public Object getOverwrittenPropertyValue(long jsonPosition, int setterIndex){
        AtomicReferenceArray<Object> mapperValues = (AtomicReferenceArray<Object>) overwrittenValues.get(jsonPosition);
        if(mapperValues != null) {
            Object value = mapperValues.get(setterIndex);
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
        return (List<?>) overwrittenValues
                .computeIfAbsent(jsonPosition, (k) -> new CopyOnWriteArrayList<>(list));
    }

    @Override
    public ValueOverwriter conceive() {
        return new ConcurrentValueOverwriter();
    }
}
