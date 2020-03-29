package com.aminebag.larjson.configuration.propertyresolver;

import java.lang.reflect.Method;

/**
 * @author Amine Bagdouri
 */
public class CamelCasePropertyResolver extends AbstractPropertyResolver {

    public CamelCasePropertyResolver(Class<?> rootInterface) {
        super(rootInterface);
    }

    @Override
    protected String getAttributeNameInternal(Method getter) {
        String getterName = getGetterNameWithoutPrefix(getter);
        return Character.toLowerCase(getterName.charAt(0)) + getterName.substring(1);
    }
}
