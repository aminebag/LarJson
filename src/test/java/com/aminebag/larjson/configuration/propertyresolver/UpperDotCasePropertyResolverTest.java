package com.aminebag.larjson.configuration.propertyresolver;

import com.aminebag.larjson.configuration.PropertyResolverFactory;

/**
 * @author Amine Bagdouri
 */
public class UpperDotCasePropertyResolverTest extends AbstractPropertyResolverTest {

    @Override
    protected String secondAttributeName() {
        return "MY.SECOND.LIFE";
    }

    @Override
    protected String secondAttributeNameIncorrect() {
        return "my.second.life";
    }

    @Override
    protected String thirdAttributeName() {
        return "M.Y.THIRD.LIFE";
    }

    @Override
    protected String thirdAttributeNameIncorrect() {
        return "MY.THIRD.LIFE";
    }

    @Override
    protected String fourthAttributeName() {
        return "MY.FOURTH.LIFE";
    }

    @Override
    protected PropertyResolverFactory propertyResolverFactory() {
        return UpperDotCasePropertyResolver::new;
    }
}
