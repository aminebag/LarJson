package com.aminebag.larjson.mapper.propertymapper;

import com.aminebag.larjson.exception.LarJsonException;
import com.aminebag.larjson.configuration.LarJsonTypedWriteConfiguration;
import com.aminebag.larjson.mapper.element.LarJsonContext;
import com.aminebag.larjson.parser.LarJsonValueParser;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Amine Bagdouri
 *
 * A mapper for {@link BigInteger} model properties
 */
public class BigIntegerPropertyMapper extends BlankFriendlyPropertyMapper<BigInteger> {

    public BigIntegerPropertyMapper(String name, Method getterMethod, Method setterMethod, int getterIndex,
                                    int setterIndex, boolean required, LarJsonValueParser larJsonValueParser) {
        super(name, getterMethod, setterMethod, getterIndex, setterIndex, required, larJsonValueParser);
    }

    @Override
    protected BigInteger calculateBlankFriendlyValue(LarJsonContext context, long jsonPosition) throws IOException, LarJsonException {
        return getLarJsonValueParser().parseBigInteger(context.getCharacterStream(jsonPosition));
    }

    @Override
    protected void writeNotNullValue(BigInteger value, JsonWriter writer, LarJsonTypedWriteConfiguration writeConfiguration) throws IOException {
        writer.value(value);
    }

    @Override
    public Class<BigInteger> getType() {
        return BigInteger.class;
    }
}
