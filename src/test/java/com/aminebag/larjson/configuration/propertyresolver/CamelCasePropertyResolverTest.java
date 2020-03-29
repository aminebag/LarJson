package com.aminebag.larjson.configuration.propertyresolver;

import com.aminebag.larjson.configuration.PropertyResolverFactory;

/**
 * @author Amine Bagdouri
 */
public class CamelCasePropertyResolverTest extends AbstractPropertyResolverTest {

    @Override
    protected String secondAttributeName() {
        return "mySecondLife";
    }

    @Override
    protected String secondAttributeNameIncorrect() {
        return "my_second_life";
    }

    @Override
    protected String thirdAttributeName() {
        return "mYThirdLife";
    }

    @Override
    protected String thirdAttributeNameIncorrect() {
        return "myThirdLife";
    }

    @Override
    protected String fourthAttributeName() {
        return "myFourthLife";
    }

    @Override
    protected PropertyResolverFactory propertyResolverFactory() {
        return CamelCasePropertyResolver::new;
    }
}
