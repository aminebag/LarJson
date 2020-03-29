package com.aminebag.larjson.exception;

/**
 * @author Amine Bagdouri
 */
public class LarJsonConversionException extends LarJsonException {
    public LarJsonConversionException() {
    }

    public LarJsonConversionException(String message) {
        super(message);
    }

    public LarJsonConversionException(String message, Throwable cause) {
        super(message, cause);
    }

    public LarJsonConversionException(Throwable cause) {
        super(cause);
    }
}
