package com.aminebag.larjson.jsonparser;

import com.aminebag.larjson.exception.LarJsonCheckedException;

public class LarJsonParseException extends LarJsonCheckedException {
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
