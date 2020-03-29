package com.aminebag.larjson.mapper.propertymapper;

import com.aminebag.larjson.api.LarJsonPath;
import com.aminebag.larjson.blueprint.LarJsonBlueprintWriter;
import com.aminebag.larjson.configuration.*;
import com.aminebag.larjson.exception.LarJsonException;
import com.aminebag.larjson.mapper.LarJsonMapperUtils;
import com.aminebag.larjson.mapper.element.LarJsonContext;
import com.aminebag.larjson.mapper.valueoverwriter.ValueOverwriter;
import com.aminebag.larjson.parser.LarJsonTokenParser;
import com.aminebag.larjson.valueconverter.StringValueConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author Amine Bagdouri
 *
 * A mapper for simple values model properties
 */
abstract class SimpleValuePropertyMapper<T> extends LarJsonPropertyMapper<T> {
    public SimpleValuePropertyMapper(String name, Method getterMethod, Method setterMethod, int getterIndex,
                                     int setterIndex, boolean required) {
        super(name, getterMethod, setterMethod, getterIndex, setterIndex, required);
    }

    @Override
    public final T calculateValue(LarJsonContext context, long key, long parentJsonPosition, long parentBlueprintPosition
            , LarJsonPath parentPath, String pathElement)
            throws IOException, LarJsonException {
        return calculateValue(context, key, parentJsonPosition);
    }

    private static StringValueConverter<?> getStringValueConverter(
            Class<?> returnType, Method getterMethod, AnnotationConfigurationFactory annotationConfigurationFactory,
            PropertyConfigurationFactory propertyConfigurationFactory,
            StringValueConverterFactory stringValueConverterFactory) {
        if(getterMethod != null) {
            StringValueConverter converter = LarJsonMapperUtils.getPropertyStringValueConverter(getterMethod, propertyConfigurationFactory);
            if (converter != null) {
                return converter;
            }

            JsonFormat jsonFormat;
            if ((jsonFormat = LarJsonMapperUtils.getMethodAnnotation(
                    getterMethod, JsonFormat.class, annotationConfigurationFactory)) != null) {
                converter = LarJsonMapperUtils.getJsonFormatValueConverter(returnType, jsonFormat);
                if (converter != null) {
                    return converter;
                }
            }
        }

        return stringValueConverterFactory.get(returnType);
    }

    @Override
    public void write(LarJsonContext context, long key, long parentJsonPosition, long parentBlueprintPosition,
                      JsonWriter jsonWriter, ValueOverwriter valueOverwriter,
                      LarJsonTypedWriteConfiguration writeConfiguration, PropertyResolver propertyResolver) throws IOException, LarJsonException {

        StringValueConverter converter = getStringValueConverter(getType(), getGetterMethod(),
                writeConfiguration.getAnnotationConfigurationFactory(),
                writeConfiguration.getPropertyConfigurationFactory(),
                writeConfiguration.getStringValueConverterFactory());

        T value = calculateValue(context, key, parentJsonPosition);
        if(converter != null) {
            jsonWriter.value(converter.toString(value));
        } else {
            writeNotNullValue(value, jsonWriter, writeConfiguration);
        }
    }

    @Override
    public void deepClone(LarJsonContext context, long key, long parentJsonPosition, long parentBlueprintPosition,
                          ValueOverwriter sourceValueOverwriter, ValueOverwriter destValueOverwriter) throws IOException {
        //do nothing
    }

    @Override
    public long enrichBlueprint(LarJsonContext context, LarJsonBlueprintWriter blueprintWriter, LarJsonTokenParser tokenParser,
                                long parentJsonPosition, long parentBlueprintPosition) throws IOException, LarJsonException {
        long jsonPosition = tokenParser.getCurrentPosition() - parentJsonPosition;
        tokenParser.skipValue();
        return jsonPosition;
    }

    protected abstract void writeNotNullValue(T value, JsonWriter writer, LarJsonTypedWriteConfiguration writeConfiguration)
            throws IOException, LarJsonException;

    protected abstract T calculateValue(LarJsonContext context, long key, long parentJsonPosition)
            throws IOException, LarJsonException;

    @Override
    public T nullValue() throws LarJsonException {
        return null;
    }
}
