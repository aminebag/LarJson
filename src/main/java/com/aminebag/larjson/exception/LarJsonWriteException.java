package com.aminebag.larjson.exception;

import com.aminebag.larjson.exception.LarJsonException;

/**
 * @author Amine Bagdouri
 */
public class LarJsonWriteException extends LarJsonException {

    public LarJsonWriteException() {
    }

    public LarJsonWriteException(String message) {
        super(message);
    }

    public LarJsonWriteException(String message, Throwable cause) {
        super(message, cause);
    }

    public LarJsonWriteException(Throwable cause) {
        super(cause);
    }
}
