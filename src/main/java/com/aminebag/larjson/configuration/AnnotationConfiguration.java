package com.aminebag.larjson.configuration;

/**
 * @author Amine Bagdouri
 *
 * Annotation configuration
 */
public interface AnnotationConfiguration {

    /**
     * @return the class of the concerned annotation
     */
    Class<?> getAnnotation();

    /**
     * @return {@code true} if this annotation is enabled, {@code false} otherwise
     */
    boolean isEnabled();
}
