package com.aminebag.larjson.jsonmapper.configuration;

import com.aminebag.larjson.jsonparser.LarJsonValueParser;

public interface LarJsonConfiguration {
    boolean isJsonLenient();
    ValueReadFailedBehavior getValueReadFailedBehavior();
    boolean isUnsupportedMethodAllowed();
    boolean isUnsupportedReturnTypeAllowed();
    PropertyIgnoreResolver getPropertyIgnoreResolver();
    PropertyGetterResolver getPropertyGetterMatcher();
    UnsupportedMethodCalledBehavior getUnsupportedMethodCalledBehavior();
    StringConverterFactory getStringConverterFactory();
    LarJsonValueParser getValueParser();

}
