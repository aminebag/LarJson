package com.aminebag.larjson.configuration.propertyresolver;

import com.aminebag.larjson.configuration.PropertyResolver;
import com.aminebag.larjson.mapper.LarJsonMapperUtils;
import org.springframework.core.ResolvableType;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;

/**
 * @author Amine Bagdouri
 */
public abstract class AbstractPropertyResolver implements PropertyResolver {

    private final Map<Method, String> attributeByGetter = new HashMap<>();
    private final Map<String, Collection<Method>> gettersByAttribute = new HashMap<>();
    private final Map<Method, Method> setterByGetter = new HashMap<>();

    public AbstractPropertyResolver(Class<?> rootInterface) {
        populate(rootInterface, new HashSet<>());
    }

    protected static String getGetterNameWithoutPrefix(Method getter) {
        String name = getter.getName();
        String prefix = "get";
        if(name.startsWith("is")) {
            prefix = "is";
        }
        return name.substring(prefix.length());
    }

    private void populate(Class<?> clazz, Set<Class<?>> handled) {
        if(!clazz.isInterface() || handled.contains(clazz)) {
            return;
        }
        handled.add(clazz);
        Collection<Method> getters = new ArrayList<>();
        Collection<Method> setters = new ArrayList<>();
        for(Method method : clazz.getMethods()) {
            if(method.getParameterCount() == 0 && !method.getReturnType().equals(void.class)) {
                if(isGetterInternal(method)) {
                    handleGetter(handled, getters, method);
                }
            } else if(method.getReturnType().equals(void.class) && method.getParameterCount() == 1) {
                if(isSetterInternal(method)) {
                    setters.add(method);
                }
            }
        }
        for(Method setter : setters) {
            Method getter = findGetterInternal(setter, getters);
            if(getter == null || !getter.getReturnType().equals(setter.getParameterTypes()[0])) {
                continue;
            }
            setterByGetter.put(getter, setter);
        }
    }

    private void handleGetter(Set<Class<?>> handled, Collection<Method> getters, Method method) {
        String attributeName = getAttributeNameInternal(method);
        attributeByGetter.put(method, attributeName);
        gettersByAttribute.computeIfAbsent(attributeName, a -> new ArrayList<>()).add(method);
        getters.add(method);

        Class<?> returnType = method.getReturnType();
        ResolvableType resolvableType = null;
        int[] genericIndexes = null;
        while (LarJsonMapperUtils.isCollectionType(returnType)) {
            resolvableType = resolvableType != null ?
                    resolvableType : ResolvableType.forMethodReturnType(method);
            genericIndexes = genericIndexes != null ?
                    Arrays.copyOf(genericIndexes, genericIndexes.length + 1) : new int[1];
            genericIndexes[genericIndexes.length - 1] = 0;
            returnType = resolvableType.getGeneric(genericIndexes).resolve();
            if (returnType == null) {
                return;
            }
        }
        populate(returnType, handled);
    }

    protected abstract String getAttributeNameInternal(Method getter);

    @Override
    public final Method findGetter(String attributeName, Collection<Method> candidates) {
        Collection<Method> getters = gettersByAttribute.get(attributeName);
        if(getters == null) {
            return null;
        }
        for(Method getter : getters) {
            if(candidates.contains(getter)) {
                return getter;
            }
        }
        return null;
    }

    @Override
    public final boolean isGetter(Method method) {
        return attributeByGetter.containsKey(method);
    }

    @Override
    public final Method findSetter(Method getter, Collection<Method> candidates) {
        Method setter = setterByGetter.get(getter);
        if(setter == null || !candidates.contains(setter)) {
            return null;
        }
        return setter;
    }

    @Override
    public final String getAttributeName(Method getter) {
        return attributeByGetter.get(getter);
    }

    protected boolean isGetterInternal(Method method) {
        String methodName = method.getName();
        return (methodName.startsWith("get") && methodName.length() >= 4 && isNotLowerCase(methodName.charAt(3))) ||
                (methodName.startsWith("is") && methodName.length() >= 3 && isNotLowerCase(methodName.charAt(2)) &&
                        (method.getReturnType().equals(boolean.class) || method.getReturnType().equals(Boolean.class)));
    }

    protected boolean isSetterInternal(Method method) {
        String methodName = method.getName();
        return methodName.startsWith("set") && methodName.length() >= 4;
    }

    protected Method findGetterInternal(Method setter, Collection<Method> candidates) {
        String name = setter.getName().substring("set".length());
        for(Method getter : candidates) {
            String getterName = getter.getName();
            String prefix = "get";
            if(getterName.startsWith("is")) {
                prefix = "is";
            }
            if(getterName.substring(prefix.length()).equals(name)) {
                return getter;
            }
        }
        return null;
    }

    private static boolean isNotLowerCase(char c){
        return c < 'a' || c > 'z';
    }

    protected static String getSeparatedAttributeName(
            Method getter, char separator, Function<Character, Character> transformCharacter) {
        String getterName = getGetterNameWithoutPrefix(getter);
        StringBuilder attributeName = new StringBuilder();
        attributeName.append(transformCharacter.apply(getterName.charAt(0)));
        for(int i=1; i<getterName.length(); i++) {
            char c = getterName.charAt(i);
            if(Character.isUpperCase(c)) {
                attributeName.append(separator);
                attributeName.append(transformCharacter.apply(c));
            } else {
                attributeName.append(transformCharacter.apply(c));
            }
        }
        return attributeName.toString();
    }
}
