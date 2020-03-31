package com.aminebag.larjson.exception;

public class LarJsonCheckedException extends Exception {
    public LarJsonCheckedException() {
    }

    public LarJsonCheckedException(String message) {
        super(message);
    }

    public LarJsonCheckedException(String message, Throwable cause) {
        super(message, cause);
    }

    public LarJsonCheckedException(Throwable cause) {
        super(cause);
    }
}
