package com.aminebag.larjson.exception;

public class LarJsonRuntimeException extends RuntimeException {
    public LarJsonRuntimeException() {
    }

    public LarJsonRuntimeException(String message) {
        super(message);
    }

    public LarJsonRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public LarJsonRuntimeException(Throwable cause) {
        super(cause);
    }
}
