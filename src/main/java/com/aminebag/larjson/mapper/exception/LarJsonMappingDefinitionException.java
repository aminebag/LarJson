package com.aminebag.larjson.mapper.exception;

import com.aminebag.larjson.exception.LarJsonRuntimeException;

/**
 * @author Amine Bagdouri
 */
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
