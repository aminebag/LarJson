package com.aminebag.larjson.configuration;

import com.aminebag.larjson.api.LarJsonRootTypedElement;
import com.aminebag.larjson.mapper.LarJsonMapperUtils;

import java.lang.reflect.Method;

/**
 * @author Amine Bagdouri
 *
 * Indicates how to behave when an unsupported method is called.
 * Supported methods are the following :
 * <ul>
 *     <li>All methods of {{@link LarJsonRootTypedElement} and its sub-interfaces}.</li>
 *     <li>Any method representing a property getter</li>
 *     <li>Any method representing a property setter corresponding to a property getter</li>
 * </ul>
 */
public interface UnsupportedMethodCalledBehavior {

    /**
     * Always return {@code null}
     */
    UnsupportedMethodCalledBehavior RETURN_NULL = (o,m,a)-> LarJsonMapperUtils.nullValue(m);

    /**
     * Alwayss thorw an {@link UnsupportedOperationException}
     */
    UnsupportedMethodCalledBehavior THROW_EXCEPTION = (o,m,a)->{
        throw new UnsupportedOperationException("Unsupported method : " + m);
    };

    /**
     * Indicates how to behave when an unsupported method is called on a model object.
     * This method's return value must be compatible with the return type of the provided {@code method} argument.
     * This method may throw a runtime exception.
     *
     * @param object the model object
     * @param method the called method
     * @param args the args of the called method
     */
    Object onUnsupportedMethodCalled(Object object, Method method, Object[] args);
}
