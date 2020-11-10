package com.aminebag.larjson.configuration.propertyresolver;

import java.lang.reflect.Method;

/**
 * @author Amine Bagdouri
 */
public class UpperDotCasePropertyResolver extends AbstractPropertyResolver {

    public UpperDotCasePropertyResolver(Class<?> rootInterface) {
        super(rootInterface);
    }

    @Override
    protected String getAttributeNameInternal(Method getter) {
        return getSeparatedAttributeName(getter, '.', Character::toUpperCase);
    }
}