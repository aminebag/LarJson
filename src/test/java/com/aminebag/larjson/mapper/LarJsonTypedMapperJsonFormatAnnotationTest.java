package com.aminebag.larjson.mapper;

import com.aminebag.larjson.exception.LarJsonException;
import com.aminebag.larjson.configuration.LarJsonTypedReadConfiguration;
import com.aminebag.larjson.exception.LarJsonConversionException;
import com.aminebag.larjson.mapper.exception.LarJsonMappingDefinitionException;
import com.aminebag.larjson.exception.LarJsonValueReadException;
import com.aminebag.larjson.parser.LarJsonParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static com.aminebag.larjson.mapper.LarJsonMapperTestUtils.jsonToFile;
import static com.aminebag.larjson.mapper.LarJsonTypedMapperTestModels.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Amine Bagdouri
 */
public class LarJsonTypedMapperJsonFormatAnnotationTest {

    @Test
    void testJsonFormatEnumDefaultShape(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedEnumDefaultShape> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedEnumDefaultShape.class,
                        new LarJsonTypedReadConfiguration.Builder().enableJsonFormatAnnotation().build());

        String json1 = "{\"whatever\" : \"SALUT\"}";
        try(ModelWithJsonFormatAnnotatedEnumDefaultShape model = mapper.readObject(jsonToFile(tempDir, json1))) {
            assertNotNull(model);
            assertEquals(TestEnum.SALUT, model.getWhatever());
        }

        String json2 = "{\"whatever\" : 1}";
        try(ModelWithJsonFormatAnnotatedEnumDefaultShape model = mapper.readObject(jsonToFile(tempDir, json2))) {
            fail();
        } catch (LarJsonConversionException expected) {
        }
    }

    @Test
    void testJsonFormatEnumShapeString(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedEnumShapeString> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedEnumShapeString.class,
                        new LarJsonTypedReadConfiguration.Builder().enableJsonFormatAnnotation().build());

        String json1 = "{\"whatever\" : \"SALUT\"}";
        try(ModelWithJsonFormatAnnotatedEnumShapeString model = mapper.readObject(jsonToFile(tempDir, json1))) {
            assertNotNull(model);
            assertEquals(TestEnum.SALUT, model.getWhatever());
        }

        String json2 = "{\"whatever\" : 1}";
        try(ModelWithJsonFormatAnnotatedEnumShapeString model = mapper.readObject(jsonToFile(tempDir, json2))) {
            fail();
        } catch (LarJsonConversionException expected) {
        }
    }

    @Test
    void testJsonFormatEnumShapeNumber(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedEnumShapeNumber> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedEnumShapeNumber.class,
                        new LarJsonTypedReadConfiguration.Builder().enableJsonFormatAnnotation().build());

        String json1 = "{\"whatever\" : \"SALUT\"}";
        try(ModelWithJsonFormatAnnotatedEnumShapeNumber model = mapper.readObject(jsonToFile(tempDir, json1))) {
            fail();
        } catch (LarJsonParseException expected) {
        }

        String json2 = "{\"whatever\" : 1}";
        try(ModelWithJsonFormatAnnotatedEnumShapeNumber model = mapper.readObject(jsonToFile(tempDir, json2))) {
            assertNotNull(model);
            assertEquals(TestEnum.SALUT, model.getWhatever());
        }
    }

    @Test
    void testJsonFormatEnumShapeNumberList(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedEnumList> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedEnumList.class,
                        new LarJsonTypedReadConfiguration.Builder().enableJsonFormatAnnotation().build());

        String json1 = "{\"whatever\" : [\"SALUT\", null]}";
        try(ModelWithJsonFormatAnnotatedEnumList model = mapper.readObject(jsonToFile(tempDir, json1))) {
            fail();
        } catch (LarJsonParseException expected) {
        }

        String json2 = "{\"whatever\" : [1, 0, null]}";
        try(ModelWithJsonFormatAnnotatedEnumList model = mapper.readObject(jsonToFile(tempDir, json2))) {
            assertNotNull(model);
            assertEquals(Arrays.asList(TestEnum.SALUT, TestEnum.HELLO, null), model.getWhatever());
        }
    }

    @Test
    void testJsonFormatEnumAnnotationsDisabled(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedEnumShapeNumber> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedEnumShapeNumber.class,
                        new LarJsonTypedReadConfiguration.Builder().build());

        String json1 = "{\"whatever\" : \"SALUT\"}";
        try(ModelWithJsonFormatAnnotatedEnumShapeNumber model = mapper.readObject(jsonToFile(tempDir, json1))) {
            assertNotNull(model);
            assertEquals(TestEnum.SALUT, model.getWhatever());
        }

        String json2 = "{\"whatever\" : 1}";
        try(ModelWithJsonFormatAnnotatedEnumShapeNumber model = mapper.readObject(jsonToFile(tempDir, json2))) {
            fail();
        } catch (LarJsonConversionException expected) {
        }
    }

    @Test
    void testJsonFormatString(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedString> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedString.class,
                        new LarJsonTypedReadConfiguration.Builder().enableJsonFormatAnnotation().build());
        String json = "{\"whatever\" : \"bonjour\"}";
        try(ModelWithJsonFormatAnnotatedString model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals("bonjour", model.getWhatever());
        }
    }

    @Test
    void testJsonFormatDateDefaultShape(@TempDir Path tempDir) throws IOException, LarJsonException, ParseException {
        String dateFormat = "yyyy dd";
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedDateDefaultShape> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedDateDefaultShape.class,
                        new LarJsonTypedReadConfiguration.Builder().enableJsonFormatAnnotation()
                                .setDateFormat(dateFormat).build());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        String date = simpleDateFormat.format(new Date());
        String json = "{\"whatever\" : \"" + date + "\"}";
        try(ModelWithJsonFormatAnnotatedDateDefaultShape model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(simpleDateFormat.parse(date), model.getWhatever());
        }
    }

    @Test
    void testJsonFormatDateWithoutPattern(@TempDir Path tempDir)
            throws IOException, LarJsonException, ParseException {
        String dateFormat = "yyyy dd";
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedDateShapeString> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedDateShapeString.class,
                        new LarJsonTypedReadConfiguration.Builder().enableJsonFormatAnnotation()
                                .setDateFormat(dateFormat).build());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        String date = simpleDateFormat.format(new Date());
        String json = "{\"whatever\" : \"" + date + "\"}";
        try(ModelWithJsonFormatAnnotatedDateShapeString model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(simpleDateFormat.parse(date), model.getWhatever());
        }
    }

    @Test
    void testJsonFormatDateWithPattern(@TempDir Path tempDir) throws IOException, LarJsonException, ParseException {
        String dateFormat = "yyyy dd";
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedDateWithPattern> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedDateWithPattern.class,
                        new LarJsonTypedReadConfiguration.Builder().enableJsonFormatAnnotation()
                                .setDateFormat(dateFormat).build());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        SimpleDateFormat annotationDateFormat = new SimpleDateFormat("yyyy.MMM.dd G 'at' HH:mm:ss z");
        String date = simpleDateFormat.format(new Date());
        String annotationDate = annotationDateFormat.format(new Date());
        String json1 = "{\"whatever\" : \"" + date + "\"}";
        String json2 = "{\"whatever\" : \"" + annotationDate + "\"}";
        try(ModelWithJsonFormatAnnotatedDateWithPattern model = mapper.readObject(jsonToFile(tempDir, json1))) {
            assertNotNull(model);
            try {
                model.getWhatever();
                fail();
            } catch (LarJsonValueReadException expected){
            }
        }
        try(ModelWithJsonFormatAnnotatedDateWithPattern model = mapper.readObject(jsonToFile(tempDir, json2))) {
            assertNotNull(model);
            assertEquals(annotationDateFormat.parse(annotationDate), model.getWhatever());
        }
    }

    @Test
    void testJsonFormatDateList(@TempDir Path tempDir) throws IOException, LarJsonException, ParseException {
        String dateFormat = "yyyy dd";
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedDateList> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedDateList.class,
                        new LarJsonTypedReadConfiguration.Builder().enableJsonFormatAnnotation()
                                .setDateFormat(dateFormat).build());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        SimpleDateFormat annotationDateFormat = new SimpleDateFormat("yyyy.MMM.dd G 'at' HH:mm:ss z");
        String date = simpleDateFormat.format(new Date());
        String annotationDate = annotationDateFormat.format(new Date());
        String json1 = "{\"whatever\" : [\"" + date + "\", null]}";
        String json2 = "{\"whatever\" : [\"" + annotationDate + "\", null]}";
        try(ModelWithJsonFormatAnnotatedDateList model = mapper.readObject(jsonToFile(tempDir, json1))) {
            assertNotNull(model);
            try {
                assertNotNull(model.getWhatever());
                assertEquals(2, model.getWhatever().size());
                model.getWhatever().get(0);
                fail();
            } catch (LarJsonValueReadException expected){
            }
        }
        try(ModelWithJsonFormatAnnotatedDateList model = mapper.readObject(jsonToFile(tempDir, json2))) {
            assertNotNull(model);
            assertEquals(Arrays.asList(annotationDateFormat.parse(annotationDate), null), model.getWhatever());
        }
    }

    @Test
    void testJsonFormatDateAnnotationsDisabled(@TempDir Path tempDir) throws IOException, LarJsonException,
            ParseException {
        String dateFormat = "yyyy dd";
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedDateWithPattern> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedDateWithPattern.class,
                        new LarJsonTypedReadConfiguration.Builder().setDateFormat(dateFormat).build());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        SimpleDateFormat annotationDateFormat = new SimpleDateFormat("yyyy.MMM.dd G 'at' HH:mm:ss z");
        String date = simpleDateFormat.format(new Date());
        String annotationDate = annotationDateFormat.format(new Date());
        String json1 = "{\"whatever\" : \"" + date + "\"}";
        String json2 = "{\"whatever\" : \"" + annotationDate + "\"}";
        try(ModelWithJsonFormatAnnotatedDateWithPattern model = mapper.readObject(jsonToFile(tempDir, json1))) {
            assertNotNull(model);
            assertEquals(simpleDateFormat.parse(date), model.getWhatever());
        }
        try(ModelWithJsonFormatAnnotatedDateWithPattern model = mapper.readObject(jsonToFile(tempDir, json2))) {
            assertNotNull(model);
            try {
                model.getWhatever();
                fail();
            } catch (LarJsonValueReadException expected){
            }
        }
    }

    @Test
    void testJsonFormatDateWithLocale(@TempDir Path tempDir) throws IOException, LarJsonException, ParseException {
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedDateWithLocale> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedDateWithLocale.class,
                        new LarJsonTypedReadConfiguration.Builder().enableJsonFormatAnnotation().build());
        SimpleDateFormat annotationDateFormat = new SimpleDateFormat("yyyy.MMM.dd G 'at' HH:mm:ss z", Locale.JAPAN);
        String annotationDate = annotationDateFormat.format(new Date());
        String json = "{\"whatever\" : \"" + annotationDate + "\"}";
        try(ModelWithJsonFormatAnnotatedDateWithLocale model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(annotationDateFormat.parse(annotationDate), model.getWhatever());
        }
    }

    @Test
    void testJsonFormatDateWithTimeZone(@TempDir Path tempDir) throws IOException, LarJsonException, ParseException {
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedDateWithTimeZone> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedDateWithTimeZone.class,
                        new LarJsonTypedReadConfiguration.Builder().enableJsonFormatAnnotation().build());
        SimpleDateFormat annotationDateFormat = new SimpleDateFormat("yyyy.MMM.dd G 'at' HH:mm:ss z");
        annotationDateFormat.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Brazil/East")));
        String annotationDate = annotationDateFormat.format(new Date());
        String json = "{\"whatever\" : \"" + annotationDate + "\"}";
        try(ModelWithJsonFormatAnnotatedDateWithTimeZone model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(annotationDateFormat.parse(annotationDate), model.getWhatever());
        }
    }

    @Test
    void testJsonFormatDateWithInvalidPattern() {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .enableJsonFormatAnnotation().build();
        try {
            new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedDateWithInvalidPattern.class, configuration);
            fail();
        } catch (LarJsonMappingDefinitionException expected) {
        }
    }

    @Test
    void testJsonFormatDateWithInvalidTimeZone() {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .enableJsonFormatAnnotation().build();
        try {
            new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedDateWithInvalidTimeZone.class, configuration);
            fail();
        } catch (LarJsonMappingDefinitionException expected) {
        }
    }

    @Test
    void testJsonFormatLocalDateTimeDefaultShape(@TempDir Path tempDir) throws IOException, LarJsonException {
        String dateFormat = "yyyy_-_MM_-_dd_-_HH_-_mm_-_ss_-_SSS";
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedLocalDateTimeDefaultShape> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedLocalDateTimeDefaultShape.class,
                        new LarJsonTypedReadConfiguration.Builder().enableJsonFormatAnnotation()
                                .setLocalDateTimeFormat(dateFormat).build());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        String date = formatter.format(LocalDateTime.now());
        String json = "{\"whatever\" : \"" + date + "\"}";
        try(ModelWithJsonFormatAnnotatedLocalDateTimeDefaultShape model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(LocalDateTime.parse(date, formatter), model.getWhatever());
        }
    }

    @Test
    void testJsonFormatLocalDateTimeWithoutPattern(@TempDir Path tempDir) throws IOException, LarJsonException {
        String dateFormat = "yyyy_-_MM_-_dd_-_HH_-_mm_-_ss_-_SSS";
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedLocalDateTimeShapeString> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedLocalDateTimeShapeString.class,
                        new LarJsonTypedReadConfiguration.Builder().enableJsonFormatAnnotation()
                                .setLocalDateTimeFormat(dateFormat).build());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        String date = formatter.format(LocalDateTime.now());
        String json = "{\"whatever\" : \"" + date + "\"}";
        try(ModelWithJsonFormatAnnotatedLocalDateTimeShapeString model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(LocalDateTime.parse(date, formatter), model.getWhatever());
        }
    }

    @Test
    void testJsonFormatLocalDateTimeWithPattern(@TempDir Path tempDir) throws IOException, LarJsonException {
        String dateFormat = "yyyy_-_MM_-_dd_-_HH_-_mm_-_ss_-_SSS";
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedLocalDateTimeWithPattern> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedLocalDateTimeWithPattern.class,
                        new LarJsonTypedReadConfiguration.Builder().enableJsonFormatAnnotation()
                                .setLocalDateTimeFormat(dateFormat).build());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        DateTimeFormatter annotationFormatter = DateTimeFormatter.ofPattern("yyyy.MMM.dd G 'at' HH:mm:ss");
        String date = formatter.format(LocalDateTime.now());
        String annotationDate = annotationFormatter.format(LocalDateTime.now());
        String json1 = "{\"whatever\" : \"" + date + "\"}";
        String json2 = "{\"whatever\" : \"" + annotationDate + "\"}";
        try(ModelWithJsonFormatAnnotatedLocalDateTimeWithPattern model =
                    mapper.readObject(jsonToFile(tempDir, json1))) {
            try {
                model.getWhatever();
                fail();
            } catch (LarJsonValueReadException expected){
            }
        }
        try(ModelWithJsonFormatAnnotatedLocalDateTimeWithPattern model =
                    mapper.readObject(jsonToFile(tempDir, json2))) {
            assertNotNull(model);
            assertEquals(LocalDateTime.parse(annotationDate, annotationFormatter), model.getWhatever());
        }
    }

    @Test
    void testJsonFormatLocalDateTimeList(@TempDir Path tempDir) throws IOException, LarJsonException {
        String dateFormat = "yyyy_-_MM_-_dd_-_HH_-_mm_-_ss_-_SSS";
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedLocalDateTimeList> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedLocalDateTimeList.class,
                        new LarJsonTypedReadConfiguration.Builder().enableJsonFormatAnnotation()
                                .setLocalDateTimeFormat(dateFormat).build());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        DateTimeFormatter annotationFormatter = DateTimeFormatter.ofPattern("yyyy.MMM.dd G 'at' HH:mm:ss");
        String date = formatter.format(LocalDateTime.now());
        String annotationDate = annotationFormatter.format(LocalDateTime.now());
        String json1 = "{\"whatever\" : [\"" + date + "\", null]}";
        String json2 = "{\"whatever\" : [\"" + annotationDate + "\", null]}";
        try(ModelWithJsonFormatAnnotatedLocalDateTimeList model =
                    mapper.readObject(jsonToFile(tempDir, json1))) {
            try {
                assertNotNull(model.getWhatever());
                assertEquals(2, model.getWhatever().size());
                model.getWhatever().get(0);
                fail();
            } catch (LarJsonValueReadException expected){
            }
        }
        try(ModelWithJsonFormatAnnotatedLocalDateTimeList model =
                    mapper.readObject(jsonToFile(tempDir, json2))) {
            assertNotNull(model);
            assertEquals(Arrays.asList(LocalDateTime.parse(annotationDate, annotationFormatter), null),
                    model.getWhatever());
        }
    }

    @Test
    void testJsonFormatLocalDateTimeWithLocale(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedLocalDateTimeWithLocale> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedLocalDateTimeWithLocale.class,
                        new LarJsonTypedReadConfiguration.Builder().enableJsonFormatAnnotation().build());
        DateTimeFormatter annotationFormatter =
                DateTimeFormatter.ofPattern("yyyy.MMM.dd G 'at' HH:mm:ss", Locale.JAPAN);
        String annotationDate = annotationFormatter.format(LocalDateTime.now());
        String json = "{\"whatever\" : \"" + annotationDate + "\"}";
        try(ModelWithJsonFormatAnnotatedLocalDateTimeWithLocale model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(LocalDateTime.parse(annotationDate, annotationFormatter), model.getWhatever());
        }
    }

    @Test
    void testJsonFormatLocalDateTimeWithTimeZone(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedLocalDateTimeWithTimeZone> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedLocalDateTimeWithTimeZone.class,
                        new LarJsonTypedReadConfiguration.Builder().enableJsonFormatAnnotation().build());
        DateTimeFormatter annotationFormatter =
                DateTimeFormatter.ofPattern("yyyy.MMM.dd G 'at' HH:mm:ss").withZone(ZoneId.of("Brazil/East"));
        String annotationDate = annotationFormatter.format(LocalDateTime.now());
        String json = "{\"whatever\" : \"" + annotationDate + "\"}";
        try(ModelWithJsonFormatAnnotatedLocalDateTimeWithTimeZone model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(LocalDateTime.parse(annotationDate, annotationFormatter), model.getWhatever());
        }
    }

    @Test
    void testJsonFormatLocalDateTimeWithInvalidPattern() {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .enableJsonFormatAnnotation().build();
        try {
            new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedLocalDateTimeWithInvalidPattern.class, configuration);
            fail();
        } catch (LarJsonMappingDefinitionException expected) {
        }
    }

    @Test
    void testJsonFormatLocalDateTimeWithInvalidTimeZone() {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .enableJsonFormatAnnotation().build();
        try {
            new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedLocalDateTimeWithInvalidTimeZone.class, configuration);
            fail();
        } catch (LarJsonMappingDefinitionException expected) {
        }
    }

    @Test
    void testJsonFormatLocalDateDefaultShape(@TempDir Path tempDir) throws IOException, LarJsonException {
        String dateFormat = "yyyy_-_MM_-_dd";
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedLocalDateDefaultShape> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedLocalDateDefaultShape.class,
                        new LarJsonTypedReadConfiguration.Builder().enableJsonFormatAnnotation()
                                .setLocalDateFormat(dateFormat).build());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        String date = formatter.format(LocalDate.now());
        String json = "{\"whatever\" : \"" + date + "\"}";
        try(ModelWithJsonFormatAnnotatedLocalDateDefaultShape model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(LocalDate.parse(date, formatter), model.getWhatever());
        }
    }

    @Test
    void testJsonFormatLocalDateWithoutPattern(@TempDir Path tempDir) throws IOException, LarJsonException {
        String dateFormat = "yyyy_-_MM_-_dd";
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedLocalDateShapeString> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedLocalDateShapeString.class,
                        new LarJsonTypedReadConfiguration.Builder().enableJsonFormatAnnotation()
                                .setLocalDateFormat(dateFormat).build());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        String date = formatter.format(LocalDate.now());
        String json = "{\"whatever\" : \"" + date + "\"}";
        try(ModelWithJsonFormatAnnotatedLocalDateShapeString model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(LocalDate.parse(date, formatter), model.getWhatever());
        }
    }

    @Test
    void testJsonFormatLocalDateWithPattern(@TempDir Path tempDir) throws IOException, LarJsonException {
        String dateFormat = "yyyy_-_MM_-_dd";
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedLocalDateWithPattern> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedLocalDateWithPattern.class,
                        new LarJsonTypedReadConfiguration.Builder().enableJsonFormatAnnotation()
                                .setLocalDateFormat(dateFormat).build());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        DateTimeFormatter annotationFormatter = DateTimeFormatter.ofPattern("yyyy.MMM.dd G");
        String date = formatter.format(LocalDate.now());
        String annotationDate = annotationFormatter.format(LocalDate.now());
        String json1 = "{\"whatever\" : \"" + date + "\"}";
        String json2 = "{\"whatever\" : \"" + annotationDate + "\"}";
        try(ModelWithJsonFormatAnnotatedLocalDateWithPattern model =
                    mapper.readObject(jsonToFile(tempDir, json1))) {
            try {
                model.getWhatever();
                fail();
            } catch (LarJsonValueReadException expected){
            }
        }
        try(ModelWithJsonFormatAnnotatedLocalDateWithPattern model =
                    mapper.readObject(jsonToFile(tempDir, json2))) {
            assertNotNull(model);
            assertEquals(LocalDate.parse(annotationDate, annotationFormatter), model.getWhatever());
        }
    }

    @Test
    void testJsonFormatLocalDateList(@TempDir Path tempDir) throws IOException, LarJsonException {
        String dateFormat = "yyyy_-_MM_-_dd";
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedLocalDateList> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedLocalDateList.class,
                        new LarJsonTypedReadConfiguration.Builder().enableJsonFormatAnnotation()
                                .setLocalDateFormat(dateFormat).build());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        DateTimeFormatter annotationFormatter = DateTimeFormatter.ofPattern("yyyy.MMM.dd G");
        String date = formatter.format(LocalDate.now());
        String annotationDate = annotationFormatter.format(LocalDate.now());
        String json1 = "{\"whatever\" : [\"" + date + "\", null]}";
        String json2 = "{\"whatever\" : [\"" + annotationDate + "\", null]}";
        try(ModelWithJsonFormatAnnotatedLocalDateList model =
                    mapper.readObject(jsonToFile(tempDir, json1))) {
            try {
                assertNotNull(model.getWhatever());
                assertEquals(2, model.getWhatever().size());
                model.getWhatever().get(0);
                fail();
            } catch (LarJsonValueReadException expected){
            }
        }
        try(ModelWithJsonFormatAnnotatedLocalDateList model =
                    mapper.readObject(jsonToFile(tempDir, json2))) {
            assertNotNull(model);
            assertEquals(Arrays.asList(LocalDate.parse(annotationDate, annotationFormatter), null),
                    model.getWhatever());
        }
    }

    @Test
    void testJsonFormatLocalDateWithLocale(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedLocalDateWithLocale> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedLocalDateWithLocale.class,
                        new LarJsonTypedReadConfiguration.Builder().enableJsonFormatAnnotation().build());
        DateTimeFormatter annotationFormatter =
                DateTimeFormatter.ofPattern("yyyy.MMM.dd G", Locale.JAPAN);
        String annotationDate = annotationFormatter.format(LocalDate.now());
        String json = "{\"whatever\" : \"" + annotationDate + "\"}";
        try(ModelWithJsonFormatAnnotatedLocalDateWithLocale model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(LocalDate.parse(annotationDate, annotationFormatter), model.getWhatever());
        }
    }

    @Test
    void testJsonFormatLocalDateWithTimeZone(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedLocalDateWithTimeZone> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedLocalDateWithTimeZone.class,
                        new LarJsonTypedReadConfiguration.Builder().enableJsonFormatAnnotation().build());
        DateTimeFormatter annotationFormatter =
                DateTimeFormatter.ofPattern("yyyy.MMM.dd G").withZone(ZoneId.of("Brazil/East"));
        String annotationDate = annotationFormatter.format(LocalDate.now());
        String json = "{\"whatever\" : \"" + annotationDate + "\"}";
        try(ModelWithJsonFormatAnnotatedLocalDateWithTimeZone model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(LocalDate.parse(annotationDate, annotationFormatter), model.getWhatever());
        }
    }

    @Test
    void testJsonFormatLocalDateWithInvalidPattern() {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .enableJsonFormatAnnotation().build();
        try {
            new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedLocalDateWithInvalidPattern.class, configuration);
            fail();
        } catch (LarJsonMappingDefinitionException expected) {
        }
    }

    @Test
    void testJsonFormatLocalDateWithInvalidTimeZone() {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .enableJsonFormatAnnotation().build();
        try {
            new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedLocalDateWithInvalidTimeZone.class, configuration);
            fail();
        } catch (LarJsonMappingDefinitionException expected) {
        }
    }

    @Test
    void testJsonFormatLocalTimeDefaultShape(@TempDir Path tempDir) throws IOException, LarJsonException {
        String dateFormat = "HH_-_mm_-_ss_-_SSS";
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedLocalTimeDefaultShape> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedLocalTimeDefaultShape.class,
                        new LarJsonTypedReadConfiguration.Builder().enableJsonFormatAnnotation()
                                .setLocalTimeFormat(dateFormat).build());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        String date = formatter.format(LocalTime.now());
        String json = "{\"whatever\" : \"" + date + "\"}";
        try(ModelWithJsonFormatAnnotatedLocalTimeDefaultShape model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(LocalTime.parse(date, formatter), model.getWhatever());
        }
    }

    @Test
    void testJsonFormatLocalTimeWithoutPattern(@TempDir Path tempDir) throws IOException, LarJsonException {
        String dateFormat = "HH_-_mm_-_ss_-_SSS";
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedLocalTimeShapeString> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedLocalTimeShapeString.class,
                        new LarJsonTypedReadConfiguration.Builder().enableJsonFormatAnnotation()
                                .setLocalTimeFormat(dateFormat).build());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        String date = formatter.format(LocalTime.now());
        String json = "{\"whatever\" : \"" + date + "\"}";
        try(ModelWithJsonFormatAnnotatedLocalTimeShapeString model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(LocalTime.parse(date, formatter), model.getWhatever());
        }
    }

    @Test
    void testJsonFormatLocalTimeWithPattern(@TempDir Path tempDir) throws IOException, LarJsonException {
        String dateFormat = "HH_-_mm_-_ss_-_SSS";
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedLocalTimeWithPattern> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedLocalTimeWithPattern.class,
                        new LarJsonTypedReadConfiguration.Builder().enableJsonFormatAnnotation()
                                .setLocalTimeFormat(dateFormat).build());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        DateTimeFormatter annotationFormatter = DateTimeFormatter.ofPattern("HH_mm_ss_SSS");
        String date = formatter.format(LocalTime.now());
        String annotationDate = annotationFormatter.format(LocalTime.now());
        String json1 = "{\"whatever\" : \"" + date + "\"}";
        String json2 = "{\"whatever\" : \"" + annotationDate + "\"}";
        try(ModelWithJsonFormatAnnotatedLocalTimeWithPattern model =
                    mapper.readObject(jsonToFile(tempDir, json1))) {
            try {
                model.getWhatever();
                fail();
            } catch (LarJsonValueReadException expected){
            }
        }
        try(ModelWithJsonFormatAnnotatedLocalTimeWithPattern model =
                    mapper.readObject(jsonToFile(tempDir, json2))) {
            assertNotNull(model);
            assertEquals(LocalTime.parse(annotationDate, annotationFormatter), model.getWhatever());
        }
    }

    @Test
    void testJsonFormatLocalTimeList(@TempDir Path tempDir) throws IOException, LarJsonException {
        String dateFormat = "HH_-_mm_-_ss_-_SSS";
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedLocalTimeList> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedLocalTimeList.class,
                        new LarJsonTypedReadConfiguration.Builder().enableJsonFormatAnnotation()
                                .setLocalTimeFormat(dateFormat).build());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        DateTimeFormatter annotationFormatter = DateTimeFormatter.ofPattern("HH_mm_ss_SSS");
        String date = formatter.format(LocalTime.now());
        String annotationDate = annotationFormatter.format(LocalTime.now());
        String json1 = "{\"whatever\" : [\"" + date + "\", null]}";
        String json2 = "{\"whatever\" : [\"" + annotationDate + "\", null]}";
        try(ModelWithJsonFormatAnnotatedLocalTimeList model =
                    mapper.readObject(jsonToFile(tempDir, json1))) {
            try {
                assertNotNull(model.getWhatever());
                assertEquals(2, model.getWhatever().size());
                model.getWhatever().get(0);
                fail();
            } catch (LarJsonValueReadException expected){
            }
        }
        try(ModelWithJsonFormatAnnotatedLocalTimeList model =
                    mapper.readObject(jsonToFile(tempDir, json2))) {
            assertNotNull(model);
            assertEquals(Arrays.asList(LocalTime.parse(annotationDate, annotationFormatter), null),
                    model.getWhatever());
        }
    }

    @Test
    void testJsonFormatLocalTimeWithLocale(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedLocalTimeWithLocale> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedLocalTimeWithLocale.class,
                        new LarJsonTypedReadConfiguration.Builder().enableJsonFormatAnnotation().build());
        DateTimeFormatter annotationFormatter =
                DateTimeFormatter.ofPattern("HH_mm_ss_SSS", Locale.JAPAN);
        String annotationDate = annotationFormatter.format(LocalTime.now());
        String json = "{\"whatever\" : \"" + annotationDate + "\"}";
        try(ModelWithJsonFormatAnnotatedLocalTimeWithLocale model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(LocalTime.parse(annotationDate, annotationFormatter), model.getWhatever());
        }
    }

    @Test
    void testJsonFormatLocalTimeWithTimeZone(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedLocalTimeWithTimeZone> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedLocalTimeWithTimeZone.class,
                        new LarJsonTypedReadConfiguration.Builder().enableJsonFormatAnnotation().build());
        DateTimeFormatter annotationFormatter =
                DateTimeFormatter.ofPattern("HH_mm_ss_SSS").withZone(ZoneId.of("Brazil/East"));
        String annotationDate = annotationFormatter.format(LocalTime.now());
        String json = "{\"whatever\" : \"" + annotationDate + "\"}";
        try(ModelWithJsonFormatAnnotatedLocalTimeWithTimeZone model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(LocalTime.parse(annotationDate, annotationFormatter), model.getWhatever());
        }
    }

    @Test
    void testJsonFormatLocalTimeWithInvalidPattern() {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .enableJsonFormatAnnotation().build();
        try {
            new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedLocalTimeWithInvalidPattern.class, configuration);
            fail();
        } catch (LarJsonMappingDefinitionException expected) {
        }
    }

    @Test
    void testJsonFormatLocalTimeWithInvalidTimeZone() {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .enableJsonFormatAnnotation().build();
        try {
            new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedLocalTimeWithInvalidTimeZone.class, configuration);
            fail();
        } catch (LarJsonMappingDefinitionException expected) {
        }
    }

    @Test
    void testJsonFormatZonedDateTimeDefaultShape(@TempDir Path tempDir) throws IOException, LarJsonException {
        String dateFormat = "yyyy_-_MM_-_dd_-_HH_-_mm_-_ss_-_SSS_-_Z";
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedZonedDateTimeDefaultShape> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedZonedDateTimeDefaultShape.class,
                        new LarJsonTypedReadConfiguration.Builder().enableJsonFormatAnnotation()
                                .setZonedDateTimeFormat(dateFormat).build());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        String date = formatter.format(ZonedDateTime.now());
        String json = "{\"whatever\" : \"" + date + "\"}";
        try(ModelWithJsonFormatAnnotatedZonedDateTimeDefaultShape model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(ZonedDateTime.parse(date, formatter), model.getWhatever());
        }
    }

    @Test
    void testJsonFormatZonedDateTimeWithoutPattern(@TempDir Path tempDir) throws IOException, LarJsonException {
        String dateFormat = "yyyy_-_MM_-_dd_-_HH_-_mm_-_ss_-_SSS_-_Z";
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedZonedDateTimeShapeString> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedZonedDateTimeShapeString.class,
                        new LarJsonTypedReadConfiguration.Builder().enableJsonFormatAnnotation()
                                .setZonedDateTimeFormat(dateFormat).build());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        String date = formatter.format(ZonedDateTime.now());
        String json = "{\"whatever\" : \"" + date + "\"}";
        try(ModelWithJsonFormatAnnotatedZonedDateTimeShapeString model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(ZonedDateTime.parse(date, formatter), model.getWhatever());
        }
    }

    @Test
    void testJsonFormatZonedDateTimeWithPattern(@TempDir Path tempDir) throws IOException, LarJsonException {
        String dateFormat = "yyyy_-_MM_-_dd_-_HH_-_mm_-_ss_-_SSS_-_Z";
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedZonedDateTimeWithPattern> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedZonedDateTimeWithPattern.class,
                        new LarJsonTypedReadConfiguration.Builder().enableJsonFormatAnnotation()
                                .setZonedDateTimeFormat(dateFormat).build());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        DateTimeFormatter annotationFormatter = DateTimeFormatter.ofPattern("yyyy.MMM.dd G 'at' HH:mm:ss Z");
        String date = formatter.format(ZonedDateTime.now());
        String annotationDate = annotationFormatter.format(ZonedDateTime.now());
        String json1 = "{\"whatever\" : \"" + date + "\"}";
        String json2 = "{\"whatever\" : \"" + annotationDate + "\"}";
        try(ModelWithJsonFormatAnnotatedZonedDateTimeWithPattern model =
                    mapper.readObject(jsonToFile(tempDir, json1))) {
            try {
                model.getWhatever();
                fail();
            } catch (LarJsonValueReadException expected){
            }
        }
        try(ModelWithJsonFormatAnnotatedZonedDateTimeWithPattern model =
                    mapper.readObject(jsonToFile(tempDir, json2))) {
            assertNotNull(model);
            assertEquals(ZonedDateTime.parse(annotationDate, annotationFormatter), model.getWhatever());
        }
    }

    @Test
    void testJsonFormatZonedDateTimeList(@TempDir Path tempDir) throws IOException, LarJsonException {
        String dateFormat = "yyyy_-_MM_-_dd_-_HH_-_mm_-_ss_-_SSS_-_Z";
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedZonedDateTimeList> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedZonedDateTimeList.class,
                        new LarJsonTypedReadConfiguration.Builder().enableJsonFormatAnnotation()
                                .setZonedDateTimeFormat(dateFormat).build());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        DateTimeFormatter annotationFormatter = DateTimeFormatter.ofPattern("yyyy.MMM.dd G 'at' HH:mm:ss Z");
        String date = formatter.format(ZonedDateTime.now());
        String annotationDate = annotationFormatter.format(ZonedDateTime.now());
        String json1 = "{\"whatever\" : [\"" + date + "\", null]}";
        String json2 = "{\"whatever\" : [\"" + annotationDate + "\", null]}";
        try(ModelWithJsonFormatAnnotatedZonedDateTimeList model =
                    mapper.readObject(jsonToFile(tempDir, json1))) {
            try {
                assertNotNull(model.getWhatever());
                assertEquals(2, model.getWhatever().size());
                model.getWhatever().get(0);
                fail();
            } catch (LarJsonValueReadException expected){
            }
        }
        try(ModelWithJsonFormatAnnotatedZonedDateTimeList model =
                    mapper.readObject(jsonToFile(tempDir, json2))) {
            assertNotNull(model);
            assertEquals(Arrays.asList(ZonedDateTime.parse(annotationDate, annotationFormatter), null),
                    model.getWhatever());
        }
    }

    @Test
    void testJsonFormatZonedDateTimeWithLocale(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedZonedDateTimeWithLocale> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedZonedDateTimeWithLocale.class,
                        new LarJsonTypedReadConfiguration.Builder().enableJsonFormatAnnotation().build());
        DateTimeFormatter annotationFormatter =
                DateTimeFormatter.ofPattern("yyyy.MMM.dd G 'at' HH:mm:ss Z", Locale.JAPAN);
        String annotationDate = annotationFormatter.format(ZonedDateTime.now());
        String json = "{\"whatever\" : \"" + annotationDate + "\"}";
        try(ModelWithJsonFormatAnnotatedZonedDateTimeWithLocale model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(ZonedDateTime.parse(annotationDate, annotationFormatter), model.getWhatever());
        }
    }

    @Test
    void testJsonFormatZonedDateTimeWithTimeZone(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedZonedDateTimeWithTimeZone> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedZonedDateTimeWithTimeZone.class,
                        new LarJsonTypedReadConfiguration.Builder().enableJsonFormatAnnotation().build());
        DateTimeFormatter annotationFormatter =
                DateTimeFormatter.ofPattern("yyyy.MMM.dd G 'at' HH:mm:ss Z").withZone(ZoneId.of("Brazil/East"));
        String annotationDate = annotationFormatter.format(ZonedDateTime.now());
        String json = "{\"whatever\" : \"" + annotationDate + "\"}";
        try(ModelWithJsonFormatAnnotatedZonedDateTimeWithTimeZone model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(ZonedDateTime.parse(annotationDate, annotationFormatter), model.getWhatever());
        }
    }

    @Test
    void testJsonFormatZonedDateTimeWithInvalidPattern() {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .enableJsonFormatAnnotation().build();
        try {
            new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedZonedDateTimeWithInvalidPattern.class, configuration);
            fail();
        } catch (LarJsonMappingDefinitionException expected) {
        }
    }

    @Test
    void testJsonFormatZonedDateTimeWithInvalidTimeZone() {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .enableJsonFormatAnnotation().build();
        try {
            new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedZonedDateTimeWithInvalidTimeZone.class, configuration);
            fail();
        } catch (LarJsonMappingDefinitionException expected) {
        }
    }

    @Test
    void testJsonFormatDateShapeNumber(@TempDir Path tempDir) throws IOException, LarJsonException {
        String dateFormat = "yyyy_-_MM_-_dd_-_HH_-_mm_-_ss_-_SSS_-_z";
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedDateShapeNumber> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedDateShapeNumber.class,
                        new LarJsonTypedReadConfiguration.Builder().enableJsonFormatAnnotation()
                                .setDateFormat(dateFormat).build());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        String date = simpleDateFormat.format(new Date());
        long epoch = System.currentTimeMillis();
        String json1 = "{\"whatever\" : \"" + date + "\"}";
        String json2 = "{\"whatever\" : \"" + epoch + "\"}";
        try(ModelWithJsonFormatAnnotatedDateShapeNumber model =
                    mapper.readObject(jsonToFile(tempDir, json1))) {
            try {
                model.getWhatever();
                fail();
            } catch (LarJsonValueReadException expected){
            }
        }
        try(ModelWithJsonFormatAnnotatedDateShapeNumber model =
                    mapper.readObject(jsonToFile(tempDir, json2))) {
            assertNotNull(model);
            assertEquals(new Date(epoch), model.getWhatever());
        }
    }

    @Test
    void testJsonFormatLocalDateTimeShapeNumber(@TempDir Path tempDir) throws IOException, LarJsonException {
        String dateFormat = "yyyy_-_MM_-_dd_-_HH_-_mm_-_ss_-_SSS";
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedLocalDateTimeShapeNumber> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedLocalDateTimeShapeNumber.class,
                        new LarJsonTypedReadConfiguration.Builder().enableJsonFormatAnnotation()
                                .setLocalDateTimeFormat(dateFormat).build());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        String date = formatter.format(LocalDateTime.now());
        long epoch = System.currentTimeMillis();
        String json1 = "{\"whatever\" : \"" + date + "\"}";
        String json2 = "{\"whatever\" : \"" + epoch + "\"}";
        try(ModelWithJsonFormatAnnotatedLocalDateTimeShapeNumber model =
                    mapper.readObject(jsonToFile(tempDir, json1))) {
            try {
                model.getWhatever();
                fail();
            } catch (LarJsonValueReadException expected){
            }
        }
        try(ModelWithJsonFormatAnnotatedLocalDateTimeShapeNumber model =
                    mapper.readObject(jsonToFile(tempDir, json2))) {
            assertNotNull(model);
            assertEquals(LocalDateTime.ofEpochSecond(epoch/1000, (int)(epoch%1000), ZoneOffset.UTC),
                    model.getWhatever());
        }
    }

    @Test
    void testJsonFormatLocalDateShapeNumber(@TempDir Path tempDir) throws IOException, LarJsonException {
        String dateFormat = "yyyy_-_MM_-_dd";
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedLocalDateShapeNumber> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedLocalDateShapeNumber.class,
                        new LarJsonTypedReadConfiguration.Builder().enableJsonFormatAnnotation()
                                .setLocalDateFormat(dateFormat).build());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        String date = formatter.format(LocalDate.now());
        long epoch = System.currentTimeMillis();
        String json1 = "{\"whatever\" : \"" + date + "\"}";
        String json2 = "{\"whatever\" : \"" + epoch + "\"}";
        try(ModelWithJsonFormatAnnotatedLocalDateShapeNumber model =
                    mapper.readObject(jsonToFile(tempDir, json1))) {
            try {
                model.getWhatever();
                fail();
            } catch (LarJsonValueReadException expected){
            }
        }
        try(ModelWithJsonFormatAnnotatedLocalDateShapeNumber model =
                    mapper.readObject(jsonToFile(tempDir, json2))) {
            assertNotNull(model);
            assertEquals(LocalDateTime.ofEpochSecond(epoch/1000, (int)(epoch%1000), ZoneOffset.UTC)
                            .toLocalDate(), model.getWhatever());
        }
    }

    @Test
    void testJsonFormatLocalTimeShapeNumber(@TempDir Path tempDir) throws IOException, LarJsonException {
        String dateFormat = "HH_-_mm_-_ss_-_SSS";
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedLocalTimeShapeNumber> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedLocalTimeShapeNumber.class,
                        new LarJsonTypedReadConfiguration.Builder().enableJsonFormatAnnotation()
                                .setLocalTimeFormat(dateFormat).build());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        String date = formatter.format(LocalTime.now());
        long epoch = System.currentTimeMillis();
        String json1 = "{\"whatever\" : \"" + date + "\"}";
        String json2 = "{\"whatever\" : \"" + epoch + "\"}";
        try(ModelWithJsonFormatAnnotatedLocalTimeShapeNumber model =
                    mapper.readObject(jsonToFile(tempDir, json1))) {
            assertNotNull(model);
            assertEquals(LocalTime.parse(date, formatter), model.getWhatever());
        }
        try(ModelWithJsonFormatAnnotatedLocalTimeShapeNumber model =
                    mapper.readObject(jsonToFile(tempDir, json2))) {
            assertNotNull(model);
            try {
                model.getWhatever();
                fail();
            } catch (LarJsonValueReadException expected){
            }
        }
    }

    @Test
    void testJsonFormatZonedDateTimeShapeNumber(@TempDir Path tempDir) throws IOException, LarJsonException {
        String dateFormat = "yyyy_-_MM_-_dd_-_HH_-_mm_-_ss_-_SSS_-_Z";
        LarJsonTypedMapper<ModelWithJsonFormatAnnotatedZonedDateTimeShapeNumber> mapper =
                new LarJsonTypedMapper<>(ModelWithJsonFormatAnnotatedZonedDateTimeShapeNumber.class,
                        new LarJsonTypedReadConfiguration.Builder().enableJsonFormatAnnotation()
                                .setZonedDateTimeFormat(dateFormat).build());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        String date = formatter.format(ZonedDateTime.now());
        long epoch = System.currentTimeMillis();
        String json1 = "{\"whatever\" : \"" + date + "\"}";
        String json2 = "{\"whatever\" : \"" + epoch + "\"}";
        try(ModelWithJsonFormatAnnotatedZonedDateTimeShapeNumber model =
                    mapper.readObject(jsonToFile(tempDir, json1))) {
            assertNotNull(model);
            assertEquals(ZonedDateTime.parse(date, formatter), model.getWhatever());
        }
        try(ModelWithJsonFormatAnnotatedZonedDateTimeShapeNumber model =
                    mapper.readObject(jsonToFile(tempDir, json2))) {
            assertNotNull(model);
            try {
                model.getWhatever();
                fail();
            } catch (LarJsonValueReadException expected){
            }
        }
    }
}
