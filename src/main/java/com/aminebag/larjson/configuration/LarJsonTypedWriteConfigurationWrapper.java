package com.aminebag.larjson.configuration;

/**
 * @author Amine Bagdouri
 *
 * A wrapper of the {@link LarJsonTypedWriteConfiguration} that delegates all calls to its wrapped configuration. It
 * can be subclassed and its methods can be overridden to present an altered view of the configuration.
 */
public class LarJsonTypedWriteConfigurationWrapper implements LarJsonTypedWriteConfiguration {

    private final LarJsonTypedWriteConfiguration configuration;

    public LarJsonTypedWriteConfigurationWrapper(LarJsonTypedWriteConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public StringValueConverterFactory getStringValueConverterFactory() {
        return configuration.getStringValueConverterFactory();
    }

    @Override
    public PropertyResolverFactory getPropertyResolverFactory() {
        return configuration.getPropertyResolverFactory();
    }

    @Override
    public boolean isLenient() {
        return configuration.isLenient();
    }

    @Override
    public boolean isHtmlSafe() {
        return configuration.isHtmlSafe();
    }

    @Override
    public String getIndent() {
        return configuration.getIndent();
    }

    @Override
    public PropertyConfigurationFactory getPropertyConfigurationFactory() {
        return configuration.getPropertyConfigurationFactory();
    }

    @Override
    public AnnotationConfigurationFactory getAnnotationConfigurationFactory() {
        return configuration.getAnnotationConfigurationFactory();
    }
}
