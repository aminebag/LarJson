package com.aminebag.larjson.parser;

import com.aminebag.larjson.parser.exception.LarJsonDefinitionException;
import com.aminebag.larjson.parser.getter.*;
import org.springframework.core.ResolvableType;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LarJsonObjectParser<T> {

    private final Class<T> rootInterface;
    private final ObjectLarJsonGetter rootGetter;

    public LarJsonObjectParser(Class<T> rootInterface){
        if(!rootInterface.isInterface()){
            throw new IllegalArgumentException(rootInterface + " is not an interface");
        }
        this.rootInterface = rootInterface;
        Map<Method, LarJsonGetter<?>> getters = new HashMap<>();
        initGetters(getters, rootInterface, 0, 1);
        this.rootGetter = new ObjectLarJsonGetter(0, (short)0, getters);

    }

    private static boolean isGetter(Method method) {
        String methodName = method.getName();
        return (methodName.startsWith("get") && methodName.length() >= 4
                && isNotLowerCase(methodName.charAt(3))) ||
                (methodName.startsWith("is") && methodName.length() >= 3
                        && isNotLowerCase(methodName.charAt(2)));
    }

    private static int initGetters(Map<Method, LarJsonGetter<?>> getters, Class<?> clazz, int index, int level){
        if(level > Short.MAX_VALUE){
            throw new LarJsonDefinitionException("The number of references in a chain of possible calls " +
                    "starting from the root exceeds the maximum allowed (" + Short.MAX_VALUE + ")." +
                    "Check for possible circular references.");
        }
        for(Method method : clazz.getMethods()){
            if(isGetter(method)){
                Class<?> returnType = method.getReturnType();
                if(returnType.equals(String.class)){
                    getters.put(method, new StringLarJsonGetter(index++));
                } else if(returnType.equals(boolean.class)){
                    getters.put(method, new PrimitiveBooleanLarJsonGetter(index++));
                } else if (returnType.equals(Boolean.class)){
                    getters.put(method, new BooleanLarJsonGetter(index++));
                } else if (returnType.isEnum()) {
                    Enum[] values = (Enum[]) returnType.getEnumConstants();
                    if(values.length <= SmallEnumLarJsonGetter.MAX_VALUES) {
                        getters.put(method, new SmallEnumLarJsonGetter(index++, values));
                    } else {
                        getters.put(method, new BigEnumLarJsonGetter(index++));
                    }
                } else if (returnType.equals(List.class)){
                    ResolvableType resolvableType = ResolvableType.forMethodReturnType(method);
                    Class<?> parameterType = resolvableType.getGeneric(0).resolve();
                    Map<Method, LarJsonGetter<?>> subGetters = new HashMap<>();
                    int objectSize = initGetters(subGetters, parameterType, 0, 1);
                    getters.put(method, new ListLarJsonGetter(index++, objectSize, subGetters));

                } else {
                    Map<Method, LarJsonGetter<?>> subGetters = new HashMap<>();
                    int firstSubGetterIndex = index;
                    index = initGetters(subGetters, returnType, index, (short)(level+1));
                    getters.put(method, new ObjectLarJsonGetter(firstSubGetterIndex, (short)level, subGetters));

                }
            }
        }
        return index;
    }

    private static boolean isNotLowerCase(char c){
        return c < 'a' || c > 'z';
    }

    public T parse(File jsonFile) {
        return null;
    }
}