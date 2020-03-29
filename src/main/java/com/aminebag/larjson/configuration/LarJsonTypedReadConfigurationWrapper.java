package com.aminebag.larjson.configuration;

import com.aminebag.larjson.chardecoder.CharacterDecoder;
import com.aminebag.larjson.utils.TemporaryFileFactory;

/**
 * @author Amine Bagdouri
 *
 * A wrapper of the {@link LarJsonTypedReadConfiguration} that delegates all calls to its wrapped configuration. It
 * can be subclassed and its methods can be overridden to present an altered view of the configuration.
 */
public class LarJsonTypedReadConfigurationWrapper implements LarJsonTypedReadConfiguration {

    private final LarJsonTypedReadConfiguration configuration;

    public LarJsonTypedReadConfigurationWrapper(LarJsonTypedReadConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public boolean isUnsupportedMethodAllowed() {
        return configuration.isUnsupportedMethodAllowed();
    }

    @Override
    public boolean isUnknownJsonAttributeAllowed() {
        return configuration.isUnknownJsonAttributeAllowed();
    }

    @Override
    public boolean isValidationEnabled() {
        return configuration.isValidationEnabled();
    }

    @Override
    public UnsupportedMethodCalledBehavior getUnsupportedMethodCalledBehavior() {
        return configuration.getUnsupportedMethodCalledBehavior();
    }

    @Override
    public StringValueConverterFactory getStringValueConverterFactory() {
        return configuration.getStringValueConverterFactory();
    }

    @Override
    public EqualsDelegateFactory getEqualsDelegateFactory() {
        return configuration.getEqualsDelegateFactory();
    }

    @Override
    public LarJsonTypedWriteConfiguration toWriteConfiguration() {
        return configuration.toWriteConfiguration();
    }

    @Override
    public boolean isLenient() {
        return configuration.isLenient();
    }

    @Override
    public int getCacheSize() {
        return configuration.getCacheSize();
    }

    @Override
    public ValueReadFailedBehavior getValueReadFailedBehavior() {
        return configuration.getValueReadFailedBehavior();
    }

    @Override
    public boolean isMutable() {
        return configuration.isMutable();
    }

    @Override
    public boolean isThreadSafe() {
        return configuration.isThreadSafe();
    }

    @Override
    public LarJsonValueParserFactory getValueParserFactory() {
        return configuration.getValueParserFactory();
    }

    @Override
    public LarJsonTokenParserFactory getTokenParserFactory() {
        return configuration.getTokenParserFactory();
    }

    @Override
    public CharacterDecoder getCharacterDecoder() {
        return configuration.getCharacterDecoder();
    }

    @Override
    public TemporaryFileFactory getTemporaryFileFactory() {
        return configuration.getTemporaryFileFactory();
    }

    @Override
    public long getMaxMemoryBlueprintSize() {
        return configuration.getMaxMemoryBlueprintSize();
    }

    @Override
    public boolean getAllPropertiesRequired() {
        return configuration.getAllPropertiesRequired();
    }

    @Override
    public PropertyResolverFactory getPropertyResolverFactory() {
        return configuration.getPropertyResolverFactory();
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
