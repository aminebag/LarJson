package com.aminebag.larjson.mapper.propertymapper;

import com.aminebag.larjson.exception.LarJsonException;
import com.aminebag.larjson.configuration.LarJsonTypedWriteConfiguration;
import com.aminebag.larjson.mapper.element.LarJsonContext;
import com.aminebag.larjson.parser.LarJsonValueParser;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author Amine Bagdouri
 *
 * A mapper for {@link Character} model properties
 */
public class CharPropertyMapper extends CacheableValuePropertyMapper<Character> {

    private final LarJsonValueParser larJsonValueParser;

    public CharPropertyMapper(String name, Method getterMethod, Method setterMethod, int getterIndex,
                              int setterIndex, boolean required, LarJsonValueParser larJsonValueParser) {
        super(name, getterMethod, setterMethod, getterIndex, setterIndex, required);
        this.larJsonValueParser = larJsonValueParser;
    }

    @Override
    protected Character calculateCacheableValue(LarJsonContext context, long jsonPosition)
            throws IOException, LarJsonException {
        return larJsonValueParser.parseChar(context.getCharacterStream(jsonPosition));
    }

    @Override
    protected void writeNotNullValue(Character value, JsonWriter writer, LarJsonTypedWriteConfiguration writeConfiguration) throws IOException {
        writer.value(Character.toString(value));
    }

    @Override
    public Class<Character> getType() {
        return Character.class;
    }
}

