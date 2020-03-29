package com.aminebag.larjson.exception;

/**
 * @author Amine Bagdouri
 */
public class LarJsonException extends Exception {
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
