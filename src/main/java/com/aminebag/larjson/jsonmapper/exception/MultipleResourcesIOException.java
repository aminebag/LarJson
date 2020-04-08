package com.aminebag.larjson.jsonmapper.exception;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MultipleResourcesIOException extends IOException {

    private final List<IOException> exceptions;

    public MultipleResourcesIOException(List<IOException> exceptions) {
        super(exceptions.stream().map(IOException::toString).collect(Collectors.joining(", ")),
                exceptions.get(0));
        this.exceptions = Collections.unmodifiableList(exceptions);
    }

    public List<IOException> getExceptions() {
        return exceptions;
    }
}
