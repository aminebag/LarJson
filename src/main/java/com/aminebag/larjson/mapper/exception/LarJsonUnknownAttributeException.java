package com.aminebag.larjson.mapper.exception;

import com.aminebag.larjson.exception.LarJsonException;

/**
 * @author Amine Bagdouri
 */
public class LarJsonUnknownAttributeException extends LarJsonException {

    public LarJsonUnknownAttributeException(String message) {
        super(message);
    }

    public LarJsonUnknownAttributeException(String message, Throwable cause) {
        super(message, cause);
    }

    public LarJsonUnknownAttributeException(Throwable cause) {
        super(cause);
    }
}
