package com.aminebag.larjson.parser;

import com.aminebag.larjson.stream.CharacterStream;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * A stateless parser that parses a simple JSON value
 *
 * @author Amine Bagdouri
 */
public interface LarJsonValueParser {

    /**
     * Parse a String from the character stream at its current position
     * @return the parsed String
     */
    String parseString(CharacterStream characterStream) throws LarJsonParseException, IOException;

    /**
     * Parse a short from the character stream at its current position
     * @return the parsed short
     */
    short parseShort(CharacterStream characterStream) throws LarJsonParseException, IOException;

    /**
     * Parse an int from the character stream at its current position
     * @return the parsed int
     */
    int parseInt(CharacterStream characterStream) throws LarJsonParseException, IOException;

    /**
     * Parse a long from the character stream at its current position
     * @return the parsed long
     */
    long parseLong(CharacterStream characterStream) throws LarJsonParseException, IOException;

    /**
     * Parse a float from the character stream at its current position
     * @return the parsed float
     */
    float parseFloat(CharacterStream characterStream) throws LarJsonParseException, IOException;

    /**
     * Parse a double from the character stream at its current position
     * @return the parsed double
     */
    double parseDouble(CharacterStream characterStream) throws LarJsonParseException, IOException;

    /**
     * Parse a char from the character stream at its current position
     * @return the parsed char
     */
    char parseChar(CharacterStream characterStream) throws LarJsonParseException, IOException;

    /**
     * Parse a boolean from the character stream at its current position
     * @return the parsed boolean
     */
    boolean parseBoolean(CharacterStream characterStream) throws LarJsonParseException, IOException;

    /**
     * Parse a byte from the character stream at its current position
     * @return the parsed byte
     */
    byte parseByte(CharacterStream characterStream) throws LarJsonParseException, IOException;

    /**
     * Parse a {@link Number} from the character stream at its current position
     * @return the parsed {@link Number}
     */
    Number parseNumber(CharacterStream characterStream) throws LarJsonParseException, IOException;

    /**
     * Parse a {@link BigInteger} from the character stream at its current position
     * @return the parsed {@link BigInteger}
     */
    BigInteger parseBigInteger(CharacterStream characterStream) throws LarJsonParseException, IOException;

    /**
     * Parse a {@link BigDecimal} from the character stream at its current position
     * @return the parsed {@link BigDecimal}
     */
    BigDecimal parseBigDecimal(CharacterStream characterStream) throws LarJsonParseException, IOException;

}
