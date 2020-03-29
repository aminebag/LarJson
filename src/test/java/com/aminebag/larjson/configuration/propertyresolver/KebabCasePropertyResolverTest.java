package com.aminebag.larjson.configuration.propertyresolver;

import com.aminebag.larjson.configuration.PropertyResolverFactory;

/**
 * @author Amine Bagdouri
 */
public class KebabCasePropertyResolverTest extends AbstractPropertyResolverTest {

    @Override
    protected String secondAttributeName() {
        return "my-second-life";
    }

    @Override
    protected String secondAttributeNameIncorrect() {
        return "mySecondLife";
    }

    @Override
    protected String thirdAttributeName() {
        return "m-y-third-life";
    }

    @Override
    protected String thirdAttributeNameIncorrect() {
        return "my-third-life";
    }

    @Override
    protected String fourthAttributeName() {
        return "my-fourth-life";
    }

    @Override
    protected PropertyResolverFactory propertyResolverFactory() {
        return KebabCasePropertyResolver::new;
    }
}
