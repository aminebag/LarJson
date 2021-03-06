package com.aminebag.larjson.configuration.propertyresolver;

import java.lang.reflect.Method;

/**
 * @author Amine Bagdouri
 */
public class UpperSnakeCasePropertyResolver extends AbstractPropertyResolver {

    public UpperSnakeCasePropertyResolver(Class<?> rootInterface) {
        super(rootInterface);
    }

    @Override
    protected String getAttributeNameInternal(Method getter) {
        return getSeparatedAttributeName(getter, '_', Character::toUpperCase);
    }
}
