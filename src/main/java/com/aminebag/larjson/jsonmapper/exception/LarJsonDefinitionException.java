package com.aminebag.larjson.jsonmapper.exception;

import com.aminebag.larjson.exception.LarJsonRuntimeException;

public class LarJsonDefinitionException extends LarJsonRuntimeException {
    public LarJsonDefinitionException() {
    }

    public LarJsonDefinitionException(String message) {
        super(message);
    }

    public LarJsonDefinitionException(String message, Throwable cause) {
        super(message, cause);
    }

    public LarJsonDefinitionException(Throwable cause) {
        super(cause);
    }
}
