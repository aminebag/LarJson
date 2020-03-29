package com.aminebag.larjson.exception;

/**
 * @author Amine Bagdouri
 */
public class LarJsonRuntimeException extends RuntimeException {
    protected LarJsonRuntimeException() {
    }

    protected LarJsonRuntimeException(String message) {
        super(message);
    }

    protected LarJsonRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    protected LarJsonRuntimeException(Throwable cause) {
        super(cause);
    }
}
