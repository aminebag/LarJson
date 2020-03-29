package com.aminebag.larjson.mapper.element;

import com.aminebag.larjson.api.LarJsonPath;
import com.aminebag.larjson.api.LarJsonTypedWriteable;
import com.aminebag.larjson.configuration.EqualsDelegate;
import com.aminebag.larjson.configuration.LarJsonTypedWriteConfiguration;
import com.aminebag.larjson.configuration.PropertyResolver;
import com.aminebag.larjson.exception.LarJsonException;
import com.aminebag.larjson.exception.LarJsonRuntimeException;
import com.aminebag.larjson.exception.LarJsonValueReadException;
import com.aminebag.larjson.exception.LarJsonWriteException;
import com.aminebag.larjson.mapper.LarJsonMapperUtils;
import com.aminebag.larjson.mapper.propertymapper.LarJsonPropertyMapper;
import com.aminebag.larjson.mapper.propertymapper.ObjectLarJsonPropertyMapper;
import com.aminebag.larjson.mapper.valueoverwriter.ValueOverwriter;
import com.aminebag.larjson.utils.LarJsonMethods;
import com.aminebag.larjson.utils.LarJsonProxy;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;

/**
 * @author Amine Bagdouri
 *
 * The base implementation of a typed LarJson model object
 */
public abstract class AbstractLarJsonObject implements InvocationHandler, LarJsonTypedWriteable, LarJsonPath {

    private static final Method GET_PROXIED_METHOD = findGetProxiedMethod();

    private final ObjectLarJsonPropertyMapper<?> mapper;
    private final long[] keys;
    private final long jsonPosition;

    protected AbstractLarJsonObject(ObjectLarJsonPropertyMapper<?> mapper, long[] keys, long jsonPosition) {
        this.mapper = mapper;
        this.keys = keys;
        this.jsonPosition = jsonPosition;
    }

    private static Method findGetProxiedMethod(){
        try {
            return LarJsonProxy.class.getDeclaredMethod("getProxied");
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    @Override
    public final Object invoke(Object proxy, Method method, Object[] args) throws IOException, LarJsonException {
        if(LarJsonMethods.isCloseMethod(method)) {
            onClose();
            return null;
        }
        getContext().checkClosed();
        LarJsonPropertyMapper subMapper;
        if ((subMapper = mapper.getPropertyMapperByGetter().get(method)) != null) {
            return onGetterMethodCalled(proxy, method, args, subMapper);
        } else if ((subMapper = mapper.getPropertyMapperBySetter().get(method)) != null) {
            return onSetterMethodCalled(args[0], subMapper);
        } else if(GET_PROXIED_METHOD.equals(method)) {
            return this;
        } else if(LarJsonMethods.isEqualsMethod(method)) {
            return equals(proxy, args[0]);
        } else if(LarJsonMethods.isHashcodeMethod(method)) {
            return hashCode(proxy);
        } else if(LarJsonMethods.isToStringMethod(method)) {
            return toString();
        } else if(LarJsonMethods.isCloneMethod(method)) {
            return onCloneMethodCalled(proxy, method, args);
        } else if(LarJsonMethods.isDefaultConfigWriteMethod(method)) {
            write((Writer) args[0]);
            return null;
        } else if(LarJsonMethods.isTypedCustomConfigWriteMethod(method)) {
            write((Writer) args[0], (LarJsonTypedWriteConfiguration) args[1]);
            return null;
        } else if(LarJsonMethods.isDefaultConfigJsonWriterWriteMethod(method)) {
            write((JsonWriter) args[0]);
            return null;
        } else if(LarJsonMethods.isTypedCustomConfigJsonWriterWriteMethod(method)) {
            write((JsonWriter) args[0], (LarJsonTypedWriteConfiguration) args[1]);
            return null;
        } else if(LarJsonMethods.isGetLarJsonPathMethod(method)) {
            return getLarJsonPath();
        } else if(LarJsonMethods.isGetLarJsonPathWithStringBuilderMethod(method)) {
            getLarJsonPath((StringBuilder) args[0]);
            return null;
        } else if(LarJsonMethods.isGetParentLarJsonPathMethod(method)) {
            return getParentLarJsonPath();
        } else {
            return getContext().getReadConfiguration()
                    .getUnsupportedMethodCalledBehavior()
                    .onUnsupportedMethodCalled(proxy, method, args);
        }
    }

    private Object onSetterMethodCalled(Object arg, LarJsonPropertyMapper subMapper) {
        getValueOverwriter().overwritePropertyValue(jsonPosition, subMapper.getSetterIndex(),
                mapper.getPropertyMapperBySetter().size(), arg);
        return null;
    }

    private Object onGetterMethodCalled(Object proxy, Method method, Object[] args, LarJsonPropertyMapper subMapper) {
        if(subMapper.hasSetter()) {
            Object value = getValueOverwriter().getOverwrittenPropertyValue(jsonPosition, subMapper.getSetterIndex());
            if(getValueOverwriter().isPropertyValueOverwritten(value)) {
                return value;
            }
        }
        String pathElement = '.' + subMapper.getName();
        long key = keys[subMapper.getGetterIndex()];
        try {
            if(key < 0L) {
                return subMapper.nullValue();
            }
            return subMapper.calculateValue(getContext(), key, jsonPosition, getBlueprintPosition(), this, pathElement);
        } catch (IOException | LarJsonException e) {
            StringBuilder pathBuilder = new StringBuilder();
            getLarJsonPath(pathBuilder);
            pathBuilder.append(pathElement);
            return getContext().getReadConfiguration()
                    .getValueReadFailedBehavior()
                    .onValueReadFailed(proxy, method, args, pathBuilder.toString(), e);
        }
    }

    private Object onCloneMethodCalled(Object proxy, Method method, Object[] args) {
        if(getContext().getReadConfiguration().isMutable()) {
            try{
                return Proxy.newProxyInstance(
                        mapper.getType().getClassLoader(),
                        LarJsonMapperUtils.getProxiedInterfaces(mapper.getType()),
                        new CloneLarJsonObject(mapper, keys, jsonPosition, getContext(), getBlueprintPosition(),
                                getValueOverwriter(), getParentLarJsonPath(), getPathElement()));
            } catch (IOException e) {
                return getContext().getReadConfiguration()
                        .getValueReadFailedBehavior()
                        .onValueReadFailed(proxy, method, args, getLarJsonPath(), e);
            }
        } else {
            return proxy;
        }
    }

    @Override
    public void write(JsonWriter jsonWriter, LarJsonTypedWriteConfiguration writeConfiguration)
            throws IOException, LarJsonException {
        PropertyResolver propertyResolver = writeConfiguration.getPropertyResolverFactory()
                .get(getContext().getRootInterface());
        try{
            jsonWriter.beginObject();
            for(LarJsonPropertyMapper<?> subMapper : mapper.getPropertyMapperByGetter().values()) {
                if(LarJsonMapperUtils.isPropertyIgnored(
                        subMapper.getGetterMethod(), writeConfiguration.getPropertyConfigurationFactory(),
                        propertyResolver, writeConfiguration.getAnnotationConfigurationFactory())) {
                    continue;
                }
                long key = keys[subMapper.getGetterIndex()];
                if(key < 0L) {
                    continue;
                }
                jsonWriter.name(LarJsonMapperUtils.getPropertyName(
                        subMapper.getGetterMethod(), propertyResolver,
                        writeConfiguration.getPropertyConfigurationFactory(),
                        writeConfiguration.getAnnotationConfigurationFactory()));
                if(subMapper.hasSetter()) {
                    Object value = getValueOverwriter().getOverwrittenPropertyValue(jsonPosition, subMapper.getSetterIndex());
                    if(getValueOverwriter().isPropertyValueOverwritten(value)) {
                        LarJsonMapperUtils.writeValue(jsonWriter, value, writeConfiguration);
                        continue;
                    }
                }
                subMapper.write(getContext(), key, jsonPosition, getBlueprintPosition(), jsonWriter, getValueOverwriter(),
                        writeConfiguration, propertyResolver);
            }
            jsonWriter.endObject();
        }catch (IllegalArgumentException | IllegalStateException | LarJsonRuntimeException e) {
            throw new LarJsonWriteException(e);
        }
    }

    @Override
    public void write(JsonWriter jsonWriter) throws IOException, LarJsonException {
        write(jsonWriter, getContext().getReadConfiguration().toWriteConfiguration());
    }

    @Override
    public void write(Writer writer) throws IOException, LarJsonException {
        write(writer, getContext().getReadConfiguration().toWriteConfiguration());
    }

    public void onClose() throws IOException {
        throw new UnsupportedOperationException("Only a root object/array can be closed");
    }

    protected abstract long getBlueprintPosition() throws IOException;
    protected abstract ValueOverwriter getValueOverwriter();
    protected abstract LarJsonContext getContext();

    @Override
    public String toString() {
        return LarJsonMapperUtils.toString(this);
    }

    private boolean equals(Object thisProxy, Object other) {
        EqualsDelegate equalsDelegate =
                getContext().getReadConfiguration().getEqualsDelegateFactory().get(mapper.getType());
        if(equalsDelegate != null) {
            return equalsDelegate.equals(thisProxy, other);
        }
        if(thisProxy == other) {
            return true;
        }
        if(!mapper.getType().isInstance(other)) {
            return false;
        }
        if(other instanceof LarJsonProxy){
            Object proxied = ((LarJsonProxy)other).getProxied();
            if(proxied instanceof AbstractLarJsonObject) {
                AbstractLarJsonObject larJsonObject = (AbstractLarJsonObject) proxied;
                if(larJsonObject.getContext() == this.getContext() && larJsonObject.jsonPosition == this.jsonPosition &&
                        larJsonObject.getValueOverwriter() == this.getValueOverwriter()) {
                    return true;
                }
            }
        }
        for(Method method : mapper.getPropertyMapperByGetter().keySet()) {
            try {
                method.setAccessible(true);
                Object leftValue = method.invoke(thisProxy);
                Object rightValue = method.invoke(other);
                if(!Objects.equals(leftValue, rightValue)) {
                    return false;
                }
            } catch (IllegalAccessException e){
                throw new LarJsonValueReadException(e);
            } catch (InvocationTargetException e) {
                if(e.getCause() instanceof RuntimeException) {
                    throw (RuntimeException) e.getCause();
                }
                throw new LarJsonValueReadException(e.getCause());
            }
        }
        return true;
    }

    private int hashCode(Object thisProxy) {
        EqualsDelegate equalsDelegate =
                getContext().getReadConfiguration().getEqualsDelegateFactory().get(mapper.getType());
        if(equalsDelegate != null) {
            return equalsDelegate.hashCode(thisProxy);
        }
        int result = 0;
        for(Method method : mapper.getPropertyMapperByGetter().keySet()) {
            try {
                method.setAccessible(true);
                Object element = method.invoke(thisProxy);
                result += (element == null ? 0 : element.hashCode());
            } catch (IllegalAccessException e){
                throw new LarJsonValueReadException(e);
            } catch (InvocationTargetException e) {
                if(e.getCause() instanceof RuntimeException) {
                    throw (RuntimeException) e.getCause();
                }
                throw new LarJsonValueReadException(e.getCause());
            }
        }
        return result;
    }

    abstract String getPathElement();

    @Override
    public void getLarJsonPath(StringBuilder sb) {
        LarJsonPath parentPath = getParentLarJsonPath();
        if(parentPath != null) {
            parentPath.getLarJsonPath(sb);
        }
        sb.append(getPathElement());
    }
}