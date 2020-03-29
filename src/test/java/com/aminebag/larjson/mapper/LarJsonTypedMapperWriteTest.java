package com.aminebag.larjson.mapper;

import com.aminebag.larjson.api.LarJsonPerspectives;
import com.aminebag.larjson.api.LarJsonRootList;
import com.aminebag.larjson.configuration.LarJsonTypedReadConfiguration;
import com.aminebag.larjson.configuration.LarJsonTypedWriteConfiguration;
import com.aminebag.larjson.configuration.PropertyResolver;
import com.aminebag.larjson.exception.LarJsonConversionException;
import com.aminebag.larjson.exception.LarJsonException;
import com.aminebag.larjson.exception.LarJsonWriteException;
import com.aminebag.larjson.valueconverter.StringValueConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.Closeable;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;

import static com.aminebag.larjson.mapper.LarJsonMapperTestUtils.jsonToFile;
import static com.aminebag.larjson.mapper.LarJsonTypedMapperTestModels.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Amine Bagdouri
 */
public class LarJsonTypedMapperWriteTest {

    @Test
    void testWrite(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : true}";
        LarJsonTypedMapper<ModelWithBooleanWrapper> mapper = new LarJsonTypedMapper<>(ModelWithBooleanWrapper.class);
        try(ModelWithBooleanWrapper model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            StringWriter writer = new StringWriter();
            LarJsonPerspectives.write(model, writer, new LarJsonTypedWriteConfiguration.Builder().build());
            String json2 = writer.toString();
            assertEquals("{\n\t\"whatever\": true\n}", json2);
        }
    }

    @Test
    void testWriteStrict(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : " + Float.NEGATIVE_INFINITY + "}";
        LarJsonTypedMapper<ModelWithFloatWrapper> mapper = new LarJsonTypedMapper<>(ModelWithFloatWrapper.class,
                new LarJsonTypedReadConfiguration.Builder().setLenient(true).build());
        try(ModelWithFloatWrapper model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            StringWriter writer = new StringWriter();
            try {
                LarJsonPerspectives.write(model, writer, new LarJsonTypedWriteConfiguration.Builder().build());
                fail();
            } catch (LarJsonWriteException expected) {
            }
        }
    }

    @Test
    void testWriteLenient(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : " + Float.NEGATIVE_INFINITY + "}";
        LarJsonTypedMapper<ModelWithFloatWrapper> mapper = new LarJsonTypedMapper<>(ModelWithFloatWrapper.class,
                new LarJsonTypedReadConfiguration.Builder().setLenient(true).build());
        try(ModelWithFloatWrapper model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            StringWriter writer = new StringWriter();
            LarJsonPerspectives.write(model, writer,
                    new LarJsonTypedWriteConfiguration.Builder().setLenient(true).build());
            String json2 = writer.toString();
            assertEquals("{\n\t\"whatever\": -Infinity\n}", json2);
        }
    }

    @Test
    void testWriteEmptyIndent(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : true}";
        LarJsonTypedMapper<ModelWithBooleanWrapper> mapper = new LarJsonTypedMapper<>(ModelWithBooleanWrapper.class);
        try(ModelWithBooleanWrapper model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            StringWriter writer = new StringWriter();
            LarJsonPerspectives.write(model, writer, new LarJsonTypedWriteConfiguration.Builder().setIndent("").build());
            String json2 = writer.toString();
            assertEquals("{\"whatever\":true}", json2);
        }
    }

    @Test
    void testWriteIndentSpaces(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : true}";
        LarJsonTypedMapper<ModelWithBooleanWrapper> mapper = new LarJsonTypedMapper<>(ModelWithBooleanWrapper.class);
        try(ModelWithBooleanWrapper model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            StringWriter writer = new StringWriter();
            LarJsonPerspectives.write(model, writer,
                    new LarJsonTypedWriteConfiguration.Builder().setIndent("   ").build());
            String json2 = writer.toString();
            assertEquals("{\n   \"whatever\": true\n}", json2);
        }
    }

    @Test
    void testWriteHtmlUnsafe(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : \"<html>5=3&c'=d</html>\"}";
        LarJsonTypedMapper<ModelWithString> mapper = new LarJsonTypedMapper<>(ModelWithString.class);
        try(ModelWithString model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            StringWriter writer = new StringWriter();
            LarJsonPerspectives.write(model, writer,
                    new LarJsonTypedWriteConfiguration.Builder().build());
            String json2 = writer.toString();
            assertEquals("{\n\t\"whatever\": \"<html>5=3&c'=d</html>\"\n}", json2);
        }
    }

    @Test
    void testWriteHtmlSafe(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : \"<html>5=3&c'=d</html>\"}";
        LarJsonTypedMapper<ModelWithString> mapper = new LarJsonTypedMapper<>(ModelWithString.class);
        try(ModelWithString model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            StringWriter writer = new StringWriter();
            LarJsonPerspectives.write(model, writer,
                    new LarJsonTypedWriteConfiguration.Builder().setHtmlSafe(true).build());
            String json2 = writer.toString();
            assertEquals("{\n" +
                    "\t\"whatever\": \"\\u003chtml\\u003e5\\u003d3\\u0026c\\u0027\\u003dd\\u003c/html\\u003e\"\n" +
                    "}", json2);
        }
    }

    @Test
    void testWriteJsonFormatAnnotationEnabled(@TempDir Path tempDir) throws IOException, LarJsonException, ParseException {
        String dateFormat = "yyyy dd";
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedDateWithPattern> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedDateWithPattern.class,
                        new LarJsonTypedReadConfiguration.Builder().setDateFormat(dateFormat).build());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        SimpleDateFormat annotationDateFormat = new SimpleDateFormat("yyyy.MMM.dd G 'at' HH:mm:ss z");
        String date = simpleDateFormat.format(new Date());
        String annotationDate = annotationDateFormat.format(simpleDateFormat.parse(date));
        String json = "{\"whatever\" : \"" + date + "\"}";
        try(ModelWithJsonFormatAnnotatedDateWithPattern model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            StringWriter writer = new StringWriter();
            LarJsonPerspectives.write(model, writer,
                    new LarJsonTypedWriteConfiguration.Builder().enableJsonFormatAnnotation().build());
            String json2 = writer.toString();
            assertEquals("{\n\t\"whatever\": \"" + annotationDate + "\"\n}", json2);
        }
    }

    @Test
    void testWriteJsonFormatAnnotationDisabled(@TempDir Path tempDir) throws IOException, LarJsonException {
        String dateFormat = "yyyy dd";
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedDateWithPattern> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedDateWithPattern.class,
                        new LarJsonTypedReadConfiguration.Builder().setDateFormat(dateFormat).build());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        String date = simpleDateFormat.format(new Date());
        String json = "{\"whatever\" : \"" + date + "\"}";
        try(ModelWithJsonFormatAnnotatedDateWithPattern model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            StringWriter writer = new StringWriter();
            LarJsonPerspectives.write(model, writer, mapper.getConfiguration().toWriteConfiguration());
            String json2 = writer.toString();
            assertEquals("{\n\t\"whatever\": \"" + date + "\"\n}", json2);
        }
    }

    @Test
    void testWriteCustomPropertyResolver(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : \"hello\"}";
        LarJsonTypedMapper<ModelWithString> mapper = new LarJsonTypedMapper<>(ModelWithString.class);
        try(ModelWithString model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            StringWriter writer = new StringWriter();
            LarJsonPerspectives.write(model, writer,
                    new LarJsonTypedWriteConfiguration.Builder().setPropertyResolverFactory(
                            rootInterface -> new PropertyResolver() {

                                @Override
                                public Method findGetter(String attributeName, Collection<Method> candidates) {
                                    return null;
                                }

                                @Override
                                public boolean isGetter(Method method) {
                                    return false;
                                }

                                @Override
                                public Method findSetter(Method getterMethod, Collection<Method> candidates) {
                                    return null;
                                }

                                @Override
                                public String getAttributeName(Method getter) {
                                    return "something";
                                }
                    }).build());
            String json2 = writer.toString();
            assertEquals("{\n\t\"something\": \"hello\"\n}", json2);
        }
    }

    @Test
    void testWriteCustomStringValueConverter(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : \"hello\"}";
        LarJsonTypedMapper<ModelWithEnum> mapper = new LarJsonTypedMapper<>(ModelWithEnum.class,
                new LarJsonTypedReadConfiguration.Builder().setStringValueConverter(TestEnum.class,
                        new StringValueConverter<TestEnum>() {
                    @Override
                    public TestEnum fromString(String s) throws LarJsonConversionException {
                        if(s == null) {
                            return null;
                        }
                        return TestEnum.valueOf(s.toUpperCase());
                    }

                    @Override
                    public String toString(TestEnum value) {
                        if(value == null) {
                            return null;
                        }
                        return value.name();
                    }
                }).build());
        try(ModelWithEnum model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            StringWriter writer = new StringWriter();
            LarJsonPerspectives.write(model, writer,
                    new LarJsonTypedWriteConfiguration.Builder().setStringValueConverter(TestEnum.class, new StringValueConverter<TestEnum>() {
                        @Override
                        public TestEnum fromString(String s) throws LarJsonConversionException {
                            return null;
                        }

                        @Override
                        public String toString(TestEnum value) {
                            return "@@" + value.name() + "@@";
                        }
                    }).build());
            String json2 = writer.toString();
            assertEquals("{\n\t\"whatever\": \"@@HELLO@@\"\n}", json2);
        }
    }

    @Test
    void testWriteRootMutableValue(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : \"hello\"}";
        LarJsonTypedMapper<ModelWithStringMutable> mapper = new LarJsonTypedMapper<>(ModelWithStringMutable.class,
                new LarJsonTypedReadConfiguration.Builder().setMutable(true).build());
        try(ModelWithStringMutable model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            model.setWhatever("\"salut\"");
            StringWriter writer = new StringWriter();
            LarJsonPerspectives.write(model, writer, new LarJsonTypedWriteConfiguration.Builder().build());
            String json2 = writer.toString();
            try(ModelWithStringMutable model2 = mapper.readObject(jsonToFile(tempDir, json2))) {
                assertEquals(model, model2);
            }
        }
    }

    @Test
    void testWriteRootMutableObject(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"something\" : {\"whatever\" : \"hello\"}}";
        LarJsonTypedMapper<ModelWithObjectMutable> mapper = new LarJsonTypedMapper<>(ModelWithObjectMutable.class,
                new LarJsonTypedReadConfiguration.Builder().setMutable(true).build());
        try(ModelWithObjectMutable model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            model.setSomething(new ModelWithStringMutableImpl());
            StringWriter writer = new StringWriter();
            LarJsonPerspectives.write(model, writer, new LarJsonTypedWriteConfiguration.Builder().build());
            String json2 = writer.toString();
            try(ModelWithObjectMutable model2 = mapper.readObject(jsonToFile(tempDir, json2))) {
                assertEquals(model, model2);
            }
        }
    }

    @Test
    void testWriteChildMutableObject(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"something\" : {\"whatever\" : \"hello\"}}";
        LarJsonTypedMapper<ModelWithObjectMutable> mapper = new LarJsonTypedMapper<>(ModelWithObjectMutable.class,
                new LarJsonTypedReadConfiguration.Builder().setMutable(true).build());
        try(ModelWithObjectMutable model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            model.getSomething().setWhatever("bonjour");
            StringWriter writer = new StringWriter();
            LarJsonPerspectives.write(model, writer, new LarJsonTypedWriteConfiguration.Builder().build());
            String json2 = writer.toString();
            try(ModelWithObjectMutable model2 = mapper.readObject(jsonToFile(tempDir, json2))) {
                assertEquals(model, model2);
            }
        }
    }

    @Test
    void testWriteRootMutableList(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "[{\"whatever\" : \"something\"}, null, {}]";
        LarJsonTypedMapper<ModelWithString> mapper = new LarJsonTypedMapper<>(ModelWithString.class,
                new LarJsonTypedReadConfiguration.Builder().setMutable(true).build());
        try(LarJsonRootList<ModelWithString> model = mapper.readArray(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            model.remove(2);
            StringWriter writer = new StringWriter();
            LarJsonPerspectives.write(model, writer, new LarJsonTypedWriteConfiguration.Builder().build());
            String json2 = writer.toString();
            try(LarJsonRootList<ModelWithString> model2 = mapper.readArray(jsonToFile(tempDir, json2))) {
                assertEquals(model, model2);
            }
        }
    }

    @Test
    void testWriteChildMutableList(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : [\"something\", \"hello\"]}";
        LarJsonTypedMapper<ModelWithStringList> mapper = new LarJsonTypedMapper<>(ModelWithStringList.class,
                new LarJsonTypedReadConfiguration.Builder().setMutable(true).build());
        try(ModelWithStringList model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            model.getWhatever().add("salut");
            StringWriter writer = new StringWriter();
            LarJsonPerspectives.write(model, writer, new LarJsonTypedWriteConfiguration.Builder().build());
            String json2 = writer.toString();
            try(ModelWithStringList model2 = mapper.readObject(jsonToFile(tempDir, json2))) {
                assertEquals(model, model2);
            }
        }
    }

    @Test
    void testWriteObjectWithPrimitiveBoolean(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : true}";
        testWriteObject(tempDir, ModelWithPrimitiveBoolean.class, json);
    }

    @Test
    void testWriteObjectWithPrimitiveBooleanNull(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : null}";
        testWriteObject(tempDir, ModelWithPrimitiveBoolean.class, json);
    }

    @Test
    void testWriteObjectWithBooleanWrapper(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : false}";
        testWriteObject(tempDir, ModelWithBooleanWrapper.class, json);
    }

    @Test
    void testWriteListOfBoolean(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : [true, false, null]}";
        testWriteObject(tempDir, ModelWithBooleanList.class, json);
    }

    @Test
    void testWriteObjectWithPrimitiveInt(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : 13}";
        testWriteObject(tempDir, ModelWithPrimitiveInt.class, json);
    }

    @Test
    void testWriteObjectWithPrimitiveIntNull(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : null}";
        testWriteObject(tempDir, ModelWithPrimitiveInt.class, json);
    }

    @Test
    void testWriteObjectWithIntegerWrapper(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : -27}";
        testWriteObject(tempDir, ModelWithIntegerWrapper.class, json);
    }

    @Test
    void testWriteListOfInteger(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : [12, -785.0, null, 0, " + Integer.MIN_VALUE + ", " + Integer.MAX_VALUE + "]}";
        testWriteObject(tempDir, ModelWithIntegerList.class, json);
    }

    @Test
    void testWriteObjectWithPrimitiveLong(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : 13}";
        testWriteObject(tempDir, ModelWithPrimitiveLong.class, json);
    }

    @Test
    void testWriteObjectWithPrimitiveLongNull(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : null}";
        testWriteObject(tempDir, ModelWithPrimitiveLong.class, json);
    }

    @Test
    void testWriteObjectWithLongWrapper(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : -27}";
        testWriteObject(tempDir, ModelWithLongWrapper.class, json);
    }

    @Test
    void testWriteListOfLong(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : [12, -785.0, null, 0, " + Long.MIN_VALUE + ", " + Long.MAX_VALUE + "]}";
        testWriteObject(tempDir, ModelWithLongList.class, json);
    }

    @Test
    void testWriteObjectWithPrimitiveShort(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : 13}";
        testWriteObject(tempDir, ModelWithPrimitiveShort.class, json);
    }

    @Test
    void testWriteObjectWithPrimitiveShortNull(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : null}";
        testWriteObject(tempDir, ModelWithPrimitiveShort.class, json);
    }

    @Test
    void testWriteObjectWithShortWrapper(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : -27}";
        testWriteObject(tempDir, ModelWithShortWrapper.class, json);
    }

    @Test
    void testWriteListOfShort(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : [12, -785.0, null, 0, " + Short.MIN_VALUE + ", " + Short.MAX_VALUE + "]}";
        testWriteObject(tempDir, ModelWithShortList.class, json);
    }

    @Test
    void testWriteObjectWithPrimitiveByte(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : 13}";
        testWriteObject(tempDir, ModelWithPrimitiveByte.class, json);
    }

    @Test
    void testWriteObjectWithPrimitiveByteNull(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : null}";
        testWriteObject(tempDir, ModelWithPrimitiveByte.class, json);
    }

    @Test
    void testWriteObjectWithByteWrapper(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : -27}";
        testWriteObject(tempDir, ModelWithByteWrapper.class, json);
    }

    @Test
    void testWriteListOfByte(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : [12, -78.0, null, 0, " + Byte.MIN_VALUE + ", " + Byte.MAX_VALUE + "]}";
        testWriteObject(tempDir, ModelWithByteList.class, json);
    }

    @Test
    void testWriteObjectWithPrimitiveChar(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : \"q\"}";
        testWriteObject(tempDir, ModelWithPrimitiveChar.class, json);
    }

    @Test
    void testWriteObjectWithPrimitiveCharNull(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : null}";
        testWriteObject(tempDir, ModelWithPrimitiveChar.class, json);
    }

    @Test
    void testWriteObjectWithCharacterWrapper(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : \"@\"}";
        testWriteObject(tempDir, ModelWithCharacterWrapper.class, json);
    }

    @Test
    void testWriteListOfCharacter(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : [\"B\", null, \"\uABCD\", \"\\uABCE\", \"\n\", \"\\t\"]}";
        testWriteObject(tempDir, ModelWithCharacterList.class, json);
    }

    @Test
    void testWriteObjectWithPrimitiveDouble(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : 13.45704}";
        testWriteObject(tempDir, ModelWithPrimitiveDouble.class, json);
    }

    @Test
    void testWriteObjectWithPrimitiveDoubleNull(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : null}";
        testWriteObject(tempDir, ModelWithPrimitiveDouble.class, json);
    }

    @Test
    void testWriteObjectWithDoubleWrapper(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : -0.0427667}";
        testWriteObject(tempDir, ModelWithDoubleWrapper.class, json);
    }

    @Test
    void testWriteListOfDouble(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : [12, 0.0, null, 513e+10, -12.789E-75, " + Double.MAX_VALUE + ", " +
                Double.MIN_VALUE + "]}";
        testWriteObject(tempDir, ModelWithDoubleList.class, json);
    }

    @Test
    void testLenientReadListOfDouble(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : [" + Double.NaN + ", " + Double.NEGATIVE_INFINITY + ", " +
                Double.POSITIVE_INFINITY + "]}";
        testWriteObject(tempDir, ModelWithDoubleList.class, json,
                new LarJsonTypedReadConfiguration.Builder().setLenient(true).build());
    }

    @Test
    void testWriteObjectWithPrimitiveFloat(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : 13.45704}";
        testWriteObject(tempDir, ModelWithPrimitiveFloat.class, json);
    }

    @Test
    void testWriteObjectWithPrimitiveFloatNull(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : null}";
        testWriteObject(tempDir, ModelWithPrimitiveFloat.class, json);
    }

    @Test
    void testWriteObjectWithFloatWrapper(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : -0.0427667}";
        testWriteObject(tempDir, ModelWithFloatWrapper.class, json);
    }

    @Test
    void testWriteListOfFloat(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : [12, 0.0, null, 513e+10, -12.789E-33, " + Float.MAX_VALUE + ", " +
                Float.MIN_VALUE + "]}";
        testWriteObject(tempDir, ModelWithFloatList.class, json);
    }

    @Test
    void testLenientReadListOfFloat(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : [" + Float.NaN + ", " + Float.NEGATIVE_INFINITY + ", " +
                Float.POSITIVE_INFINITY + "]}";
        testWriteObject(tempDir, ModelWithFloatList.class, json,
                new LarJsonTypedReadConfiguration.Builder().setLenient(true).build());
    }

    @Test
    void testWriteObjectWithString(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : \" hell0-th€re !\"}";
        testWriteObject(tempDir, ModelWithString.class, json);
    }

    @Test
    void testWriteListOfString(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : [\"hello\", \"Yes\uABCD\", null, \"\", \"\\uBBDD \t\\n\\b\", \"null\"]}";
        testWriteObject(tempDir, ModelWithStringList.class, json);
    }

    @Test
    void testWriteLarJsonListOfString(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : [\"hello\", \"Yes\uABCD\", null, \"\", \"\\uBBDD \t\\n\\b\", \"null\"]}";
        testWriteObject(tempDir, ModelWithStringLarJsonList.class, json);
    }

    @Test
    void testWriteCollectionOfString(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : [\"hello\", \"Yes\uABCD\", null, \"\", \"\\uBBDD \t\\n\\b\", \"null\"]}";
        testWriteObject(tempDir, ModelWithStringCollection.class, json);
    }

    @Test
    void testWriteIterableOfString(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : [\"hello\", \"Yes\uABCD\", null, \"\", \"\\uBBDD \t\\n\\b\", \"null\"]}";
        testWriteObject(tempDir, ModelWithStringIterable.class, json);
    }

    @Test
    void testWriteObjectWithCharSequence(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : \" hell0-th€re !\"}";
        testWriteObject(tempDir, ModelWithCharSequence.class, json);
    }

    @Test
    void testWriteListOfCharSequence(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : [\"hello\", \"Yes\uABCD\", null, \"\", \"\\uBBDD \t\\n\\b\", \"null\"]}";
        testWriteObject(tempDir, ModelWithCharSequenceList.class, json);
    }

    @Test
    void testWriteObjectWithBigInteger(@TempDir Path tempDir) throws IOException, LarJsonException {
        BigInteger bigInteger = new BigInteger(Long.MIN_VALUE + "" + Long.MAX_VALUE);
        String json = "{\"whatever\" : " + bigInteger + "}";
        testWriteObject(tempDir, ModelWithBigInteger.class, json);
    }

    @Test
    void testWriteListOfBigInteger(@TempDir Path tempDir) throws IOException, LarJsonException {
        BigInteger bigInteger = new BigInteger(Long.MIN_VALUE + "" + Long.MAX_VALUE);
        String json =
                "{\"whatever\" : [12, 0.0, null, -182, " + bigInteger + ", " + bigInteger + ".0]}";
        testWriteObject(tempDir, ModelWithBigIntegerList.class, json);
    }

    @Test
    void testWriteObjectWithBigDecimal(@TempDir Path tempDir) throws IOException, LarJsonException {
        BigDecimal bigDecimal = new BigDecimal(Long.MIN_VALUE + "" + Long.MAX_VALUE + "." + Integer.MAX_VALUE);
        String json = "{\"whatever\" : " + bigDecimal + "}";
        testWriteObject(tempDir, ModelWithBigDecimal.class, json);
    }

    @Test
    void testWriteListOfBigDecimal(@TempDir Path tempDir) throws IOException, LarJsonException {
        BigDecimal bigDecimal1 = new BigDecimal(Long.MIN_VALUE + "" + Long.MAX_VALUE + "." + Integer.MAX_VALUE);
        BigDecimal bigDecimal2 = new BigDecimal("-0.000000000000000000000000000000000000000000000000000000000000"
                + Long.MAX_VALUE);
        String json =
                "{\"whatever\" : [12, 0.0, null, -182.05, " + bigDecimal1 + ", " + bigDecimal2 + "]}";
        testWriteObject(tempDir, ModelWithBigDecimalList.class, json);
    }

    @Test
    void testWriteObjectWithNumber(@TempDir Path tempDir) throws IOException, LarJsonException {
        BigDecimal bigDecimal = new BigDecimal(Long.MIN_VALUE + "" + Long.MAX_VALUE + "." + Integer.MAX_VALUE);
        String json = "{\"whatever\" : " + bigDecimal + "}";
        testWriteObject(tempDir, ModelWithNumber.class, json);
    }

    @Test
    void testWriteListOfNumber(@TempDir Path tempDir) throws IOException, LarJsonException {
        BigDecimal bigDecimal = new BigDecimal("-0.000000000000000000000000000000000000000000000000000000000000"
                + Long.MAX_VALUE);
        BigInteger bigInteger = new BigInteger(Long.MIN_VALUE + "" + Long.MAX_VALUE);
        String json = "{\"whatever\" : [12, 0.0, null, -182.05, " + Long.MIN_VALUE +  ", " + Long.MAX_VALUE + ", " +
                bigDecimal + ", " + Double.MIN_VALUE + ", " + bigInteger + ", " + Double.MAX_VALUE + "]}";
        testWriteObject(tempDir, ModelWithNumberList.class, json);
    }

    @Test
    void testWriteObjectWithEnum(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : \"" + TestEnum.HOLA + "\"}";
        testWriteObject(tempDir, ModelWithEnum.class, json);
    }

    @Test
    void testWriteListOfEnum(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : [null, \"HELLO\", \"SALUT\"]}";
        testWriteObject(tempDir, ModelWithEnumList.class, json);
    }

    @Test
    void testWriteObjectWithConverted(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithEnum> mapper = new LarJsonTypedMapper<>(ModelWithEnum.class,
                new LarJsonTypedReadConfiguration.Builder().setStringValueConverter(
                        TestEnum.class, new TestEnumConverter()).build());
        String json = "{\"whatever\" : null}";
        testWriteObject(tempDir, ModelWithEnum.class, json);
    }

    @Test
    void testWriteListOfConverted(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" : [null, \"   \t\", \"EN\"]}";
        testWriteObject(tempDir, ModelWithEnumList.class, json, new LarJsonTypedReadConfiguration.Builder()
                .setStringValueConverter(TestEnum.class, new TestEnumConverter()).build());
    }

    @Test
    void testWriteObjectWithDefaultFormatDate(@TempDir Path tempDir) throws IOException, LarJsonException {
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        String date = dateFormat.format(new Date());
        String json = "{\"whatever\" : \"" + date + "\"}";
        testWriteObject(tempDir, ModelWithDate.class, json);
    }

    @Test
    void testWriteObjectWithCustomFormatDate(@TempDir Path tempDir) throws IOException, LarJsonException {
        String format = "yyyy.MM.dd G 'at' HH:mm:ss z";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        String date = dateFormat.format(new Date());
        String json = "{\"whatever\" : \"" + date + "\"}";
        testWriteObject(tempDir, ModelWithDate.class, json,
                new LarJsonTypedReadConfiguration.Builder().setDateFormat(format).build());
    }

    @Test
    void testWriteListOfDate(@TempDir Path tempDir) throws IOException, LarJsonException {
        String format = "yyyy.MM.dd G 'at' HH:mm:ss z";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        String date = dateFormat.format(new Date());
        String json = "{\"whatever\" : [null, \"" + date + "\"]}";
        testWriteObject(tempDir, ModelWithDateList.class, json,
                new LarJsonTypedReadConfiguration.Builder().setDateFormat(format).build());
    }

    @Test
    void testWriteObjectWithDefaultFormatLocalDateTime(@TempDir Path tempDir) throws IOException, LarJsonException {
        LocalDateTime now = LocalDateTime.now();
        String json = "{\"whatever\" : \"" + now.toString() + "\"}";
        testWriteObject(tempDir, ModelWithLocalDateTime.class, json);
    }

    @Test
    void testWriteObjectWithCustomFormatLocalDateTime(@TempDir Path tempDir) throws IOException, LarJsonException {
        String format = "yyyy.MM.dd G 'at' HH:mm:ss";
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(format);
        String date = LocalDateTime.now().format(dateFormat);
        String json = "{\"whatever\" : \"" + date + "\"}";
        testWriteObject(tempDir, ModelWithLocalDateTime.class, json,
                new LarJsonTypedReadConfiguration.Builder().setLocalDateTimeFormat(format).build());
    }

    @Test
    void testWriteListOfLocalDateTime(@TempDir Path tempDir) throws IOException, LarJsonException {
        String format = "yyyy.MM.dd G 'at' HH:mm:ss";
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(format);
        String date = LocalDateTime.now().format(dateFormat);
        String json = "{\"whatever\" : [null, \"" + date + "\"]}";
        testWriteObject(tempDir, ModelWithLocalDateTimeList.class, json,
                new LarJsonTypedReadConfiguration.Builder().setLocalDateTimeFormat(format).build());
    }

    @Test
    void testWriteObjectWithDefaultFormatLocalDate(@TempDir Path tempDir) throws IOException, LarJsonException {
        LocalDate now = LocalDate.now();
        String json = "{\"whatever\" : \"" + now.toString() + "\"}";
        testWriteObject(tempDir, ModelWithLocalDate.class, json);
    }

    @Test
    void testWriteObjectWithCustomFormatLocalDate(@TempDir Path tempDir) throws IOException, LarJsonException {
        String format = "yyyy.MM.dd G";
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(format);
        String date = LocalDate.now().format(dateFormat);
        String json = "{\"whatever\" : \"" + date + "\"}";
        testWriteObject(tempDir, ModelWithLocalDate.class, json,
                new LarJsonTypedReadConfiguration.Builder().setLocalDateFormat(format).build());
    }

    @Test
    void testWriteListOfLocalDate(@TempDir Path tempDir) throws IOException, LarJsonException {
        String format = "yyyy.MM.dd G";
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(format);
        String date = LocalDate.now().format(dateFormat);
        String json = "{\"whatever\" : [null, \"" + date + "\"]}";
        testWriteObject(tempDir, ModelWithLocalDateList.class, json,
                new LarJsonTypedReadConfiguration.Builder().setLocalDateFormat(format).build());
    }

    @Test
    void testWriteObjectWithDefaultFormatLocalTime(@TempDir Path tempDir) throws IOException, LarJsonException {
        LocalTime now = LocalTime.now();
        String json = "{\"whatever\" : \"" + now.toString() + "\"}";
        testWriteObject(tempDir, ModelWithLocalTime.class, json);
    }

    @Test
    void testWriteObjectWithCustomFormatLocalTime(@TempDir Path tempDir) throws IOException, LarJsonException {
        String format = "HH_mm_ss__SSS";
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(format);
        String date = LocalTime.now().format(dateFormat);
        String json = "{\"whatever\" : \"" + date + "\"}";
        testWriteObject(tempDir, ModelWithLocalTime.class, json,
                new LarJsonTypedReadConfiguration.Builder().setLocalTimeFormat(format).build());
    }

    @Test
    void testWriteListOfLocalTime(@TempDir Path tempDir) throws IOException, LarJsonException {
        String format = "HH_mm_ss__SSS";
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(format);
        String date = LocalTime.now().format(dateFormat);
        String json = "{\"whatever\" : [null, \"" + date + "\"]}";
        testWriteObject(tempDir, ModelWithLocalTimeList.class, json,
                new LarJsonTypedReadConfiguration.Builder().setLocalTimeFormat(format).build());
    }

    @Test
    void testWriteObjectWithDefaultFormatZonedDateTime(@TempDir Path tempDir) throws IOException, LarJsonException {
        ZonedDateTime now = ZonedDateTime.now();
        String json = "{\"whatever\" : \"" + now.toString() + "\"}";
        testWriteObject(tempDir, ModelWithZonedDateTime.class, json);
    }

    @Test
    void testWriteObjectWithCustomFormatZonedDateTime(@TempDir Path tempDir) throws IOException, LarJsonException {
        String format = "yyyy.MM.dd G 'at' HH:mm:ss Z";
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(format);
        String date = ZonedDateTime.now().format(dateFormat);
        String json = "{\"whatever\" : \"" + date + "\"}";
        testWriteObject(tempDir, ModelWithZonedDateTime.class, json,
                new LarJsonTypedReadConfiguration.Builder().setZonedDateTimeFormat(format).build());
    }

    @Test
    void testWriteListOfZonedDateTime(@TempDir Path tempDir) throws IOException, LarJsonException {
        String format = "yyyy.MM.dd G 'at' HH:mm:ss Z";
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(format);
        String date = ZonedDateTime.now().format(dateFormat);
        String json = "{\"whatever\" : [null, \"" + date + "\"]}";
        testWriteObject(tempDir, ModelWithZonedDateTimeList.class, json,
                new LarJsonTypedReadConfiguration.Builder().setZonedDateTimeFormat(format).build());
    }

    @Test
    void testWriteNestedLists(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"whatever\" :[[[null, [], [null, [], [\"hello\", null, \"bonjour\"], [\"hola\"]]], null, " +
                "[]], [], null]}";
        testWriteObject(tempDir, ModelWithNestedLists.class, json);
    }

    @Test
    void testWriteObjectOfObject(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"something\": {\"whatever\" : \"hello\"}}";
        testWriteObject(tempDir, ModelWithObject.class, json);
    }

    @Test
    void testWriteListOfObject(@TempDir Path tempDir) throws IOException, LarJsonException {
        String json = "{\"something\": [{\"whatever\" : \"hello\"}, null, {\"whatever\": null}, {}]}";
        testWriteObject(tempDir, ModelWithObjectList.class, json);
    }

    @Test
    void testWriteArray(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithString> mapper = new LarJsonTypedMapper<>(ModelWithString.class);
        String json = "[{\"whatever\" : \"hello\"}, null, {\"whatever\": null}, {}]";
        try(LarJsonRootList<ModelWithString> model = mapper.readArray(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            StringWriter writer = new StringWriter();
            LarJsonPerspectives.write(model, writer);
            String json2 = writer.toString();
            try(LarJsonRootList<ModelWithString> model2 = mapper.readArray(jsonToFile(tempDir, json2))) {
                assertEquals(model, model2);
            }
        }
    }

    @Test
    void testWriteRichObject(@TempDir Path tempDir) throws IOException, LarJsonException {
        String birthDate = new SimpleDateFormat().format(new Date());
        String json = "{\"address\":{\"whatever\":\"Hotel California\"}, \"firstName\":\"Amine\", \"lasName\":null, " +
                "\"birthDate\":\"" + birthDate + "\", \"happy\":true, \"mad\":false, \"age\": 28, " +
                "\"whatever\":\"anything\", \"words\":[\"SALUT\", null, \"HOLA\"], \"scores\":[{\"whatever\":12.70}, " +
                "{\"whatever\":20}, {\"whatever\":-0.014}, {}]}";
        testWriteObject(tempDir, RichModel.class, json);
    }

    private <T extends Closeable> void testWriteObject(
            Path tempDir, Class<T> clazz, String json, LarJsonTypedReadConfiguration configuration)
            throws IOException, LarJsonException {
        LarJsonTypedMapper<T> mapper = new LarJsonTypedMapper<>(clazz, configuration);
        try(T model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            StringWriter writer = new StringWriter();
            LarJsonPerspectives.write(model, writer);
            String json2 = writer.toString();
            try(T model2 = mapper.readObject(jsonToFile(tempDir, json2))) {
                assertEquals(model, model2);
            }
        }
    }

    private <T extends Closeable> void testWriteObject(Path tempDir, Class<T> clazz, String json)
            throws IOException, LarJsonException {
        testWriteObject(tempDir, clazz, json, new LarJsonTypedReadConfiguration.Builder().build());
    }

}
