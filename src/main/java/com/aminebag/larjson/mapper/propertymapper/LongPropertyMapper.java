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
 * A mapper for {@link Long} model properties
 */
public class LongPropertyMapper extends BlankFriendlyPropertyMapper<Long> {

    public LongPropertyMapper(String name, Method getterMethod, Method setterMethod, int getterIndex,
                              int setterIndex, boolean required, LarJsonValueParser larJsonValueParser) {
        super(name, getterMethod, setterMethod, getterIndex, setterIndex, required, larJsonValueParser);
    }

    @Override
    protected Long calculateBlankFriendlyValue(LarJsonContext context, long jsonPosition) throws IOException, LarJsonException {
        return getLarJsonValueParser().parseLong(context.getCharacterStream(jsonPosition));
    }

    @Override
    protected void writeNotNullValue(Long value, JsonWriter writer, LarJsonTypedWriteConfiguration writeConfiguration) throws IOException {
        writer.value(value);
    }

    @Override
    public Class<Long> getType() {
        return Long.class;
    }
}
