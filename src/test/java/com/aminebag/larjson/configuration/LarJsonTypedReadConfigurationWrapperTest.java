package com.aminebag.larjson.configuration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Amine Bagdouri
 */
public class LarJsonTypedReadConfigurationWrapperTest {

    @Test
    void test() {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder().build();
        LarJsonTypedReadConfigurationWrapper wrapper = new LarJsonTypedReadConfigurationWrapper(configuration);
        assertEquals(configuration.getPropertyResolverFactory(), wrapper.getPropertyResolverFactory());
        assertEquals(configuration.getAnnotationConfigurationFactory(), wrapper.getAnnotationConfigurationFactory());
        assertEquals(configuration.getAllPropertiesRequired(), wrapper.getAllPropertiesRequired());
        assertEquals(configuration.getEqualsDelegateFactory(), wrapper.getEqualsDelegateFactory());
        assertEquals(configuration.getPropertyConfigurationFactory(), wrapper.getPropertyConfigurationFactory());
        assertEquals(configuration.getStringValueConverterFactory(), wrapper.getStringValueConverterFactory());
        assertEquals(configuration.getUnsupportedMethodCalledBehavior(), wrapper.getUnsupportedMethodCalledBehavior());
        assertEquals(configuration.getCacheSize(), wrapper.getCacheSize());
        assertEquals(configuration.getCharacterDecoder(), wrapper.getCharacterDecoder());
        assertEquals(configuration.getMaxMemoryBlueprintSize(), wrapper.getMaxMemoryBlueprintSize());
        assertEquals(configuration.getTemporaryFileFactory(), wrapper.getTemporaryFileFactory());
        assertEquals(configuration.getTokenParserFactory(), wrapper.getTokenParserFactory());
        assertEquals(configuration.getValueParserFactory(), wrapper.getValueParserFactory());
        assertEquals(configuration.getValueReadFailedBehavior(), wrapper.getValueReadFailedBehavior());
        assertEquals(configuration.isUnknownJsonAttributeAllowed(), wrapper.isUnknownJsonAttributeAllowed());
        assertEquals(configuration.isUnsupportedMethodAllowed(), wrapper.isUnsupportedMethodAllowed());
        assertEquals(configuration.isValidationEnabled(), wrapper.isValidationEnabled());
        assertEquals(configuration.isLenient(), wrapper.isLenient());
        assertEquals(configuration.isMutable(), wrapper.isMutable());
        assertEquals(configuration.isThreadSafe(), wrapper.isThreadSafe());
        assertNotNull(wrapper.toWriteConfiguration());
    }
}
