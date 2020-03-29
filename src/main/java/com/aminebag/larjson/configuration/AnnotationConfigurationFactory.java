package com.aminebag.larjson.configuration;

/**
 * @author Amine Bagdouri
 *
 * A factory of annotation configurations
 */
public interface AnnotationConfigurationFactory {

    /**
     * @param annotation the requested configuration annotation
     * @return a configuration for the provided annotation, or {@code null} if none found
     */
    AnnotationConfiguration get(Class<?> annotation);
}
