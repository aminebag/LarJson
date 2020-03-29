package com.aminebag.larjson.stringdecoder;

public class StringDecodingException extends Exception {
    public StringDecodingException() {
    }

    public StringDecodingException(String message) {
        super(message);
    }

    public StringDecodingException(String message, Throwable cause) {
        super(message, cause);
    }

    public StringDecodingException(Throwable cause) {
        super(cause);
    }

    public StringDecodingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
