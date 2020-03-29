package com.aminebag.larjson.configuration.propertyresolver;

import java.lang.reflect.Method;

/**
 * @author Amine Bagdouri
 */
public class LowerDotCasePropertyResolver extends AbstractPropertyResolver {

    public LowerDotCasePropertyResolver(Class<?> rootInterface) {
        super(rootInterface);
    }

    @Override
    protected String getAttributeNameInternal(Method getter) {
        return getSeparatedAttributeName(getter, '.', Character::toLowerCase);
    }
}
