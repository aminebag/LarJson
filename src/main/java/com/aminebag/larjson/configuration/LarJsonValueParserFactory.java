package com.aminebag.larjson.configuration;

import com.aminebag.larjson.parser.LarJsonValueParser;

/**
 * @author Amine Bagdouri
 *
 * A factory of {@link LarJsonValueParser}
 */
public interface LarJsonValueParserFactory {

    /**
     * @return a parser based on the provided configuration
     */
    LarJsonValueParser get(LarJsonReadConfiguration readConfiguration);
}
