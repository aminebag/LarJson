package com.aminebag.larjson.jsonmapper;

import com.aminebag.larjson.exception.LarJsonCheckedException;
import com.aminebag.larjson.jsonmapper.configuration.LarJsonConfiguration;
import com.aminebag.larjson.jsonmapper.propertymapper.LarJsonPropertyMapper;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

abstract class LarJsonInvocationHandler implements InvocationHandler, LarJsonBranch {
    final Map<Method, LarJsonPropertyMapper> mappers;

    LarJsonInvocationHandler(Map<Method, LarJsonPropertyMapper> mappers) {
        this.mappers = mappers;
    }

    @Override
    public final Object invoke(Object proxy, Method method, Object[] args) throws IOException {
        if(LarJsonMapper.isCloseMethod(method)) {
            onClose();
            return null;
        } else {
            LarJsonPropertyMapper mapper = mappers.get(method);
            if (mapper == null) {
                return getConfiguration()
                        .getUnsupportedMethodCalledBehavior()
                        .onUnsupportedMethodCalled(proxy, method, args);
            } else {
                try {
                    return mapper.calculateValue(this, getTopObjectOffset());
                } catch (IOException | LarJsonCheckedException e) {
                    return getConfiguration()
                            .getValueReadFailedBehavior()
                            .onValueReadFailed(proxy, method, args, e);
                }
            }
        }
    }

    public void onClose() throws IOException {
        throw new UnsupportedOperationException("Only a root object/array can be closed");
    }

    abstract LarJsonConfiguration getConfiguration();
}
