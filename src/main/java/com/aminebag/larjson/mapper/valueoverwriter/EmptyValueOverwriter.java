package com.aminebag.larjson.mapper.valueoverwriter;

import java.util.List;

class EmptyValueOverwriter implements ValueOverwriter{

    static final EmptyValueOverwriter INSTANCE = new EmptyValueOverwriter();

    private EmptyValueOverwriter(){

    }

    @Override
    public void overwritePropertyValue(long jsonPosition, int setterIndex, int setterCount, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getOverwrittenPropertyValue(long jsonPosition, int setterIndex) {
        return null;
    }

    @Override
    public boolean isPropertyValueOverwritten(Object value) {
        return false;
    }

    @Override
    public List<?> putListIfAbsent(long jsonPosition, List<?> list) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<?> getListOrDefault(long jsonPosition, List<?> list) {
        return list;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public ValueOverwriter conceive() {
        return this;
    }
}
