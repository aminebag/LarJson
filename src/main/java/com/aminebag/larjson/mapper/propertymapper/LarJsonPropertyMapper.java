package com.aminebag.larjson.mapper.propertymapper;

import com.aminebag.larjson.api.LarJsonPath;
import com.aminebag.larjson.blueprint.LarJsonBlueprintWriter;
import com.aminebag.larjson.configuration.LarJsonTypedWriteConfiguration;
import com.aminebag.larjson.configuration.PropertyResolver;
import com.aminebag.larjson.exception.LarJsonException;
import com.aminebag.larjson.mapper.element.LarJsonContext;
import com.aminebag.larjson.mapper.valueoverwriter.ValueOverwriter;
import com.aminebag.larjson.parser.LarJsonTokenParser;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author Amine Bagdouri
 *
 * An abstract mapper for model properties
 */
public abstract class LarJsonPropertyMapper<T> {

    private final String name;
    private final Method getterMethod;
    private final Method setterMethod;
    private final int getterIndex;
    private final int setterIndex;
    private final boolean required;

    public LarJsonPropertyMapper(String name, Method getterMethod, Method setterMethod, int getterIndex,
                                 int setterIndex, boolean required) {
        this.name = name;
        this.getterMethod = getterMethod;
        this.setterMethod = setterMethod;
        this.getterIndex = getterIndex;
        this.setterIndex = setterIndex;
        this.required = required;
    }

    public abstract T calculateValue(
            LarJsonContext context, long key, long parentJsonPosition, long parentBlueprintPosition,
            LarJsonPath parentPath, String pathElement) throws IOException, LarJsonException;

    public abstract void write(LarJsonContext context, long key, long parentJsonPosition,
                               long parentBlueprintPosition, JsonWriter jsonWriter,
                               ValueOverwriter valueOverwriter, LarJsonTypedWriteConfiguration writeConfiguration,
                               PropertyResolver propertyResolver)
            throws IOException, LarJsonException;

    public abstract long enrichBlueprint(
            LarJsonContext context, LarJsonBlueprintWriter blueprintWriter, LarJsonTokenParser tokenParser,
            long parentJsonPosition, long parentBlueprintPosition)
            throws IOException, LarJsonException;

    public abstract void deepClone(
            LarJsonContext context, long key, long parentJsonPosition, long parentBlueprintPosition,
            ValueOverwriter sourceValueOverwriter, ValueOverwriter destValueOverwriter) throws IOException;

    public abstract T nullValue() throws LarJsonException;

    public abstract Class<T> getType();

    protected final IllegalArgumentException illegalKey(long key){
        throw new IllegalArgumentException("Key not supported : " + key);
    }

    public final Method getGetterMethod() {
        return getterMethod;
    }

    public final Method getSetterMethod() {
        return setterMethod;
    }

    public boolean hasSetter() {
        return setterMethod != null;
    }

    public int getGetterIndex() {
        return getterIndex;
    }

    public int getSetterIndex() {
        return setterIndex;
    }

    public String getName() {
        return name;
    }

    public boolean isRequired() {
        return required;
    }
}
