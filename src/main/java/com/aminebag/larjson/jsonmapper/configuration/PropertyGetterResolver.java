package com.aminebag.larjson.jsonmapper.configuration;

import java.lang.reflect.Method;
import java.util.Collection;

public interface PropertyGetterResolver {
    Method findBestCandidate(String propertyName, Collection<Method> candidates);
    boolean match(Method getter, String propertyName);
    boolean isGetter(Method method);
}
