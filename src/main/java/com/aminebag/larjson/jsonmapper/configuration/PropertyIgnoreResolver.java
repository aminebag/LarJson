package com.aminebag.larjson.jsonmapper.configuration;

import java.lang.reflect.Method;

public interface PropertyIgnoreResolver {
    boolean isPropertyIgnored(Method method);
}
