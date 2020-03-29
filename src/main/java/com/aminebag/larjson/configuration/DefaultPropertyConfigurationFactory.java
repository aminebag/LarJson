package com.aminebag.larjson.configuration;

import com.aminebag.larjson.valueconverter.StringValueConverter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Amine Bagdouri
 */
public class DefaultPropertyConfigurationFactory implements PropertyConfigurationFactory {

    private final Map<Method, DefaultPropertyConfiguration> getters = new HashMap<>();
    private final Map<Method, DefaultPropertyConfiguration> setters = new HashMap<>();

    @Override
    public PropertyConfiguration get(Method getterMethod) {
        return getters.get(getterMethod);
    }

    void setPropertyName(Method getterMethod, String name) {
        DefaultPropertyConfiguration propertyConfiguration = getters.get(getterMethod);
        if(propertyConfiguration == null) {
            propertyConfiguration = new DefaultPropertyConfiguration(getterMethod, null, name,
                    null, null);
            getters.put(getterMethod, propertyConfiguration);
        } else {
            propertyConfiguration.name = name;
        }
    }

    void setPropertyIgnored(Method getterMethod, boolean ignored) {
        DefaultPropertyConfiguration propertyConfiguration = getters.get(getterMethod);
        if(propertyConfiguration == null) {
            propertyConfiguration = new DefaultPropertyConfiguration(getterMethod, ignored, null,
                    null, null);
            getters.put(getterMethod, propertyConfiguration);
        } else {
            propertyConfiguration.ignored = ignored;
        }
    }

    void setPropertyRequired(Method getterMethod, boolean required) {
        DefaultPropertyConfiguration propertyConfiguration = getters.get(getterMethod);
        if(propertyConfiguration == null) {
            propertyConfiguration = new DefaultPropertyConfiguration(getterMethod, null, null,
                    null, required);
            getters.put(getterMethod, propertyConfiguration);
        } else {
            propertyConfiguration.required = required;
        }
    }

    void setPropertyStringValueConverter(Method getterMethod, StringValueConverter<?> stringValueConverter) {
        DefaultPropertyConfiguration propertyConfiguration = getters.get(getterMethod);
        if(propertyConfiguration == null) {
            propertyConfiguration = new DefaultPropertyConfiguration(getterMethod, null, null,
                    stringValueConverter, null);
            getters.put(getterMethod, propertyConfiguration);
        } else {
            propertyConfiguration.stringValueConverter = stringValueConverter;
        }
    }

    private static class DefaultPropertyConfiguration implements PropertyConfiguration {

        private final Method getter;
        Boolean ignored;
        String name;
        StringValueConverter<?> stringValueConverter;
        Boolean required;

        public DefaultPropertyConfiguration(Method getter, Boolean ignored, String name,
                                            StringValueConverter<?> stringValueConverter, Boolean required) {
            this.getter = getter;
            this.ignored = ignored;
            this.name = name;
            this.stringValueConverter = stringValueConverter;
            this.required = required;
        }

        @Override
        public Boolean isIgnored() {
            return ignored;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public StringValueConverter<?> getStringValueConverter() {
            return stringValueConverter;
        }

        @Override
        public Method getGetter() {
            return getter;
        }

        @Override
        public Boolean isRequired() {
            return required;
        }
    }
}
