package com.aminebag.larjson.configuration.propertyresolver;

import com.aminebag.larjson.configuration.PropertyResolverFactory;

/**
 * @author Amine Bagdouri
 */
public class PascalCasePropertyResolverTest extends AbstractPropertyResolverTest {

    @Override
    protected String secondAttributeName() {
        return "MySecondLife";
    }

    @Override
    protected String secondAttributeNameIncorrect() {
        return "mySecondLife";
    }

    @Override
    protected String thirdAttributeName() {
        return "MYThirdLife";
    }

    @Override
    protected String thirdAttributeNameIncorrect() {
        return "MyThirdLife";
    }

    @Override
    protected String fourthAttributeName() {
        return "MyFourthLife";
    }

    @Override
    protected PropertyResolverFactory propertyResolverFactory() {
        return PascalCasePropertyResolver::new;
    }
}
