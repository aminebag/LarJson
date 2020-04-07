package com.aminebag.larjson.jsonmapper.exception;

import com.aminebag.larjson.exception.LarJsonCheckedException;

public class LarJsonConversionException extends LarJsonCheckedException {
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
