package com.aminebag.larjson.parser;

import com.aminebag.larjson.chardecoder.CharacterDecodingException;
import com.aminebag.larjson.stream.CharacterStream;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.DoubleToLongFunction;
import java.util.function.ToLongFunction;

/**
 * Credits : This code is inspired, to a great degree, by the class JsonReader of the Gson library
 *
 * {@inheritDoc}
 *
 * @author Amine Bagdouri
 */
public class GsonValueParser implements LarJsonValueParser {

    private final boolean lenient;

    public GsonValueParser(boolean lenient) {
        this.lenient = lenient;
    }

    @Override
    public String parseString(CharacterStream characterStream) throws LarJsonParseException, IOException {
        try {
            int c;
            if ((c = characterStream.next()) != -1 && (c == '\'' || c == '"')) {
                if(c == '\'') {
                    checkLenient(characterStream);
                }
                return nextQuotedValue(characterStream, c);
            }

            checkLenient(characterStream);
            return nextUnquotedValue(characterStream, c);

        } catch (CharacterDecodingException e) {
            throw syntaxError(characterStream, e);
        }
    }

    @Override
    public byte parseByte(CharacterStream characterStream) throws LarJsonParseException, IOException {
        return (byte) parseInteger(characterStream, Byte::parseByte, d->(byte)d);
    }

    @Override
    public Number parseNumber(CharacterStream characterStream) throws LarJsonParseException, IOException {
        String str = getString(characterStream);

        try {
            return Long.parseLong(str);
        } catch (NumberFormatException longException) {
            try {
                return new BigInteger(str);
            } catch (NumberFormatException bigIntegerException) {
                try {
                    BigDecimal bigDecimal = new BigDecimal(str);
                    double doubleValue = bigDecimal.doubleValue();
                    if(str.equals(Double.toString(doubleValue))) {
                        return doubleValue;
                    } else {
                        return bigDecimal;
                    }
                } catch (NumberFormatException bigDecimalException) {
                    try {
                        double value = Double.parseDouble(str);
                        if(Double.isInfinite(value) || Double.isNaN(value)) {
                            checkLenient(characterStream);
                        }
                        return value;
                    } catch (NumberFormatException doubleException) {
                        throw syntaxError(characterStream, "Expected a Number but got '" + str + "'");
                    }
                }
            }
        }
    }

    @Override
    public BigInteger parseBigInteger(CharacterStream characterStream) throws LarJsonParseException, IOException {
        String str = getString(characterStream);

        try {
            return new BigInteger(str);
        } catch (NumberFormatException bigIntegerException) {
            try {
                return new BigDecimal(str).toBigIntegerExact();
            } catch (NumberFormatException | ArithmeticException bigDecimalException) {
                throw syntaxError(characterStream, "Expected a BigInteger but got '" + str + "'");
            }
        }
    }

    @Override
    public BigDecimal parseBigDecimal(CharacterStream characterStream) throws LarJsonParseException, IOException {
        String str = getString(characterStream);

        try {
            return new BigDecimal(str);
        } catch (NumberFormatException exception) {
            throw syntaxError(characterStream, "Expected a BigDecimal but got '" + str + "'");
        }
    }

    @Override
    public short parseShort(CharacterStream characterStream) throws LarJsonParseException, IOException {
        return (short) parseInteger(characterStream, Short::parseShort, d->(short)d);
    }

    @Override
    public int parseInt(CharacterStream characterStream) throws LarJsonParseException, IOException {
        return (int) parseInteger(characterStream, Integer::parseInt, d->(int)d);
    }

    @Override
    public long parseLong(CharacterStream characterStream) throws LarJsonParseException, IOException {
        return parseInteger(characterStream, Long::parseLong, d->(long)d);
    }

    @Override
    public boolean parseBoolean(CharacterStream characterStream) throws LarJsonParseException, IOException {
        String str = getString(characterStream);

        switch (str) {
            case "TRUE":
                checkLenient(characterStream); // fall-through
            case "true":
                return true;
            case "FALSE":
                checkLenient(characterStream); // fall-through
            case "false":
                return false;
            default:
                throw syntaxError(characterStream, "Unrecognized boolean value '" + str + "'");
        }
    }

    @Override
    public float parseFloat(CharacterStream characterStream) throws LarJsonParseException, IOException {
        String str = getString(characterStream);

        try {
            float value = Float.parseFloat(str);
            if(Float.isInfinite(value) || Float.isNaN(value)) {
                checkLenient(characterStream);
            }
            return value;
        } catch (NumberFormatException exception) {
            throw syntaxError(characterStream, "Expected a float but got '" + str + "'");
        }
    }

    @Override
    public double parseDouble(CharacterStream characterStream) throws LarJsonParseException, IOException {
        String str = getString(characterStream);

        try {
            double value = Double.parseDouble(str);
            if(Double.isInfinite(value) || Double.isNaN(value)) {
                checkLenient(characterStream);
            }
            return value;
        } catch (NumberFormatException exception) {
            throw syntaxError(characterStream, "Expected a double but got '" + str + "'");
        }
    }

    @Override
    public char parseChar(CharacterStream characterStream) throws LarJsonParseException, IOException {
        String str = getString(characterStream);

        if(str.length() != 1) {
            throw syntaxError(characterStream, "Expected a char but got '" + str + "'");
        } else {
            return str.charAt(0);
        }
    }

    private long parseInteger(CharacterStream characterStream, ToLongFunction<String> parse,
                             DoubleToLongFunction castDouble) throws LarJsonParseException, IOException {
        String str = getString(characterStream);

        try {
            return parse.applyAsLong(str);
        } catch (NumberFormatException exception) {
            try {
                double doubleValue = Double.parseDouble(str);
                long value = castDouble.applyAsLong(doubleValue);
                if(doubleValue != value) {
                    throw syntaxError(characterStream, exception);
                } else {
                    return value;
                }
            } catch (NumberFormatException ignored) {
                throw syntaxError(characterStream, "Expected an integer but got '" + str + "'");
            }

        }
    }

    private String getString(CharacterStream characterStream) throws IOException, LarJsonParseException {
        String str;
        try {
            int c;
            if ((c = characterStream.next()) != -1 && (c == '\'' || c == '"')) {
                str = nextQuotedValue(characterStream, c);
            } else {
                str = nextUnquotedValue(characterStream, c);
            }
        } catch (CharacterDecodingException e) {
            throw syntaxError(characterStream, e);
        }
        return str;
    }

    /**
     * Returns an unquoted value as a string.
     */
    @SuppressWarnings("fallthrough")
    private String nextUnquotedValue(CharacterStream characterStream, int c) throws IOException, LarJsonParseException, CharacterDecodingException {
        StringBuilder builder = new StringBuilder();

        findNonLiteralCharacter:
        while (c != -1) {
            switch (c) {
                case '/':
                case '\\':
                case ';':
                case '#':
                case '=':
                    checkLenient(characterStream); // fall-through
                case '{':
                case '}':
                case '[':
                case ']':
                case ':':
                case ',':
                case ' ':
                case '\t':
                case '\f':
                case '\r':
                case '\n':
                    break findNonLiteralCharacter;
                default:
                    builder.append((char)c);
                    break;
            }
            c = characterStream.next();
        }
        return builder.toString();
    }

    /**
     * Returns the string up to but not including {@code quote}, unescaping any
     * character escape sequences encountered along the way. The opening quote
     * should have already been read. This consumes the closing quote, but does
     * not include it in the returned string.
     */
    private String nextQuotedValue(CharacterStream characterStream, int quote)
            throws IOException, LarJsonParseException, CharacterDecodingException {
        if(quote == '\'') {
            checkLenient(characterStream);
        }
        StringBuilder builder = new StringBuilder();
        int c;
        while((c = characterStream.next()) != -1) {
            if (c == quote) {
                return builder.toString();
            } else if (c == '\\') {
                builder.append(readEscapeCharacter(characterStream));
            } else {
                builder.append((char) c);
            }
        }
        throw syntaxError(characterStream, "Unterminated string");
    }

    /**
     * Unescapes the character identified by the character or characters that
     * immediately follow a backslash. The backslash '\' should have already
     * been read. This supports both unicode escapes "u000A" and two-character
     * escapes "\n".
     */
    private char readEscapeCharacter(CharacterStream characterStream) throws IOException, LarJsonParseException, CharacterDecodingException {
        int escaped = characterStream.next();
        if (escaped < 0) {
            throw syntaxError(characterStream, "Unterminated escape sequence");
        }

        switch (escaped) {
            case 'u':
                char[] array = new char[4];
                for(int i = 0; i<4; i++) {
                    int c = characterStream.next();
                    if (c < 0) {
                        throw syntaxError(characterStream, "Unterminated escape sequence");
                    }
                    array[i] = (char) c;
                }

                char result = 0;
                for (int i = 0; i < 4; i++) {
                    char c = array[i];
                    result <<= 4;
                    if (c >= '0' && c <= '9') {
                        result += (c - '0');
                    } else if (c >= 'a' && c <= 'f') {
                        result += (c - 'a' + 10);
                    } else if (c >= 'A' && c <= 'F') {
                        result += (c - 'A' + 10);
                    } else {
                        throw syntaxError(characterStream, "Unrecognized number \\u" +
                                new String(array, 0, 4));
                    }
                }
                return result;

            case 't':
                return '\t';

            case 'b':
                return '\b';

            case 'n':
                return '\n';

            case 'r':
                return '\r';

            case 'f':
                return '\f';

            case '\n':
            case '\'':
            case '"':
            case '\\':
            case '/':
                return (char) escaped;
            default:
                // throw error when none of the above cases are matched
                throw syntaxError(characterStream, "Invalid escape sequence");
        }
    }

    /**
     * Throws a new LarJsonParseException with the given message and a context snippet
     */
    private LarJsonParseException syntaxError(CharacterStream characterStream, String message)
            throws LarJsonParseException, IOException {
        throw new LarJsonParseException(message + locationString(characterStream));
    }

    /**
     * Throws a new LarJsonParseException with the given message and a context snippet
     */
    private LarJsonParseException syntaxError(CharacterStream characterStream, Exception e)
            throws LarJsonParseException, IOException {
        throw new LarJsonParseException(e.getMessage() + locationString(characterStream), e);
    }

    private String locationString(CharacterStream characterStream) throws IOException {
        return " at byte number " + characterStream.getBytePosition();
    }

    private void checkLenient(CharacterStream characterStream) throws IOException, LarJsonParseException {
        if (!lenient) {
            throw syntaxError(characterStream, "Set lenient to true to accept malformed JSON");
        }
    }
}
