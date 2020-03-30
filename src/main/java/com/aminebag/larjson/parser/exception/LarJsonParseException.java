package com.aminebag.larjson.parser.exception;

public class LarJsonParseException extends LarJsonException {
    public LarJsonParseException() {
    }

    public LarJsonParseException(String message) {
        super(message);
    }

    public LarJsonParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public LarJsonParseException(Throwable cause) {
        super(cause);
    }
}
