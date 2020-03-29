package com.aminebag.larjson.configuration;

/**
 * @author Amine Bagdouri
 *
 * A factory of {@link PropertyResolver}
 */
public interface PropertyResolverFactory {

    /**
     * @param rootInterface the root model interface
     * @return a property resolver corresponding to the provided root model interface
     */
    PropertyResolver get(Class<?> rootInterface);
}
