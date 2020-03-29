package com.aminebag.larjson.mapper.propertymapper;

import com.aminebag.larjson.exception.LarJsonException;
import com.aminebag.larjson.mapper.element.LarJsonContext;
import com.aminebag.larjson.parser.LarJsonParseException;
import com.aminebag.larjson.parser.LarJsonValueParser;

import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;

/**
 * @author Amine Bagdouri
 *
 * A mapper for model properties that can tolerate blank string values
 */
abstract class BlankFriendlyPropertyMapper<T> extends CacheableValuePropertyMapper<T> {

    private final LarJsonValueParser larJsonValueParser;

    protected BlankFriendlyPropertyMapper(String name, Method getterMethod, Method setterMethod, int getterIndex,
                                          int setterIndex, boolean required, LarJsonValueParser larJsonValueParser) {
        super(name, getterMethod, setterMethod, getterIndex, setterIndex, required);
        this.larJsonValueParser = larJsonValueParser;
    }

    @Override
    protected final T calculateCacheableValue(LarJsonContext context, long jsonPosition) throws IOException,
            LarJsonException {
        try {
            return calculateBlankFriendlyValue(context, jsonPosition);
        } catch (LarJsonParseException e) {
            if(context.getReadConfiguration().isLenient()) {
                String value = larJsonValueParser.parseString(context.getCharacterStream(jsonPosition));
                if(value.trim().isEmpty()) {
                    return nullValue();
                }
            }
            throw e;
        }
    }

    protected abstract T calculateBlankFriendlyValue(LarJsonContext context, long jsonPosition) throws IOException,
            LarJsonException;

    protected final LarJsonValueParser getLarJsonValueParser() {
        return larJsonValueParser;
    }
}
