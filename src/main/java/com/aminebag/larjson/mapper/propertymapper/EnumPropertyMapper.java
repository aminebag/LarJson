package com.aminebag.larjson.mapper.propertymapper;

import com.aminebag.larjson.api.LarJsonPath;
import com.aminebag.larjson.configuration.AnnotationConfigurationFactory;
import com.aminebag.larjson.configuration.LarJsonTypedWriteConfiguration;
import com.aminebag.larjson.configuration.PropertyConfigurationFactory;
import com.aminebag.larjson.configuration.PropertyResolver;
import com.aminebag.larjson.exception.LarJsonException;
import com.aminebag.larjson.mapper.LarJsonMapperUtils;
import com.aminebag.larjson.mapper.element.LarJsonContext;
import com.aminebag.larjson.mapper.valueoverwriter.ValueOverwriter;
import com.aminebag.larjson.valueconverter.StringValueConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author Amine Bagdouri
 *
 * A mapper for enum model properties
 */
abstract class EnumPropertyMapper<T extends Enum<T>> extends LarJsonPropertyMapper<T> {
    protected final Class<T> enumClass;
    protected final T[] values;

    public EnumPropertyMapper(String name, Method getterMethod, Method setterMethod, int getterIndex,
                              int setterIndex, boolean required, Class<T> enumClass) {
        super(name, getterMethod, setterMethod, getterIndex, setterIndex, required);
        this.enumClass = enumClass;
        this.values = enumClass.getEnumConstants();
    }

    @Override
    public final T calculateValue(LarJsonContext context, long key, long parentJsonPosition, long parentBlueprintPosition
            , LarJsonPath parentPath, String pathElement) {
        if(key > values.length) {
            throw illegalKey(key);
        } else {
            return values[(int)key];
        }
    }

    @Override
    public void write(LarJsonContext context, long key, long parentJsonPosition, long parentBlueprintPosition,
                      JsonWriter jsonWriter, ValueOverwriter valueOverwriter,
                      LarJsonTypedWriteConfiguration writeConfiguration, PropertyResolver propertyResolver) throws IOException, LarJsonException {
        if(key > values.length) {
            throw illegalKey(key);
        }
        T value = values[(int)key];

        Method getterMethod = getGetterMethod();
        if(getterMethod != null) {
            PropertyConfigurationFactory propertyConfigurationFactory = writeConfiguration.getPropertyConfigurationFactory();
            StringValueConverter converter = LarJsonMapperUtils.getPropertyStringValueConverter(
                    getterMethod, propertyConfigurationFactory);
            if (converter != null) {
                jsonWriter.value(converter.toString(value));
                return;
            }

            AnnotationConfigurationFactory annotationConfigurationFactory =
                    writeConfiguration.getAnnotationConfigurationFactory();
            JsonFormat jsonFormat;
            if ((jsonFormat = LarJsonMapperUtils.getMethodAnnotation(
                    getterMethod, JsonFormat.class, annotationConfigurationFactory)) != null) {
                switch (jsonFormat.shape()) {
                    case STRING:
                        jsonWriter.value(value.name());
                        return;
                    case NUMBER:
                        jsonWriter.value(value.ordinal());
                        return;
                }
            }
        }

        StringValueConverter converter = writeConfiguration.getStringValueConverterFactory().get(enumClass);
        if(converter != null) {
            jsonWriter.value(converter.toString(value.name()));
            return;
        }

        jsonWriter.value(value.name());
    }

    @Override
    public void deepClone(LarJsonContext context, long key, long parentJsonPosition, long parentBlueprintPosition,
                          ValueOverwriter sourceValueOverwriter, ValueOverwriter destValueOverwriter) {
        //do nothing
    }

    @Override
    public T nullValue() {
        return null;
    }

    @Override
    public Class<T> getType() {
        return enumClass;
    }
}
