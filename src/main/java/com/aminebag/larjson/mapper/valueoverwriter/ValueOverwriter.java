package com.aminebag.larjson.mapper.valueoverwriter;

import java.util.List;

public interface ValueOverwriter {

    void overwritePropertyValue(long jsonPosition, int setterIndex, int setterCount, Object value);

    Object getOverwrittenPropertyValue(long jsonPosition, int setterIndex);

    boolean isPropertyValueOverwritten(Object value);

    List<?> putListIfAbsent(long jsonPosition, List<?> list);

    List<?> getListOrDefault(long jsonPosition, List<?> list);

    boolean isEmpty();

    ValueOverwriter conceive();

    static ValueOverwriter get(boolean mutable, boolean threadSafe) {
        return mutable ? (threadSafe ? new ConcurrentValueOverwriter() : new SimpleValueOverwriter()) :
                EmptyValueOverwriter.INSTANCE;
    }
}
