package com.aminebag.larjson.resource;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Amine Bagdouri
 */
public class MultipleResourcesIOException extends IOException {

    private final List<IOException> ioExceptions;
    private final List<RuntimeException> runtimeExceptions;

    public MultipleResourcesIOException(List<IOException> ioExceptions, List<RuntimeException> runtimeExceptions) {
        super(Stream.concat(ioExceptions.stream(), runtimeExceptions.stream())
                        .map(Exception::toString)
                        .collect(Collectors.joining(", ")),
                ioExceptions.get(0));
        this.ioExceptions = Collections.unmodifiableList(ioExceptions);
        this.runtimeExceptions = runtimeExceptions;
    }

    public List<IOException> getIOExceptions() {
        return ioExceptions;
    }

    public List<RuntimeException> getRuntimeExceptions() {
        return runtimeExceptions;
    }
}
