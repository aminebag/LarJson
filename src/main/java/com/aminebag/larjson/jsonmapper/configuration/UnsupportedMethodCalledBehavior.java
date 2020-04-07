package com.aminebag.larjson.jsonmapper.configuration;

import java.lang.reflect.Method;

public interface UnsupportedMethodCalledBehavior {

    UnsupportedMethodCalledBehavior THROW_EXCEPTION = (o,m,a)->{
        throw new UnsupportedOperationException();
    };

    Object onUnsupportedMethodCalled(Object object, Method method, Object[] args);
}
