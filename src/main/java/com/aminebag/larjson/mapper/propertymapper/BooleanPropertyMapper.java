package com.aminebag.larjson.mapper.propertymapper;

import com.aminebag.larjson.blueprint.LarJsonBlueprintWriter;
import com.aminebag.larjson.exception.LarJsonException;
import com.aminebag.larjson.configuration.LarJsonTypedWriteConfiguration;
import com.aminebag.larjson.mapper.element.LarJsonContext;
import com.aminebag.larjson.parser.LarJsonParseException;
import com.aminebag.larjson.parser.LarJsonTokenParser;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;

/**
 * @author Amine Bagdouri
 *
 * A mapper for {@link Boolean} model properties
 */
public class BooleanPropertyMapper extends SimpleValuePropertyMapper<Boolean> {
    private static final long FALSE_VALUE = 0L;
    private static final long TRUE_VALUE = 1L;

    public BooleanPropertyMapper(String name, Method getterMethod, Method setterMethod, int getterIndex,
                                 int setterIndex, boolean required) {
        super(name, getterMethod, setterMethod, getterIndex, setterIndex, required);
    }

    @Override
    public Boolean calculateValue(LarJsonContext context, long key, long parentJsonPosition) throws IOException, LarJsonException {
        if(key == FALSE_VALUE) {
            return false;
        } else if(key == TRUE_VALUE) {
            return true;
        } else {
            throw illegalKey(key);
        }
    }

    @Override
    protected void writeNotNullValue(Boolean value, JsonWriter writer, LarJsonTypedWriteConfiguration writeConfiguration)
            throws IOException {
        writer.value((boolean) value);
    }

    @Override
    public long enrichBlueprint(LarJsonContext context, LarJsonBlueprintWriter blueprintWriter,
                                LarJsonTokenParser tokenParser, long parentJsonPosition, long parentBlueprintPosition)
            throws IOException, LarJsonParseException {
        boolean value = tokenParser.nextBoolean();
        return value ? TRUE_VALUE : FALSE_VALUE;
    }

    @Override
    public Class<Boolean> getType() {
        return Boolean.class;
    }

}
