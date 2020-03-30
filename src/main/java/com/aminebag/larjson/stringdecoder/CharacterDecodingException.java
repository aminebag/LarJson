package com.aminebag.larjson.stringdecoder;

public class CharacterDecodingException extends Exception {
    public CharacterDecodingException() {
    }

    public CharacterDecodingException(String message) {
        super(message);
    }

    public CharacterDecodingException(String message, Throwable cause) {
        super(message, cause);
    }

    public CharacterDecodingException(Throwable cause) {
        super(cause);
    }

}
