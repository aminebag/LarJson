package com.aminebag.larjson.configuration.propertyresolver;

import com.aminebag.larjson.configuration.PropertyResolverFactory;

/**
 * @author Amine Bagdouri
 */
public class LowerSnakeCasePropertyResolverTest extends AbstractPropertyResolverTest {

    @Override
    protected String secondAttributeName() {
        return "my_second_life";
    }

    @Override
    protected String secondAttributeNameIncorrect() {
        return "mySecondLife";
    }

    @Override
    protected String thirdAttributeName() {
        return "m_y_third_life";
    }

    @Override
    protected String thirdAttributeNameIncorrect() {
        return "my_third_life";
    }

    @Override
    protected String fourthAttributeName() {
        return "my_fourth_life";
    }

    @Override
    protected PropertyResolverFactory propertyResolverFactory() {
        return LowerSnakeCasePropertyResolver::new;
    }
}
