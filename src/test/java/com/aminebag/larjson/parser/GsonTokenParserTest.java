package com.aminebag.larjson.parser;

import com.aminebag.larjson.stream.ArrayByteStream;
import com.aminebag.larjson.chardecoder.Utf8CharacterDecoder;
import com.aminebag.larjson.exception.LarJsonException;
import com.aminebag.larjson.stream.ByteStream;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Credits : This code is inspired, to a great degree, by the test class JsonReaderTest of the Gson library
 * @author Amine Bagdouri
 */
public class GsonTokenParserTest {

    @Test public void testParseArray() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[true, true]");
        tokenParser.beginArray();
        assertEquals(true, tokenParser.nextBoolean());
        assertEquals(true, tokenParser.nextBoolean());
        tokenParser.endArray();
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser.peek());
    }

    @Test public void testParseEmptyArray() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[]");
        tokenParser.beginArray();
        assertFalse(tokenParser.hasNext());
        tokenParser.endArray();
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser.peek());
    }

    @Test public void testParseObject() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("{\"a\": \"android\", \"b\": \"banana\"}");
        tokenParser.beginObject();
        assertEquals("a", tokenParser.nextName());
        assertEquals("android", tokenParser.nextString());
        assertEquals("b", tokenParser.nextName());
        assertEquals("banana", tokenParser.nextString());
        tokenParser.endObject();
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser.peek());
    }

    @Test public void testParseEmptyObject() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("{}");
        tokenParser.beginObject();
        assertFalse(tokenParser.hasNext());
        tokenParser.endObject();
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser.peek());
    }

    @Test public void testSkipArray() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("{\"a\": [\"one\", \"two\", \"three\"], \"b\": 123}");
        tokenParser.beginObject();
        assertEquals("a", tokenParser.nextName());
        tokenParser.skipValue();
        assertEquals("b", tokenParser.nextName());
        assertEquals(123, tokenParser.nextInt());
        tokenParser.endObject();
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser.peek());
    }

    @Test public void testSkipArrayAfterPeek() throws Exception {
        GsonTokenParser tokenParser = tokenParser("{\"a\": [\"one\", \"two\", \"three\"], \"b\": 123}");
        tokenParser.beginObject();
        assertEquals("a", tokenParser.nextName());
        assertEquals(LarJsonToken.BEGIN_ARRAY, tokenParser.peek());
        tokenParser.skipValue();
        assertEquals("b", tokenParser.nextName());
        assertEquals(123, tokenParser.nextInt());
        tokenParser.endObject();
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser.peek());
    }

    @Test public void testSkipTopLevelObject() throws Exception {
        GsonTokenParser tokenParser = tokenParser("{\"a\": [\"one\", \"two\", \"three\"], \"b\": 123}");
        tokenParser.skipValue();
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser.peek());
    }

    @Test public void testSkipObject() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("{\"a\": { \"c\": [], \"d\": [true, true, {}] }, \"b\": \"banana\"}");
        tokenParser.beginObject();
        assertEquals("a", tokenParser.nextName());
        tokenParser.skipValue();
        assertEquals("b", tokenParser.nextName());
        tokenParser.skipValue();
        tokenParser.endObject();
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser.peek());
    }

    @Test public void testSkipObjectAfterPeek() throws Exception {
        String json = "{" + "  \"one\": { \"num\": 1 }"
                + ", \"two\": { \"num\": 2 }" + ", \"three\": { \"num\": 3 }" + "}";
        GsonTokenParser tokenParser = tokenParser(json);
        tokenParser.beginObject();
        assertEquals("one", tokenParser.nextName());
        assertEquals(LarJsonToken.BEGIN_OBJECT, tokenParser.peek());
        tokenParser.skipValue();
        assertEquals("two", tokenParser.nextName());
        assertEquals(LarJsonToken.BEGIN_OBJECT, tokenParser.peek());
        tokenParser.skipValue();
        assertEquals("three", tokenParser.nextName());
        tokenParser.skipValue();
        tokenParser.endObject();
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser.peek());
    }

    @Test public void testSkipInteger() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("{\"a\":123456789,\"b\":-123456789}");
        tokenParser.beginObject();
        assertEquals("a", tokenParser.nextName());
        tokenParser.skipValue();
        assertEquals("b", tokenParser.nextName());
        tokenParser.skipValue();
        tokenParser.endObject();
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser.peek());
    }

    @Test public void testSkipDouble() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("{\"a\":-123.456e-789,\"b\":123456789.0}");
        tokenParser.beginObject();
        assertEquals("a", tokenParser.nextName());
        tokenParser.skipValue();
        assertEquals("b", tokenParser.nextName());
        tokenParser.skipValue();
        tokenParser.endObject();
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser.peek());
    }

    @Test public void testHelloWorld() throws IOException, LarJsonParseException {
        String json = "{\n" +
                "   \"hello\": true,\n" +
                "   \"foo\": [\"world\"]\n" +
                "}";
        GsonTokenParser tokenParser = tokenParser(json);
        tokenParser.beginObject();
        assertEquals("hello", tokenParser.nextName());
        assertEquals(true, tokenParser.nextBoolean());
        assertEquals("foo", tokenParser.nextName());
        tokenParser.beginArray();
        assertEquals("world", tokenParser.nextString());
        tokenParser.endArray();
        tokenParser.endObject();
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser.peek());
    }

    @Test public void testInvalidJsonInput() throws IOException, LarJsonParseException {
        String json = "{\n"
                + "   \"h\\ello\": true,\n"
                + "   \"foo\": [\"world\"]\n"
                + "}";

        GsonTokenParser tokenParser = tokenParser(json);
        tokenParser.beginObject();
        try {
            tokenParser.nextName();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Invalid escape sequence"));
        }
    }

    @SuppressWarnings("unused")
    @Test public void testNulls() {
        try {
            tokenParser(null);
            fail();
        } catch (NullPointerException expected) {
        }
    }

    @Test public void testEmptyString() throws IOException {
        try {
            tokenParser("").beginArray();
            fail();
        } catch (LarJsonParseException expected) {
        }
        try {
            tokenParser("").beginObject();
            fail();
        } catch (LarJsonParseException expected) {
        }
    }

    @Test public void testCharacterUnescaping() throws IOException, LarJsonParseException {
        String json = "[\"a\","
                + "\"a\\\"\","
                + "\"\\\"\","
                + "\":\","
                + "\",\","
                + "\"\\b\","
                + "\"\\f\","
                + "\"\\n\","
                + "\"\\r\","
                + "\"\\t\","
                + "\" \","
                + "\"\\\\\","
                + "\"{\","
                + "\"}\","
                + "\"[\","
                + "\"]\","
                + "\"\\u0000\","
                + "\"\\u0019\","
                + "\"\\u20AC\""
                + "]";
        GsonTokenParser tokenParser = tokenParser(json);
        tokenParser.beginArray();
        assertEquals("a", tokenParser.nextString());
        assertEquals("a\"", tokenParser.nextString());
        assertEquals("\"", tokenParser.nextString());
        assertEquals(":", tokenParser.nextString());
        assertEquals(",", tokenParser.nextString());
        assertEquals("\b", tokenParser.nextString());
        assertEquals("\f", tokenParser.nextString());
        assertEquals("\n", tokenParser.nextString());
        assertEquals("\r", tokenParser.nextString());
        assertEquals("\t", tokenParser.nextString());
        assertEquals(" ", tokenParser.nextString());
        assertEquals("\\", tokenParser.nextString());
        assertEquals("{", tokenParser.nextString());
        assertEquals("}", tokenParser.nextString());
        assertEquals("[", tokenParser.nextString());
        assertEquals("]", tokenParser.nextString());
        assertEquals("\0", tokenParser.nextString());
        assertEquals("\u0019", tokenParser.nextString());
        assertEquals("\u20AC", tokenParser.nextString());
        tokenParser.endArray();
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser.peek());
    }

    @Test public void testUnescapingInvalidCharacters() throws IOException, LarJsonParseException {
        String json = "[\"\\u000g\"]";
        GsonTokenParser tokenParser = tokenParser(json);
        tokenParser.beginArray();
        try {
            tokenParser.nextString();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Unrecognized number"));
        }
    }

    @Test public void testUnescapingTruncatedCharacters() throws IOException, LarJsonParseException {
        String json = "[\"\\u000";
        GsonTokenParser tokenParser = tokenParser(json);
        tokenParser.beginArray();
        try {
            tokenParser.nextString();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Unterminated escape sequence"));
        }
    }

    @Test public void testUnescapingTruncatedSequence() throws IOException, LarJsonParseException {
        String json = "[\"\\";
        GsonTokenParser tokenParser = tokenParser(json);
        tokenParser.beginArray();
        try {
            tokenParser.nextString();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Unterminated escape sequence"));
        }
    }

    @Test public void testIntegersWithFractionalPartSpecified() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[1.0,1.0,1.0]");
        tokenParser.beginArray();
        assertEquals(1.0, tokenParser.nextDouble());
        assertEquals(1, tokenParser.nextInt());
        assertEquals(1L, tokenParser.nextLong());
    }

    @Test public void testDoubles() throws IOException, LarJsonParseException {
        String json = "[-0.0,"
                + "1.0,"
                + "1.7976931348623157E308,"
                + "4.9E-324,"
                + "0.0,"
                + "-0.5,"
                + "2.2250738585072014E-308,"
                + "3.141592653589793,"
                + "2.718281828459045]";
        GsonTokenParser tokenParser = tokenParser(json);
        tokenParser.beginArray();
        assertEquals(-0.0, tokenParser.nextDouble());
        assertEquals(1.0, tokenParser.nextDouble());
        assertEquals(1.7976931348623157E308, tokenParser.nextDouble());
        assertEquals(4.9E-324, tokenParser.nextDouble());
        assertEquals(0.0, tokenParser.nextDouble());
        assertEquals(-0.5, tokenParser.nextDouble());
        assertEquals(2.2250738585072014E-308, tokenParser.nextDouble());
        assertEquals(3.141592653589793, tokenParser.nextDouble());
        assertEquals(2.718281828459045, tokenParser.nextDouble());
        tokenParser.endArray();
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser.peek());
    }

    @Test public void testStrictNonFiniteDoubles() throws IOException, LarJsonParseException {
        String json = "[NaN]";
        GsonTokenParser tokenParser = tokenParser(json);
        tokenParser.beginArray();
        try {
            tokenParser.nextDouble();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("JSON forbids NaN and infinities"));
        }
    }

    @Test public void testStrictQuotedNonFiniteDoubles() throws IOException, LarJsonParseException {
        String json = "[\"NaN\"]";
        GsonTokenParser tokenParser = tokenParser(json);
        tokenParser.beginArray();
        try {
            tokenParser.nextDouble();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("JSON forbids NaN and infinities"));
        }
    }

    @Test public void testLenientNonFiniteDoubles() throws IOException, LarJsonParseException {
        String json = "[NaN, -Infinity, Infinity]";
        GsonTokenParser tokenParser = tokenParser(json, true);
        tokenParser.beginArray();
        assertTrue(Double.isNaN(tokenParser.nextDouble()));
        assertEquals(Double.NEGATIVE_INFINITY, tokenParser.nextDouble());
        assertEquals(Double.POSITIVE_INFINITY, tokenParser.nextDouble());
        tokenParser.endArray();
    }

    @Test public void testLenientQuotedNonFiniteDoubles() throws IOException, LarJsonParseException {
        String json = "[\"NaN\", \"-Infinity\", \"Infinity\"]";
        GsonTokenParser tokenParser = tokenParser(json, true);
        tokenParser.beginArray();
        assertTrue(Double.isNaN(tokenParser.nextDouble()));
        assertEquals(Double.NEGATIVE_INFINITY, tokenParser.nextDouble());
        assertEquals(Double.POSITIVE_INFINITY, tokenParser.nextDouble());
        tokenParser.endArray();
    }

    @Test public void testLongs() throws IOException, LarJsonParseException {
        String json = "[0,0,0,"
                + "1,1,1,"
                + "-1,-1,-1,"
                + "-9223372036854775808,"
                + "9223372036854775807]";
        GsonTokenParser tokenParser = tokenParser(json);
        tokenParser.beginArray();
        assertEquals(0L, tokenParser.nextLong());
        assertEquals(0, tokenParser.nextInt());
        assertEquals(0.0, tokenParser.nextDouble());
        assertEquals(1L, tokenParser.nextLong());
        assertEquals(1, tokenParser.nextInt());
        assertEquals(1.0, tokenParser.nextDouble());
        assertEquals(-1L, tokenParser.nextLong());
        assertEquals(-1, tokenParser.nextInt());
        assertEquals(-1.0, tokenParser.nextDouble());
        try {
            tokenParser.nextInt();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Expected int but was -9223372036854775808"));
        }
    }

    @Test public void testBooleans() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[true,false]");
        tokenParser.beginArray();
        assertEquals(true, tokenParser.nextBoolean());
        assertEquals(false, tokenParser.nextBoolean());
        tokenParser.endArray();
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser.peek());
    }

    @Test public void testPeekingUnquotedStringsPrefixedWithBooleans() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[truey]", true);
        tokenParser.beginArray();
        assertEquals(LarJsonToken.SIMPLE_VALUE, tokenParser.peek());
        try {
            tokenParser.nextBoolean();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Expected a boolean but was STRING"));
        }
        assertEquals("truey", tokenParser.nextString());
        tokenParser.endArray();
    }

    @Test public void testMalformedNumbers() throws IOException, LarJsonParseException {
        assertNotANumber("-");
        assertNotANumber(".");

        // exponent lacks digit
        assertNotANumber("e");
        assertNotANumber("0e");
        assertNotANumber(".e");
        assertNotANumber("0.e");
        assertNotANumber("-.0e");

        // no integer
        assertNotANumber("e1");
        assertNotANumber(".e1");
        assertNotANumber("-e1");

        // trailing characters
        assertNotANumber("1x");
        assertNotANumber("1.1x");
        assertNotANumber("1e1x");
        assertNotANumber("1ex");
        assertNotANumber("1.1ex");
        assertNotANumber("1.1e1x");

        // fraction has no digit
        assertNotANumber("0.");
        assertNotANumber("-0.");
        assertNotANumber("0.e1");
        assertNotANumber("-0.e1");

        // no leading digit
        assertNotANumber(".0");
        assertNotANumber("-.0");
        assertNotANumber(".0e1");
        assertNotANumber("-.0e1");
    }

    private void assertNotANumber(String s) throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[" + s + "]", true);
        tokenParser.beginArray();
        assertEquals(LarJsonToken.SIMPLE_VALUE, tokenParser.peek());
        assertEquals(s, tokenParser.nextString());
        tokenParser.endArray();
    }

    @Test public void testPeekingUnquotedStringsPrefixedWithIntegers() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[12.34e5x]", true);
        tokenParser.beginArray();
        assertEquals(LarJsonToken.SIMPLE_VALUE, tokenParser.peek());
        try {
            tokenParser.nextInt();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Expected int but was STRING"));
        }
    }

    @Test public void testPeekLongMinValue() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[-9223372036854775808]", true);
        tokenParser.beginArray();
        assertEquals(LarJsonToken.SIMPLE_VALUE, tokenParser.peek());
        assertEquals(-9223372036854775808L, tokenParser.nextLong());
    }

    @Test public void testPeekLongMaxValue() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[9223372036854775807]", true);
        tokenParser.beginArray();
        assertEquals(LarJsonToken.SIMPLE_VALUE, tokenParser.peek());
        assertEquals(9223372036854775807L, tokenParser.nextLong());
    }

    @Test public void testLongLargerThanMaxLongThatWrapsAround() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[22233720368547758070]", true);
        tokenParser.beginArray();
        assertEquals(LarJsonToken.SIMPLE_VALUE, tokenParser.peek());
        try {
            tokenParser.nextLong();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Expected a long but was 22233720368547758070"));
        }
    }

    @Test public void testLongLargerThanMinLongThatWrapsAround() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[-22233720368547758070]", true);
        tokenParser.beginArray();
        assertEquals(LarJsonToken.SIMPLE_VALUE, tokenParser.peek());
        try {
            tokenParser.nextLong();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Expected a long but was -22233720368547758070"));
        }
    }

    /**
     * Issue 1053, negative zero.
     * @throws Exception
     */
    @Test public void testNegativeZero() throws Exception {
        GsonTokenParser tokenParser = tokenParser("[-0]");
        tokenParser.beginArray();
        assertEquals(LarJsonToken.SIMPLE_VALUE, tokenParser.peek());
        assertEquals("-0", tokenParser.nextString());
    }

    @Test public void testPeekMuchLargerThanLongMinValue() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[-92233720368547758080]", true);
        tokenParser.beginArray();
        assertEquals(LarJsonToken.SIMPLE_VALUE, tokenParser.peek());
        try {
            tokenParser.nextLong();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Expected a long but was -92233720368547758080"));
        }
    }

    @Test public void testQuotedNumberWithEscape() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[\"12\u00334\"]", true);
        tokenParser.beginArray();
        assertEquals(LarJsonToken.SIMPLE_VALUE, tokenParser.peek());
        assertEquals(1234, tokenParser.nextInt());
    }

    @Test public void testMixedCaseLiterals() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[True,TruE,False,FALSE,NULL,nulL]");
        tokenParser.beginArray();
        assertEquals(true, tokenParser.nextBoolean());
        assertEquals(true, tokenParser.nextBoolean());
        assertEquals(false, tokenParser.nextBoolean());
        assertEquals(false, tokenParser.nextBoolean());
        tokenParser.nextNull();
        tokenParser.nextNull();
        tokenParser.endArray();
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser.peek());
    }

    @Test public void testMissingValue() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("{\"a\":}");
        tokenParser.beginObject();
        assertEquals("a", tokenParser.nextName());
        try {
            tokenParser.nextString();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Expected value"));
        }
    }

    @Test public void testPrematureEndOfInput() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("{\"a\":true,");
        tokenParser.beginObject();
        assertEquals("a", tokenParser.nextName());
        assertEquals(true, tokenParser.nextBoolean());
        try {
            tokenParser.nextName();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("End of input"));
        }
    }

    @Test public void testIntegerMismatchFailuresDoNotAdvance() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[1.5]");
        tokenParser.beginArray();
        try {
            tokenParser.nextInt();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Expected int but was DOUBLE"));
        }
    }

    @Test public void testStringNullIsNotNull() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[\"null\"]");
        tokenParser.beginArray();
        try {
            tokenParser.nextNull();
            fail();
        } catch (LarJsonParseException expected) {
        }
    }

    @Test public void testNullLiteralIsNotAString() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[null]");
        tokenParser.beginArray();
        try {
            tokenParser.nextString();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Expected a string but was NULL"));
        }
    }

    @Test public void testStrictNameValueSeparator() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("{\"a\"=true}");
        tokenParser.beginObject();
        assertEquals("a", tokenParser.nextName());
        try {
            tokenParser.nextBoolean();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Set lenient to true to accept malformed JSON"));
        }

        tokenParser = tokenParser("{\"a\"=>true}");
        tokenParser.beginObject();
        assertEquals("a", tokenParser.nextName());
        try {
            tokenParser.nextBoolean();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Set lenient to true to accept malformed JSON"));
        }
    }

    @Test public void testLenientNameValueSeparator() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("{\"a\"=true}", true);
        tokenParser.beginObject();
        assertEquals("a", tokenParser.nextName());
        assertEquals(true, tokenParser.nextBoolean());

        tokenParser = tokenParser("{\"a\"=>true}", true);
        tokenParser.beginObject();
        assertEquals("a", tokenParser.nextName());
        assertEquals(true, tokenParser.nextBoolean());
    }

    @Test public void testStrictNameValueSeparatorWithSkipValue() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("{\"a\"=true}");
        tokenParser.beginObject();
        assertEquals("a", tokenParser.nextName());
        try {
            tokenParser.skipValue();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Set lenient to true to accept malformed JSON"));
        }

        tokenParser = tokenParser("{\"a\"=>true}");
        tokenParser.beginObject();
        assertEquals("a", tokenParser.nextName());
        try {
            tokenParser.skipValue();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Set lenient to true to accept malformed JSON"));
        }
    }

    @Test public void testCommentsInStringValue() throws Exception {
        GsonTokenParser tokenParser = tokenParser("[\"// comment\"]");
        tokenParser.beginArray();
        assertEquals("// comment", tokenParser.nextString());
        tokenParser.endArray();

        tokenParser = tokenParser("{\"a\":\"#someComment\"}");
        tokenParser.beginObject();
        assertEquals("a", tokenParser.nextName());
        assertEquals("#someComment", tokenParser.nextString());
        tokenParser.endObject();

        tokenParser = tokenParser("{\"#//a\":\"#some //Comment\"}");
        tokenParser.beginObject();
        assertEquals("#//a", tokenParser.nextName());
        assertEquals("#some //Comment", tokenParser.nextString());
        tokenParser.endObject();
    }

    @Test public void testStrictComments() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[// comment \n true]");
        tokenParser.beginArray();
        try {
            tokenParser.nextBoolean();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Set lenient to true to accept malformed JSON"));
        }

        tokenParser = tokenParser("[# comment \n true]");
        tokenParser.beginArray();
        try {
            tokenParser.nextBoolean();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Set lenient to true to accept malformed JSON"));
        }

        tokenParser = tokenParser("[/* comment */ true]");
        tokenParser.beginArray();
        try {
            tokenParser.nextBoolean();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Set lenient to true to accept malformed JSON"));
        }
    }

    @Test public void testLenientComments() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[// comment \n true]", true);
        tokenParser.beginArray();
        assertEquals(true, tokenParser.nextBoolean());

        tokenParser = tokenParser("[# comment \n true]", true);
        tokenParser.beginArray();
        assertEquals(true, tokenParser.nextBoolean());

        tokenParser = tokenParser("[/* comment */ true]", true);
        tokenParser.beginArray();
        assertEquals(true, tokenParser.nextBoolean());
    }

    @Test public void testStrictCommentsWithSkipValue() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[// comment \n true]");
        tokenParser.beginArray();
        try {
            tokenParser.skipValue();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Set lenient to true to accept malformed JSON"));
        }

        tokenParser = tokenParser("[# comment \n true]");
        tokenParser.beginArray();
        try {
            tokenParser.skipValue();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Set lenient to true to accept malformed JSON"));
        }

        tokenParser = tokenParser("[/* comment */ true]");
        tokenParser.beginArray();
        try {
            tokenParser.skipValue();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Set lenient to true to accept malformed JSON"));
        }
    }

    @Test public void testStrictUnquotedNames() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("{a:true}");
        tokenParser.beginObject();
        try {
            tokenParser.nextName();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Set lenient to true to accept malformed JSON"));
        }
    }

    @Test public void testLenientUnquotedNames() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("{a:true}", true);
        tokenParser.beginObject();
        assertEquals("a", tokenParser.nextName());
    }

    @Test public void testStrictUnquotedNamesWithSkipValue() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("{a:true}");
        tokenParser.beginObject();
        try {
            tokenParser.skipValue();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Set lenient to true to accept malformed JSON"));
        }
    }

    @Test public void testStrictSingleQuotedNames() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("{'a':true}");
        tokenParser.beginObject();
        try {
            tokenParser.nextName();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Set lenient to true to accept malformed JSON"));
        }
    }

    @Test public void testLenientSingleQuotedNames() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("{'a':true}", true);
        tokenParser.beginObject();
        assertEquals("a", tokenParser.nextName());
    }

    @Test public void testStrictSingleQuotedNamesWithSkipValue() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("{'a':true}");
        tokenParser.beginObject();
        try {
            tokenParser.skipValue();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Set lenient to true to accept malformed JSON"));
        }
    }

    @Test public void testStrictUnquotedStrings() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[a]");
        tokenParser.beginArray();
        try {
            tokenParser.nextString();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Set lenient to true to accept malformed JSON"));
        }
    }

    @Test public void testLenientUnquotedStrings() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[a]", true);
        tokenParser.beginArray();
        assertEquals("a", tokenParser.nextString());
    }

    @Test public void testStrictSingleQuotedStrings() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("['a']");
        tokenParser.beginArray();
        try {
            tokenParser.nextString();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Set lenient to true to accept malformed JSON"));
        }
    }

    @Test public void testLenientSingleQuotedStrings() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("['a']", true);
        tokenParser.beginArray();
        assertEquals("a", tokenParser.nextString());
    }

    @Test public void testStrictSingleQuotedStringsWithSkipValue() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("['a']");
        tokenParser.beginArray();
        try {
            tokenParser.skipValue();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Set lenient to true to accept malformed JSON"));
        }
    }

    @Test public void testStrictSemicolonDelimitedArray() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[true;true]");
        tokenParser.beginArray();
        try {
            tokenParser.nextBoolean();
            tokenParser.nextBoolean();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Set lenient to true to accept malformed JSON"));
        }
    }

    @Test public void testLenientSemicolonDelimitedArray() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[true;true]", true);
        tokenParser.beginArray();
        assertEquals(true, tokenParser.nextBoolean());
        assertEquals(true, tokenParser.nextBoolean());
    }

    @Test public void testStrictSemicolonDelimitedArrayWithSkipValue() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[true;true]");
        tokenParser.beginArray();
        try {
            tokenParser.skipValue();
            tokenParser.skipValue();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Set lenient to true to accept malformed JSON"));
        }
    }

    @Test public void testStrictSemicolonDelimitedNameValuePair() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("{\"a\":true;\"b\":true}");
        tokenParser.beginObject();
        assertEquals("a", tokenParser.nextName());
        try {
            tokenParser.nextBoolean();
            tokenParser.nextName();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Set lenient to true to accept malformed JSON"));
        }
    }

    @Test public void testLenientSemicolonDelimitedNameValuePair() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("{\"a\":true;\"b\":true}", true);
        tokenParser.beginObject();
        assertEquals("a", tokenParser.nextName());
        assertEquals(true, tokenParser.nextBoolean());
        assertEquals("b", tokenParser.nextName());
    }

    @Test public void testStrictSemicolonDelimitedNameValuePairWithSkipValue() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("{\"a\":true;\"b\":true}");
        tokenParser.beginObject();
        assertEquals("a", tokenParser.nextName());
        try {
            tokenParser.skipValue();
            tokenParser.skipValue();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Set lenient to true to accept malformed JSON"));
        }
    }

    @Test public void testStrictUnnecessaryArraySeparators() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[true,,true]");
        tokenParser.beginArray();
        assertEquals(true, tokenParser.nextBoolean());
        try {
            tokenParser.nextNull();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Set lenient to true to accept malformed JSON"));
        }

        tokenParser = tokenParser("[,true]");
        tokenParser.beginArray();
        try {
            tokenParser.nextNull();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Set lenient to true to accept malformed JSON"));
        }

        tokenParser = tokenParser("[true,]");
        tokenParser.beginArray();
        assertEquals(true, tokenParser.nextBoolean());
        try {
            tokenParser.nextNull();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Set lenient to true to accept malformed JSON"));
        }

        tokenParser = tokenParser("[,]");
        tokenParser.beginArray();
        try {
            tokenParser.nextNull();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Set lenient to true to accept malformed JSON"));
        }
    }

    @Test public void testLenientUnnecessaryArraySeparators() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[true,,true]", true);
        tokenParser.beginArray();
        assertEquals(true, tokenParser.nextBoolean());
        tokenParser.nextNull();
        assertEquals(true, tokenParser.nextBoolean());
        tokenParser.endArray();

        tokenParser = tokenParser("[,true]", true);
        tokenParser.beginArray();
        tokenParser.nextNull();
        assertEquals(true, tokenParser.nextBoolean());
        tokenParser.endArray();

        tokenParser = tokenParser("[true,]", true);
        tokenParser.beginArray();
        assertEquals(true, tokenParser.nextBoolean());
        tokenParser.nextNull();
        tokenParser.endArray();

        tokenParser = tokenParser("[,]", true);
        tokenParser.beginArray();
        tokenParser.nextNull();
        tokenParser.nextNull();
        tokenParser.endArray();
    }

    @Test public void testStrictUnnecessaryArraySeparatorsWithSkipValue() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[true,,true]");
        tokenParser.beginArray();
        assertEquals(true, tokenParser.nextBoolean());
        try {
            tokenParser.skipValue();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Set lenient to true to accept malformed JSON"));
        }

        tokenParser = tokenParser("[,true]");
        tokenParser.beginArray();
        try {
            tokenParser.skipValue();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Set lenient to true to accept malformed JSON"));
        }

        tokenParser = tokenParser("[true,]");
        tokenParser.beginArray();
        assertEquals(true, tokenParser.nextBoolean());
        try {
            tokenParser.skipValue();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Set lenient to true to accept malformed JSON"));
        }

        tokenParser = tokenParser("[,]");
        tokenParser.beginArray();
        try {
            tokenParser.skipValue();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Set lenient to true to accept malformed JSON"));
        }
    }

    @Test public void testStrictMultipleTopLevelValues() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[] []");
        tokenParser.beginArray();
        tokenParser.endArray();
        try {
            tokenParser.peek();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Set lenient to true to accept malformed JSON"));
        }
    }

    @Test public void testLenientMultipleTopLevelValues() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[] true {}", true);
        tokenParser.beginArray();
        tokenParser.endArray();
        assertEquals(true, tokenParser.nextBoolean());
        tokenParser.beginObject();
        tokenParser.endObject();
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser.peek());
    }

    @Test public void testStrictMultipleTopLevelValuesWithSkipValue() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[] []");
        tokenParser.beginArray();
        tokenParser.endArray();
        try {
            tokenParser.skipValue();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Set lenient to true to accept malformed JSON"));
        }
    }

    @Test public void testTopLevelValueTypes() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser1 = tokenParser("true");
        assertTrue(tokenParser1.nextBoolean());
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser1.peek());

        GsonTokenParser tokenParser2 = tokenParser("false");
        assertFalse(tokenParser2.nextBoolean());
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser2.peek());

        GsonTokenParser tokenParser3 = tokenParser("null");
        assertEquals(LarJsonToken.NULL, tokenParser3.peek());
        tokenParser3.nextNull();
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser3.peek());

        GsonTokenParser tokenParser4 = tokenParser("123");
        assertEquals(123, tokenParser4.nextInt());
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser4.peek());

        GsonTokenParser tokenParser5 = tokenParser("123.4");
        assertEquals(123.4, tokenParser5.nextDouble());
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser5.peek());

        GsonTokenParser tokenParser6 = tokenParser("\"a\"");
        assertEquals("a", tokenParser6.nextString());
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser6.peek());
    }

    @Test public void testTopLevelValueTypeWithSkipValue() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("true");
        tokenParser.skipValue();
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser.peek());
    }

    @Test public void testStrictNonExecutePrefix() throws LarJsonParseException, IOException {
        GsonTokenParser tokenParser = tokenParser(")]}'\n []");
        try {
            tokenParser.beginArray();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Expected BEGIN_ARRAY but was STRING"));
        }
    }

    @Test public void testLenientNonExecutePrefix() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser(")]}'\n []", true);
        tokenParser.beginArray();
        tokenParser.endArray();
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser.peek());
    }

    @Test public void testLenientNonExecutePrefixWithLeadingWhitespace() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("\r\n \t)]}'\n []", true);
        tokenParser.beginArray();
        tokenParser.endArray();
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser.peek());
    }

    @Test public void testLenientPartialNonExecutePrefix() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser(")]}' []", true);
        try {
            assertEquals(")", tokenParser.nextString());
            tokenParser.nextString();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Unexpected value"));
        }
    }

    @Test public void testBomIgnoredAsFirstCharacterOfDocument() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("\ufeff[]");
        tokenParser.beginArray();
        tokenParser.endArray();
    }

    @Test public void testBomForbiddenAsOtherCharacterInDocument() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[\ufeff]");
        tokenParser.beginArray();
        try {
            tokenParser.endArray();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Expected END_ARRAY but was STRING"));
        }
    }

    @Test public void testFailWithPosition() throws IOException, LarJsonParseException {
        testFailWithPosition("Expected value at line 6 column 5 path $[1]",
                "[\n\n\n\n\n\"a\",}]");
    }

    @Test public void testFailWithPositionGreaterThanBufferSize() throws IOException, LarJsonParseException {
        String spaces = repeat(' ', 8192);
        testFailWithPosition("Expected value at line 6 column 5 path $[1]",
                "[\n\n" + spaces + "\n\n\n\"a\",}]");
    }

    @Test public void testFailWithPositionOverSlashSlashEndOfLineComment() throws IOException, LarJsonParseException {
        testFailWithPosition("Expected value at line 5 column 6 path $[1]",
                "\n// foo\n\n//bar\r\n[\"a\",}");
    }

    @Test public void testFailWithPositionOverHashEndOfLineComment() throws IOException, LarJsonParseException {
        testFailWithPosition("Expected value at line 5 column 6 path $[1]",
                "\n# foo\n\n#bar\r\n[\"a\",}");
    }

    @Test public void testFailWithPositionOverCStyleComment() throws IOException, LarJsonParseException {
        testFailWithPosition("Expected value at line 6 column 12 path $[1]",
                "\n\n/* foo\n*\n*\r\nbar */[\"a\",}");
    }

    @Test public void testFailWithPositionOverQuotedString() throws IOException, LarJsonParseException {
        testFailWithPosition("Expected value at line 5 column 3 path $[1]",
                "[\"foo\nbar\r\nbaz\n\",\n  }");
    }

    @Test public void testFailWithPositionOverUnquotedString() throws IOException, LarJsonParseException {
        testFailWithPosition("Expected value at line 5 column 2 path $[1]", "[\n\nabcd\n\n,}");
    }

    @Test public void testFailWithEscapedNewlineCharacter() throws IOException, LarJsonParseException {
        testFailWithPosition("Expected value at line 5 column 3 path $[1]", "[\n\n\"\\\n\n\",}");
    }

    @Test public void testFailWithPositionIsOffsetByBom() throws IOException, LarJsonParseException {
        testFailWithPosition("Expected value at line 1 column 6 path $[1]",
                "\ufeff[\"a\",}]");
    }

    private void testFailWithPosition(String message, String json) throws IOException, LarJsonParseException {
        // Validate that it works reading the string normally.
        GsonTokenParser tokenParser1 = tokenParser(json, true);
        tokenParser1.beginArray();
        tokenParser1.nextString();
        try {
            tokenParser1.peek();
            fail();
        } catch (LarJsonParseException expected) {
            assertEquals(message, expected.getMessage());
        }

        // Also validate that it works when skipping.
        GsonTokenParser tokenParser2 = tokenParser(json, true);
        tokenParser2.beginArray();
        tokenParser2.skipValue();
        try {
            tokenParser2.peek();
            fail();
        } catch (LarJsonParseException expected) {
            assertEquals(message, expected.getMessage());
        }
    }

    @Test public void testFailWithPositionDeepPath() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[1,{\"a\":[2,3,}");
        tokenParser.beginArray();
        tokenParser.nextInt();
        tokenParser.beginObject();
        tokenParser.nextName();
        tokenParser.beginArray();
        tokenParser.nextInt();
        tokenParser.nextInt();
        try {
            tokenParser.peek();
            fail();
        } catch (LarJsonParseException expected) {
            assertEquals("Expected value at line 1 column 14 path $[1].a[2]", expected.getMessage());
        }
    }

    @Test public void testLenientVeryLongNumber() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[0." + repeat('9', 8192) + "]");
        tokenParser.beginArray();
        assertEquals(LarJsonToken.SIMPLE_VALUE, tokenParser.peek());
        assertEquals(1d, tokenParser.nextDouble());
        tokenParser.endArray();
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser.peek());
    }

    @Test public void testVeryLongUnquotedLiteral() throws IOException, LarJsonParseException {
        String literal = "a" + repeat('b', 8192) + "c";
        GsonTokenParser tokenParser = tokenParser("[" + literal + "]", true);
        tokenParser.beginArray();
        assertEquals(literal, tokenParser.nextString());
        tokenParser.endArray();
    }

    @Test public void testDeeplyNestedArrays() throws IOException, LarJsonParseException {
        // this is nested 40 levels deep; Gson is tuned for nesting is 30 levels deep or fewer
        GsonTokenParser tokenParser = tokenParser(
                "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]");
        for (int i = 0; i < 40; i++) {
            tokenParser.beginArray();
        }
        assertEquals("$[0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0]"
                + "[0][0][0][0][0][0][0][0][0][0][0][0][0][0]", tokenParser.getPath());
        for (int i = 0; i < 40; i++) {
            tokenParser.endArray();
        }
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser.peek());
    }

    @Test public void testDeeplyNestedObjects() throws IOException, LarJsonParseException {
        // Build a JSON document structured like {"a":{"a":{"a":{"a":true}}}}, but 40 levels deep
        String array = "{\"a\":%s}";
        String json = "true";
        for (int i = 0; i < 40; i++) {
            json = String.format(array, json);
        }

        GsonTokenParser tokenParser = tokenParser(json);
        for (int i = 0; i < 40; i++) {
            tokenParser.beginObject();
            assertEquals("a", tokenParser.nextName());
        }
        assertEquals("$.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a"
                + ".a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a", tokenParser.getPath());
        assertEquals(true, tokenParser.nextBoolean());
        for (int i = 0; i < 40; i++) {
            tokenParser.endObject();
        }
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser.peek());
    }

    @Test public void testStringEndingInSlash() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("/", true);
        try {
            tokenParser.peek();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Expected value"));
        }
    }

    @Test public void testDocumentWithCommentEndingInSlash() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("/* foo *//", true);
        try {
            tokenParser.peek();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Expected value"));
        }
    }

    @Test public void testStringWithLeadingSlash() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("/x", true);
        try {
            tokenParser.peek();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Expected value"));
        }
    }

    @Test public void testUnterminatedObject() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("{\"a\":\"android\"x", true);
        tokenParser.beginObject();
        assertEquals("a", tokenParser.nextName());
        assertEquals("android", tokenParser.nextString());
        try {
            tokenParser.peek();
            fail();
        } catch (LarJsonParseException expected) {
            assertTrue(expected.getMessage().contains("Unterminated object"));
        }
    }

    @Test public void testVeryLongQuotedString() throws IOException, LarJsonParseException {
        char[] stringChars = new char[1024 * 16];
        Arrays.fill(stringChars, 'x');
        String string = new String(stringChars);
        String json = "[\"" + string + "\"]";
        GsonTokenParser tokenParser = tokenParser(json);
        tokenParser.beginArray();
        assertEquals(string, tokenParser.nextString());
        tokenParser.endArray();
    }

    @Test public void testVeryLongUnquotedString() throws IOException, LarJsonParseException {
        char[] stringChars = new char[1024 * 16];
        Arrays.fill(stringChars, 'x');
        String string = new String(stringChars);
        String json = "[" + string + "]";
        GsonTokenParser tokenParser = tokenParser(json, true);
        tokenParser.beginArray();
        assertEquals(string, tokenParser.nextString());
        tokenParser.endArray();
    }

    @Test public void testVeryLongUnterminatedString() throws IOException, LarJsonParseException {
        char[] stringChars = new char[1024 * 16];
        Arrays.fill(stringChars, 'x');
        String string = new String(stringChars);
        String json = "[" + string;
        GsonTokenParser tokenParser = tokenParser(json, true);
        tokenParser.beginArray();
        assertEquals(string, tokenParser.nextString());
        try {
            tokenParser.peek();
            fail();
        } catch (LarJsonException expected) {
            assertTrue(expected.getMessage().contains("End of input"));
        }
    }

    @Test public void testSkipVeryLongUnquotedString() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[" + repeat('x', 8192) + "]", true);
        tokenParser.beginArray();
        tokenParser.skipValue();
        tokenParser.endArray();
    }

    @Test public void testSkipTopLevelUnquotedString() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser(repeat('x', 8192), true);
        tokenParser.skipValue();
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser.peek());
    }

    @Test public void testSkipVeryLongQuotedString() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[\"" + repeat('x', 8192) + "\"]");
        tokenParser.beginArray();
        tokenParser.skipValue();
        tokenParser.endArray();
    }

    @Test public void testSkipTopLevelQuotedString() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("\"" + repeat('x', 8192) + "\"", true);
        tokenParser.skipValue();
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser.peek());
    }

    @Test public void testStringAsNumberWithTruncatedExponent() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[123e]", true);
        tokenParser.beginArray();
        assertEquals(LarJsonToken.SIMPLE_VALUE, tokenParser.peek());
    }

    @Test public void testStringAsNumberWithDigitAndNonDigitExponent() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[123e4b]", true);
        tokenParser.beginArray();
        assertEquals(LarJsonToken.SIMPLE_VALUE, tokenParser.peek());
    }

    @Test public void testStringAsNumberWithNonDigitExponent() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[123eb]", true);
        tokenParser.beginArray();
        assertEquals(LarJsonToken.SIMPLE_VALUE, tokenParser.peek());
    }

    @Test public void testEmptyStringName() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("{\"\":true}", true);
        assertEquals(LarJsonToken.BEGIN_OBJECT, tokenParser.peek());
        tokenParser.beginObject();
        assertEquals(LarJsonToken.NAME, tokenParser.peek());
        assertEquals("", tokenParser.nextName());
        assertEquals(LarJsonToken.SIMPLE_VALUE, tokenParser.peek());
        assertEquals(true, tokenParser.nextBoolean());
        assertEquals(LarJsonToken.END_OBJECT, tokenParser.peek());
        tokenParser.endObject();
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser.peek());
    }

    @Test public void testStrictExtraCommasInMaps() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("{\"a\":\"b\",}");
        tokenParser.beginObject();
        assertEquals("a", tokenParser.nextName());
        assertEquals("b", tokenParser.nextString());
        try {
            tokenParser.peek();
            fail();
        } catch (LarJsonException expected) {
            assertTrue(expected.getMessage().contains("Expected name"));
        }
    }

    @Test public void testLenientExtraCommasInMaps() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("{\"a\":\"b\",}", true);
        tokenParser.beginObject();
        assertEquals("a", tokenParser.nextName());
        assertEquals("b", tokenParser.nextString());
        try {
            tokenParser.peek();
            fail();
        } catch (LarJsonException expected) {
            assertTrue(expected.getMessage().contains("Expected name"));
        }
    }

    @Test public void testGetPositionString() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[true, \"hello\"]");
        tokenParser.beginArray();
        tokenParser.nextBoolean();
        assertEquals(7, tokenParser.getCurrentPosition());
        tokenParser.nextString();
        tokenParser.endArray();
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser.peek());
    }

    @Test public void testGetPositionBoolean() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[true, \"hello\"]");
        tokenParser.beginArray();
        assertEquals(1, tokenParser.getCurrentPosition());
        tokenParser.nextBoolean();
        tokenParser.nextString();
        tokenParser.endArray();
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser.peek());
    }

    @Test public void testGetPositionNumber() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[null,   12.33, \"hello\"]");
        tokenParser.beginArray();
        tokenParser.nextNull();
        assertEquals(9, tokenParser.getCurrentPosition());
        tokenParser.nextNumber();
        tokenParser.nextString();
        tokenParser.endArray();
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser.peek());
    }

    @Test public void testGetPositionObject() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[null,   12.33,   {  \"hello\":20}]");
        tokenParser.beginArray();
        tokenParser.nextNull();
        tokenParser.nextNumber();
        assertEquals(18, tokenParser.getCurrentPosition());
        tokenParser.beginObject();
        tokenParser.nextName();
        tokenParser.nextNumber();
        tokenParser.endObject();
        tokenParser.endArray();
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser.peek());
    }

    @Test public void testGetPositionArray() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[null,   12.33,   [  20]]");
        assertEquals(0, tokenParser.getCurrentPosition());
        tokenParser.beginArray();
        tokenParser.nextNull();
        tokenParser.nextNumber();
        assertEquals(18, tokenParser.getCurrentPosition());
        tokenParser.beginArray();
        tokenParser.nextNumber();
        tokenParser.endArray();
        tokenParser.endArray();
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser.peek());
    }

    @Test public void testGetPositionNull() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[null,   12.33,   [  20]]");
        tokenParser.beginArray();
        assertEquals(1, tokenParser.getCurrentPosition());
        tokenParser.nextNull();
        tokenParser.nextNumber();
        tokenParser.beginArray();
        tokenParser.nextNumber();
        tokenParser.endArray();
        tokenParser.endArray();
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser.peek());
    }

    @Test public void testGetPositionUtf8() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[null, \"\u0644\u0627\u0631\u062c\u0633\u0648\u0646\" ,[  20]]");
        tokenParser.beginArray();
        tokenParser.nextNull();
        tokenParser.nextString();
        assertEquals(25, tokenParser.getCurrentPosition());
        tokenParser.beginArray();
        tokenParser.nextNumber();
        tokenParser.endArray();
        tokenParser.endArray();
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser.peek());
    }

    @Test public void testParseBigInteger() throws IOException, LarJsonParseException {
        String str = "784" + repeat('8', 9999) + "135";
        GsonTokenParser tokenParser = tokenParser("[null, " + str + "]");
        tokenParser.beginArray();
        tokenParser.nextNull();
        assertEquals(new BigInteger(str), tokenParser.nextBigInteger());
        tokenParser.endArray();
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser.peek());
    }

    @Test public void testParseBigDecimal() throws IOException, LarJsonParseException {
        String str = "784.60" + repeat('8', 9999) + "135";
        GsonTokenParser tokenParser = tokenParser("[null, " + str + "]");
        tokenParser.beginArray();
        tokenParser.nextNull();
        assertEquals(new BigDecimal(str), tokenParser.nextBigDecimal());
        tokenParser.endArray();
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser.peek());
    }

    @Test public void testParseNumber() throws IOException, LarJsonParseException {
        String str = "0.12" + repeat('8', 9999) + "135";
        GsonTokenParser tokenParser = tokenParser("[null, " + str + "]");
        tokenParser.beginArray();
        tokenParser.nextNull();
        assertEquals(new BigDecimal(str), tokenParser.nextNumber());
        tokenParser.endArray();
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser.peek());
    }

    @Test public void testParseChar() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[null, \"{\"]");
        tokenParser.beginArray();
        tokenParser.nextNull();
        assertEquals('{', tokenParser.nextChar());
        tokenParser.endArray();
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser.peek());
    }

    @Test public void testParseByte() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[null, -13]");
        tokenParser.beginArray();
        tokenParser.nextNull();
        assertEquals(-13, tokenParser.nextByte());
        tokenParser.endArray();
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser.peek());
    }

    @Test public void testParseShort() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[null, 1783]");
        tokenParser.beginArray();
        tokenParser.nextNull();
        assertEquals(1783, tokenParser.nextShort());
        tokenParser.endArray();
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser.peek());
    }

    @Test public void testParseUtf8String() throws IOException, LarJsonParseException {
        String str = "لارجسون";
        GsonTokenParser tokenParser = tokenParser("[null, \"" + str + "\"]");
        tokenParser.beginArray();
        tokenParser.nextNull();
        assertEquals(str, tokenParser.nextString());
        tokenParser.endArray();
        assertEquals(LarJsonToken.END_DOCUMENT, tokenParser.peek());
    }

    private String repeat(char c, int count) {
        char[] array = new char[count];
        Arrays.fill(array, c);
        return new String(array);
    }

    @Test public void testMalformedDocuments() throws IOException, LarJsonParseException {
        assertDocument("{]", LarJsonToken.BEGIN_OBJECT, LarJsonParseException.class);
        assertDocument("{,", LarJsonToken.BEGIN_OBJECT, LarJsonParseException.class);
        assertDocument("{{", LarJsonToken.BEGIN_OBJECT, LarJsonParseException.class);
        assertDocument("{[", LarJsonToken.BEGIN_OBJECT, LarJsonParseException.class);
        assertDocument("{:", LarJsonToken.BEGIN_OBJECT, LarJsonParseException.class);
        assertDocument("{\"name\",", LarJsonToken.BEGIN_OBJECT, LarJsonToken.NAME, LarJsonParseException.class);
        assertDocument("{\"name\",", LarJsonToken.BEGIN_OBJECT, LarJsonToken.NAME, LarJsonParseException.class);
        assertDocument("{\"name\":}", LarJsonToken.BEGIN_OBJECT, LarJsonToken.NAME, LarJsonParseException.class);
        assertDocument("{\"name\"::", LarJsonToken.BEGIN_OBJECT, LarJsonToken.NAME, LarJsonParseException.class);
        assertDocument("{\"name\":,", LarJsonToken.BEGIN_OBJECT, LarJsonToken.NAME, LarJsonParseException.class);
        assertDocument("{\"name\"=}", LarJsonToken.BEGIN_OBJECT, LarJsonToken.NAME, LarJsonParseException.class);
        assertDocument("{\"name\"=>}", LarJsonToken.BEGIN_OBJECT, LarJsonToken.NAME, LarJsonParseException.class);
        assertDocument("{\"name\"=>\"string\":", LarJsonToken.BEGIN_OBJECT, LarJsonToken.NAME, LarJsonToken.SIMPLE_VALUE, LarJsonParseException.class);
        assertDocument("{\"name\"=>\"string\"=", LarJsonToken.BEGIN_OBJECT, LarJsonToken.NAME, LarJsonToken.SIMPLE_VALUE, LarJsonParseException.class);
        assertDocument("{\"name\"=>\"string\"=>", LarJsonToken.BEGIN_OBJECT, LarJsonToken.NAME, LarJsonToken.SIMPLE_VALUE, LarJsonParseException.class);
        assertDocument("{\"name\"=>\"string\",", LarJsonToken.BEGIN_OBJECT, LarJsonToken.NAME, LarJsonToken.SIMPLE_VALUE, LarJsonParseException.class);
        assertDocument("{\"name\"=>\"string\",\"name\"", LarJsonToken.BEGIN_OBJECT, LarJsonToken.NAME, LarJsonToken.SIMPLE_VALUE, LarJsonToken.NAME);
        assertDocument("[}", LarJsonToken.BEGIN_ARRAY, LarJsonParseException.class);
        assertDocument("[,]", LarJsonToken.BEGIN_ARRAY, LarJsonToken.NULL, LarJsonToken.NULL, LarJsonToken.END_ARRAY);
        assertDocument("{", LarJsonToken.BEGIN_OBJECT, LarJsonParseException.class);
        assertDocument("{\"name\"", LarJsonToken.BEGIN_OBJECT, LarJsonToken.NAME, LarJsonParseException.class);
        assertDocument("{\"name\",", LarJsonToken.BEGIN_OBJECT, LarJsonToken.NAME, LarJsonParseException.class);
        assertDocument("{'name'", LarJsonToken.BEGIN_OBJECT, LarJsonToken.NAME, LarJsonParseException.class);
        assertDocument("{'name',", LarJsonToken.BEGIN_OBJECT, LarJsonToken.NAME, LarJsonParseException.class);
        assertDocument("{name", LarJsonToken.BEGIN_OBJECT, LarJsonToken.NAME, LarJsonParseException.class);
        assertDocument("[", LarJsonToken.BEGIN_ARRAY, LarJsonParseException.class);
        assertDocument("[string", LarJsonToken.BEGIN_ARRAY, LarJsonToken.SIMPLE_VALUE, LarJsonParseException.class);
        assertDocument("[\"string\"", LarJsonToken.BEGIN_ARRAY, LarJsonToken.SIMPLE_VALUE, LarJsonParseException.class);
        assertDocument("{\"name\":\"string\"", LarJsonToken.BEGIN_OBJECT, LarJsonToken.NAME, LarJsonToken.SIMPLE_VALUE, LarJsonParseException.class);
        assertDocument("{\"name\":\"string\",", LarJsonToken.BEGIN_OBJECT, LarJsonToken.NAME, LarJsonToken.SIMPLE_VALUE, LarJsonParseException.class);
        assertDocument("{\"name\":'string'", LarJsonToken.BEGIN_OBJECT, LarJsonToken.NAME, LarJsonToken.SIMPLE_VALUE, LarJsonParseException.class);
        assertDocument("{\"name\":'string',", LarJsonToken.BEGIN_OBJECT, LarJsonToken.NAME, LarJsonToken.SIMPLE_VALUE, LarJsonParseException.class);
    }

    @Test public void testUnterminatedStringFailure() throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser("[\"string", true);
        tokenParser.beginArray();
        assertEquals(LarJsonToken.SIMPLE_VALUE, tokenParser.peek());
        try {
            tokenParser.nextString();
            fail();
        } catch (LarJsonException expected) {
            assertTrue(expected.getMessage().contains("Unterminated string"));
        }
    }

    private void assertDocument(String document, Object... expectations) throws IOException, LarJsonParseException {
        GsonTokenParser tokenParser = tokenParser(document, true);
        for (Object expectation : expectations) {
            if (expectation == LarJsonToken.BEGIN_OBJECT) {
                tokenParser.beginObject();
            } else if (expectation == LarJsonToken.BEGIN_ARRAY) {
                tokenParser.beginArray();
            } else if (expectation == LarJsonToken.END_OBJECT) {
                tokenParser.endObject();
            } else if (expectation == LarJsonToken.END_ARRAY) {
                tokenParser.endArray();
            } else if (expectation == LarJsonToken.NAME) {
                assertEquals("name", tokenParser.nextName());
            } else if (expectation == LarJsonToken.SIMPLE_VALUE) {
                assertEquals("string", tokenParser.nextString());
            } else if (expectation == LarJsonToken.NULL) {
                tokenParser.nextNull();
            } else if (expectation == LarJsonParseException.class) {
                try {
                    tokenParser.peek();
                    fail();
                } catch (LarJsonParseException expected) {
                }
            } else {
                throw new AssertionError();
            }
        }
    }

    private GsonTokenParser tokenParser(String s) {
        return tokenParser(s, false);
    }

    private GsonTokenParser tokenParser(String s, boolean lenient) {
        return new GsonTokenParser(byteStream(s), new Utf8CharacterDecoder(), lenient);
    }
    
    private ByteStream byteStream(final String s) {
        return new ArrayByteStream(s, StandardCharsets.UTF_8);
    }
}
