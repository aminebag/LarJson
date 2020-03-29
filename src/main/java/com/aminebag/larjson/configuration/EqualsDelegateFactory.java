package com.aminebag.larjson.configuration;

/**
 * @author Amine Bagdouri
 *
 * A factory of {@link EqualsDelegate}s
 */
public interface EqualsDelegateFactory {

    /**
     * @return an {@link EqualsDelegate} for the provided class if it exists, otherwise return {@code null}
     */
    <T> EqualsDelegate<T> get(Class<T> clazz);
}
