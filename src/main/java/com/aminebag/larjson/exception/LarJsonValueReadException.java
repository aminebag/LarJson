package com.aminebag.larjson.exception;

import com.aminebag.larjson.exception.LarJsonRuntimeException;

/**
 * @author Amine Bagdouri
 */
public class LarJsonValueReadException extends LarJsonRuntimeException {
    public LarJsonValueReadException() {
    }

    public LarJsonValueReadException(String message) {
        super(message);
    }

    public LarJsonValueReadException(String message, Throwable cause) {
        super(message, cause);
    }

    public LarJsonValueReadException(Throwable cause) {
        super(cause);
    }
}
