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
 * A mapper for {@link String} model properties
 */
public class StringPropertyMapper extends CacheableValuePropertyMapper<String> {

    private final LarJsonValueParser larJsonValueParser;

    public StringPropertyMapper(String name, Method getterMethod, Method setterMethod, int getterIndex,
                                int setterIndex, boolean required, LarJsonValueParser larJsonValueParser) {
        super(name, getterMethod, setterMethod, getterIndex, setterIndex, required);
        this.larJsonValueParser = larJsonValueParser;
    }

    @Override
    protected String calculateCacheableValue(LarJsonContext context, long jsonPosition)
            throws IOException, LarJsonException {
        return larJsonValueParser.parseString(context.getCharacterStream(jsonPosition));
    }

    @Override
    protected void writeNotNullValue(String value, JsonWriter writer, LarJsonTypedWriteConfiguration writeConfiguration) throws IOException {
        writer.value(value);
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }
}
