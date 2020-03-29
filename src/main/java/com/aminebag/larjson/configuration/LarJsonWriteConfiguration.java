package com.aminebag.larjson.configuration;

/**
 * @author Amine Bagdouri
 *
 * This interface represents a base configuration for a LarJson mapper used to customize how a model object is written.
 * Implementations must be immutable and thread-safe.
 */
public interface LarJsonWriteConfiguration {

    /**
     * @return {@code true} if the writer must have relaxed syntax rules, {@code false} otherwise
     */
    boolean isLenient();

    /**
     * @return {@code true} if the writer must write JSON that's safe for inclusion in HTML and XML documents,
     * {@code false} otherwise
     */
    boolean isHtmlSafe();

    /**
     * @return the indentation string to be repeated for each level of indentation in the encoded document.
     * If {@code indent.isEmpty()} the encoded document will be compact. Otherwise the encoded document will be more
     * human-readable. The returned indent must only contain whitespace.
     */
    String getIndent();

    abstract class Builder<B extends Builder> {

        private boolean built = false;
        private boolean lenient = false;
        private boolean htmlSafe = false;
        private String indent = "\t";

        /**
         * Set the leniency of the writer
         * The default value is {@code false}.
         * @return this builder
         * @see LarJsonWriteConfiguration#isLenient()
         */
        public final B setLenient(boolean lenient) {
            checkNotBuilt();
            this.lenient = lenient;
            return (B) this;
        }

        /**
         * Set the HTML safety of the writer
         * The default value is {@code false}.
         * @return this builder
         * @see LarJsonWriteConfiguration#isHtmlSafe()
         */
        public final B setHtmlSafe(boolean htmlSafe) {
            checkNotBuilt();
            this.htmlSafe = htmlSafe;
            return (B) this;
        }

        /**
         * Set the indent to be used by the writer
         * The default value is {@code '\t'}.
         * @return this builder
         * @see LarJsonWriteConfiguration#getIndent()
         */
        public final B setIndent(String indent) {
            checkNotBuilt();
            this.indent = indent;
            return (B) this;
        }

        /**
         * Set whether the written JSON is pretty (human-readable) or minimized.
         * If set to {@code true} a tab character will be used to indent the document, otherwise an empty string will
         * be used as an indent and the document will be minimized.
         * By default the JSON is pretty.
         * @return this builder
         * @see LarJsonWriteConfiguration#getIndent()
         */
        public final B setPretty(boolean pretty) {
            checkNotBuilt();
            if (pretty) {
                this.indent = "\t";
            } else {
                this.indent = "";
            }
            return (B) this;
        }

        protected class Configuration implements LarJsonWriteConfiguration {
            @Override
            public final boolean isLenient() {
                return lenient;
            }

            @Override
            public final boolean isHtmlSafe() {
                return htmlSafe;
            }

            @Override
            public final String getIndent() {
                return indent;
            }
        }

        protected final void flagBuilt() {
            checkNotBuilt();
            built = true;
        }

        protected final void checkNotBuilt() {
            if (built) {
                throw new IllegalStateException("A configuration was already built using this builder");
            }
        }
    }
}
