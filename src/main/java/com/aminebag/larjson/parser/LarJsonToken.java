package com.aminebag.larjson.parser;

/**
 * Credits : This code is inspired, to a great degree, by the enum JsonToken of the Gson library
 *
 * @author Amine Bagdouri
 */
public enum LarJsonToken {

    /**
     * The opening of a JSON array
     */
    BEGIN_ARRAY,

    /**
     * The closing of a JSON array
     */
    END_ARRAY,

    /**
     * The opening of a JSON object
     */
    BEGIN_OBJECT,

    /**
     * The closing of a JSON object
     */
    END_OBJECT,

    /**
     * A JSON property name. Within objects, tokens alternate between names and
     * their values
     */
    NAME,

    SIMPLE_VALUE,

    /**
     * A JSON {@code null}
     */
    NULL,

    /**
     * The end of the JSON stream
     */
    END_DOCUMENT;
}
