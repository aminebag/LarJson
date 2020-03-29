package com.aminebag.larjson.configuration;

import java.util.HashMap;

/**
 * @author Amine Bagdouri
 */
class DefaultEqualsDelegateFactory implements EqualsDelegateFactory {

    private final HashMap<Class<?>, EqualsDelegate<?>> delegates = new HashMap<>();

    @Override
    public <T> EqualsDelegate<T> get(Class<T> clazz) {
        return (EqualsDelegate<T>) delegates.get(clazz);
    }

    <T> void setEqualsDelegate(Class<T> clazz, EqualsDelegate<T> equalsDelegate) {
        delegates.put(clazz, equalsDelegate);
    }
}
