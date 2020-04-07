package com.aminebag.larjson.jsonmapper;

import com.aminebag.larjson.jsonmapper.configuration.LarJsonConfiguration;
import com.aminebag.larjson.jsonmapper.exception.LarJsonMappingDefinitionException;
import com.aminebag.larjson.jsonmapper.propertymapper.*;
import com.aminebag.larjson.jsonmapper.stringconverter.EnumStringConverter;
import com.aminebag.larjson.jsonmapper.stringconverter.StringConverter;
import org.springframework.core.ResolvableType;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LarJsonMapper<T> {
    private static final Method CLOSE_METHOD;

    static {
        Method closeMethod;
        try {
            closeMethod = Closeable.class.getDeclaredMethod("close");
        } catch (NoSuchMethodException e) {
            closeMethod = null;
        }
        CLOSE_METHOD = closeMethod;
    }

    private final Class<T> rootInterface;
    private final LarJsonConfiguration configuration;
    private final ObjectLarJsonPropertyMapper<T> rootMapper;

    public LarJsonMapper(Class<T> rootInterface, LarJsonConfiguration configuration){
        this.configuration = configuration;
        if(!rootInterface.isInterface()){
            throw new IllegalArgumentException(rootInterface + " is not an interface");
        }
        this.rootInterface = rootInterface;
        this.rootMapper = getObjectLarJsonPropertyMapper(rootInterface, 0, 0);
    }

    private LarJsonPropertyMapper<?> getListFriendlyLarJsonMapper(Class<?> returnType, int index, int level) {
        if(returnType.equals(String.class)) {
            return new StringPropertyMapper(index, configuration.getValueParser());
        } else if(returnType.equals(boolean.class)){
            return new PrimitiveBooleanPropertyMapper(index);
        } else if(returnType.equals(int.class)){
            return new PrimitiveIntegerPropertyMapper(index, configuration.getValueParser());
        } else if(returnType.equals(long.class)){
            return new PrimitiveLongPropertyMapper(index, configuration.getValueParser());
        } else if(returnType.equals(float.class)){
            return new PrimitiveFloatPropertyMapper(index, configuration.getValueParser());
        } else if(returnType.equals(double.class)){
            return new PrimitiveDoublePropertyMapper(index, configuration.getValueParser());
        } else if(returnType.equals(char.class)){
            return new PrimitiveCharPropertyMapper(index, configuration.getValueParser());
        } else if(returnType.equals(byte.class)){
            return new PrimitiveBytePropertyMapper(index);
        } else if(returnType.equals(short.class)){
            return new PrimitiveShortPropertyMapper(index, configuration.getValueParser());
        } else if(returnType.equals(Boolean.class)){
            return new BooleanPropertyMapper(index);
        } else if(returnType.equals(Integer.class)){
            return new IntegerPropertyMapper(index, configuration.getValueParser());
        } else if(returnType.equals(Long.class)){
            return new LongPropertyMapper(index, configuration.getValueParser());
        } else if(returnType.equals(Float.class)){
            return new FloatPropertyMapper(index, configuration.getValueParser());
        } else if(returnType.equals(Double.class)){
            return new DoublePropertyMapper(index, configuration.getValueParser());
        } else if(returnType.equals(Character.class)){
            return new CharPropertyMapper(index, configuration.getValueParser());
        } else if(returnType.equals(Byte.class)){
            return new BytePropertyMapper(index);
        } else if(returnType.equals(Short.class)){
            return new ShortPropertyMapper(index, configuration.getValueParser());
        } else if(returnType.isEnum()){
            Enum[] values = (Enum[]) returnType.getEnumConstants();
            if(values.length <= SmallEnumPropertyMapper.MAX_VALUES) {
                return new SmallEnumPropertyMapper(index, returnType, values);
            } else {
                return new ConvertedStringPropertyMapper(index, configuration.getValueParser(),
                        new EnumStringConverter(returnType));
            }
        } else {
            StringConverter<?> stringConverter = configuration.getStringConverterFactory()
                    .getStringConverter(returnType);
            if(stringConverter != null) {
                return new ConvertedStringPropertyMapper(index, configuration.getValueParser(), stringConverter);
            } else {
                return getObjectLarJsonPropertyMapper(returnType, index, level + 1);
            }
        }
    }

    private LarJsonPropertyMapper<?> getLarJsonMapper(Method method, int index, int level) {
        Class<?> returnType = method.getReturnType();
        if(returnType.equals(List.class) || returnType.equals(Collection.class)
                || returnType.equals(Iterable.class)) {
            ResolvableType resolvableType = ResolvableType.forMethodReturnType(method);
            Class<?> parameterType = resolvableType.getGeneric(0).resolve();
            LarJsonPropertyMapper<?> subMapper = getListFriendlyLarJsonMapper(parameterType, 0, 0);
            return new ListLarJsonPropertyMapper(index, subMapper);
        } else {
            return getListFriendlyLarJsonMapper(returnType, index, level);
        }
    }

    private ObjectLarJsonPropertyMapper getObjectLarJsonPropertyMapper(Class<?> clazz, int index, int level){
        int initialIndex = index;
        if(!clazz.isInterface()) {
            if(configuration.isUnsupportedReturnTypeAllowed() && index > 0) {
                return null;
            } else {
                throw new LarJsonMappingDefinitionException("The type " + clazz.getName() + " is not an interface");
            }
        }
        Map<Method, LarJsonPropertyMapper<?>> mappers = new HashMap<>();
        for(Method method : clazz.getMethods()){
            if(configuration.getPropertyGetterMatcher().isGetter(method)){
                if(!configuration.getPropertyIgnoreResolver().isPropertyIgnored(method)){
                    LarJsonPropertyMapper mapper = getLarJsonMapper(method, index, level+1);
                    if(mapper != null) {
                        index += mapper.getLength();
                    }
                    mappers.put(method, mapper);
                }
            } else if (!isCloseMethod(method) && !configuration.isUnsupportedMethodAllowed()) {
                throw new LarJsonMappingDefinitionException("The signature of the method " + clazz.getName() +
                        "::" + method.getName() + " doesn't match any of the supported getter methods. " +
                        "Consider changing the method signature " +
                        "or the unsupported method behavior configuration.");
            }
        }
        return new ObjectLarJsonPropertyMapper(initialIndex, clazz, index - initialIndex, level, mappers);
    }

    public T readObject(File jsonFile) {
        return null;
    }
    public CloseableList<T> readArray(File jsonFile) {
        return null;
    }

    public void close(T object) throws IOException {
        if(object instanceof Closeable) {
            ((Closeable)object).close();
        }
    }

    public void close(List<T> list) throws IOException {
        if(list instanceof CloseableList<?>) {
            ((CloseableList<?>)list).close();
        }
    }

    static boolean isCloseMethod(Method method) {
        return method == LarJsonMapper.CLOSE_METHOD || (
                method.getDeclaringClass().equals(Closeable.class) &&
                        method.getReturnType() == void.class &&
                        method.getName().equals("close") &&
                        method.getParameterCount() == 0 &&
                        method.getExceptionTypes().length == 1 &&
                        method.getExceptionTypes()[0] == IOException.class);
    }
}