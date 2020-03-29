package com.aminebag.larjson.utils;

import com.aminebag.larjson.api.LarJsonCloneable;
import com.aminebag.larjson.api.LarJsonPath;
import com.aminebag.larjson.api.LarJsonTypedWriteable;
import com.aminebag.larjson.api.LarJsonWriteable;
import com.aminebag.larjson.configuration.LarJsonTypedWriteConfiguration;
import com.google.gson.stream.JsonWriter;

import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Amine Bagdouri
 *
 * Utility methods that give access to {@link Method} objects used by the mapper
 */
public class LarJsonMethods {
    private static final Method EQUALS_METHOD = findEqualsMethod();
    private static final Method HASHCODE_METHOD = findHashcodeMethod();
    private static final Method TO_STRING_METHOD = findToStringMethod();
    private static final Method CLOSE_METHOD = findCloseMethod();
    private static final Method CLONE_METHOD = findCloneMethod();
    private static final Method DEFAULT_CONFIG_WRITE_METHOD = findDefaultConfigWriteMethod();
    private static final Method TYPED_CUSTOM_CONFIG_WRITE_METHOD = findTypedCustomConfigWriteMethod();
    private static final Method DEFAULT_CONFIG_JSON_WRITER_WRITE_METHOD = findDefaultConfigJsonWriterWriteMethod();
    private static final Method TYPED_CUSTOM_CONFIG_JSON_WRITER_WRITE_METHOD = findTypedCustomConfigJsonWriterWriteMethod();
    private static final Method GET_LARJSON_PATH_METHOD = findGetLarJsonPathMethod();
    private static final Method GET_LARJSON_PATH_WITH_STRING_BUILDER_METHOD = findGetLarJsonPathWithStringBuilderMethod();
    private static final Method GET_PARENT_LARJSON_PATH_METHOD = findGetParentLarJsonPathMethod();
    private static final Set<Method> ALL_METHODS;

    static {
        Set<Method> allMethods = new HashSet<>();
        allMethods.add(EQUALS_METHOD);
        allMethods.add(HASHCODE_METHOD);
        allMethods.add(TO_STRING_METHOD);
        allMethods.add(CLOSE_METHOD);
        allMethods.add(CLONE_METHOD);
        allMethods.add(DEFAULT_CONFIG_WRITE_METHOD);
        allMethods.add(TYPED_CUSTOM_CONFIG_WRITE_METHOD);
        allMethods.add(DEFAULT_CONFIG_JSON_WRITER_WRITE_METHOD);
        allMethods.add(TYPED_CUSTOM_CONFIG_JSON_WRITER_WRITE_METHOD);
        allMethods.add(GET_LARJSON_PATH_METHOD);
        allMethods.add(GET_LARJSON_PATH_WITH_STRING_BUILDER_METHOD);
        allMethods.add(GET_PARENT_LARJSON_PATH_METHOD);
        ALL_METHODS = Collections.unmodifiableSet(allMethods);
    }

    private LarJsonMethods() {

    }

    public static boolean isEqualsMethod(Method method) {
        return EQUALS_METHOD.equals(method);
    }

    public static boolean isHashcodeMethod(Method method) {
        return HASHCODE_METHOD.equals(method);
    }

    public static boolean isToStringMethod(Method method) {
        return TO_STRING_METHOD.equals(method);
    }

    public static boolean isCloseMethod(Method method) {
        if(CLOSE_METHOD.equals(method)) {
            return true;
        }
        if(!"close".equals(method.getName()) || method.getParameterCount() > 0 ||
                !method.getReturnType().equals(void.class)) {
            return false;
        }
        Class[] exceptionTypes = method.getExceptionTypes();
        for(Class<?> exceptionType : exceptionTypes) {
            if(exceptionType.isAssignableFrom(IOException.class)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isCloneMethod(Method method) {
        return CLONE_METHOD.equals(method) ||
                ("clone".equals(method.getName()) && method.getParameterCount() == 0
                        && method.getReturnType().isAssignableFrom(method.getDeclaringClass()));
    }

    public static boolean isDefaultConfigWriteMethod(Method method) {
        return DEFAULT_CONFIG_WRITE_METHOD.equals(method);
    }

    public static boolean isTypedCustomConfigWriteMethod(Method method) {
        return TYPED_CUSTOM_CONFIG_WRITE_METHOD.equals(method);
    }

    public static boolean isDefaultConfigJsonWriterWriteMethod(Method method) {
        return DEFAULT_CONFIG_JSON_WRITER_WRITE_METHOD.equals(method);
    }

    public static boolean isTypedCustomConfigJsonWriterWriteMethod(Method method) {
        return TYPED_CUSTOM_CONFIG_JSON_WRITER_WRITE_METHOD.equals(method);
    }

    public static boolean isGetLarJsonPathMethod(Method method) {
        return GET_LARJSON_PATH_METHOD.equals(method);
    }

    public static boolean isGetLarJsonPathWithStringBuilderMethod(Method method) {
        return GET_LARJSON_PATH_WITH_STRING_BUILDER_METHOD.equals(method);
    }

    public static boolean isGetParentLarJsonPathMethod(Method method) {
        return GET_PARENT_LARJSON_PATH_METHOD.equals(method);
    }

    public static boolean isSupportedMethod(Method method) {
        return ALL_METHODS.contains(method) || isCloneMethod(method) || isCloseMethod(method);
    }

    private static Method findEqualsMethod(){
        try {
            return Object.class.getDeclaredMethod("equals", Object.class);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private static Method findHashcodeMethod(){
        try {
            return Object.class.getDeclaredMethod("hashCode");
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private static Method findToStringMethod(){
        try {
            return Object.class.getDeclaredMethod("toString");
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private static Method findCloseMethod(){
        try {
            return Closeable.class.getDeclaredMethod("close");
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private static Method findCloneMethod(){
        try {
            return LarJsonCloneable.class.getDeclaredMethod("clone");
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private static Method findDefaultConfigWriteMethod(){
        try {
            return LarJsonWriteable.class.getDeclaredMethod("write", Writer.class);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private static Method findTypedCustomConfigWriteMethod(){
        try {
            return LarJsonTypedWriteable.class.getDeclaredMethod("write", Writer.class,
                    LarJsonTypedWriteConfiguration.class);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private static Method findDefaultConfigJsonWriterWriteMethod(){
        try {
            return LarJsonWriteable.class.getDeclaredMethod("write", JsonWriter.class);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private static Method findTypedCustomConfigJsonWriterWriteMethod(){
        try {
            return LarJsonTypedWriteable.class.getDeclaredMethod("write", JsonWriter.class,
                    LarJsonTypedWriteConfiguration.class);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private static Method findGetLarJsonPathMethod(){
        try {
            return LarJsonPath.class.getDeclaredMethod("getLarJsonPath");
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private static Method findGetLarJsonPathWithStringBuilderMethod(){
        try {
            return LarJsonPath.class.getDeclaredMethod("getLarJsonPath", StringBuilder.class);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private static Method findGetParentLarJsonPathMethod(){
        try {
            return LarJsonPath.class.getDeclaredMethod("getParentLarJsonPath");
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
