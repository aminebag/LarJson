package com.aminebag.larjson.configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Amine Bagdouri
 */
class DefaultAnnotationConfigurationFactory implements AnnotationConfigurationFactory {

    private final Map<Class<?>, DefaultAnnotationConfiguration> map = new HashMap<>();

    @Override
    public AnnotationConfiguration get(Class<?> annotation) {
        return map.get(annotation);
    }

    void setAnnotationConfiguration(Class<?> annotation, boolean enabled) {
        DefaultAnnotationConfiguration configuration = map.get(annotation);
        if(configuration == null) {
            configuration = new DefaultAnnotationConfiguration(annotation, enabled);
            map.put(annotation, configuration);
        } else {
            configuration.enabled = enabled;
        }
    }

    private static class DefaultAnnotationConfiguration implements AnnotationConfiguration {

        private final Class<?> annotation;
        private boolean enabled;

        private DefaultAnnotationConfiguration(Class<?> annotation, boolean enabled) {
            this.annotation = annotation;
            this.enabled = enabled;
        }

        @Override
        public Class<?> getAnnotation() {
            return annotation;
        }

        @Override
        public boolean isEnabled() {
            return enabled;
        }
    }
}
