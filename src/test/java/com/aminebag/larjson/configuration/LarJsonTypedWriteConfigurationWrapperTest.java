package com.aminebag.larjson.configuration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Amine Bagdouri
 */
public class LarJsonTypedWriteConfigurationWrapperTest {

    @Test
    void test() {
        LarJsonTypedWriteConfiguration configuration = new LarJsonTypedWriteConfiguration.Builder().build();
        LarJsonTypedWriteConfigurationWrapper wrapper = new LarJsonTypedWriteConfigurationWrapper(configuration);
        assertEquals(configuration.getPropertyResolverFactory(), wrapper.getPropertyResolverFactory());
        assertEquals(configuration.getAnnotationConfigurationFactory(), wrapper.getAnnotationConfigurationFactory());
        assertEquals(configuration.getPropertyConfigurationFactory(), wrapper.getPropertyConfigurationFactory());
        assertEquals(configuration.getStringValueConverterFactory(), wrapper.getStringValueConverterFactory());
        assertEquals(configuration.getIndent(), wrapper.getIndent());
        assertEquals(configuration.isLenient(), wrapper.isLenient());
        assertEquals(configuration.isHtmlSafe(), wrapper.isHtmlSafe());
    }
}
