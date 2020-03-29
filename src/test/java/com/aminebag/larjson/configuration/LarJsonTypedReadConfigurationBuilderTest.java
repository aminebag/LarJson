package com.aminebag.larjson.configuration;

import com.aminebag.larjson.chardecoder.CharacterDecoder;
import com.aminebag.larjson.chardecoder.LatinCharacterDecoder;
import com.aminebag.larjson.chardecoder.Utf8CharacterDecoder;
import com.aminebag.larjson.configuration.propertyresolver.*;
import com.aminebag.larjson.exception.LarJsonConversionException;
import com.aminebag.larjson.exception.LarJsonValueReadException;
import com.aminebag.larjson.utils.TemporaryFileFactory;
import com.aminebag.larjson.valueconverter.StringValueConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;

import static com.aminebag.larjson.mapper.LarJsonTypedMapperTestModels.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Amine Bagdouri
 */
public class LarJsonTypedReadConfigurationBuilderTest {

    private LarJsonTypedReadConfiguration defaultConfig() {
        return new LarJsonTypedReadConfiguration.Builder().build();
    }

    @Test
    void testLenientDefault() {
        LarJsonReadConfiguration configuration = defaultConfig();
        assertFalse(configuration.isLenient());
    }

    @Test
    void testLenientTrue() {
        LarJsonTypedReadConfiguration configuration =
                new LarJsonTypedReadConfiguration.Builder().setLenient(true).build();
        assertTrue(configuration.isLenient());
    }

    @Test
    void testLenientFalse() {
        LarJsonTypedReadConfiguration configuration =
                new LarJsonTypedReadConfiguration.Builder().setLenient(false).build();
        assertFalse(configuration.isLenient());
    }

    @Test
    void testCacheSizeDefault() {
        LarJsonReadConfiguration configuration = defaultConfig();
        assertEquals(1024, configuration.getCacheSize());
    }

    @Test
    void testCacheSizeCustom() {
        LarJsonTypedReadConfiguration configuration =
                new LarJsonTypedReadConfiguration.Builder().setCacheSize(37).build();
        assertEquals(37, configuration.getCacheSize());
    }

    @Test
    void testValueReadFailedBehaviorDefault() throws NoSuchMethodException {
        LarJsonTypedReadConfiguration configuration = defaultConfig();
        try {
            onValueReadFailed(configuration);
            fail();
        } catch (LarJsonValueReadException expected){
        }
    }

    @Test
    void testValueReadFailedBehaviorThrowException() throws NoSuchMethodException {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .throwExceptionOnValueReadFailed().build();
        try {
            onValueReadFailed(configuration);
            fail();
        } catch (LarJsonValueReadException expected){
        }
    }

    @Test
    void testValueReadFailedBehaviorReturnNull() throws NoSuchMethodException {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .returnNullOnValueReadFailed().build();
        assertNull(onValueReadFailed(configuration, ModelWithString.class.getDeclaredMethod("getWhatever")));
    }

    @Test
    void testValueReadFailedBehaviorReturnFalse() throws NoSuchMethodException {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .returnNullOnValueReadFailed().build();
        assertEquals(false, onValueReadFailed(configuration, ModelWithPrimitiveBoolean.class.getDeclaredMethod("isWhatever")));
    }

    @Test
    void testValueReadFailedBehaviorReturnZero() throws NoSuchMethodException {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .returnNullOnValueReadFailed().build();
        assertEquals(0.0, onValueReadFailed(configuration, ModelWithPrimitiveDouble.class.getDeclaredMethod("getWhatever")));
    }

    @Test
    void testValueReadFailedBehaviorCustom() throws NoSuchMethodException {
        Object o = new Object();
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .setValueReadFailedBehavior((object, method, args, path, e) -> o).build();
        assertEquals(o, onValueReadFailed(configuration));
    }

    private Object onValueReadFailed(LarJsonTypedReadConfiguration configuration, Method method) {
        return configuration.getValueReadFailedBehavior().onValueReadFailed(new Object(),
                method, new Object[]{"hello"}, "$.here", new NumberFormatException());
    }

    private Object onValueReadFailed(LarJsonTypedReadConfiguration configuration) throws NoSuchMethodException {
        return onValueReadFailed(configuration, ModelWithString.class.getDeclaredMethod("getWhatever"));
    }

    @Test
    void testMutableDefault() {
        LarJsonReadConfiguration configuration = defaultConfig();
        assertFalse(configuration.isMutable());
    }

    @Test
    void testMutableTrue() {
        LarJsonTypedReadConfiguration configuration =
                new LarJsonTypedReadConfiguration.Builder().setMutable(true).build();
        assertTrue(configuration.isMutable());
    }

    @Test
    void testMutableFalse() {
        LarJsonTypedReadConfiguration configuration =
                new LarJsonTypedReadConfiguration.Builder().setMutable(false).build();
        assertFalse(configuration.isMutable());
    }

    @Test
    void testThreadSafeDefault() {
        LarJsonReadConfiguration configuration = defaultConfig();
        assertFalse(configuration.isThreadSafe());
    }

    @Test
    void testThreadSafeTrue() {
        LarJsonTypedReadConfiguration configuration =
                new LarJsonTypedReadConfiguration.Builder().setThreadSafe(true).build();
        assertTrue(configuration.isThreadSafe());
    }

    @Test
    void testThreadSafeFalse() {
        LarJsonTypedReadConfiguration configuration =
                new LarJsonTypedReadConfiguration.Builder().setThreadSafe(false).build();
        assertFalse(configuration.isThreadSafe());
    }

    @Test
    void testValueParserFactoryCustom() {
        LarJsonValueParserFactory factory = larJsonReadConfiguration -> null;
        LarJsonTypedReadConfiguration configuration =
                new LarJsonTypedReadConfiguration.Builder().setValueParserFactory(factory).build();
        assertEquals(factory, configuration.getValueParserFactory());
    }

    @Test
    void testTokenParserFactoryCustom() {
        LarJsonTokenParserFactory factory = (byteStream, characterDecoder, configuration) -> null;
        LarJsonTypedReadConfiguration configuration =
                new LarJsonTypedReadConfiguration.Builder().setTokenParserFactory(factory).build();
        assertEquals(factory, configuration.getTokenParserFactory());
    }

    @Test
    void testCharacterDecoderDefault() {
        LarJsonTypedReadConfiguration configuration = defaultConfig();
        assertEquals(Utf8CharacterDecoder.class, configuration.getCharacterDecoder().getClass());
    }

    @Test
    void testCharacterDecoderUtf8() {
        LarJsonTypedReadConfiguration configuration =
                new LarJsonTypedReadConfiguration.Builder().setCharset(StandardCharsets.UTF_8).build();
        assertEquals(Utf8CharacterDecoder.class, configuration.getCharacterDecoder().getClass());
    }

    @Test
    void testCharacterDecoderLatin() {
        LarJsonTypedReadConfiguration configuration =
                new LarJsonTypedReadConfiguration.Builder().setCharset(StandardCharsets.ISO_8859_1).build();
        assertEquals(LatinCharacterDecoder.class, configuration.getCharacterDecoder().getClass());
    }

    @Test
    void testCharacterDecoderUnsupported() {
        LarJsonTypedReadConfiguration.Builder builder =
                new LarJsonTypedReadConfiguration.Builder();
        try{
            builder.setCharset(StandardCharsets.UTF_16LE);
            fail();
        } catch (IllegalArgumentException expected){
        }
    }

    @Test
    void testCharacterDecoderCustom() {
        CharacterDecoder characterDecoder = byteStream -> 0;
        LarJsonTypedReadConfiguration configuration =
                new LarJsonTypedReadConfiguration.Builder().setCharacterDecoder(characterDecoder).build();
        assertEquals(characterDecoder, configuration.getCharacterDecoder());
    }

    @Test
    void testTemporaryFileFactoryDefault() throws IOException {
        LarJsonTypedReadConfiguration configuration = defaultConfig();
        TemporaryFileFactory temporaryFileFactory = configuration.getTemporaryFileFactory();
        File file = temporaryFileFactory.createTemporaryFile();
        Files.write(file.toPath(), new byte[]{0,1,2,3});
        file.delete();
    }

    @Test
    void testTemporaryFileFactoryCustom() {
        TemporaryFileFactory temporaryFileFactory = () -> null;
        LarJsonTypedReadConfiguration configuration =
                new LarJsonTypedReadConfiguration.Builder().setTemporaryFileFactory(temporaryFileFactory).build();
        assertEquals(temporaryFileFactory, configuration.getTemporaryFileFactory());
    }

    @Test
    void testMaxMemoryBlueprintSizeDefault() {
        LarJsonReadConfiguration configuration = defaultConfig();
        assertTrue(configuration.getMaxMemoryBlueprintSize() <= 1024 * 1024 * 1024); // <= 1GB
        assertTrue(configuration.getMaxMemoryBlueprintSize() > 0); // <= 1GB
    }

    @Test
    void testMaxMemoryBlueprintSizeCustom() {
        LarJsonTypedReadConfiguration configuration =
                new LarJsonTypedReadConfiguration.Builder().setMaxMemoryBlueprintSize(37).build();
        assertEquals(37, configuration.getMaxMemoryBlueprintSize());
    }

    @Test
    void testUnsupportedMethodAllowedDefault() {
        LarJsonTypedReadConfiguration configuration = defaultConfig();
        assertFalse(configuration.isUnsupportedMethodAllowed());
    }

    @Test
    void testUnsupportedMethodAllowedTrue() {
        LarJsonTypedReadConfiguration configuration =
                new LarJsonTypedReadConfiguration.Builder().setUnsupportedMethodAllowed(true).build();
        assertTrue(configuration.isUnsupportedMethodAllowed());
    }

    @Test
    void testUnsupportedMethodAllowedFalse() {
        LarJsonTypedReadConfiguration configuration =
                new LarJsonTypedReadConfiguration.Builder().setUnsupportedMethodAllowed(false).build();
        assertFalse(configuration.isUnsupportedMethodAllowed());
    }

    @Test
    void testUnknownJsonAttributeAllowedDefault() {
        LarJsonTypedReadConfiguration configuration = defaultConfig();
        assertTrue(configuration.isUnknownJsonAttributeAllowed());
    }

    @Test
    void testUnknownJsonAttributeAllowedAllowedTrue() {
        LarJsonTypedReadConfiguration configuration =
                new LarJsonTypedReadConfiguration.Builder().setUnknownJsonAttributeAllowed(true).build();
        assertTrue(configuration.isUnknownJsonAttributeAllowed());
    }

    @Test
    void testUnknownJsonAttributeAllowedFalse() {
        LarJsonTypedReadConfiguration configuration =
                new LarJsonTypedReadConfiguration.Builder().setUnknownJsonAttributeAllowed(false).build();
        assertFalse(configuration.isUnknownJsonAttributeAllowed());
    }

    @Test
    void testJsonFormatAnnotationEnabledDefault() {
        LarJsonTypedReadConfiguration configuration = defaultConfig();
        assertNull(configuration.getAnnotationConfigurationFactory().get(JsonFormat.class));
    }

    @Test
    void testJsonFormatAnnotationEnabledTrue() {
        LarJsonTypedReadConfiguration configuration =
                new LarJsonTypedReadConfiguration.Builder().enableJsonFormatAnnotation().build();
        AnnotationConfiguration annotationConfiguration = configuration.getAnnotationConfigurationFactory()
                .get(JsonFormat.class);
        assertNotNull(annotationConfiguration);
        assertTrue(annotationConfiguration.isEnabled());
    }

    @Test
    void testJsonFormatAnnotationEnabledFalse() {
        LarJsonTypedReadConfiguration configuration =
                new LarJsonTypedReadConfiguration.Builder().disableJsonFormatAnnotation().build();
        AnnotationConfiguration annotationConfiguration = configuration.getAnnotationConfigurationFactory()
                .get(JsonFormat.class);
        assertNotNull(annotationConfiguration);
        assertFalse(annotationConfiguration.isEnabled());
    }

    @Test
    void testJsonIgnoreAnnotationEnabledDefault() {
        LarJsonTypedReadConfiguration configuration = defaultConfig();
        assertNull(configuration.getAnnotationConfigurationFactory().get(JsonIgnore.class));
    }

    @Test
    void testJsonIgnoreAnnotationEnabledTrue() {
        LarJsonTypedReadConfiguration configuration =
                new LarJsonTypedReadConfiguration.Builder().enableJsonIgnoreAnnotation().build();
        AnnotationConfiguration annotationConfiguration = configuration.getAnnotationConfigurationFactory()
                .get(JsonIgnore.class);
        assertNotNull(annotationConfiguration);
        assertTrue(annotationConfiguration.isEnabled());
    }

    @Test
    void testJsonIgnoreAnnotationEnabledFalse() {
        LarJsonTypedReadConfiguration configuration =
                new LarJsonTypedReadConfiguration.Builder().disableJsonIgnoreAnnotation().build();
        AnnotationConfiguration annotationConfiguration = configuration.getAnnotationConfigurationFactory()
                .get(JsonIgnore.class);
        assertNotNull(annotationConfiguration);
        assertFalse(annotationConfiguration.isEnabled());
    }

    @Test
    void testJsonPropertyAnnotationEnabledDefault() {
        LarJsonTypedReadConfiguration configuration = defaultConfig();
        assertNull(configuration.getAnnotationConfigurationFactory().get(JsonProperty.class));
    }

    @Test
    void testJsonPropertyAnnotationEnabledTrue() {
        LarJsonTypedReadConfiguration configuration =
                new LarJsonTypedReadConfiguration.Builder().enableJsonPropertyAnnotation().build();
        AnnotationConfiguration annotationConfiguration = configuration.getAnnotationConfigurationFactory()
                .get(JsonProperty.class);
        assertNotNull(annotationConfiguration);
        assertTrue(annotationConfiguration.isEnabled());
    }

    @Test
    void testJsonPropertyAnnotationEnabledFalse() {
        LarJsonTypedReadConfiguration configuration =
                new LarJsonTypedReadConfiguration.Builder().disableJsonPropertyAnnotation().build();
        AnnotationConfiguration annotationConfiguration = configuration.getAnnotationConfigurationFactory()
                .get(JsonProperty.class);
        assertNotNull(annotationConfiguration);
        assertFalse(annotationConfiguration.isEnabled());
    }

    @Test
    void testPropertyResolverFactoryDefault() throws NoSuchMethodException {
        LarJsonTypedReadConfiguration configuration = defaultConfig();
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

        LarJsonTypedReadConfiguration configuration =
                new LarJsonTypedReadConfiguration.Builder()
                        .setPropertyResolverFactory(propertyResolverFactory).build();
        assertEquals(propertyResolverFactory, configuration.getPropertyResolverFactory());
    }

    @Test
    void testUnsupportedMethodCalledBehaviorDefault() throws NoSuchMethodException {
        LarJsonTypedReadConfiguration configuration = defaultConfig();
        try {
            onUnsupportedMethodCalled(configuration);
            fail();
        } catch (UnsupportedOperationException expected){
        }
    }

    @Test
    void testUnsupportedMethodCalledBehaviorThrowExceptionFlagAllowedFalse() {
        try {
            new LarJsonTypedReadConfiguration.Builder()
                    .throwExceptionOnUnsupportedMethodCalled().build();
            fail();
        }catch (IllegalStateException expected) {
        }
    }

    @Test
    void testUnsupportedMethodCalledBehaviorReturnNullFlagAllowedFalse() {
        try {
            new LarJsonTypedReadConfiguration.Builder()
                    .returnNullOnUnsupportedMethodCalled().build();
            fail();
        }catch (IllegalStateException expected) {
        }
    }

    @Test
    void testUnsupportedMethodCalledBehaviorCustomFlagAllowedFalse() {
        try {
            new LarJsonTypedReadConfiguration.Builder()
                    .setUnsupportedMethodCalledBehavior((object, method, args) -> null).build();
            fail();
        }catch (IllegalStateException expected) {
        }
    }

    @Test
    void testUnsupportedMethodCalledBehaviorThrowException() throws NoSuchMethodException {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .setUnsupportedMethodAllowed(true)
                .throwExceptionOnUnsupportedMethodCalled().build();
        try {
            onUnsupportedMethodCalled(configuration);
            fail();
        } catch (UnsupportedOperationException expected){
        }
    }

    @Test
    void testUnsupportedMethodCalledBehaviorReturnNull() throws NoSuchMethodException {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .setUnsupportedMethodAllowed(true)
                .returnNullOnUnsupportedMethodCalled().build();
        assertNull(onUnsupportedMethodCalled(configuration, ModelWithString.class.getDeclaredMethod("getWhatever")));
    }

    @Test
    void testUnsupportedMethodCalledBehaviorReturnFalse() throws NoSuchMethodException {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .setUnsupportedMethodAllowed(true)
                .returnNullOnUnsupportedMethodCalled().build();
        assertEquals(false, onUnsupportedMethodCalled(configuration, ModelWithPrimitiveBoolean.class.getDeclaredMethod("isWhatever")));
    }

    @Test
    void testUnsupportedMethodCalledBehaviorReturnZero() throws NoSuchMethodException {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .setUnsupportedMethodAllowed(true)
                .returnNullOnUnsupportedMethodCalled().build();
        assertEquals((char)0, onUnsupportedMethodCalled(configuration, ModelWithPrimitiveChar.class.getDeclaredMethod("getWhatever")));
    }

    @Test
    void testUnsupportedMethodCalledBehaviorCustom() throws NoSuchMethodException {
        Object o = new Object();
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .setUnsupportedMethodAllowed(true)
                .setUnsupportedMethodCalledBehavior((object, method, args) -> o).build();
        assertEquals(o, onUnsupportedMethodCalled(configuration));
    }

    private Object onUnsupportedMethodCalled(LarJsonTypedReadConfiguration configuration, Method method) {
        return configuration.getUnsupportedMethodCalledBehavior().onUnsupportedMethodCalled(new Object(),
                method, new Object[]{"hello"});
    }

    private Object onUnsupportedMethodCalled(LarJsonTypedReadConfiguration configuration) throws NoSuchMethodException {
        return onUnsupportedMethodCalled(configuration, ModelWithString.class.getDeclaredMethod("getWhatever"));
    }

    @Test
    void testStringValueConverterFactoryDefault() {
        LarJsonTypedReadConfiguration configuration = defaultConfig();
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
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .setStringValueConverter(TestEnum.class, converter).build();
        assertEquals(converter, configuration.getStringValueConverterFactory().get(TestEnum.class));
    }

    @Test
    void testEqualsDelegateFactoryDefault() {
        LarJsonTypedReadConfiguration configuration = defaultConfig();
        assertNotNull(configuration.getEqualsDelegateFactory());
    }

    @Test
    void testEqualsDelegateCustom() {
        EqualsDelegate<ModelWithString> equalsDelegate = new EqualsDelegate<ModelWithString>() {
            @Override
            public boolean equals(ModelWithString thisInstance, Object otherInstance) {
                return false;
            }

            @Override
            public int hashCode(ModelWithString thisInstance) {
                return 0;
            }
        };
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .setEqualsDelegate(ModelWithString.class, equalsDelegate).build();
        assertEquals(equalsDelegate, configuration.getEqualsDelegateFactory().get(ModelWithString.class));
    }

    @Test
    void testPropertyIgnoredTrue() throws NoSuchMethodException {
        Method method = ModelWithString.class.getMethod("getWhatever");
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
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
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
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
    void testPropertyRequiredTrue() throws NoSuchMethodException {
        Method method = ModelWithString.class.getMethod("getWhatever");
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .setPropertyRequired(method, true).build();
        PropertyConfiguration propertyConfiguration = configuration.getPropertyConfigurationFactory()
                .get(method);
        assertNotNull(propertyConfiguration);
        assertEquals(method, propertyConfiguration.getGetter());
        assertNull(propertyConfiguration.isIgnored());
        assertNull(propertyConfiguration.getName());
        assertNull(propertyConfiguration.getStringValueConverter());
        assertEquals(true, propertyConfiguration.isRequired());
    }

    @Test
    void testPropertyRequiredFalse() throws NoSuchMethodException {
        Method method = ModelWithString.class.getMethod("getWhatever");
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .setPropertyRequired(method, false).build();
        PropertyConfiguration propertyConfiguration = configuration.getPropertyConfigurationFactory()
                .get(method);
        assertNotNull(propertyConfiguration);
        assertEquals(method, propertyConfiguration.getGetter());
        assertNull(propertyConfiguration.isIgnored());
        assertNull(propertyConfiguration.getName());
        assertNull(propertyConfiguration.getStringValueConverter());
        assertEquals(false, propertyConfiguration.isRequired());
    }

    @Test
    void testPropertyName() throws NoSuchMethodException {
        Method method = ModelWithString.class.getMethod("getWhatever");
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
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
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
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
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .setPropertyStringValueConverter(method1, converter)
                .setPropertyIgnored(method1, false)
                .setPropertyName(method1, "hey")
                .setPropertyRequired(method1, true).build();
        PropertyConfiguration propertyConfiguration = configuration.getPropertyConfigurationFactory()
                .get(method1);
        assertNotNull(propertyConfiguration);
        assertEquals(method1, propertyConfiguration.getGetter());
        assertEquals(false, propertyConfiguration.isIgnored());
        assertEquals(true, propertyConfiguration.isRequired());
        assertEquals("hey", propertyConfiguration.getName());
        assertEquals(converter, propertyConfiguration.getStringValueConverter());
        assertNull(configuration.getPropertyConfigurationFactory().get(method2));
    }

    @Test
    void testDefaultCase() {
        LarJsonTypedReadConfiguration configuration = defaultConfig();
        assertEquals(configuration.getPropertyResolverFactory().get(ModelWithBigDecimal.class).getClass(),
                CamelCasePropertyResolver.class);
    }

    @Test
    void testCamelCase() {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .setCamelCase()
                .build();
        assertEquals(configuration.getPropertyResolverFactory().get(ModelWithBigDecimal.class).getClass(),
                CamelCasePropertyResolver.class);
    }

    @Test
    void testKebabCase() {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .setKebabCase()
                .build();
        assertEquals(configuration.getPropertyResolverFactory().get(ModelWithBigDecimal.class).getClass(),
                KebabCasePropertyResolver.class);
    }

    @Test
    void testPascalCase() {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .setPascalCase()
                .build();
        assertEquals(configuration.getPropertyResolverFactory().get(ModelWithBigDecimal.class).getClass(),
                PascalCasePropertyResolver.class);
    }

    @Test
    void testLowerDotCase() {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .setLowerDotCase()
                .build();
        assertEquals(configuration.getPropertyResolverFactory().get(ModelWithBigDecimal.class).getClass(),
                LowerDotCasePropertyResolver.class);
    }

    @Test
    void testUpperDotCase() {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .setUpperDotCase()
                .build();
        assertEquals(configuration.getPropertyResolverFactory().get(ModelWithBigDecimal.class).getClass(),
                UpperDotCasePropertyResolver.class);
    }

    @Test
    void testLowerSnakeCase() {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .setLowerSnakeCase()
                .build();
        assertEquals(configuration.getPropertyResolverFactory().get(ModelWithBigDecimal.class).getClass(),
                LowerSnakeCasePropertyResolver.class);
    }

    @Test
    void testUpperSnakeCase() {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .setUpperSnakeCase()
                .build();
        assertEquals(configuration.getPropertyResolverFactory().get(ModelWithBigDecimal.class).getClass(),
                UpperSnakeCasePropertyResolver.class);
    }

    @Test
    void testDefaultJsonIgnorePropertiesAnnotation() {
        LarJsonTypedReadConfiguration configuration = defaultConfig();
        assertNull(configuration.getAnnotationConfigurationFactory().get(JsonIgnoreProperties.class));
    }

    @Test
    void testEnableJsonIgnorePropertiesAnnotation() {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .disableJsonIgnorePropertiesAnnotation()
                .enableJsonIgnorePropertiesAnnotation()
                .build();
        assertNotNull(configuration.getAnnotationConfigurationFactory().get(JsonIgnoreProperties.class));
        assertTrue(configuration.getAnnotationConfigurationFactory().get(JsonIgnoreProperties.class).isEnabled());
        assertEquals(JsonIgnoreProperties.class,
                configuration.getAnnotationConfigurationFactory().get(JsonIgnoreProperties.class).getAnnotation());
    }

    @Test
    void testDisableJsonIgnorePropertiesAnnotation() {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .disableJsonIgnorePropertiesAnnotation()
                .build();
        assertNotNull(configuration.getAnnotationConfigurationFactory().get(JsonIgnoreProperties.class));
        assertFalse(configuration.getAnnotationConfigurationFactory().get(JsonIgnoreProperties.class).isEnabled());
    }

    @Test
    void testDefaultValidationEnabled() {
        LarJsonTypedReadConfiguration configuration = defaultConfig();
        assertFalse(configuration.isValidationEnabled());
    }

    @Test
    void testEnableValidation() {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .enableValidation()
                .build();
        assertTrue(configuration.isValidationEnabled());
    }

    @Test
    void testDisableValidation() {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .disableValidation()
                .build();
        assertFalse(configuration.isValidationEnabled());
    }

    @Test
    void testAllPropertiesRequiredDefault() {
        LarJsonTypedReadConfiguration configuration = defaultConfig();
        assertFalse(configuration.getAllPropertiesRequired());
    }

    @Test
    void testAllPropertiesRequiredTrue() {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .setAllPropertiesRequired(true)
                .build();
        assertTrue(configuration.getAllPropertiesRequired());
    }

    @Test
    void testAllPropertiesRequiredFalse() {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .setAllPropertiesRequired(false)
                .build();
        assertFalse(configuration.getAllPropertiesRequired());
    }

    @Test
    void testToWriteConfiguration() {
        StringValueConverter<TestEnum> stringValueConverter = new StringValueConverter<TestEnum>() {
            @Override
            public TestEnum fromString(String s) {
                return null;
            }

            @Override
            public String toString(TestEnum value) {
                return null;
            }
        };
        PropertyResolverFactory propertyResolverFactory = rootInterface -> null;
        LarJsonTypedReadConfiguration readConfiguration = new LarJsonTypedReadConfiguration.Builder()
                .setLenient(true)
                .setPropertyResolverFactory(propertyResolverFactory)
                .enableJsonFormatAnnotation()
                .enableJsonIgnoreAnnotation()
                .disableJsonPropertyAnnotation()
                .setStringValueConverter(TestEnum.class, stringValueConverter)
                .build();

        LarJsonTypedWriteConfiguration writeConfiguration = readConfiguration.toWriteConfiguration();
        assertNotNull(writeConfiguration);
        assertEquals(true, writeConfiguration.isLenient());
        assertEquals(propertyResolverFactory, writeConfiguration.getPropertyResolverFactory());
        assertEquals(stringValueConverter, writeConfiguration.getStringValueConverterFactory()
                .get(TestEnum.class));
        assertTrue(writeConfiguration.getAnnotationConfigurationFactory()
                .get(JsonFormat.class).isEnabled());
        assertTrue(writeConfiguration.getAnnotationConfigurationFactory()
                .get(JsonIgnore.class).isEnabled());
        assertFalse(writeConfiguration.getAnnotationConfigurationFactory()
                .get(JsonProperty.class).isEnabled());
        assertEquals(false, writeConfiguration.isHtmlSafe());
        assertEquals("\t", writeConfiguration.getIndent());
    }

    @Test
    void testUpdateAfterBuild() {
        LarJsonTypedReadConfiguration.Builder builder =
                new LarJsonTypedReadConfiguration.Builder();
        builder.setLenient(true);
        builder.build();
        try {
            builder.setThreadSafe(true);
            fail();
        }catch (IllegalStateException expected) {
        }
    }

    @Test
    void testBuildTwice() {
        LarJsonTypedReadConfiguration.Builder builder =
                new LarJsonTypedReadConfiguration.Builder();
        builder.setLenient(true);
        builder.build();
        try {
            builder.build();
            fail();
        }catch (IllegalStateException expected) {
        }
    }
}
