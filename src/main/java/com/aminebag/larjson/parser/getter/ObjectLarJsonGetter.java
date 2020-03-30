package com.aminebag.larjson.parser.getter;

import com.aminebag.larjson.parser.LarJsonRoot;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

public class ObjectLarJsonGetter extends LarJsonGetter<Object> {

    private final short level;
    private final Map<Method, LarJsonGetter<?>> getters;

    public ObjectLarJsonGetter(int index, short level, Map<Method, LarJsonGetter<?>> getters) {
        super(index);
        this.level = level;
        this.getters = getters;
    }

    @Override
    public Object calculateValue(Method method, LarJsonRoot root, int rootObjectOffset) {
        long key = getKey(root, rootObjectOffset);
        if(key <= NULL_VALUE && key + level >= NULL_VALUE){
            return null;
        }
        Class<?> returnType = method.getReturnType();
        return Proxy.newProxyInstance(
                returnType.getClassLoader(),
                new Class[]{returnType},
                new LarJsonInvocationHandler(root, rootObjectOffset));
    }

    private class LarJsonInvocationHandler implements InvocationHandler {
        private final LarJsonRoot root;
        private final int rootObjectOffset;

        private LarJsonInvocationHandler(LarJsonRoot root, int rootObjectOffset) {
            this.root = root;
            this.rootObjectOffset = rootObjectOffset;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            LarJsonGetter<?> getter = getters.get(method);
            if(getter == null) {
                throw new UnsupportedOperationException();
            } else {
                return getter.calculateValue(method, root, rootObjectOffset);
            }
        }
    }

    public Map<Method, LarJsonGetter<?>> getGetters() {
        return getters;
    }
}
