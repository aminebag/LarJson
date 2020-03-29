package com.aminebag.larjson.configuration;

import java.lang.reflect.Method;

/**
 * @author Amine Bagdouri
 *
 * A factory of {@link PropertyConfiguration}
 */
public interface PropertyConfigurationFactory {

    /**
     * @param getter getter method
     * @return a property configuration corresponding to the given getter method if found, {@code null} otherwise
     */
    PropertyConfiguration get(Method getter);
}
