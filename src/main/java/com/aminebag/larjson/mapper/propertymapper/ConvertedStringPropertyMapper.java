package com.aminebag.larjson.mapper.propertymapper;

import com.aminebag.larjson.exception.LarJsonException;
import com.aminebag.larjson.configuration.LarJsonTypedWriteConfiguration;
import com.aminebag.larjson.mapper.element.LarJsonContext;
import com.aminebag.larjson.valueconverter.StringValueConverter;
import com.aminebag.larjson.parser.LarJsonValueParser;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author Amine Bagdouri
 *
 * A mapper for model properties that can be converted from string values using a {@link StringValueConverter}
 */
public class ConvertedStringPropertyMapper<T> extends CacheableValuePropertyMapper<T> {
    private final LarJsonValueParser larJsonValueParser;
    private final StringValueConverter stringValueConverter;
    private final Class<T> type;

    public ConvertedStringPropertyMapper(String name, Method getterMethod, Method setterMethod, int getterIndex,
                                         int setterIndex, boolean required, LarJsonValueParser larJsonValueParser,
                                         StringValueConverter stringConverter, Class<T> type) {
        super(name, getterMethod, setterMethod, getterIndex, setterIndex, required);
        this.larJsonValueParser = larJsonValueParser;
        this.stringValueConverter = stringConverter;
        this.type = type;
    }

    @Override
    protected void writeNotNullValue(T value, JsonWriter writer, LarJsonTypedWriteConfiguration writeConfiguration)
            throws IOException, LarJsonException {

        writer.value(stringValueConverter.toString(value));
    }



    @Override
    protected T calculateCacheableValue(LarJsonContext context, long jsonPosition) throws IOException, LarJsonException {
        return (T) stringValueConverter.fromString(larJsonValueParser.parseString(
                context.getCharacterStream(jsonPosition)));
    }

    @Override
    public T nullValue() throws LarJsonException {
        return (T) stringValueConverter.fromString(null);
    }

    @Override
    public Class<T> getType() {
        return type;
    }
}
