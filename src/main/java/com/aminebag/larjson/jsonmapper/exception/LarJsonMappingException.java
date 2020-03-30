package com.aminebag.larjson.jsonmapper.exception;

import com.aminebag.larjson.exception.LarJsonRuntimeException;

public class LarJsonMappingException extends LarJsonRuntimeException {
    public LarJsonMappingException() {
    }

    public LarJsonMappingException(String message) {
        super(message);
    }

    public LarJsonMappingException(String message, Throwable cause) {
        super(message, cause);
    }

    public LarJsonMappingException(Throwable cause) {
        super(cause);
    }
}
