package com.aminebag.larjson.resource;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Amine Bagdouri
 */
public class MultipleResourcesRuntimeException extends RuntimeException {

    private final List<RuntimeException> runtimeExceptions;

    public MultipleResourcesRuntimeException(List<RuntimeException> runtimeExceptions) {
        super(runtimeExceptions.stream().map(RuntimeException::toString).collect(Collectors.joining(", ")),
                runtimeExceptions.get(0));
        this.runtimeExceptions = Collections.unmodifiableList(runtimeExceptions);
    }

    public List<RuntimeException> getRuntimeExceptions() {
        return runtimeExceptions;
    }
}
