package com.aminebag.larjson.parser;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * A stateful parser that can be used to parse a JSON object or array
 * Credits : The contract of this interface is inspired, to a great degree,
 * by the class JsonReader of the Gson library
 *
 * @author Amine Bagdouri
 */
public interface LarJsonTokenParser {

    /**
     * @return the type of the next token without consuming it.
     */
    LarJsonToken peek() throws LarJsonParseException, IOException;

    /**
     * Consumes the next token from the JSON stream and asserts that it is the
     * beginning of a new array.
     */
    void beginArray() throws LarJsonParseException, IOException;

    /**
     * Consumes the next token from the JSON stream and asserts that it is the
     * beginning of a new object.
     */
    void beginObject() throws LarJsonParseException, IOException;

    /**
     * Consumes the next token from the JSON stream and asserts that it is the
     * end of the current array.
     */
    void endArray() throws LarJsonParseException, IOException;

    /**
     * Consumes the next token from the JSON stream and asserts that it is the
     * end of the current object.
     */
    void endObject() throws LarJsonParseException, IOException;

    /**
     * @return the next token as a boolean, and consumes it
     */
    boolean nextBoolean() throws LarJsonParseException, IOException;

    /**
     * @return the next token as a byte, and consumes it. If the next token is a string, this method will attempt to
     * parse it as a byte. If the next token's numeric value cannot be exactly represented by a Java {@code byte},
     * this method throws.
     */
    byte nextByte() throws LarJsonParseException, IOException;

    /**
     * @return the next token as a float, and consumes it. If the next token is a string, this method will attempt to
     * parse it as a float. If the next token's numeric value cannot be exactly represented by a Java {@code float},
     * this method throws.
     */
    float nextFloat() throws LarJsonParseException, IOException;

    /**
     * @return the next token as a double, and consumes it. If the next token is a string, this method will attempt to
     * parse it as a double. If the next token's numeric value cannot be exactly represented by a Java {@code double},
     * this method throws.
     */
    double nextDouble() throws LarJsonParseException, IOException;

    /**
     * @return the next token as a short, and consumes it. If the next token is a string, this method will attempt to
     * parse it as a short. If the next token's numeric value cannot be exactly represented by a Java {@code short},
     * this method throws.
     */
    short nextShort() throws LarJsonParseException, IOException;

    /**
     * @return the next token as an int, and consumes it. If the next token is a string, this method will attempt to
     * parse it as an int. If the next token's numeric value cannot be exactly represented by a Java {@code int},
     * this method throws.
     */
    int nextInt() throws LarJsonParseException, IOException;

    /**
     * @return the next token as a long, and consumes it. If the next token is a string, this method will attempt to
     * parse it as a long. If the next token's numeric value cannot be exactly represented by a Java {@code long},
     * this method throws.
     */
    long nextLong() throws LarJsonParseException, IOException;

    /**
     * @return the next token as a char, and consumes it
     */
    char nextChar() throws LarJsonParseException, IOException;

    /**
     * @return the Number value of the next token, consuming it. If the next token is a string, this method will
     * attempt to parse it first as a long, then as a {@link BigInteger}, then as a double, then as a
     * {@link BigDecimal}.
     */
    Number nextNumber() throws LarJsonParseException, IOException;

    /**
     * @return the next token as a {@link BigDecimal}, and consumes it. If the next token is a string, this method will
     * attempt to parse it as a {@link BigDecimal}. If the next token's numeric value cannot be exactly represented by
     * a Java {@link BigDecimal}, this method throws.
     */
    BigDecimal nextBigDecimal() throws LarJsonParseException, IOException;

    /**
     * @return the next token as a {@link BigInteger}, and consumes it. If the next token is a string, this method will
     * attempt to parse it as a {@link BigInteger}. If the next token's numeric value cannot be exactly represented by
     * a Java {@link BigInteger}, this method throws.
     */
    BigInteger nextBigInteger() throws LarJsonParseException, IOException;

    /**
     * @return the next token as an attribute name, and consumes it
     */
    String nextName() throws LarJsonParseException, IOException;

    /**
     * Consumes the next token from the JSON stream and asserts that it is a {@code null} value.
     */
    void nextNull() throws LarJsonParseException, IOException;

    /**
     * @return the next token as a {@link String}, and consumes it.
     */
    String nextString() throws LarJsonParseException, IOException;

    /**
     * Skips the next value recursively. If it is an object or array, all nested
     * elements are skipped.
     */
    void skipValue() throws LarJsonParseException, IOException;

    /**
     * @return the current byte position
     */
    long getCurrentPosition() throws LarJsonParseException, IOException;

    /**
     * @return a <a href="http://goessner.net/articles/JsonPath/">JsonPath</a> to
     * the current location in the JSON value.
     */
    String getPath() throws LarJsonParseException, IOException;
}
