package com.aminebag.larjson.mapper.propertymapper;

import com.aminebag.larjson.blueprint.LarJsonBlueprintWriter;
import com.aminebag.larjson.exception.LarJsonException;
import com.aminebag.larjson.mapper.element.LarJsonContext;
import com.aminebag.larjson.exception.LarJsonConversionException;
import com.aminebag.larjson.parser.LarJsonTokenParser;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author Amine Bagdouri
 *
 * A mapper for enum model properties based on the name of the enum
 */
public class StringEnumPropertyMapper<T extends Enum<T>> extends EnumPropertyMapper<T> {

    public StringEnumPropertyMapper(String name, Method getterMethod, Method setterMethod, int getterIndex,
                                    int setterIndex, boolean required, Class<T> enumClazz) {
        super(name, getterMethod, setterMethod, getterIndex, setterIndex, required, enumClazz);
    }

    @Override
    public long enrichBlueprint(LarJsonContext context, LarJsonBlueprintWriter blueprintWriter, LarJsonTokenParser tokenParser,
                                long parentJsonPosition, long parentBlueprintPosition) throws IOException, LarJsonException {
        String value = tokenParser.nextString();
        try {
            return Enum.valueOf(enumClass, value).ordinal();
        } catch (IllegalArgumentException e) {
            throw new LarJsonConversionException(e);
        }
    }
}
