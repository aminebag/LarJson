package com.aminebag.larjson.parser;

import com.aminebag.larjson.stream.CharacterStream;
import com.aminebag.larjson.stream.StringCharacterStream;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Amine Bagdouri
 */
public class GsonValueParserTest {

    @Test
    public void testLenientParseDoubleQuotesString() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(true);
        String str = "hello my friénds !";
        CharacterStream characterStream = new StringCharacterStream('\"' + str + '\"');
        assertEquals(str, valueParser.parseString(characterStream));
    }

    @Test
    public void testStrictParseDoubleQuotesString() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(false);
        String str = "hello my friénds !";
        CharacterStream characterStream = new StringCharacterStream('\"' + str + '\"');
        assertEquals(str, valueParser.parseString(characterStream));
    }

    @Test
    public void testLenientParseSingleQuotesString() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(true);
        String str = "hello my friénds !";
        CharacterStream characterStream = new StringCharacterStream('\'' + str + '\'');
        assertEquals(str, valueParser.parseString(characterStream));
    }

    @Test
    public void testStrictParseSingleQuotesString() throws IOException {
        GsonValueParser valueParser = new GsonValueParser(false);
        String str = "hello my friénds !";
        CharacterStream characterStream = new StringCharacterStream('\'' + str + '\'');
        try {
            valueParser.parseString(characterStream);
            fail();
        }catch (LarJsonParseException expected) {
        }
    }

    @Test
    public void testLenientParseUnquotedString() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(true);
        String str = "hello my friénds !";
        CharacterStream characterStream = new StringCharacterStream(str);
        assertEquals("hello", valueParser.parseString(characterStream));
    }

    @Test
    public void testStrictParseUnquotedString() throws IOException {
        GsonValueParser valueParser = new GsonValueParser(false);
        String str = "hello my friénds !";
        CharacterStream characterStream = new StringCharacterStream(str);
        try {
            valueParser.parseString(characterStream);
            fail();
        }catch (LarJsonParseException expected) {
        }
    }

    @Test
    public void testParseFloat() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(false);
        float value = 47.568f;
        CharacterStream characterStream = new StringCharacterStream(Float.toString(value));
        assertEquals(value, valueParser.parseFloat(characterStream));
    }

    @Test
    public void testParseDouble() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(false);
        double value = 7.568;
        CharacterStream characterStream = new StringCharacterStream(Double.toString(value));
        assertEquals(value, valueParser.parseDouble(characterStream));
    }

    @Test
    public void testLenientParseFloatNaN() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(true);
        float value = Float.NaN;
        CharacterStream characterStream = new StringCharacterStream(Float.toString(value));
        assertEquals(value, valueParser.parseFloat(characterStream));
    }

    @Test
    public void testStrictParseFloatNaN() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(false);
        float value = Float.NaN;
        CharacterStream characterStream = new StringCharacterStream(Float.toString(value));
        try {
            valueParser.parseFloat(characterStream);
            fail();
        }catch (LarJsonParseException expected) {
        }
    }

    @Test
    public void testLenientParseFloatInfinity() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(true);
        float value = Float.NEGATIVE_INFINITY;
        CharacterStream characterStream = new StringCharacterStream(Float.toString(value));
        assertEquals(value, valueParser.parseFloat(characterStream));
    }

    @Test
    public void testStrictParseFloatInfinity() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(false);
        float value = Float.NEGATIVE_INFINITY;
        CharacterStream characterStream = new StringCharacterStream(Float.toString(value));
        try {
            valueParser.parseFloat(characterStream);
            fail();
        }catch (LarJsonParseException expected) {
        }
    }

    @Test
    public void testLenientParseDoubleNaN() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(true);
        double value = Double.NaN;
        CharacterStream characterStream = new StringCharacterStream(Double.toString(value));
        assertEquals(value, valueParser.parseDouble(characterStream));
    }

    @Test
    public void testStrictParseDoubleNaN() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(false);
        double value = Double.NaN;
        CharacterStream characterStream = new StringCharacterStream(Double.toString(value));
        try {
            valueParser.parseDouble(characterStream);
            fail();
        }catch (LarJsonParseException expected) {
        }
    }

    @Test
    public void testLenientParseDoubleInfinity() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(true);
        double value = Double.NEGATIVE_INFINITY;
        CharacterStream characterStream = new StringCharacterStream(Double.toString(value));
        assertEquals(value, valueParser.parseDouble(characterStream));
    }

    @Test
    public void testStrictParseDoubleInfinity() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(false);
        double value = Double.NEGATIVE_INFINITY;
        CharacterStream characterStream = new StringCharacterStream(Double.toString(value));
        try {
            valueParser.parseDouble(characterStream);
            fail();
        }catch (LarJsonParseException expected) {
        }
    }

    @Test
    public void testParseInt() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(false);
        int value = 5_578_864;
        CharacterStream characterStream = new StringCharacterStream(Integer.toString(value));
        assertEquals(value, valueParser.parseInt(characterStream));
    }

    @Test
    public void testParseOverflowInt() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(false);
        long value = 5_578_864_758L;
        CharacterStream characterStream = new StringCharacterStream(Long.toString(value));
        try {
            valueParser.parseInt(characterStream);
            fail();
        } catch (LarJsonParseException expected) {
        }
    }

    @Test
    public void testParseDecimalLong() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(false);
        long value = 5_578_864_758L;
        CharacterStream characterStream = new StringCharacterStream(value + ".0");
        assertEquals(value, valueParser.parseLong(characterStream));
    }

    @Test
    public void testParseDoubleAsLong() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(false);
        long value = 5_578_864_758L;
        CharacterStream characterStream = new StringCharacterStream(value + ".07");
        try {
            valueParser.parseLong(characterStream);
            fail();
        } catch (LarJsonParseException expected) {
        }
    }

    @Test
    public void testParseLong() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(false);
        long value = 5_578_864_758L;
        CharacterStream characterStream = new StringCharacterStream(Long.toString(value));
        assertEquals(value, valueParser.parseLong(characterStream));
    }

    @Test
    public void testParseShort() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(false);
        short value = -5_578;
        CharacterStream characterStream = new StringCharacterStream(Short.toString(value));
        assertEquals(value, valueParser.parseShort(characterStream));
    }

    @Test
    public void testParseByte() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(false);
        byte value = 115;
        CharacterStream characterStream = new StringCharacterStream(Byte.toString(value));
        assertEquals(value, valueParser.parseByte(characterStream));
    }

    @Test
    public void testParseChar() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(false);
        char value = '@';
        CharacterStream characterStream = new StringCharacterStream('\"' + Character.toString(value) + '\"');
        assertEquals(value, valueParser.parseChar(characterStream));
    }

    @Test
    public void testParseStringAsChar() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(false);
        char value = '@';
        CharacterStream characterStream = new StringCharacterStream('\"' + Character.toString(value) + 'y' + '\"');
        try {
            valueParser.parseChar(characterStream);
            fail();
        } catch (LarJsonParseException expected) {
        }
    }

    @Test
    public void testParseBigInteger() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(false);
        BigInteger value = new BigInteger( '-' + Long.toString(Long.MAX_VALUE) + Long.MAX_VALUE);
        CharacterStream characterStream = new StringCharacterStream(value.toString());
        assertEquals(value, valueParser.parseBigInteger(characterStream));
    }

    @Test
    public void testParseBigIntegerWithNullFraction() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(false);
        BigInteger value = new BigInteger( '-' + Long.toString(Long.MAX_VALUE) + Long.MAX_VALUE);
        CharacterStream characterStream = new StringCharacterStream(value.toString() + ".000");
        assertEquals(value, valueParser.parseBigInteger(characterStream));
    }

    @Test
    public void testParseBigIntegerWithNotNullFraction() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(false);
        BigInteger value = new BigInteger( '-' + Long.toString(Long.MAX_VALUE) + Long.MAX_VALUE);
        CharacterStream characterStream = new StringCharacterStream(value.toString() + ".07");
        try {
            assertEquals(value, valueParser.parseBigInteger(characterStream));
            fail();
        } catch (LarJsonParseException expected) {
        }
    }

    @Test
    public void testParseBigDecimal() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(false);
        BigDecimal value = new BigDecimal( Long.toString(Long.MAX_VALUE) + '.' + Long.MAX_VALUE);
        CharacterStream characterStream = new StringCharacterStream(value.toString());
        assertEquals(value, valueParser.parseBigDecimal(characterStream));
    }

    @Test
    public void testParseBigDecimalAsNumber() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(false);
        BigDecimal value = new BigDecimal( Long.toString(Long.MAX_VALUE) + '.' + Long.MAX_VALUE);
        CharacterStream characterStream = new StringCharacterStream(value.toString());
        assertEquals(value, valueParser.parseNumber(characterStream));
    }

    @Test
    public void testParseDoubleAsNumber() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(false);
        double value = 657865.6465607;
        CharacterStream characterStream = new StringCharacterStream(Double.toString(value));
        assertEquals(value, valueParser.parseNumber(characterStream));
    }

    @Test
    public void testLenientParseNanAsNumber() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(true);
        double value = Double.NaN;
        CharacterStream characterStream = new StringCharacterStream(Double.toString(value));
        assertEquals(value, valueParser.parseNumber(characterStream));
    }

    @Test
    public void testParseBigIntegerAsNumber() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(false);
        BigInteger value = new BigInteger( '-' + Long.toString(Long.MAX_VALUE) + Long.MAX_VALUE);
        CharacterStream characterStream = new StringCharacterStream(value.toString());
        assertEquals(value, valueParser.parseNumber(characterStream));
    }

    @Test
    public void testParseLongAsNumber() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(false);
        long value = 5_578_864_758L;
        CharacterStream characterStream = new StringCharacterStream(Long.toString(value));
        assertEquals(value, valueParser.parseNumber(characterStream));
    }

    @Test
    public void testParseInvalidNumber() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(false);
        long value = 5_578_864_758L;
        CharacterStream characterStream = new StringCharacterStream(value+"L");
        try {
            valueParser.parseNumber(characterStream);
            fail();
        } catch (LarJsonParseException expected) {
        }
    }

    @Test
    public void testLenientParseDoubleQuotesInt() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(true);
        int value = 5_578_864;
        CharacterStream characterStream = new StringCharacterStream('\"' + Integer.toString(value) + '\"');
        assertEquals(value, valueParser.parseInt(characterStream));
    }

    @Test
    public void testStrictParseDoubleQuotesInt() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(false);
        int value = 5_578_864;
        CharacterStream characterStream = new StringCharacterStream('\"' + Integer.toString(value) + '\"');
        assertEquals(value, valueParser.parseInt(characterStream));
    }

    @Test
    public void testLenientParseSingleQuotesInt() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(true);
        int value = 5_578_864;
        CharacterStream characterStream = new StringCharacterStream('\'' + Integer.toString(value) + '\'');
        assertEquals(value, valueParser.parseInt(characterStream));
    }

    @Test
    public void testStrictParseSingleQuotesInt() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(false);
        int value = 5_578_864;
        CharacterStream characterStream = new StringCharacterStream('\'' + Integer.toString(value) + '\'');
        try {
            valueParser.parseInt(characterStream);
            fail();
        } catch (LarJsonParseException expected) {
        }
    }

    @Test
    public void testParseBoolean() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(false);
        CharacterStream characterStream = new StringCharacterStream("false");
        assertFalse(valueParser.parseBoolean(characterStream));
    }

    @Test
    public void testLenientParseDoubleQuotesBoolean() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(true);
        CharacterStream characterStream = new StringCharacterStream("\"true\"");
        assertTrue(valueParser.parseBoolean(characterStream));
    }

    @Test
    public void testStrictParseDoubleQuotesBoolean() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(false);
        CharacterStream characterStream = new StringCharacterStream("\"true\"");
        assertTrue(valueParser.parseBoolean(characterStream));
    }

    @Test
    public void testLenientParseSingleQuotesBoolean() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(true);
        CharacterStream characterStream = new StringCharacterStream("'false'");
        assertFalse(valueParser.parseBoolean(characterStream));
    }

    @Test
    public void testStrictParseSingleQuotesBoolean() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(false);
        CharacterStream characterStream = new StringCharacterStream("'true'");
        try {
            valueParser.parseBoolean(characterStream);
            fail();
        } catch (LarJsonParseException expected) {
        }
    }

    @Test
    public void testLenientParseUppercaseBoolean() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(true);
        CharacterStream characterStream = new StringCharacterStream("TRUE");
        assertTrue(valueParser.parseBoolean(characterStream));
    }

    @Test
    public void testStrictParseUppercaseBoolean() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(false);
        CharacterStream characterStream = new StringCharacterStream("TRUE");
        try {
            valueParser.parseBoolean(characterStream);
            fail();
        } catch (LarJsonParseException expected) {
        }
    }

    @Test
    public void testParseUnicodeEscapeChar() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(false);
        CharacterStream characterStream = new StringCharacterStream("\"\\uABCD\"");
        assertEquals('\uABCD', valueParser.parseChar(characterStream));
    }

    @Test
    public void testParseUnicodeEscapeString() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(false);
        CharacterStream characterStream = new StringCharacterStream("\"Hello\\uAb5Doo\"");
        assertEquals("Hello\uAb5Doo", valueParser.parseString(characterStream));
    }

    @Test
    public void testParseInvalidUnicodeEscapeString() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(false);
        CharacterStream characterStream = new StringCharacterStream("\"Hello\\uABGDoo\"");
        try {
            valueParser.parseString(characterStream);
            fail();
        } catch (LarJsonParseException expected) {
        }
    }

    @Test
    public void testParseEscapedCharactersString() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(false);
        CharacterStream characterStream = new StringCharacterStream("\"Hello\\t\\n\\\\\\bH\\r\\f\\'\\\"\\\n\\/Doo\"");
        assertEquals("Hello\t\n\\\bH\r\f'\"\n/Doo", valueParser.parseString(characterStream));
    }

    @Test
    public void testParseInvalidEscapedCharacterString() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(false);
        CharacterStream characterStream = new StringCharacterStream("\"Hello\\koo\"");
        try {
            valueParser.parseString(characterStream);
            fail();
        } catch (LarJsonParseException expected) {
        }
    }

    @Test
    public void testParseUtf8String() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(false);
        String str = "لارجسون";
        CharacterStream characterStream = new StringCharacterStream('\"' + str + '\"');
        assertEquals(str, valueParser.parseString(characterStream));
    }

    @Test
    public void testParseWithValidDelimiter() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(false);
        testParseDoubleWithDelimiterSuccess(valueParser, '{');
        testParseDoubleWithDelimiterSuccess(valueParser, '}');
        testParseDoubleWithDelimiterSuccess(valueParser, '[');
        testParseDoubleWithDelimiterSuccess(valueParser, ']');
        testParseDoubleWithDelimiterSuccess(valueParser, ':');
        testParseDoubleWithDelimiterSuccess(valueParser, ',');
        testParseDoubleWithDelimiterSuccess(valueParser, ' ');
        testParseDoubleWithDelimiterSuccess(valueParser, '\t');
        testParseDoubleWithDelimiterSuccess(valueParser, '\f');
        testParseDoubleWithDelimiterSuccess(valueParser, '\r');
        testParseDoubleWithDelimiterSuccess(valueParser, '\n');
    }

    private void testParseDoubleWithDelimiterSuccess(GsonValueParser valueParser, char delimiter) throws LarJsonParseException, IOException {
        double value = 17.42;
        CharacterStream characterStream = new StringCharacterStream(value + Character.toString(delimiter));
        assertEquals(value, valueParser.parseDouble(characterStream));
    }

    private void testParseDoubleWithDelimiterFail(GsonValueParser valueParser, char delimiter) throws IOException {
        double value = 17.42;
        CharacterStream characterStream = new StringCharacterStream(value + Character.toString(delimiter));
        try {
            valueParser.parseDouble(characterStream);
            fail();
        } catch (LarJsonParseException expected) {
        }
    }

    @Test
    public void testLenientParseWithInvalidDelimiter() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(true);
        testParseDoubleWithDelimiterSuccess(valueParser, '/');
        testParseDoubleWithDelimiterSuccess(valueParser, '\\');
        testParseDoubleWithDelimiterSuccess(valueParser, ';');
        testParseDoubleWithDelimiterSuccess(valueParser, '#');
        testParseDoubleWithDelimiterSuccess(valueParser, '=');
    }

    @Test
    public void testStrictParseWithInvalidDelimiter() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(false);
        testParseDoubleWithDelimiterFail(valueParser, '/');
        testParseDoubleWithDelimiterFail(valueParser, '\\');
        testParseDoubleWithDelimiterFail(valueParser, ';');
        testParseDoubleWithDelimiterFail(valueParser, '#');
        testParseDoubleWithDelimiterFail(valueParser, '=');
    }

    @Test
    public void testParseWithIllegalDelimiter() throws IOException, LarJsonParseException {
        GsonValueParser valueParser = new GsonValueParser(true);
        testParseDoubleWithDelimiterFail(valueParser, '@');
    }
}
