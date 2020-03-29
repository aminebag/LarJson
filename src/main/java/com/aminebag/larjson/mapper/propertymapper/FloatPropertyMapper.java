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
 * A mapper for {@link Float} model properties
 */
public class FloatPropertyMapper extends BlankFriendlyPropertyMapper<Float> {

    public FloatPropertyMapper(String name, Method getterMethod, Method setterMethod, int getterIndex,
                               int setterIndex, boolean required, LarJsonValueParser larJsonValueParser) {
        super(name, getterMethod, setterMethod, getterIndex, setterIndex, required, larJsonValueParser);
    }

    @Override
    protected Float calculateBlankFriendlyValue(LarJsonContext context, long jsonPosition)
            throws IOException, LarJsonException {
        return getLarJsonValueParser().parseFloat(context.getCharacterStream(jsonPosition));
    }

    @Override
    protected void writeNotNullValue(Float value, JsonWriter writer, LarJsonTypedWriteConfiguration writeConfiguration) throws IOException {
        writer.value(value);
    }

    @Override
    public Class<Float> getType() {
        return Float.class;
    }
}
