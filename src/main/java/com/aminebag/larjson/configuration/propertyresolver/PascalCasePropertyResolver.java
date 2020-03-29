package com.aminebag.larjson.configuration.propertyresolver;

import java.lang.reflect.Method;

/**
 * @author Amine Bagdouri
 */
public class PascalCasePropertyResolver extends AbstractPropertyResolver {

    public PascalCasePropertyResolver(Class<?> rootInterface) {
        super(rootInterface);
    }

    @Override
    protected String getAttributeNameInternal(Method getter) {
        return getGetterNameWithoutPrefix(getter);
    }

}
