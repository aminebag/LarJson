package com.aminebag.larjson.configuration;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * @author Amine Bagdouri
 *
 * Maps a JSON attribute to a property getter and setter
 */
public interface PropertyResolver {

    /**
     * Find the getter method that corresponds to the JSON attribute name among a set of candidate getter methods.
     * The returned method must be equal to one of the getter candidates.
     * The set of candidates contains only methods that have been already been approved by {@link #isGetter(Method)}
     * filter.
     * @param attributeName JSON attribute name
     * @param candidates candidate methods have a non-void return type and zero arguments
     * @return the getter that corresponds to the JSON attribute name if found, {@code null} otherwise
     */
    Method findGetter(String attributeName, Collection<Method> candidates);

    /**
     * Indicates whether the provided method matches a getter pattern.
     * @param method method has a non-void return type and zero arguments
     * @return {@code true} if the method is a getter, {@code false} otherwise
     */
    boolean isGetter(Method method);

    /**
     * Find the setter method that corresponds to a getter method among a set of candidate methods.
     * The returned method must be equal to one of the setter candidates.
     * The returned setter method single argument must be assignable to the getter's return type.
     * @param getter getter method has a non-void return type and zero arguments
     * @param candidates candidate methods have a void return type and a single argument
     * @return
     */
    Method findSetter(Method getter, Collection<Method> candidates);

    /**
     * Calculate the JSON attribute name based on the getter method. The returned name must not be null.
     * The provided getter method has already been approved by {@link #isGetter(Method)} filter
     * @param getter getter method has a non-void return type and zero arguments
     * @return the JSON attribute name corresponding to the getter method
     */
    String getAttributeName(Method getter);
}
