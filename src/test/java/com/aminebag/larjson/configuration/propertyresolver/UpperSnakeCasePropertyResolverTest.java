package com.aminebag.larjson.configuration.propertyresolver;

import com.aminebag.larjson.configuration.PropertyResolverFactory;

/**
 * @author Amine Bagdouri
 */
public class UpperSnakeCasePropertyResolverTest extends AbstractPropertyResolverTest {

    @Override
    protected String secondAttributeName() {
        return "MY_SECOND_LIFE";
    }

    @Override
    protected String secondAttributeNameIncorrect() {
        return "my_second_life";
    }

    @Override
    protected String thirdAttributeName() {
        return "M_Y_THIRD_LIFE";
    }

    @Override
    protected String thirdAttributeNameIncorrect() {
        return "MY_THIRD_LIFE";
    }

    @Override
    protected String fourthAttributeName() {
        return "MY_FOURTH_LIFE";
    }

    @Override
    protected PropertyResolverFactory propertyResolverFactory() {
        return UpperSnakeCasePropertyResolver::new;
    }
}
