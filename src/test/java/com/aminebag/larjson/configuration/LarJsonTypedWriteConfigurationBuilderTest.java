package com.aminebag.larjson.configuration;

import com.aminebag.larjson.configuration.propertyresolver.*;
import com.aminebag.larjson.exception.LarJsonConversionException;
import com.aminebag.larjson.valueconverter.StringValueConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

import static com.aminebag.larjson.mapper.LarJsonTypedMapperTestModels.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Amine Bagdouri
 */
public class LarJsonTypedWriteConfigurationBuilderTest {

    private LarJsonTypedWriteConfiguration defaultConfig() {
        return new LarJsonTypedWriteConfiguration.Builder().build();
    }

    @Test
    void testLenientDefault() {
        LarJsonWriteConfiguration configuration = defaultConfig();
        assertFalse(configuration.isLenient());
    }

    @Test
    void testLenientTrue() {
        LarJsonTypedWriteConfiguration configuration =
                new LarJsonTypedWriteConfiguration.Builder().setLenient(true).build();
        assertTrue(configuration.isLenient());
    }

    @Test
    void testLenientFalse() {
        LarJsonTypedWriteConfiguration configuration =
                new LarJsonTypedWriteConfiguration.Builder().setLenient(false).build();
        assertFalse(configuration.isLenient());
    }

    @Test
    void testJsonFormatAnnotationEnabledDefault() {
        LarJsonTypedWriteConfiguration configuration = defaultConfig();
        assertNull(configuration.getAnnotationConfigurationFactory().get(JsonFormat.class));
    }

    @Test
    void testJsonFormatAnnotationEnabledTrue() {
        LarJsonTypedWriteConfiguration configuration =
                new LarJsonTypedWriteConfiguration.Builder().enableJsonFormatAnnotation().build();
        AnnotationConfiguration annotationConfiguration = configuration.getAnnotationConfigurationFactory()
                .get(JsonFormat.class);
        assertNotNull(annotationConfiguration);
        assertTrue(annotationConfiguration.isEnabled());
    }

    @Test
    void testJsonFormatAnnotationEnabledFalse() {
        LarJsonTypedWriteConfiguration configuration =
                new LarJsonTypedWriteConfiguration.Builder().disableJsonFormatAnnotation().build();
        AnnotationConfiguration annotationConfiguration = configuration.getAnnotationConfigurationFactory()
                .get(JsonFormat.class);
        assertNotNull(annotationConfiguration);
        assertFalse(annotationConfiguration.isEnabled());
    }

    @Test
    void testJsonIgnoreAnnotationEnabledDefault() {
        LarJsonTypedWriteConfiguration configuration = defaultConfig();
        assertNull(configuration.getAnnotationConfigurationFactory().get(JsonIgnore.class));
    }

    @Test
    void testJsonIgnoreAnnotationEnabledTrue() {
        LarJsonTypedWriteConfiguration configuration =
                new LarJsonTypedWriteConfiguration.Builder().enableJsonIgnoreAnnotation().build();
        AnnotationConfiguration annotationConfiguration = configuration.getAnnotationConfigurationFactory()
                .get(JsonIgnore.class);
        assertNotNull(annotationConfiguration);
        assertTrue(annotationConfiguration.isEnabled());
    }

    @Test
    void testJsonIgnoreAnnotationEnabledFalse() {
        LarJsonTypedWriteConfiguration configuration =
                new LarJsonTypedWriteConfiguration.Builder().disableJsonIgnoreAnnotation().build();
        AnnotationConfiguration annotationConfiguration = configuration.getAnnotationConfigurationFactory()
                .get(JsonIgnore.class);
        assertNotNull(annotationConfiguration);
        assertFalse(annotationConfiguration.isEnabled());
    }

    @Test
    void testJsonPropertyAnnotationEnabledDefault() {
        LarJsonTypedWriteConfiguration configuration = defaultConfig();
        assertNull(configuration.getAnnotationConfigurationFactory().get(JsonProperty.class));
    }

    @Test
    void testJsonPropertyAnnotationEnabledTrue() {
        LarJsonTypedWriteConfiguration configuration =
                new LarJsonTypedWriteConfiguration.Builder().enableJsonPropertyAnnotation().build();
        AnnotationConfiguration annotationConfiguration = configuration.getAnnotationConfigurationFactory()
                .get(JsonProperty.class);
        assertNotNull(annotationConfiguration);
        assertTrue(annotationConfiguration.isEnabled());
    }

    @Test
    void testJsonPropertyAnnotationEnabledFalse() {
        LarJsonTypedWriteConfiguration configuration =
                new LarJsonTypedWriteConfiguration.Builder().disableJsonPropertyAnnotation().build();
        AnnotationConfiguration annotationConfiguration = configuration.getAnnotationConfigurationFactory()
                .get(JsonProperty.class);
        assertNotNull(annotationConfiguration);
        assertFalse(annotationConfiguration.isEnabled());
    }

    @Test
    void testPropertyResolverFactoryDefault() throws NoSuchMethodException {
        LarJsonTypedWriteConfiguration configuration = defaultConfig();
        PropertyResolverFactory propertyResolverFactory = configuration.getPropertyResolverFactory();

        assertTrue(propertyResolverFactory.get(ModelWithPrimitiveBoolean.class)
                .isGetter(ModelWithPrimitiveBoolean.class.getMethod("isWhatever")));

        assertTrue(propertyResolverFactory.get(ModelWithPrimitiveBooleanGetGetter.class)
                .isGetter(ModelWithPrimitiveBooleanGetGetter.class.getMethod("getWhatever")));

        assertTrue(propertyResolverFactory.get(ModelWithString.class)
                .isGetter(ModelWithString.class.getMethod("getWhatever")));

        assertTrue(propertyResolverFactory.get(ModelWithStringMutable.class)
                .isGetter(ModelWithStringMutable.class.getMethod("getWhatever")));
        assertFalse(propertyResolverFactory.get(ModelWithStringMutable.class)
                .isGetter(ModelWithStringMutable.class.getMethod("setWhatever", String.class)));
        assertEquals(ModelWithStringMutable.class.getMethod("setWhatever", String.class),
                propertyResolverFactory.get(ModelWithStringMutable.class).findSetter(ModelWithStringMutable.class.getMethod("getWhatever"),
                        Arrays.asList(
                                ModelWithStringMutable.class.getMethod("setWhatever", String.class),
                                ModelWithObjectMutable.class.getMethod("setSomething", ModelWithStringMutable.class))));

        Method booleanGetter = ModelWithPrimitiveBoolean.class.getMethod("isWhatever");
        Method stringGetter = ModelWithString.class.getMethod("getWhatever");
        Method anotherGetter = RichModel.class.getMethod("getFirstName");
        Method yetAnotherGetter = RichModel.class.getMethod("isMad");
        Method invalidGetter = StringBuilder.class.getMethod("charAt", int.class);

        assertEquals("whatever", propertyResolverFactory.get(ModelWithPrimitiveBoolean.class).getAttributeName(booleanGetter));
        assertEquals("whatever", propertyResolverFactory.get(ModelWithString.class).getAttributeName(stringGetter));

        assertEquals(stringGetter, propertyResolverFactory.get(ModelWithString.class).findGetter("whatever",
                Arrays.asList(stringGetter, anotherGetter, yetAnotherGetter, invalidGetter)));

        assertEquals(null, propertyResolverFactory.get(ModelWithString.class).findGetter("firstLife",
                Arrays.asList(anotherGetter, yetAnotherGetter, invalidGetter)));
    }

    @Test
    void testPropertyResolverFactoryCustom() {
        PropertyResolverFactory propertyResolverFactory = rootInterface -> null;

        LarJsonTypedWriteConfiguration configuration =
                new LarJsonTypedWriteConfiguration.Builder()
                        .setPropertyResolverFactory(propertyResolverFactory).build();
        assertEquals(propertyResolverFactory, configuration.getPropertyResolverFactory());
    }
    
    @Test
    void testStringValueConverterFactoryDefault() {
        LarJsonTypedWriteConfiguration configuration = defaultConfig();
        assertNotNull(configuration.getStringValueConverterFactory());
    }

    @Test
    void testStringValueConverterCustom() {
        StringValueConverter<TestEnum> converter = new StringValueConverter<TestEnum>() {
            @Override
            public TestEnum fromString(String s) {
                return null;
            }

            @Override
            public String toString(TestEnum value) {
                return null;
            }
        };
        LarJsonTypedWriteConfiguration configuration = new LarJsonTypedWriteConfiguration.Builder()
                .setStringValueConverter(TestEnum.class, converter).build();
        assertEquals(converter, configuration.getStringValueConverterFactory().get(TestEnum.class));
    }

    @Test
    void testHtmlSafeDefault() {
        LarJsonWriteConfiguration configuration = defaultConfig();
        assertFalse(configuration.isHtmlSafe());
    }

    @Test
    void testHtmlSafeTrue() {
        LarJsonTypedWriteConfiguration configuration =
                new LarJsonTypedWriteConfiguration.Builder().setHtmlSafe(true).build();
        assertTrue(configuration.isHtmlSafe());
    }

    @Test
    void testHtmlSafeFalse() {
        LarJsonTypedWriteConfiguration configuration =
                new LarJsonTypedWriteConfiguration.Builder().setHtmlSafe(false).build();
        assertFalse(configuration.isHtmlSafe());
    }

    @Test
    void testIndentDefault() {
        LarJsonWriteConfiguration configuration = defaultConfig();
        assertEquals("\t", configuration.getIndent());
    }

    @Test
    void testIndentCustom() {
        LarJsonTypedWriteConfiguration configuration =
                new LarJsonTypedWriteConfiguration.Builder().setIndent("  ").build();
        assertEquals("  ", configuration.getIndent());
    }

    @Test
    void testIndentPretty() {
        LarJsonTypedWriteConfiguration configuration =
                new LarJsonTypedWriteConfiguration.Builder().setPretty(true).build();
        assertEquals("\t", configuration.getIndent());
    }

    @Test
    void testIndentNotPretty() {
        LarJsonTypedWriteConfiguration configuration =
                new LarJsonTypedWriteConfiguration.Builder().setPretty(false).build();
        assertEquals("", configuration.getIndent());
    }

    @Test
    void testPropertyIgnoredTrue() throws NoSuchMethodException {
        Method method = ModelWithString.class.getMethod("getWhatever");
        LarJsonTypedWriteConfiguration configuration = new LarJsonTypedWriteConfiguration.Builder()
                .setPropertyIgnored(method, true).build();
        PropertyConfiguration propertyConfiguration = configuration.getPropertyConfigurationFactory()
                .get(method);
        assertNotNull(propertyConfiguration);
        assertEquals(method, propertyConfiguration.getGetter());
        assertNull(propertyConfiguration.isRequired());
        assertNull(propertyConfiguration.getName());
        assertNull(propertyConfiguration.getStringValueConverter());
        assertEquals(true, propertyConfiguration.isIgnored());
    }

    @Test
    void testPropertyIgnoredFalse() throws NoSuchMethodException {
        Method method = ModelWithString.class.getMethod("getWhatever");
        LarJsonTypedWriteConfiguration configuration = new LarJsonTypedWriteConfiguration.Builder()
                .setPropertyIgnored(method, false).build();
        PropertyConfiguration propertyConfiguration = configuration.getPropertyConfigurationFactory()
                .get(method);
        assertNotNull(propertyConfiguration);
        assertEquals(method, propertyConfiguration.getGetter());
        assertNull(propertyConfiguration.isRequired());
        assertNull(propertyConfiguration.getName());
        assertNull(propertyConfiguration.getStringValueConverter());
        assertEquals(false, propertyConfiguration.isIgnored());
    }

    @Test
    void testPropertyName() throws NoSuchMethodException {
        Method method = ModelWithString.class.getMethod("getWhatever");
        LarJsonTypedWriteConfiguration configuration = new LarJsonTypedWriteConfiguration.Builder()
                .setPropertyName(method, "yes").build();
        PropertyConfiguration propertyConfiguration = configuration.getPropertyConfigurationFactory()
                .get(method);
        assertNotNull(propertyConfiguration);
        assertEquals(method, propertyConfiguration.getGetter());
        assertNull(propertyConfiguration.isIgnored());
        assertNull(propertyConfiguration.isRequired());
        assertNull(propertyConfiguration.getStringValueConverter());
        assertEquals("yes", propertyConfiguration.getName());
    }

    @Test
    void testPropertyStringValueConverter() throws NoSuchMethodException {
        Method method = ModelWithString.class.getMethod("getWhatever");
        StringValueConverter<String> converter = new StringValueConverter<String>() {
            @Override
            public String fromString(String s) throws LarJsonConversionException {
                return null;
            }

            @Override
            public String toString(String value) {
                return null;
            }
        };
        LarJsonTypedWriteConfiguration configuration = new LarJsonTypedWriteConfiguration.Builder()
                .setPropertyStringValueConverter(method, converter).build();
        PropertyConfiguration propertyConfiguration = configuration.getPropertyConfigurationFactory()
                .get(method);
        assertNotNull(propertyConfiguration);
        assertEquals(method, propertyConfiguration.getGetter());
        assertNull(propertyConfiguration.isIgnored());
        assertNull(propertyConfiguration.isRequired());
        assertNull(propertyConfiguration.getName());
        assertEquals(converter, propertyConfiguration.getStringValueConverter());
    }

    @Test
    void testPropertyConfiguration() throws NoSuchMethodException {
        Method method1 = ModelWithString.class.getMethod("getWhatever");
        Method method2 = ModelWithBigDecimal.class.getMethod("getWhatever");
        StringValueConverter<String> converter = new StringValueConverter<String>() {
            @Override
            public String fromString(String s) throws LarJsonConversionException {
                return null;
            }

            @Override
            public String toString(String value) {
                return null;
            }
        };
        LarJsonTypedWriteConfiguration configuration = new LarJsonTypedWriteConfiguration.Builder()
                .setPropertyIgnored(method1, false)
                .setPropertyStringValueConverter(method1, converter)
                .setPropertyName(method1, "hey").build();
        PropertyConfiguration propertyConfiguration = configuration.getPropertyConfigurationFactory()
                .get(method1);
        assertNotNull(propertyConfiguration);
        assertEquals(method1, propertyConfiguration.getGetter());
        assertEquals(false, propertyConfiguration.isIgnored());
        assertNull(propertyConfiguration.isRequired());
        assertEquals("hey", propertyConfiguration.getName());
        assertEquals(converter, propertyConfiguration.getStringValueConverter());
        assertNull(configuration.getPropertyConfigurationFactory().get(method2));
    }

    @Test
    void testDefaultCase() {
        LarJsonTypedWriteConfiguration configuration = defaultConfig();
        assertEquals(configuration.getPropertyResolverFactory().get(ModelWithBigDecimal.class).getClass(),
                CamelCasePropertyResolver.class);
    }

    @Test
    void testCamelCase() {
        LarJsonTypedWriteConfiguration configuration = new LarJsonTypedWriteConfiguration.Builder()
                .setCamelCase()
                .build();
        assertEquals(configuration.getPropertyResolverFactory().get(ModelWithBigDecimal.class).getClass(),
                CamelCasePropertyResolver.class);
    }

    @Test
    void testKebabCase() {
        LarJsonTypedWriteConfiguration configuration = new LarJsonTypedWriteConfiguration.Builder()
                .setKebabCase()
                .build();
        assertEquals(configuration.getPropertyResolverFactory().get(ModelWithBigDecimal.class).getClass(),
                KebabCasePropertyResolver.class);
    }

    @Test
    void testPascalCase() {
        LarJsonTypedWriteConfiguration configuration = new LarJsonTypedWriteConfiguration.Builder()
                .setPascalCase()
                .build();
        assertEquals(configuration.getPropertyResolverFactory().get(ModelWithBigDecimal.class).getClass(),
                PascalCasePropertyResolver.class);
    }

    @Test
    void testLowerDotCase() {
        LarJsonTypedWriteConfiguration configuration = new LarJsonTypedWriteConfiguration.Builder()
                .setLowerDotCase()
                .build();
        assertEquals(configuration.getPropertyResolverFactory().get(ModelWithBigDecimal.class).getClass(),
                LowerDotCasePropertyResolver.class);
    }

    @Test
    void testUpperDotCase() {
        LarJsonTypedWriteConfiguration configuration = new LarJsonTypedWriteConfiguration.Builder()
                .setUpperDotCase()
                .build();
        assertEquals(configuration.getPropertyResolverFactory().get(ModelWithBigDecimal.class).getClass(),
                UpperDotCasePropertyResolver.class);
    }

    @Test
    void testLowerSnakeCase() {
        LarJsonTypedWriteConfiguration configuration = new LarJsonTypedWriteConfiguration.Builder()
                .setLowerSnakeCase()
                .build();
        assertEquals(configuration.getPropertyResolverFactory().get(ModelWithBigDecimal.class).getClass(),
                LowerSnakeCasePropertyResolver.class);
    }

    @Test
    void testUpperSnakeCase() {
        LarJsonTypedWriteConfiguration configuration = new LarJsonTypedWriteConfiguration.Builder()
                .setUpperSnakeCase()
                .build();
        assertEquals(configuration.getPropertyResolverFactory().get(ModelWithBigDecimal.class).getClass(),
                UpperSnakeCasePropertyResolver.class);
    }

    @Test
    void testUpdateAfterBuild() {
        LarJsonTypedWriteConfiguration.Builder builder =
                new LarJsonTypedWriteConfiguration.Builder();
        builder.setLenient(true);
        builder.build();
        try {
            builder.setHtmlSafe(true);
            fail();
        }catch (IllegalStateException expected) {
        }
    }

    @Test
    void testBuildTwice() {
        LarJsonTypedWriteConfiguration.Builder builder =
                new LarJsonTypedWriteConfiguration.Builder();
        builder.setLenient(true);
        builder.build();
        try {
            builder.build();
            fail();
        }catch (IllegalStateException expected) {
        }
    }
}
