package com.aminebag.larjson.jsonmapper.configuration;

import java.lang.reflect.Method;
import java.util.Collection;

public abstract class AbstractPropertyGetterResolver implements PropertyGetterResolver {
    @Override
    public Method findBestCandidate(String propertyName, Collection<Method> candidates) {
        return null;
    }

    @Override
    public boolean match(Method getter, String propertyName) {
        return false;
    }

    @Override
    public boolean isGetter(Method method) {
        String methodName = method.getName();
        return method.getParameterCount() == 0 &&
            ((methodName.startsWith("get") && methodName.length() >= 4 && isNotLowerCase(methodName.charAt(3))) ||
            (methodName.startsWith("is") && methodName.length() >= 3 && isNotLowerCase(methodName.charAt(2)) &&
                (method.getReturnType() == boolean.class || method.getReturnType() == Boolean.class)));
    }

    private static boolean isNotLowerCase(char c){
        return c < 'a' || c > 'z';
    }
}
