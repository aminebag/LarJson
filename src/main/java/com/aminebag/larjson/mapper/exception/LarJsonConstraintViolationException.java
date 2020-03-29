package com.aminebag.larjson.mapper.exception;

import com.aminebag.larjson.exception.LarJsonException;

/**
 * @author Amine Bagdouri
 */
public class LarJsonConstraintViolationException extends LarJsonException {

    public LarJsonConstraintViolationException() {
    }

    public LarJsonConstraintViolationException(String message) {
        super(message);
    }

    public LarJsonConstraintViolationException(String message, Throwable cause) {
        super(message, cause);
    }

    public LarJsonConstraintViolationException(Throwable cause) {
        super(cause);
    }
}
