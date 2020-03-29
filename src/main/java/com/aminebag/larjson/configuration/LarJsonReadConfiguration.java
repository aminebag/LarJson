package com.aminebag.larjson.configuration;

import com.aminebag.larjson.chardecoder.CharacterDecoder;
import com.aminebag.larjson.chardecoder.LatinCharacterDecoder;
import com.aminebag.larjson.chardecoder.Utf8CharacterDecoder;
import com.aminebag.larjson.exception.LarJsonValueReadException;
import com.aminebag.larjson.parser.GsonTokenParser;
import com.aminebag.larjson.parser.GsonValueParser;
import com.aminebag.larjson.utils.TemporaryFileFactory;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;

/**
 * @author Amine Bagdouri
 *
 * This interface represents a base configuration for a LarJson mapper used to customize its behavior.
 * Implementations must be immutable and thread-safe.
 */
interface LarJsonReadConfiguration {

    /**
     * When a mapper is strict it only accepts JSON as specified by <a
     * href="http://www.ietf.org/rfc/rfc4627.txt">RFC 4627</a>. Setting the
     * configuration to lenient causes the mapper to ignore the following syntax errors:
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
     * @return true if this parser is liberal in what it accepts.
     */
    boolean isLenient();

    /**
     * Caching allows minimizing JSON resource access for frequently used data.
     * @return The maximum number of objects cached for each root object returned by the mapper. A zero value
     * indicates that caching will not be used.
     */
    int getCacheSize();

    /**
     * How a LarJson object must behave when it fails to read a certain JSON value due to a resource access or a
     * JSON related error
     */
    ValueReadFailedBehavior getValueReadFailedBehavior();

    /**
     * Thread-safe mutable LarJson objects have a significant memory and CPU overhead.
     * @return true if LarJson objects returned by the mapper should be mutable
     */
    boolean isMutable();

    /**
     * Thread-safe mutable LarJson objects have a significant memory and CPU overhead.
     * @return true if LarJson objects returned by the mapper should be thread-safe
     */
    boolean isThreadSafe();

    /**
     * @return a parser used to parse simple JSON values at some byte position
     */
    LarJsonValueParserFactory getValueParserFactory();

    /**
     * @return a parser used to parse the entire JSON document
     */
    LarJsonTokenParserFactory getTokenParserFactory();

    /**
     * @return a decoder used to decode JSON resource bytes into characters
     */
    CharacterDecoder getCharacterDecoder();

    /**
     * @return a factory of temporary files that could be used to store JSON resource related data and that are removed
     * at most when the root LarJson object is closed
     */
    TemporaryFileFactory getTemporaryFileFactory();

    /**
     * @return the maximum size (in bytes) of the blueprint of a LarJson object in memory, before switching to a file
     * based blueprint. A zero value indicates that the blueprint will be written to a file regardless of its size.
     */
    long getMaxMemoryBlueprintSize();

    /**
     * Builds a {@link LarJsonReadConfiguration}
     */
    abstract class Builder<B extends Builder> {

        private boolean built = false;
        private boolean lenient = false;
        private boolean mutable = false;
        private boolean threadSafe = false;
        private int cacheSize = 1024;
        private long maxMemoryBlueprintSize = // Minimum of half of the free available memory and 1 GB
                (int) Math.min(Runtime.getRuntime().freeMemory() >> 1, 1024 * 1024 * 1024);
        private ValueReadFailedBehavior valueReadFailedBehavior = ValueReadFailedBehavior.THROW_EXCEPTION;
        private LarJsonValueParserFactory valueParserFactory = larJsonConfiguration ->
                new GsonValueParser(larJsonConfiguration.isLenient());
        private LarJsonTokenParserFactory tokenParserFactory = (byteStream, characterDecoder, larJsonConfiguration) ->
                new GsonTokenParser(byteStream, characterDecoder, larJsonConfiguration.isLenient());
        private CharacterDecoder characterDecoder = Utf8CharacterDecoder.getInstance();
        private TemporaryFileFactory temporaryFileFactory = new DefaultTemporaryFileFactory();

        /**
         * Set the maximum blueprint size in memory
         * The default value is the minimum of 1GB and half of available free memory.
         * @return this builder
         * @see LarJsonReadConfiguration#getMaxMemoryBlueprintSize()
         */
        public final B setMaxMemoryBlueprintSize(long maxMemoryBlueprintSize) {
            checkNotBuilt();
            this.maxMemoryBlueprintSize = maxMemoryBlueprintSize;
            return (B) this;
        }

        /**
         * Set the thread-safety of LarJson objects.
         * The default value is {@code false}.
         * @return this builder
         * @see LarJsonReadConfiguration#isThreadSafe()
         */
        public final B setThreadSafe(boolean threadSafe) {
            checkNotBuilt();
            this.threadSafe = threadSafe;
            return (B) this;
        }

        /**
         * Set the mutability of LarJson objects
         * The default value is {@code false}.
         * @return this builder
         * @see LarJsonReadConfiguration#isMutable() ()
         */
        public final B setMutable(boolean mutable) {
            checkNotBuilt();
            this.mutable = mutable;
            return (B) this;
        }

        /**
         * Set the leniency of the LarJson parser
         * The default value is {@code false}.
         * @return this builder
         * @see LarJsonReadConfiguration#isLenient()
         */
        public final B setLenient(boolean lenient) {
            checkNotBuilt();
            this.lenient = lenient;
            return (B) this;
        }

        /**
         * Set the cache size of LarJson objects
         * The default value is 1024.
         * @return this builder
         * @see LarJsonReadConfiguration#getCacheSize()
         */
        public final B setCacheSize(int cacheSize) {
            checkNotBuilt();
            this.cacheSize = cacheSize;
            return (B) this;
        }

        /**
         * Set the value read failed behavior.
         * The default behavior is throw a {@link LarJsonValueReadException}.
         * @return this builder
         * @see LarJsonReadConfiguration#getValueReadFailedBehavior()
         */
        public final B setValueReadFailedBehavior(ValueReadFailedBehavior valueReadFailedBehavior) {
            checkNotBuilt();
            this.valueReadFailedBehavior = valueReadFailedBehavior;
            return (B) this;
        }

        /**
         * Throw a {@link LarJsonValueReadException} when reading a value fails.
         * This is the default behavior.
         * @return this builder
         * @see LarJsonReadConfiguration#getValueReadFailedBehavior()
         */
        public final B throwExceptionOnValueReadFailed() {
            checkNotBuilt();
            this.valueReadFailedBehavior = ValueReadFailedBehavior.THROW_EXCEPTION;
            return (B) this;
        }

        /**
         * Return {@code nuull} when reading a value fails.
         * The default behavior is throw a {@link LarJsonValueReadException}.
         * @return this builder
         * @see LarJsonReadConfiguration#getValueReadFailedBehavior()
         */
        public final B returnNullOnValueReadFailed() {
            checkNotBuilt();
            this.valueReadFailedBehavior = ValueReadFailedBehavior.RETURN_NULL;
            return (B) this;
        }

        /**
         * Set the value parser factory
         * @return this builder
         * @see LarJsonReadConfiguration#getValueParserFactory()
         */
        public final B setValueParserFactory(LarJsonValueParserFactory valueParserFactory) {
            checkNotBuilt();
            this.valueParserFactory = valueParserFactory;
            return (B) this;
        }

        /**
         * Set the token parser factory
         * @return this builder
         * @see LarJsonReadConfiguration#getValueParserFactory()
         */
        public final B setTokenParserFactory(LarJsonTokenParserFactory tokenParserFactory) {
            checkNotBuilt();
            this.tokenParserFactory = tokenParserFactory;
            return (B) this;
        }

        /**
         * Set a character decoder.
         * The default character decoder supports UTF-8 character encoding.
         * @return this builder
         * @see LarJsonReadConfiguration#getCharacterDecoder()
         */
        public final B setCharacterDecoder(CharacterDecoder characterDecoder) {
            checkNotBuilt();
            this.characterDecoder = characterDecoder;
            return (B) this;
        }

        /**
         * Set a character encoding. The currently supported encodings out-of-the-box are UTF-8 and ISO/IEC 8859-1
         * (Latin-1). Set your own character decoder for other character encodings.
         * The default character decoder supports UTF-8 character encoding.
         * @return this builder
         * @see LarJsonReadConfiguration#getCharacterDecoder()
         */
        public final B setCharset(Charset charset) {
            checkNotBuilt();
            if(StandardCharsets.UTF_8.equals(charset)) {
                this.characterDecoder = Utf8CharacterDecoder.getInstance();
            } else if(StandardCharsets.ISO_8859_1.equals(charset)) {
                this.characterDecoder = LatinCharacterDecoder.getInstance();
            } else {
                throw new IllegalArgumentException("Charset " + charset + " is not currently supported. " +
                        "Consider providing your own character decoder");
            }
            return (B) this;
        }

        /**
         * Set a temporary file factory.
         * The default factory creates files using {@link Files#createTempFile(String, String, FileAttribute[])}.
         * @return this builder
         * @see LarJsonReadConfiguration#getTemporaryFileFactory()
         */
        public final B setTemporaryFileFactory(TemporaryFileFactory temporaryFileFactory) {
            checkNotBuilt();
            this.temporaryFileFactory = temporaryFileFactory;
            return (B) this;
        }
        
        protected class Configuration implements LarJsonReadConfiguration {

            @Override
            public final boolean isLenient() {
                return lenient;
            }

            @Override
            public final int getCacheSize() {
                return cacheSize;
            }

            @Override
            public final ValueReadFailedBehavior getValueReadFailedBehavior() {
                return valueReadFailedBehavior;
            }

            @Override
            public final boolean isMutable() {
                return mutable;
            }

            @Override
            public final boolean isThreadSafe() {
                return threadSafe;
            }

            @Override
            public final LarJsonValueParserFactory getValueParserFactory() {
                return valueParserFactory;
            }

            @Override
            public final LarJsonTokenParserFactory getTokenParserFactory() {
                return tokenParserFactory;
            }

            @Override
            public final CharacterDecoder getCharacterDecoder() {
                return characterDecoder;
            }

            @Override
            public final TemporaryFileFactory getTemporaryFileFactory() {
                return temporaryFileFactory;
            }

            @Override
            public final long getMaxMemoryBlueprintSize() {
                return maxMemoryBlueprintSize;
            }
        }
        
        protected final void flagBuilt() {
            checkNotBuilt();
            built = true;
        }

        protected final void checkNotBuilt() {
            if(built) {
                throw new IllegalStateException("A configuration was already built using this builder");
            }
        }
    }
}
