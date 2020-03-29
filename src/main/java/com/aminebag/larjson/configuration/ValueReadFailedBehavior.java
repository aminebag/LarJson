package com.aminebag.larjson.configuration;

import com.aminebag.larjson.mapper.LarJsonMapperUtils;
import com.aminebag.larjson.exception.LarJsonValueReadException;

import java.lang.reflect.Method;

/**
 * @author Amine Bagdouri
 *
 * Indicates how to behave when reading a model object value fails due to a resource access or a JSON related error
 */
public interface ValueReadFailedBehavior {

    /**
     * Return {@code null} when reading a model object value fails
     */
    ValueReadFailedBehavior RETURN_NULL = (o,m,a,p,e)-> LarJsonMapperUtils.nullValue(m);

    /**
     * Throw a {@link LarJsonValueReadException} when reading a model object value fails
     */
    ValueReadFailedBehavior THROW_EXCEPTION = (o,m,a,p,e)->{
        throw new LarJsonValueReadException("Failed to read value at path " + p, e);
    };

    /**
     * Indicates how to behave when reading a model object value fails.
     * This method's return value must be compatible with the return type of the provided {@code method} argument.
     * This method may throw a runtime exception.
     *
     * @param object the model object
     * @param method the called method
     * @param args the args of the called method
     * @param path the JSON path to the value we tried to read
     * @param e an optional exception that caused reading to fail
     */
    Object onValueReadFailed(Object object, Method method, Object[] args, String path, Exception e);
}
