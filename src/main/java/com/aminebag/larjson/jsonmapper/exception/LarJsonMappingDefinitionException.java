package com.aminebag.larjson.jsonmapper.exception;

import com.aminebag.larjson.exception.LarJsonRuntimeException;

public class LarJsonMappingDefinitionException extends LarJsonRuntimeException {
    public LarJsonMappingDefinitionException() {
    }

    public LarJsonMappingDefinitionException(String message) {
        super(message);
    }

    public LarJsonMappingDefinitionException(String message, Throwable cause) {
        super(message, cause);
    }

    public LarJsonMappingDefinitionException(Throwable cause) {
        super(cause);
    }
}
