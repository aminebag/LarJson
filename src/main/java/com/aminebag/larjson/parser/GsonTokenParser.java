package com.aminebag.larjson.parser;

import com.aminebag.larjson.chardecoder.CharacterDecoder;
import com.aminebag.larjson.chardecoder.CharacterDecodingException;
import com.aminebag.larjson.stream.ByteStream;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.function.*;

/**
 * Credits : This code is inspired, to a great degree, by the class JsonReader of the Gson library
 *
 * {@inheritDoc}
 *
 * @author Amine Bagdouri
 */
public class GsonTokenParser implements LarJsonTokenParser{

    private static final long MIN_INCOMPLETE_INTEGER = Long.MIN_VALUE / 10;

    private static final int PEEKED_NONE = 0;
    private static final int PEEKED_BEGIN_OBJECT = 1;
    private static final int PEEKED_END_OBJECT = 2;
    private static final int PEEKED_BEGIN_ARRAY = 3;
    private static final int PEEKED_END_ARRAY = 4;
    private static final int PEEKED_TRUE = 5;
    private static final int PEEKED_FALSE = 6;
    private static final int PEEKED_NULL = 7;
    private static final int PEEKED_SINGLE_QUOTED = 8;
    private static final int PEEKED_DOUBLE_QUOTED = 9;
    private static final int PEEKED_UNQUOTED = 10;
    private static final int PEEKED_SINGLE_QUOTED_NAME = 12;
    private static final int PEEKED_DOUBLE_QUOTED_NAME = 13;
    private static final int PEEKED_UNQUOTED_NAME = 14;
    /** When this is returned, the integer value is stored in peekedLong. */
    private static final int PEEKED_LONG = 15;
    private static final int PEEKED_NUMBER = 16;
    private static final int PEEKED_EOF = 17;

    /* State machine when parsing numbers */
    private static final int NUMBER_CHAR_NONE = 0;
    private static final int NUMBER_CHAR_SIGN = 1;
    private static final int NUMBER_CHAR_DIGIT = 2;
    private static final int NUMBER_CHAR_DECIMAL = 3;
    private static final int NUMBER_CHAR_FRACTION_DIGIT = 4;
    private static final int NUMBER_CHAR_EXP_E = 5;
    private static final int NUMBER_CHAR_EXP_SIGN = 6;
    private static final int NUMBER_CHAR_EXP_DIGIT = 7;

    private static final int BUFFER_SIZE = 256;

    private final ByteStream byteStream;
    private final CharacterDecoder characterDecoder;
    private final boolean lenient;

    public GsonTokenParser(ByteStream byteStream, CharacterDecoder characterDecoder, boolean lenient) {
        this.byteStream = byteStream;
        this.characterDecoder = characterDecoder;
        this.lenient = lenient;
    }

    /**
     * Use a manual buffer to easily read and unread upcoming characters, and
     * also so we can create strings without an intermediate StringBuilder.
     * We decode literals directly out of this buffer, so it must be at least as
     * long as the longest token that can be reported as a number.
     */
    private final char[] buffer = new char[BUFFER_SIZE];
    private final long[] distances = new long[BUFFER_SIZE];

    private int pos = 0;
    private int limit = 0;

    private int lineNumber = 0;
    private int lineStart = 0;

    private int peekedLength = 0;

    private int peeked = PEEKED_NONE;

    /**
     * A peeked value that was composed entirely of digits with an optional
     * leading dash. Positive values may not have a leading 0.
     */
    private long peekedLong;

    /*
     * The nesting stack. Using a manual array rather than an ArrayList saves 20%.
     */
    private int[] stack = new int[32];
    private int stackSize = 0;
    {
        stack[stackSize++] = LarJsonScope.EMPTY_DOCUMENT;
    }

    /*
     * The path members. It corresponds directly to stack: At indices where the
     * stack contains an object (EMPTY_OBJECT, DANGLING_NAME or NONEMPTY_OBJECT),
     * pathNames contains the name at this scope. Where it contains an array
     * (EMPTY_ARRAY, NONEMPTY_ARRAY) pathIndices contains the current index in
     * that array. Otherwise the value is undefined, and we take advantage of that
     * by incrementing pathIndices when doing so isn't useful.
     */
    private String[] pathNames = new String[32];
    private int[] pathIndices = new int[32];

    /**
     * By default, this parser is strict and only accepts JSON as specified by <a
     * href="http://www.ietf.org/rfc/rfc4627.txt">RFC 4627</a>. Setting the
     * parser to lenient causes it to ignore the following syntax errors:
     *
     * <ul>
     *   <li>Streams that start with the <a href="#nonexecuteprefix">non-execute
     *       prefix</a>, <code>")]}'\n"</code>.
     *   <li>Streams that include multiple top-level values. With strict parsing,
     *       each stream must contain exactly one top-level value.
     *   <li>Top-level values of any type. With strict parsing, the top-level
     *       value must be an object or an array.
     *   <li>Numbers may be {@link Double#isNaN() NaNs} or {@link
     *       Double#isInfinite() infinities}.
     *   <li>End of line comments starting with {@code //} or {@code #} and
     *       ending with a newline character.
     *   <li>C-style comments starting with {@code /*} and ending with
     *       {@code *}{@code /}. Such comments may not be nested.
     *   <li>Names that are unquoted or {@code 'single quoted'}.
     *   <li>Strings that are unquoted or {@code 'single quoted'}.
     *   <li>Array elements separated by {@code ;} instead of {@code ,}.
     *   <li>Unnecessary array separators. These are interpreted as if null
     *       was the omitted value.
     *   <li>Names and values separated by {@code =} or {@code =>} instead of
     *       {@code :}.
     *   <li>Name/value pairs separated by {@code ;} instead of {@code ,}.
     * </ul>
     * 
     * Returns true if this parser is liberal in what it accepts.
     */
    public final boolean isLenient() {
        return lenient;
    }

    /**
     * {@inheritDoc}
     */
    public void beginArray() throws IOException, LarJsonParseException {
        int p = peeked;
        if (p == PEEKED_NONE) {
            p = doPeek();
        }
        if (p == PEEKED_BEGIN_ARRAY) {
            pos++;
            push(LarJsonScope.EMPTY_ARRAY);
            pathIndices[stackSize - 1] = 0;
            peeked = PEEKED_NONE;
        } else {
            throw new LarJsonParseException("Expected BEGIN_ARRAY but was " + peekedToString() + locationString());
        }
    }

    /**
     * {@inheritDoc}
     */
    public void endArray() throws IOException, LarJsonParseException {
        int p = peeked;
        if (p == PEEKED_NONE) {
            p = doPeek();
        }
        if (p == PEEKED_END_ARRAY) {
            stackSize--;
            pathIndices[stackSize - 1]++;
            peeked = PEEKED_NONE;
        } else {
            throw new LarJsonParseException("Expected END_ARRAY but was " + peekedToString() + locationString());
        }
    }

    /**
     * {@inheritDoc}
     */
    public void beginObject() throws IOException, LarJsonParseException {
        int p = peeked;
        if (p == PEEKED_NONE) {
            p = doPeek();
        }
        if (p == PEEKED_BEGIN_OBJECT) {
            pos++;
            push(LarJsonScope.EMPTY_OBJECT);
            peeked = PEEKED_NONE;
        } else {
            throw new LarJsonParseException("Expected BEGIN_OBJECT but was " + peekedToString() + locationString());
        }
    }

    /**
     * {@inheritDoc}
     */
    public void endObject() throws IOException, LarJsonParseException {
        int p = peeked;
        if (p == PEEKED_NONE) {
            p = doPeek();
        }
        if (p == PEEKED_END_OBJECT) {
            stackSize--;
            pathNames[stackSize] = null; // Free the last path name so that it can be garbage collected!
            pathIndices[stackSize - 1]++;
            peeked = PEEKED_NONE;
        } else {
            throw new LarJsonParseException("Expected END_OBJECT but was " + peekedToString() + locationString());
        }
    }

    /**
     * Returns true if the current array or object has another element.
     */
    public boolean hasNext() throws IOException, LarJsonParseException {
        int p = peeked;
        if (p == PEEKED_NONE) {
            p = doPeek();
        }
        return p != PEEKED_END_OBJECT && p != PEEKED_END_ARRAY;
    }

    /**
     * {@inheritDoc}
     */
    public LarJsonToken peek() throws IOException, LarJsonParseException {
        int p = peeked;
        if (p == PEEKED_NONE) {
            p = doPeek();
        }

        switch (p) {
            case PEEKED_BEGIN_OBJECT:
                return LarJsonToken.BEGIN_OBJECT;
            case PEEKED_END_OBJECT:
                return LarJsonToken.END_OBJECT;
            case PEEKED_BEGIN_ARRAY:
                return LarJsonToken.BEGIN_ARRAY;
            case PEEKED_END_ARRAY:
                return LarJsonToken.END_ARRAY;
            case PEEKED_SINGLE_QUOTED_NAME:
            case PEEKED_DOUBLE_QUOTED_NAME:
            case PEEKED_UNQUOTED_NAME:
                return LarJsonToken.NAME;
            case PEEKED_NULL:
                return LarJsonToken.NULL;
            case PEEKED_TRUE:
            case PEEKED_FALSE:
            case PEEKED_SINGLE_QUOTED:
            case PEEKED_DOUBLE_QUOTED:
            case PEEKED_UNQUOTED:
            case PEEKED_LONG:
            case PEEKED_NUMBER:
                return LarJsonToken.SIMPLE_VALUE;
            case PEEKED_EOF:
                return LarJsonToken.END_DOCUMENT;
            default:
                throw new AssertionError();
        }
    }

    private String peekedToString() throws IOException, LarJsonParseException {
        int p = peeked;
        if (p == PEEKED_NONE) {
            p = doPeek();
        }
        
        switch (peeked) {
            case PEEKED_BEGIN_OBJECT:
                return "BEGIN_OBJECT";
            case PEEKED_END_OBJECT:
                return "END_OBJECT";
            case PEEKED_BEGIN_ARRAY:
                return "BEGIN_ARRAY";
            case PEEKED_END_ARRAY:
                return "END_ARRAY";
            case PEEKED_SINGLE_QUOTED_NAME:
            case PEEKED_DOUBLE_QUOTED_NAME:
            case PEEKED_UNQUOTED_NAME:
                return "NAME";
            case PEEKED_NULL:
                return "NULL";
            case PEEKED_TRUE:
                return "TRUE";
            case PEEKED_FALSE:
                return "FALSE";
            case PEEKED_SINGLE_QUOTED:
            case PEEKED_DOUBLE_QUOTED:
            case PEEKED_UNQUOTED:
                return "STRING";
            case PEEKED_LONG:
                return "LONG";
            case PEEKED_NUMBER:
                return "NUMBER";
            case PEEKED_EOF:
                return "END_DOCUMENT";
            default:
                return "UNKNOWN";
        }
    }

    private int doPeek() throws IOException, LarJsonParseException {
        int peekStack = stack[stackSize - 1];
        if (peekStack == LarJsonScope.EMPTY_ARRAY) {
            stack[stackSize - 1] = LarJsonScope.NONEMPTY_ARRAY;
        } else if (peekStack == LarJsonScope.NONEMPTY_ARRAY) {
            // Look for a comma before the next element.
            int c = nextNonWhitespace(true);
            switch (c) {
                case ']':
                    return peeked = PEEKED_END_ARRAY;
                case ';':
                    checkLenient(); // fall-through
                case ',':
                    break;
                default:
                    throw syntaxError("Unterminated array");
            }
        } else if (peekStack == LarJsonScope.EMPTY_OBJECT || peekStack == LarJsonScope.NONEMPTY_OBJECT) {
            stack[stackSize - 1] = LarJsonScope.DANGLING_NAME;
            // Look for a comma before the next element.
            if (peekStack == LarJsonScope.NONEMPTY_OBJECT) {
                int c = nextNonWhitespace(true);
                switch (c) {
                    case '}':
                        return peeked = PEEKED_END_OBJECT;
                    case ';':
                        checkLenient(); // fall-through
                    case ',':
                        break;
                    default:
                        throw syntaxError("Unterminated object");
                }
            }
            int c = nextNonWhitespace(true);
            switch (c) {
                case '"':
                    pos--;
                    return peeked = PEEKED_DOUBLE_QUOTED_NAME;
                case '\'':
                    checkLenient();
                    pos--;
                    return peeked = PEEKED_SINGLE_QUOTED_NAME;
                case '}':
                    if (peekStack != LarJsonScope.NONEMPTY_OBJECT) {
                        return peeked = PEEKED_END_OBJECT;
                    } else {
                        throw syntaxError("Expected name");
                    }
                default:
                    checkLenient();
                    pos--; // Don't consume the first character in an unquoted string.
                    if (isLiteral((char) c)) {
                        return peeked = PEEKED_UNQUOTED_NAME;
                    } else {
                        throw syntaxError("Expected name");
                    }
            }
        } else if (peekStack == LarJsonScope.DANGLING_NAME) {
            stack[stackSize - 1] = LarJsonScope.NONEMPTY_OBJECT;
            // Look for a colon before the value.
            int c = nextNonWhitespace(true);
            switch (c) {
                case ':':
                    break;
                case '=':
                    checkLenient();
                    if ((pos < limit || fillBuffer(1)) && buffer[pos] == '>') {
                        pos++;
                    }
                    break;
                default:
                    throw syntaxError("Expected ':'");
            }
        } else if (peekStack == LarJsonScope.EMPTY_DOCUMENT) {
            if (lenient) {
                consumeNonExecutePrefix();
            }
            stack[stackSize - 1] = LarJsonScope.NONEMPTY_DOCUMENT;
        } else if (peekStack == LarJsonScope.NONEMPTY_DOCUMENT) {
            int c = nextNonWhitespace(false);
            if (c == -1) {
                return peeked = PEEKED_EOF;
            } else {
                checkLenient();
                pos--;
            }
        }

        int c = nextNonWhitespace(true);
        switch (c) {
            case ']':
                if (peekStack == LarJsonScope.EMPTY_ARRAY) {
                    return peeked = PEEKED_END_ARRAY;
                }
                // fall-through to handle ",]"
            case ';':
            case ',':
                // In lenient mode, a 0-length literal in an array means 'null'.
                if (peekStack == LarJsonScope.EMPTY_ARRAY || peekStack == LarJsonScope.NONEMPTY_ARRAY) {
                    checkLenient();
                    pos--;
                    peekedLength = 0;
                    return peeked = PEEKED_NULL;
                } else {
                    throw syntaxError("Unexpected value");
                }
            case '\'':
                checkLenient();
                pos--;
                return peeked = PEEKED_SINGLE_QUOTED;
            case '"':
                pos--;
                return peeked = PEEKED_DOUBLE_QUOTED;
            case '[':
                pos--;
                return peeked = PEEKED_BEGIN_ARRAY;
            case '{':
                pos--;
                return peeked = PEEKED_BEGIN_OBJECT;
            default:
                pos--; // Don't consume the first character in a literal value.
        }

        int result = peekKeyword();
        if (result != PEEKED_NONE) {
            return result;
        }

        result = peekNumber();
        if (result != PEEKED_NONE) {
            return result;
        }

        if (!isLiteral(buffer[pos])) {
            throw syntaxError("Expected value");
        }

        return peeked = PEEKED_UNQUOTED;
    }

    private int peekKeyword() throws IOException, LarJsonParseException {
        // Figure out which keyword we're matching against by its first character.
        char c = buffer[pos];
        String keyword;
        String keywordUpper;
        int peeking;
        if (c == 't' || c == 'T') {
            keyword = "true";
            keywordUpper = "TRUE";
            peeking = PEEKED_TRUE;
        } else if (c == 'f' || c == 'F') {
            keyword = "false";
            keywordUpper = "FALSE";
            peeking = PEEKED_FALSE;
        } else if (c == 'n' || c == 'N') {
            keyword = "null";
            keywordUpper = "NULL";
            peeking = PEEKED_NULL;
        } else {
            return PEEKED_NONE;
        }

        // Confirm that chars [1..length) match the keyword.
        int length = keyword.length();
        for (int i = 1; i < length; i++) {
            if (pos + i >= limit && !fillBuffer(i + 1)) {
                return PEEKED_NONE;
            }
            c = buffer[pos + i];
            if (c != keyword.charAt(i) && c != keywordUpper.charAt(i)) {
                return PEEKED_NONE;
            }
        }

        if ((pos + length < limit || fillBuffer(length + 1))
                && isLiteral(buffer[pos + length])) {
            return PEEKED_NONE; // Don't match trues, falsey or nullsoft!
        }

        // We've found the keyword followed either by EOF or by a non-literal character.
        peekedLength = keyword.length();
        return peeked = peeking;
    }

    private int peekNumber() throws IOException, LarJsonParseException {
        // Like nextNonWhitespace, this uses locals 'p' and 'l' to save inner-loop field access.
        char[] buffer = this.buffer;
        int p = pos;
        int l = limit;

        long value = 0; // Negative to accommodate Long.MIN_VALUE more easily.
        boolean negative = false;
        boolean fitsInLong = true;
        int last = NUMBER_CHAR_NONE;

        int i = 0;

        charactersOfNumber:
        for (; true; i++) {
            if (p + i == l) {
                if (i == buffer.length) {
                    // Though this looks like a well-formed number, it's too long to continue reading. Give up
                    // and let the application handle this as an unquoted literal.
                    return PEEKED_NONE;
                }
                if (!fillBuffer(i + 1)) {
                    break;
                }
                p = pos;
                l = limit;
            }

            char c = buffer[p + i];
            switch (c) {
                case '-':
                    if (last == NUMBER_CHAR_NONE) {
                        negative = true;
                        last = NUMBER_CHAR_SIGN;
                        continue;
                    } else if (last == NUMBER_CHAR_EXP_E) {
                        last = NUMBER_CHAR_EXP_SIGN;
                        continue;
                    }
                    return PEEKED_NONE;

                case '+':
                    if (last == NUMBER_CHAR_EXP_E) {
                        last = NUMBER_CHAR_EXP_SIGN;
                        continue;
                    }
                    return PEEKED_NONE;

                case 'e':
                case 'E':
                    if (last == NUMBER_CHAR_DIGIT || last == NUMBER_CHAR_FRACTION_DIGIT) {
                        last = NUMBER_CHAR_EXP_E;
                        continue;
                    }
                    return PEEKED_NONE;

                case '.':
                    if (last == NUMBER_CHAR_DIGIT) {
                        last = NUMBER_CHAR_DECIMAL;
                        continue;
                    }
                    return PEEKED_NONE;

                default:
                    if (c < '0' || c > '9') {
                        if (!isLiteral(c)) {
                            break charactersOfNumber;
                        }
                        return PEEKED_NONE;
                    }
                    if (last == NUMBER_CHAR_SIGN || last == NUMBER_CHAR_NONE) {
                        value = -(c - '0');
                        last = NUMBER_CHAR_DIGIT;
                    } else if (last == NUMBER_CHAR_DIGIT) {
                        if (value == 0) {
                            return PEEKED_NONE; // Leading '0' prefix is not allowed (since it could be octal).
                        }
                        long newValue = value * 10 - (c - '0');
                        fitsInLong &= value > MIN_INCOMPLETE_INTEGER
                                || (value == MIN_INCOMPLETE_INTEGER && newValue < value);
                        value = newValue;
                    } else if (last == NUMBER_CHAR_DECIMAL) {
                        last = NUMBER_CHAR_FRACTION_DIGIT;
                    } else if (last == NUMBER_CHAR_EXP_E || last == NUMBER_CHAR_EXP_SIGN) {
                        last = NUMBER_CHAR_EXP_DIGIT;
                    }
            }
        }

        // We've read a complete number. Decide if it's a PEEKED_LONG or a PEEKED_NUMBER.
        if (last == NUMBER_CHAR_DIGIT && fitsInLong && (value != Long.MIN_VALUE || negative) && (value!=0 || !negative)) {
            peekedLong = negative ? value : -value;
            peekedLength = i;
            return peeked = PEEKED_LONG;
        } else if (last == NUMBER_CHAR_DIGIT || last == NUMBER_CHAR_FRACTION_DIGIT
                || last == NUMBER_CHAR_EXP_DIGIT) {
            peekedLength = i;
            return peeked = PEEKED_NUMBER;
        } else {
            return PEEKED_NONE;
        }
    }

    private boolean isLiteral(char c) throws IOException, LarJsonParseException {
        switch (c) {
            case '/':
            case '\\':
            case ';':
            case '#':
            case '=':
                checkLenient(); // fall-through
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
                return false;
            default:
                return true;
        }
    }

    /**
     * {@inheritDoc}
     */
    public String nextName() throws IOException, LarJsonParseException {
        int p = peeked;
        if (p == PEEKED_NONE) {
            p = doPeek();
        }
        String result;
        if (p == PEEKED_UNQUOTED_NAME) {
            checkLenient();
            result = nextUnquotedValue();
        } else if (p == PEEKED_SINGLE_QUOTED_NAME) {
            checkLenient();
            result = nextQuotedValue();
        } else if (p == PEEKED_DOUBLE_QUOTED_NAME) {
            result = nextQuotedValue();
        } else {
            throw new LarJsonParseException("Expected a name but was " + peekedToString() + locationString());
        }
        peeked = PEEKED_NONE;
        pathNames[stackSize - 1] = result;
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public String nextString() throws IOException, LarJsonParseException {
        int p = peeked;
        if (p == PEEKED_NONE) {
            p = doPeek();
        }
        String result;
        if (p == PEEKED_UNQUOTED) {
            checkLenient();
            result = nextUnquotedValue();
        } else if (p == PEEKED_SINGLE_QUOTED) {
            checkLenient();
            result = nextQuotedValue();
        } else if (p == PEEKED_DOUBLE_QUOTED) {
            result = nextQuotedValue();
        } else if (p == PEEKED_LONG) {
            result = Long.toString(peekedLong);
            pos += peekedLength;
        } else if (p == PEEKED_NUMBER) {
            result = new String(buffer, pos, peekedLength);
            pos += peekedLength;
        } else {
            throw new LarJsonParseException("Expected a string but was " + peekedToString() + locationString());
        }
        peeked = PEEKED_NONE;
        pathIndices[stackSize - 1]++;
        return result;
    }

    /**
     * Returns the char value of the next token,
     * consuming it. If the next token is a digit, this method will return it as a {@code char}.
     */
    @Override
    public char nextChar() throws IOException, LarJsonParseException {
        try {
            String str = nextString();
            if(str.length() == 1) {
                return str.charAt(0);
            }
        } catch (LarJsonParseException ignore) {
        }
        throw new LarJsonParseException("Expected a char but was " + peekedToString() + locationString());
    }

    /**
     * {@inheritDoc}
     */
    public boolean nextBoolean() throws IOException, LarJsonParseException {
        int p = peeked;
        if (p == PEEKED_NONE) {
            p = doPeek();
        }
        if (p == PEEKED_TRUE) {
            pos += peekedLength;
            peeked = PEEKED_NONE;
            pathIndices[stackSize - 1]++;
            return true;
        } else if (p == PEEKED_FALSE) {
            pos += peekedLength;
            peeked = PEEKED_NONE;
            pathIndices[stackSize - 1]++;
            return false;
        }
        throw new LarJsonParseException("Expected a boolean but was " + peekedToString() + locationString());
    }

    /**
     * {@inheritDoc}
     */
    public void nextNull() throws IOException, LarJsonParseException {
        int p = peeked;
        if (p == PEEKED_NONE) {
            p = doPeek();
        }
        if (p == PEEKED_NULL) {
            pos += peekedLength;
            peeked = PEEKED_NONE;
            pathIndices[stackSize - 1]++;
        } else {
            throw new LarJsonParseException("Expected null but was " + peekedToString() + locationString());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal nextBigDecimal() throws IOException, LarJsonParseException {
        return nextNumberObject(BigDecimal::valueOf, BigDecimal::new, BigDecimal.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigInteger nextBigInteger() throws IOException, LarJsonParseException {
        return nextNumberObject(BigInteger::valueOf, (s)-> {
            try {
                return new BigInteger(s);
            } catch (NumberFormatException e) {
                return new BigDecimal(s).toBigIntegerExact();
            }
        }, BigInteger.class);
    }

    private <T extends Number> T nextNumberObject(LongFunction<T> fromLong, Function<String, T> fromString, Class<T> clazz)
            throws IOException, LarJsonParseException {
        int p = peeked;
        if (p == PEEKED_NONE) {
            p = doPeek();
        }

        if (p == PEEKED_LONG) {
            peeked = PEEKED_NONE;
            pathIndices[stackSize - 1]++;
            pos += peekedLength;
            return fromLong.apply(peekedLong);
        }

        String peekedString = getPeekedNumberString(p, clazz);
        T result;
        try {
            result = fromString.apply(peekedString);
        } catch (NumberFormatException | ArithmeticException e) {
            throw new LarJsonParseException("Expected a " + clazz.getName() + " but was " + peekedToString() + locationString(), e);
        }
        peeked = PEEKED_NONE;
        pathIndices[stackSize - 1]++;
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double nextDouble() throws IOException, LarJsonParseException {
        return nextFloatingValue(Double::parseDouble, double.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float nextFloat() throws LarJsonParseException, IOException {
        return (float) nextFloatingValue(Float::parseFloat, float.class);
    }

    private <T> double nextFloatingValue(ToDoubleFunction<String> parse, Class<T> type) throws IOException, LarJsonParseException {
        int p = peeked;
        if (p == PEEKED_NONE) {
            p = doPeek();
        }

        if (p == PEEKED_LONG) {
            peeked = PEEKED_NONE;
            pathIndices[stackSize - 1]++;
            pos += peekedLength;
            return (double) peekedLong;
        }

        String peekedString = getPeekedNumberString(p, type);
        double result;
        try {
            result = parse.applyAsDouble(peekedString);
            if (!lenient && (Double.isNaN(result) || Double.isInfinite(result))) {
                throw new LarJsonParseException(
                        "JSON forbids NaN and infinities: " + result + locationString());
            }
        } catch (NumberFormatException e) {
            throw new LarJsonParseException("Expected a " + type.getName() + " but was " + peekedToString() + locationString(), e);
        }
        peeked = PEEKED_NONE;
        pathIndices[stackSize - 1]++;
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Number nextNumber() throws IOException, LarJsonParseException {
        int p = peeked;
        if (p == PEEKED_NONE) {
            p = doPeek();
        }

        if (p == PEEKED_LONG) {
            peeked = PEEKED_NONE;
            pathIndices[stackSize - 1]++;
            pos += peekedLength;
            return peekedLong;
        }

        String peekedString = getPeekedNumberString(p, Number.class);

        Number result;
        try{
            result = Long.parseLong(peekedString);
        } catch (NumberFormatException longException) {
            try {
                result = new BigInteger(peekedString);
            } catch (NumberFormatException bigIntegerException) {
                try {
                    result = new BigDecimal(peekedString);
                    double doubleValue = result.doubleValue();
                    if(peekedString.equals(Double.toString(doubleValue))) {
                        result = doubleValue;
                    }
                } catch (NumberFormatException bigDecimalException) {
                    try {
                        double doubleValue = Double.parseDouble(peekedString);
                        if (!lenient && (Double.isNaN(doubleValue) || Double.isInfinite(doubleValue))) {
                            throw new LarJsonParseException(
                                    "JSON forbids NaN and infinities: " + peekedString + locationString());
                        }
                        result = doubleValue;
                    } catch (NumberFormatException doubleException) {
                        throw new LarJsonParseException("Expected a Number but was " + peekedToString() + locationString(),
                                doubleException);
                    }
                }
            }
        }

        peeked = PEEKED_NONE;
        pathIndices[stackSize - 1]++;
        return result;
    }

    private <T> String getPeekedNumberString(int p, Class<T> type) throws IOException, LarJsonParseException {
        String peekedString;
        if (p == PEEKED_NUMBER) {
            peekedString = new String(buffer, pos, peekedLength);
            pos += peekedLength;
        } else if (p == PEEKED_SINGLE_QUOTED || p == PEEKED_DOUBLE_QUOTED) {
            peekedString = nextQuotedValue();
        } else if (p == PEEKED_UNQUOTED) {
            peekedString = nextUnquotedValue();
        } else {
            throw new LarJsonParseException("Expected a " + type.getName() + " but was " + peekedToString() + locationString());
        }
        return peekedString;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long nextLong() throws IOException, LarJsonParseException {
        int p = peeked;
        if (p == PEEKED_NONE) {
            p = doPeek();
        }

        if (p == PEEKED_LONG) {
            peeked = PEEKED_NONE;
            pathIndices[stackSize - 1]++;
            pos += peekedLength;
            return peekedLong;
        }

        String peekedString;
        if (p == PEEKED_NUMBER) {
            peekedString = new String(buffer, pos, peekedLength);
            pos += peekedLength;
        } else if (p == PEEKED_SINGLE_QUOTED || p == PEEKED_DOUBLE_QUOTED || p == PEEKED_UNQUOTED) {
            if (p == PEEKED_UNQUOTED) {
                peekedString = nextUnquotedValue();
            } else {
                peekedString = nextQuotedValue();
            }
            try {
                long result = Long.parseLong(peekedString);
                peeked = PEEKED_NONE;
                pathIndices[stackSize - 1]++;
                return result;
            } catch (NumberFormatException ignored) {
                // Fall back to parse as a double below.
            }
        } else {
            throw new LarJsonParseException("Expected a long but was " + peekedToString() + locationString());
        }

        double asDouble;
        try {
            asDouble = Double.parseDouble(peekedString);
        } catch(NumberFormatException e) {
            throw new LarJsonParseException("Expected a long but was " + peekedString + locationString());
        }

        long result = (long) asDouble;
        if (result != asDouble) { // Make sure no precision was lost casting to 'long'.
            throw new LarJsonParseException("Expected a long but was " + peekedString + locationString());
        }
        peeked = PEEKED_NONE;
        pathIndices[stackSize - 1]++;
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int nextInt() throws IOException, LarJsonParseException {
        return nextInteger(l->(int)l, d->(int)d, Integer::parseInt, int.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public short nextShort() throws IOException, LarJsonParseException {
        return (short) nextInteger(l->(short)l, d->(short)d, Short::parseShort, short.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte nextByte() throws IOException, LarJsonParseException {
        return (byte) nextInteger(l->(byte)l, d->(byte)d, Byte::parseByte, byte.class);
    }

    private <T> int nextInteger(LongToIntFunction castLong, DoubleToIntFunction castDouble,
                                ToIntFunction<String> parse, Class<T> type)
            throws IOException, LarJsonParseException {
        int p = peeked;
        if (p == PEEKED_NONE) {
            p = doPeek();
        }

        int result;
        if (p == PEEKED_LONG) {
            result = castLong.applyAsInt(peekedLong);
            if (peekedLong != result) { // Make sure value fits in this integer.
                throw new LarJsonParseException("Expected " + type.getName() + " but was " + peekedLong + locationString());
            }
            peeked = PEEKED_NONE;
            pathIndices[stackSize - 1]++;
            pos += peekedLength;
            return result;
        }

        String peekedString;
        if (p == PEEKED_NUMBER) {
            peekedString = new String(buffer, pos, peekedLength);
            pos += peekedLength;
        } else if (p == PEEKED_SINGLE_QUOTED || p == PEEKED_DOUBLE_QUOTED || p == PEEKED_UNQUOTED) {
            if (p == PEEKED_UNQUOTED) {
                peekedString = nextUnquotedValue();
            } else {
                peekedString = nextQuotedValue();
            }
            try {
                result = parse.applyAsInt(peekedString);
                peeked = PEEKED_NONE;
                pathIndices[stackSize - 1]++;
                return result;
            } catch (NumberFormatException ignored) {
                // Fall back to parse as a double below.
            }
        } else {
            throw new LarJsonParseException("Expected " + type.getName() + " but was " + peekedToString() + locationString());
        }

        double asDouble;
        try {
            asDouble = Double.parseDouble(peekedString);
        } catch (NumberFormatException e) {
            throw new LarJsonParseException("Expected " + type.getName() + " but was " + peekedToString() + locationString());
        }

        result = castDouble.applyAsInt(asDouble);
        if (result != asDouble) { // Make sure no precision was lost casting to integer.
            throw new LarJsonParseException("Expected " + type.getName() + " but was DOUBLE" + locationString());
        }
        peeked = PEEKED_NONE;
        pathIndices[stackSize - 1]++;
        return result;
    }

    /**
     * Returns the string up to but not including {@code quote}, unescaping any
     * character escape sequences encountered along the way. The opening quote
     * should have already been read. This consumes the closing quote, but does
     * not include it in the returned string.
     */
    private String nextQuotedValue() throws IOException, LarJsonParseException {
        // Like nextNonWhitespace, this uses locals 'p' and 'l' to save inner-loop field access.
        char[] buffer = this.buffer;
        char quote = buffer[pos++];
        StringBuilder builder = null;
        while (true) {
            int p = pos;
            int l = limit;
            /* the index of the first character not yet appended to the builder. */
            int start = p;
            while (p < l) {
                int c = buffer[p++];

                if (c == quote) {
                    pos = p;
                    int len = p - start - 1;
                    if (builder == null) {
                        return new String(buffer, start, len);
                    } else {
                        builder.append(buffer, start, len);
                        return builder.toString();
                    }
                } else if (c == '\\') {
                    pos = p;
                    int len = p - start - 1;
                    if (builder == null) {
                        int estimatedLength = (len + 1) * 2;
                        builder = new StringBuilder(Math.max(estimatedLength, 16));
                    }
                    builder.append(buffer, start, len);
                    builder.append(readEscapeCharacter());
                    p = pos;
                    l = limit;
                    start = p;
                } else if (c == '\n') {
                    lineNumber++;
                    lineStart = p;
                }
            }

            if (builder == null) {
                int estimatedLength = (p - start) * 2;
                builder = new StringBuilder(Math.max(estimatedLength, 16));
            }
            builder.append(buffer, start, p - start);
            pos = p;
            if (!fillBuffer(1)) {
                throw syntaxError("Unterminated string");
            }
        }
    }

    /**
     * Returns an unquoted value as a string.
     */
    @SuppressWarnings("fallthrough")
    private String nextUnquotedValue() throws IOException, LarJsonParseException {
        StringBuilder builder = null;
        int i = 0;

        findNonLiteralCharacter:
        while (true) {
            for (; pos + i < limit; i++) {
                switch (buffer[pos + i]) {
                    case '/':
                    case '\\':
                    case ';':
                    case '#':
                    case '=':
                        checkLenient(); // fall-through
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
                }
            }

            // Attempt to load the entire literal into the buffer at once.
            if (i < buffer.length) {
                if (fillBuffer(i + 1)) {
                    continue;
                } else {
                    break;
                }
            }

            // use a StringBuilder when the value is too long. This is too long to be a number!
            if (builder == null) {
                builder = new StringBuilder(Math.max(i,16));
            }
            builder.append(buffer, pos, i);
            pos += i;
            i = 0;
            if (!fillBuffer(1)) {
                break;
            }
        }

        String result = (null == builder) ? new String(buffer, pos, i) : builder.append(buffer, pos, i).toString();
        pos += i;
        return result;
    }

    private void skipQuotedValue() throws LarJsonParseException, IOException {
        // Like nextNonWhitespace, this uses locals 'p' and 'l' to save inner-loop field access.
        char[] buffer = this.buffer;
        char quote = buffer[pos++];
        do {
            int p = pos;
            int l = limit;
            /* the index of the first character not yet appended to the builder. */
            while (p < l) {
                int c = buffer[p++];
                if (c == quote) {
                    pos = p;
                    return;
                } else if (c == '\\') {
                    pos = p;
                    readEscapeCharacter();
                    p = pos;
                    l = limit;
                } else if (c == '\n') {
                    lineNumber++;
                    lineStart = p;
                }
            }
            pos = p;
        } while (fillBuffer(1));
        throw syntaxError("Unterminated string");
    }

    private void skipUnquotedValue() throws IOException, LarJsonParseException {
        do {
            int i = 0;
            for (; pos + i < limit; i++) {
                switch (buffer[pos + i]) {
                    case '/':
                    case '\\':
                    case ';':
                    case '#':
                    case '=':
                        checkLenient(); // fall-through
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
                        pos += i;
                        return;
                }
            }
            pos += i;
        } while (fillBuffer(1));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void skipValue() throws IOException, LarJsonParseException {
        int count = 0;
        do {
            int p = peeked;
            if (p == PEEKED_NONE) {
                p = doPeek();
            }

            switch (p) {
                case PEEKED_BEGIN_ARRAY:
                    pos++;
                    push(LarJsonScope.EMPTY_ARRAY);
                    count++;
                    break;
                case PEEKED_BEGIN_OBJECT:
                    pos++;
                    push(LarJsonScope.EMPTY_OBJECT);
                    count++;
                    break;
                case PEEKED_END_ARRAY:
                case PEEKED_END_OBJECT:
                    stackSize--;
                    count--;
                    break;
                case PEEKED_UNQUOTED_NAME:
                case PEEKED_UNQUOTED:
                    skipUnquotedValue();
                    break;
                case PEEKED_SINGLE_QUOTED:
                case PEEKED_SINGLE_QUOTED_NAME:
                case PEEKED_DOUBLE_QUOTED:
                case PEEKED_DOUBLE_QUOTED_NAME:
                    skipQuotedValue();
                    break;
                case PEEKED_NUMBER:
                case PEEKED_LONG:
                case PEEKED_NULL:
                case PEEKED_TRUE:
                case PEEKED_FALSE:
                    pos += peekedLength;
                    break;
            }
            peeked = PEEKED_NONE;
        } while (count != 0);

        pathIndices[stackSize - 1]++;
        pathNames[stackSize - 1] = "null";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getCurrentPosition() throws IOException, LarJsonParseException {
        if (peeked == PEEKED_NONE) {
            doPeek();
        }
        return distances[pos];
    }

    private void push(int newTop) {
        if (stackSize == stack.length) {
            int newLength = stackSize * 2;
            stack = Arrays.copyOf(stack, newLength);
            pathIndices = Arrays.copyOf(pathIndices, newLength);
            pathNames = Arrays.copyOf(pathNames, newLength);
        }
        stack[stackSize++] = newTop;
    }

    /**
     * Returns true when {@code limit - pos >= minimum}. If the data is
     * exhausted before that many characters are available, this returns
     * false.
     */
    private boolean fillBuffer(int minimum) throws IOException, LarJsonParseException {
        char[] buffer = this.buffer;
        long[] distances = this.distances;
        lineStart -= pos;
        if (limit != pos) {
            limit -= pos;
            System.arraycopy(buffer, pos, buffer, 0, limit);
            System.arraycopy(distances, pos, distances, 0, limit);
        } else {
            limit = 0;
        }

        pos = 0;

        int c;
        long d = byteStream.currentPosition();
        try {
            while((c = characterDecoder.decodeCharacter(byteStream)) != -1) {
                buffer[limit] = (char)c;
                distances[limit] = d;
                limit++;

                // if this is the first read, consume an optional byte order mark (BOM) if it exists
                if (lineNumber == 0 && lineStart == 0 && limit > 0 && buffer[0] == '\ufeff') {
                    pos++;
                    lineStart++;
                    minimum++;
                }

                if (limit >= buffer.length) {
                    return true;
                }

                d = byteStream.currentPosition();
            }
        } catch (CharacterDecodingException e) {
            throw new LarJsonParseException(e);
        }
        return limit >= minimum;
    }

    /**
     * Returns the next character in the stream that is neither whitespace nor a
     * part of a comment. When this returns, the returned character is always at
     * {@code buffer[pos-1]}; this means the caller can always push back the
     * returned character by decrementing {@code pos}.
     */
    private int nextNonWhitespace(boolean throwOnEof) throws IOException, LarJsonParseException {
        /*
         * This code uses ugly local variables 'p' and 'l' representing the 'pos'
         * and 'limit' fields respectively. Using locals rather than fields saves
         * a few field reads for each whitespace character in a pretty-printed
         * document, resulting in a 5% speedup. We need to flush 'p' to its field
         * before any (potentially indirect) call to fillBuffer() and reread both
         * 'p' and 'l' after any (potentially indirect) call to the same method.
         */
        char[] buffer = this.buffer;
        int p = pos;
        int l = limit;
        while (true) {
            if (p == l) {
                pos = p;
                if (!fillBuffer(1)) {
                    break;
                }
                p = pos;
                l = limit;
            }

            int c = buffer[p++];
            if (c == '\n') {
                lineNumber++;
                lineStart = p;
                continue;
            } else if (c == ' ' || c == '\r' || c == '\t') {
                continue;
            }

            if (c == '/') {
                pos = p;
                if (p == l) {
                    pos--; // push back '/' so it's still in the buffer when this method returns
                    boolean charsLoaded = fillBuffer(2);
                    pos++; // consume the '/' again
                    if (!charsLoaded) {
                        return c;
                    }
                }

                checkLenient();
                char peek = buffer[pos];
                switch (peek) {
                    case '*':
                        // skip a /* c-style comment */
                        pos++;
                        if (!skipTo("*/")) {
                            throw syntaxError("Unterminated comment");
                        }
                        p = pos + 2;
                        l = limit;
                        continue;

                    case '/':
                        // skip a // end-of-line comment
                        pos++;
                        skipToEndOfLine();
                        p = pos;
                        l = limit;
                        continue;

                    default:
                        return c;
                }
            } else if (c == '#') {
                pos = p;
                /*
                 * Skip a # hash end-of-line comment. The JSON RFC doesn't
                 * specify this behaviour, but it's required to parse
                 * existing documents. See http://b/2571423.
                 */
                checkLenient();
                skipToEndOfLine();
                p = pos;
                l = limit;
            } else {
                pos = p;
                return c;
            }
        }
        if (throwOnEof) {
            throw new LarJsonParseException("End of input" + locationString());
        } else {
            return -1;
        }
    }

    private void checkLenient() throws LarJsonParseException {
        if (!lenient) {
            throw syntaxError("Set lenient to true to accept malformed JSON");
        }
    }

    /**
     * Advances the position until after the next newline character. If the line
     * is terminated by "\r\n", the '\n' must be consumed as whitespace by the
     * caller.
     */
    private void skipToEndOfLine() throws IOException, LarJsonParseException {
        while (pos < limit || fillBuffer(1)) {
            char c = buffer[pos++];
            if (c == '\n') {
                lineNumber++;
                lineStart = pos;
                break;
            } else if (c == '\r') {
                break;
            }
        }
    }

    /**
     * @param toFind a string to search for. Must not contain a newline.
     */
    private boolean skipTo(String toFind) throws IOException, LarJsonParseException {
        int length = toFind.length();
        outer:
        for (; pos + length <= limit || fillBuffer(length); pos++) {
            if (buffer[pos] == '\n') {
                lineNumber++;
                lineStart = pos + 1;
                continue;
            }
            for (int c = 0; c < length; c++) {
                if (buffer[pos + c] != toFind.charAt(c)) {
                    continue outer;
                }
            }
            return true;
        }
        return false;
    }

    @Override public String toString() {
        return getClass().getSimpleName() + locationString();
    }

    private String locationString() {
        int line = lineNumber + 1;
        int column = pos - lineStart + 1;
        return " at line " + line + " column " + column + " path " + getPath();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPath() {
        StringBuilder result = new StringBuilder().append('$');
        for (int i = 0, size = stackSize; i < size; i++) {
            switch (stack[i]) {
                case LarJsonScope.EMPTY_ARRAY:
                case LarJsonScope.NONEMPTY_ARRAY:
                    result.append('[').append(pathIndices[i]).append(']');
                    break;

                case LarJsonScope.EMPTY_OBJECT:
                case LarJsonScope.DANGLING_NAME:
                case LarJsonScope.NONEMPTY_OBJECT:
                    result.append('.');
                    if (pathNames[i] != null) {
                        result.append(pathNames[i]);
                    }
                    break;

                case LarJsonScope.NONEMPTY_DOCUMENT:
                case LarJsonScope.EMPTY_DOCUMENT:
                    break;
            }
        }
        return result.toString();
    }

    /**
     * Unescapes the character identified by the character or characters that
     * immediately follow a backslash. The backslash '\' should have already
     * been read. This supports both unicode escapes "u000A" and two-character
     * escapes "\n".
     */
    private char readEscapeCharacter() throws IOException, LarJsonParseException {
        if (pos == limit && !fillBuffer(1)) {
            throw syntaxError("Unterminated escape sequence");
        }

        char escaped = buffer[pos++];
        switch (escaped) {
            case 'u':
                if (pos + 4 > limit && !fillBuffer(4)) {
                    throw syntaxError("Unterminated escape sequence");
                }
                // Equivalent to Integer.parseInt(stringPool.get(buffer, pos, 4), 16);
                char result = 0;
                for (int i = pos, end = i + 4; i < end; i++) {
                    char c = buffer[i];
                    result <<= 4;
                    if (c >= '0' && c <= '9') {
                        result += (c - '0');
                    } else if (c >= 'a' && c <= 'f') {
                        result += (c - 'a' + 10);
                    } else if (c >= 'A' && c <= 'F') {
                        result += (c - 'A' + 10);
                    } else {
                        throw syntaxError("Unrecognized number \\u" + new String(buffer, pos, 4));
                    }
                }
                pos += 4;
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
                lineNumber++;
                lineStart = pos;
                // fall-through

            case '\'':
            case '"':
            case '\\':
            case '/':
                return escaped;
            default:
                // throw error when none of the above cases are matched
                throw syntaxError("Invalid escape sequence");
        }
    }

    /**
     * Throws a new LarJsonParseException with the given message and a context snippet
     * with this reader's content.
     */
    private LarJsonParseException syntaxError(String message) throws LarJsonParseException {
        throw new LarJsonParseException(message + locationString());
    }

    /**
     * Consumes the non-execute prefix if it exists.
     */
    private void consumeNonExecutePrefix() throws IOException, LarJsonParseException {
        // fast forward through the leading whitespace
        nextNonWhitespace(true);
        pos--;

        int p = pos;
        if (p + 5 > limit && !fillBuffer(5)) {
            return;
        }

        char[] buf = buffer;
        if(buf[p] != ')' || buf[p + 1] != ']' || buf[p + 2] != '}' || buf[p + 3] != '\'' || buf[p + 4] != '\n') {
            return; // not a security token!
        }

        // we consumed a security token!
        pos += 5;
    }

}
