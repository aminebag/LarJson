package com.aminebag.larjson.configuration;

import com.aminebag.larjson.valueconverter.StringValueConverter;

import java.lang.reflect.Method;

/**
 * @author Amine Bagdouri
 *
 * Model property configuration
 */
public interface PropertyConfiguration {

    /**
     * Indicates whether this property must be ignored (i.e. it will not be read during JSON deserialization)
     * @return {@code true} if this property must be ignored, {@code false} if it must not be ignored,
     * or {@code null} if it can't be decided
     */
    Boolean isIgnored();

    /**
     * @return the JSON attribute name for this property, or {@code null} if it can't be decided
     */
    String getName();

    /**
     * @return the string value converter to be used for this property, or {@code null} if it can't be decided
     */
    StringValueConverter<?> getStringValueConverter();

    /**
     * @return the getter method corresponding to this property, must not be {@code null}
     */
    Method getGetter();

    /**
     * Indicates whether this property is required (i.e. it must be present in the JSON object even as a {@code null})
     * @return {@code true} if this property is required, {@code false} if it is not required,
     * or {@code null} if it can't be decided
     */
    Boolean isRequired();

}
