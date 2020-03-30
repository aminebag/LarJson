package com.aminebag.larjson.parser.exception;

public class LarJsonException extends RuntimeException {
    public LarJsonException() {
    }

    public LarJsonException(String message) {
        super(message);
    }

    public LarJsonException(String message, Throwable cause) {
        super(message, cause);
    }

    public LarJsonException(Throwable cause) {
        super(cause);
    }
}
