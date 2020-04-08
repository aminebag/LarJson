package com.aminebag.larjson.jsonmapper.configuration;

import com.aminebag.larjson.jsonmapper.exception.LarJsonValueReadException;

import java.lang.reflect.Method;

public interface ValueReadFailedBehavior {

    ValueReadFailedBehavior RETURN_NULL = (o,m,a,e)->{
        Class<?> returnType = m.getReturnType();
        if(returnType == boolean.class) return false;
        else if(returnType == int.class) return 0;
        else if(returnType == double.class) return 0d;
        else if(returnType == float.class) return 0f;
        else if(returnType == long.class) return 0L;
        else if(returnType == char.class) return (char)0;
        else if(returnType == short.class) return (short)0;
        else if(returnType == byte.class) return (byte)0;
        else return null;
    };

    ValueReadFailedBehavior THROW_EXCEPTION = (o,m,a,e)->{
        throw new LarJsonValueReadException(e);
    };

    Object onValueReadFailed(Object object, Method method, Object[] args, Exception e);
}
