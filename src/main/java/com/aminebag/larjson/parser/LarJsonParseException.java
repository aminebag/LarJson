package com.aminebag.larjson.parser;

import com.aminebag.larjson.exception.LarJsonException;

/**
 * @author Amine Bagdouri
 */
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
