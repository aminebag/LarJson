package com.aminebag.larjson.configuration;

import com.aminebag.larjson.exception.LarJsonConversionException;
import com.aminebag.larjson.valueconverter.StringValueConverter;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Amine Bagdouri
 */
abstract class StringValueConverterTest {

    protected abstract StringValueConverterFactory getFactoryDefault();

    protected abstract StringValueConverterFactory getFactoryCustomDateFormat(String format);

    protected abstract StringValueConverterFactory getFactoryCustomLocalDateTimeFormat(String format);

    protected abstract StringValueConverterFactory getFactoryCustomLocalDateTimeFormatter(DateTimeFormatter formatter);

    protected abstract StringValueConverterFactory getFactoryCustomLocalDateFormat(String format);

    protected abstract StringValueConverterFactory getFactoryCustomLocalDateFormatter(DateTimeFormatter formatter);

    protected abstract StringValueConverterFactory getFactoryCustomLocalTimeFormat(String format);

    protected abstract StringValueConverterFactory getFactoryCustomLocalTimeFormatter(DateTimeFormatter formatter);

    protected abstract StringValueConverterFactory getFactoryCustomZonedDateTimeFormat(String format);

    protected abstract StringValueConverterFactory getFactoryCustomZonedDateTimeFormatter(DateTimeFormatter formatter);

    @Test
    void testDefaultDateValueConverterFromString() throws LarJsonConversionException, ParseException {
        StringValueConverterFactory factory = getFactoryDefault();
        StringValueConverter<Date> converter = factory.get(Date.class);
        assertNotNull(converter);
        SimpleDateFormat formatter = new SimpleDateFormat();
        Date now = new Date();
        String date = formatter.format(now);
        assertEquals(formatter.parse(date), converter.fromString(date));
    }

    @Test
    void testDefaultDateValueConverterFromStringNull() throws LarJsonConversionException {
        StringValueConverterFactory factory = getFactoryDefault();
        StringValueConverter<Date> converter = factory.get(Date.class);
        assertNotNull(converter);
        assertEquals(null, converter.fromString(null));
    }

    @Test
    void testDefaultDateValueConverterFromStringInvalid() {
        StringValueConverterFactory factory = getFactoryDefault();
        StringValueConverter<Date> converter = factory.get(Date.class);
        assertNotNull(converter);
        try {
            converter.fromString("hello");
            fail();
        } catch (LarJsonConversionException expected) {
        }
    }

    @Test
    void testDefaultDateValueConverterToString() {
        StringValueConverterFactory factory = getFactoryDefault();
        StringValueConverter<Date> converter = factory.get(Date.class);
        assertNotNull(converter);
        SimpleDateFormat formatter = new SimpleDateFormat();
        Date now = new Date();
        String date = formatter.format(now);
        assertEquals(date, converter.toString(now));
    }

    @Test
    void testDefaultDateValueConverterToStringNull() {
        StringValueConverterFactory factory = getFactoryDefault();
        StringValueConverter<Date> converter = factory.get(Date.class);
        assertNotNull(converter);
        try {
            converter.toString(null);
            fail();
        } catch (NullPointerException expected) {
        }
    }

    @Test
    void testCustomFormatDateValueConverterFromString() throws ParseException, LarJsonConversionException {
        String format = "yyyy.MM.dd G 'at' HH:mm:ss z";
        StringValueConverterFactory factory = getFactoryCustomDateFormat(format);
        StringValueConverter<Date> converter = factory.get(Date.class);
        assertNotNull(converter);
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date now = new Date();
        String date = formatter.format(now);
        assertEquals(formatter.parse(date), converter.fromString(date));
    }

    @Test
    void testCustomFormatDateValueConverterFromStringNull() throws LarJsonConversionException {
        String format = "yyyy.MM.dd G 'at' HH:mm:ss z";
        StringValueConverterFactory factory = getFactoryCustomDateFormat(format);
        StringValueConverter<Date> converter = factory.get(Date.class);
        assertNotNull(converter);
        assertEquals(null, converter.fromString(null));
    }

    @Test
    void testCustomFormatDateValueConverterFromStringInvalid() {
        String format = "yyyy.MM.dd G 'at' HH:mm:ss z";
        StringValueConverterFactory factory = getFactoryCustomDateFormat(format);
        StringValueConverter<Date> converter = factory.get(Date.class);
        assertNotNull(converter);
        try {
            converter.fromString("hello");
            fail();
        } catch (LarJsonConversionException expected) {
        }
    }

    @Test
    void testCustomFormatDateValueConverterToString() {
        String format = "yyyy.MM.dd G 'at' HH:mm:ss z";
        StringValueConverterFactory factory = getFactoryCustomDateFormat(format);
        StringValueConverter<Date> converter = factory.get(Date.class);
        assertNotNull(converter);
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date now = new Date();
        String date = formatter.format(now);
        assertEquals(date, converter.toString(now));
    }

    @Test
    void testCustomFormatDateValueConverterToStringNull() {
        String format = "yyyy.MM.dd G 'at' HH:mm:ss z";
        StringValueConverterFactory factory = getFactoryCustomDateFormat(format);
        StringValueConverter<Date> converter = factory.get(Date.class);
        assertNotNull(converter);
        try {
            converter.toString(null);
            fail();
        } catch (NullPointerException expected) {
        }
    }

    @Test
    void testDefaultLocalDateTimeValueConverterFromString() throws LarJsonConversionException {
        StringValueConverterFactory factory = getFactoryDefault();
        StringValueConverter<?> converter = factory.get(LocalDateTime.class);
        assertNotNull(converter);
        LocalDateTime now = LocalDateTime.now();
        assertEquals(now, converter.fromString(now.toString()));
    }

    @Test
    void testDefaultLocalDateTimeValueConverterFromStringNull() throws LarJsonConversionException {
        StringValueConverterFactory factory = getFactoryDefault();
        StringValueConverter<?> converter = factory.get(LocalDateTime.class);
        assertNotNull(converter);
        assertEquals(null, converter.fromString(null));
    }

    @Test
    void testDefaultLocalDateTimeValueConverterFromStringInvalid() {
        StringValueConverterFactory factory = getFactoryDefault();
        StringValueConverter<?> converter = factory.get(LocalDateTime.class);
        assertNotNull(converter);
        try {
            converter.fromString("hello");
            fail();
        } catch (LarJsonConversionException expected) {
        }
    }

    @Test
    void testDefaultLocalDateTimeValueConverterToString() {
        StringValueConverterFactory factory = getFactoryDefault();
        StringValueConverter<LocalDateTime> converter = factory.get(LocalDateTime.class);
        assertNotNull(converter);
        LocalDateTime now = LocalDateTime.now();
        assertEquals(now.toString(), converter.toString(now));
    }

    @Test
    void testDefaultLocalDateTimeValueConverterToStringNull() {
        StringValueConverterFactory factory = getFactoryDefault();
        StringValueConverter<?> converter = factory.get(LocalDateTime.class);
        assertNotNull(converter);
        try {
            converter.toString(null);
            fail();
        } catch (NullPointerException expected) {
        }
    }

    @Test
    void testCustomFormatLocalDateTimeValueConverterFromString() throws LarJsonConversionException {
        String format = "yyyy.MM.dd G 'at' HH:mm:ss";
        StringValueConverterFactory factory = getFactoryCustomLocalDateTimeFormat(format);
        StringValueConverter<?> converter = factory.get(LocalDateTime.class);
        assertNotNull(converter);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDateTime now = LocalDateTime.now();
        String date = formatter.format(now);
        assertEquals(LocalDateTime.parse(date, formatter), converter.fromString(date));
    }

    @Test
    void testCustomFormatLocalDateTimeValueConverterFromStringNull() throws LarJsonConversionException {
        String format = "yyyy.MM.dd G 'at' HH:mm:ss";
        StringValueConverterFactory factory = getFactoryCustomLocalDateTimeFormat(format);
        StringValueConverter<?> converter = factory.get(LocalDateTime.class);
        assertNotNull(converter);
        assertEquals(null, converter.fromString(null));
    }

    @Test
    void testCustomFormatLocalDateTimeValueConverterFromStringInvalid() {
        String format = "yyyy.MM.dd G 'at' HH:mm:ss";
        StringValueConverterFactory factory = getFactoryCustomLocalDateTimeFormat(format);
        StringValueConverter<?> converter = factory.get(LocalDateTime.class);
        assertNotNull(converter);
        try {
            converter.fromString("hello");
            fail();
        } catch (LarJsonConversionException expected) {
        }
    }

    @Test
    void testCustomFormatLocalDateTimeValueConverterToString() {
        String format = "yyyy.MM.dd G 'at' HH:mm:ss";
        StringValueConverterFactory factory = getFactoryCustomLocalDateTimeFormat(format);
        StringValueConverter<LocalDateTime> converter = factory.get(LocalDateTime.class);
        assertNotNull(converter);
        LocalDateTime now = LocalDateTime.now();
        assertEquals(DateTimeFormatter.ofPattern(format).format(now), converter.toString(now));
    }

    @Test
    void testCustomFormatLocalDateTimeValueConverterToStringNull() {
        String format = "yyyy.MM.dd G 'at' HH:mm:ss";
        StringValueConverterFactory factory = getFactoryCustomLocalDateTimeFormat(format);
        StringValueConverter<?> converter = factory.get(LocalDateTime.class);
        assertNotNull(converter);
        try {
            converter.toString(null);
            fail();
        } catch (NullPointerException expected) {
        }
    }

    @Test
    void testCustomFormatterLocalDateTimeValueConverterFromString() throws LarJsonConversionException {
        String format = "yyyy.MM.dd G 'at' HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        StringValueConverterFactory factory = getFactoryCustomLocalDateTimeFormatter(formatter);
        StringValueConverter<?> converter = factory.get(LocalDateTime.class);
        assertNotNull(converter);
        LocalDateTime now = LocalDateTime.now();
        String date = formatter.format(now);
        assertEquals(LocalDateTime.parse(date, formatter), converter.fromString(date));
    }

    @Test
    void testCustomFormatterLocalDateTimeValueConverterFromStringNull() throws LarJsonConversionException {
        String format = "yyyy.MM.dd G 'at' HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        StringValueConverterFactory factory = getFactoryCustomLocalDateTimeFormatter(formatter);
        StringValueConverter<?> converter = factory.get(LocalDateTime.class);
        assertNotNull(converter);
        assertEquals(null, converter.fromString(null));
    }

    @Test
    void testCustomFormatterLocalDateTimeValueConverterFromStringInvalid() {
        String format = "yyyy.MM.dd G 'at' HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        StringValueConverterFactory factory = getFactoryCustomLocalDateTimeFormatter(formatter);
        StringValueConverter<?> converter = factory.get(LocalDateTime.class);
        assertNotNull(converter);
        try {
            converter.fromString("hello");
            fail();
        } catch (LarJsonConversionException expected) {
        }
    }

    @Test
    void testCustomFormatterLocalDateTimeValueConverterToString() {
        String format = "yyyy.MM.dd G 'at' HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        StringValueConverterFactory factory = getFactoryCustomLocalDateTimeFormatter(formatter);
        StringValueConverter<LocalDateTime> converter = factory.get(LocalDateTime.class);
        assertNotNull(converter);
        LocalDateTime now = LocalDateTime.now();
        assertEquals(formatter.format(now), converter.toString(now));
    }

    @Test
    void testCustomFormatterLocalDateTimeValueConverterToStringNull() {
        String format = "yyyy.MM.dd G 'at' HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        StringValueConverterFactory factory = getFactoryCustomLocalDateTimeFormatter(formatter);
        StringValueConverter<?> converter = factory.get(LocalDateTime.class);
        assertNotNull(converter);
        try {
            converter.toString(null);
            fail();
        } catch (NullPointerException expected) {
        }
    }

    @Test
    void testDefaultLocalDateValueConverterFromString() throws LarJsonConversionException {
        StringValueConverterFactory factory = getFactoryDefault();
        StringValueConverter<?> converter = factory.get(LocalDate.class);
        assertNotNull(converter);
        LocalDate now = LocalDate.now();
        assertEquals(now, converter.fromString(now.toString()));
    }

    @Test
    void testDefaultLocalDateValueConverterFromStringNull() throws LarJsonConversionException {
        StringValueConverterFactory factory = getFactoryDefault();
        StringValueConverter<?> converter = factory.get(LocalDate.class);
        assertNotNull(converter);
        assertEquals(null, converter.fromString(null));
    }

    @Test
    void testDefaultLocalDateValueConverterFromStringInvalid() {
        StringValueConverterFactory factory = getFactoryDefault();
        StringValueConverter<?> converter = factory.get(LocalDate.class);
        assertNotNull(converter);
        try {
            converter.fromString("hello");
            fail();
        } catch (LarJsonConversionException expected) {
        }
    }

    @Test
    void testDefaultLocalDateValueConverterToString() {
        StringValueConverterFactory factory = getFactoryDefault();
        StringValueConverter<LocalDate> converter = factory.get(LocalDate.class);
        assertNotNull(converter);
        LocalDate now = LocalDate.now();
        assertEquals(now.toString(), converter.toString(now));
    }

    @Test
    void testDefaultLocalDateValueConverterToStringNull() {
        StringValueConverterFactory factory = getFactoryDefault();
        StringValueConverter<?> converter = factory.get(LocalDate.class);
        assertNotNull(converter);
        try {
            converter.toString(null);
            fail();
        } catch (NullPointerException expected) {
        }
    }

    @Test
    void testCustomFormatLocalDateValueConverterFromString() throws LarJsonConversionException {
        String format = "yyyy.MM.dd G";
        StringValueConverterFactory factory = getFactoryCustomLocalDateFormat(format);
        StringValueConverter<?> converter = factory.get(LocalDate.class);
        assertNotNull(converter);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDate now = LocalDate.now();
        String date = formatter.format(now);
        assertEquals(LocalDate.parse(date, formatter), converter.fromString(date));
    }

    @Test
    void testCustomFormatLocalDateValueConverterFromStringNull() throws LarJsonConversionException {
        String format = "yyyy.MM.dd G";
        StringValueConverterFactory factory = getFactoryCustomLocalDateFormat(format);
        StringValueConverter<?> converter = factory.get(LocalDate.class);
        assertNotNull(converter);
        assertEquals(null, converter.fromString(null));
    }

    @Test
    void testCustomFormatLocalDateValueConverterFromStringInvalid() {
        String format = "yyyy.MM.dd G";
        StringValueConverterFactory factory = getFactoryCustomLocalDateFormat(format);
        StringValueConverter<?> converter = factory.get(LocalDate.class);
        assertNotNull(converter);
        try {
            converter.fromString("hello");
            fail();
        } catch (LarJsonConversionException expected) {
        }
    }

    @Test
    void testCustomFormatLocalDateValueConverterToString() {
        String format = "yyyy.MM.dd G";
        StringValueConverterFactory factory = getFactoryCustomLocalDateFormat(format);
        StringValueConverter<LocalDate> converter = factory.get(LocalDate.class);
        assertNotNull(converter);
        LocalDate now = LocalDate.now();
        assertEquals(DateTimeFormatter.ofPattern(format).format(now), converter.toString(now));
    }

    @Test
    void testCustomFormatLocalDateValueConverterToStringNull() {
        String format = "yyyy.MM.dd G";
        StringValueConverterFactory factory = getFactoryCustomLocalDateFormat(format);
        StringValueConverter<?> converter = factory.get(LocalDate.class);
        assertNotNull(converter);
        try {
            converter.toString(null);
            fail();
        } catch (NullPointerException expected) {
        }
    }

    @Test
    void testCustomFormatterLocalDateValueConverterFromString() throws LarJsonConversionException {
        String format = "yyyy.MM.dd G";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        StringValueConverterFactory factory = getFactoryCustomLocalDateFormatter(formatter);
        StringValueConverter<?> converter = factory.get(LocalDate.class);
        assertNotNull(converter);
        LocalDate now = LocalDate.now();
        String date = formatter.format(now);
        assertEquals(LocalDate.parse(date, formatter), converter.fromString(date));
    }

    @Test
    void testCustomFormatterLocalDateValueConverterFromStringNull() throws LarJsonConversionException {
        String format = "yyyy.MM.dd G";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        StringValueConverterFactory factory = getFactoryCustomLocalDateFormatter(formatter);
        StringValueConverter<?> converter = factory.get(LocalDate.class);
        assertNotNull(converter);
        assertEquals(null, converter.fromString(null));
    }

    @Test
    void testCustomFormatterLocalDateValueConverterFromStringInvalid() {
        String format = "yyyy.MM.dd G";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        StringValueConverterFactory factory = getFactoryCustomLocalDateFormatter(formatter);
        StringValueConverter<?> converter = factory.get(LocalDate.class);
        assertNotNull(converter);
        try {
            converter.fromString("hello");
            fail();
        } catch (LarJsonConversionException expected) {
        }
    }

    @Test
    void testCustomFormatterLocalDateValueConverterToString() {
        String format = "yyyy.MM.dd G";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        StringValueConverterFactory factory = getFactoryCustomLocalDateFormatter(formatter);
        StringValueConverter<LocalDate> converter = factory.get(LocalDate.class);
        assertNotNull(converter);
        LocalDate now = LocalDate.now();
        assertEquals(formatter.format(now), converter.toString(now));
    }

    @Test
    void testCustomFormatterLocalDateValueConverterToStringNull() {
        String format = "yyyy.MM.dd G";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        StringValueConverterFactory factory = getFactoryCustomLocalDateFormatter(formatter);
        StringValueConverter<?> converter = factory.get(LocalDate.class);
        assertNotNull(converter);
        try {
            converter.toString(null);
            fail();
        } catch (NullPointerException expected) {
        }
    }

    @Test
    void testDefaultLocalTimeValueConverterFromString() throws LarJsonConversionException {
        StringValueConverterFactory factory = getFactoryDefault();
        StringValueConverter<?> converter = factory.get(LocalTime.class);
        assertNotNull(converter);
        LocalTime now = LocalTime.now();
        assertEquals(now, converter.fromString(now.toString()));
    }

    @Test
    void testDefaultLocalTimeValueConverterFromStringNull() throws LarJsonConversionException {
        StringValueConverterFactory factory = getFactoryDefault();
        StringValueConverter<?> converter = factory.get(LocalTime.class);
        assertNotNull(converter);
        assertEquals(null, converter.fromString(null));
    }

    @Test
    void testDefaultLocalTimeValueConverterFromStringInvalid() {
        StringValueConverterFactory factory = getFactoryDefault();
        StringValueConverter<?> converter = factory.get(LocalTime.class);
        assertNotNull(converter);
        try {
            converter.fromString("hello");
            fail();
        } catch (LarJsonConversionException expected) {
        }
    }

    @Test
    void testDefaultLocalTimeValueConverterToString() {
        StringValueConverterFactory factory = getFactoryDefault();
        StringValueConverter<LocalTime> converter = factory.get(LocalTime.class);
        assertNotNull(converter);
        LocalTime now = LocalTime.now();
        assertEquals(now.toString(), converter.toString(now));
    }

    @Test
    void testDefaultLocalTimeValueConverterToStringNull() {
        StringValueConverterFactory factory = getFactoryDefault();
        StringValueConverter<?> converter = factory.get(LocalTime.class);
        assertNotNull(converter);
        try {
            converter.toString(null);
            fail();
        } catch (NullPointerException expected) {
        }
    }

    @Test
    void testCustomFormatLocalTimeValueConverterFromString() throws LarJsonConversionException {
        String format = "HH_mm_ss__SSS";
        StringValueConverterFactory factory = getFactoryCustomLocalTimeFormat(format);
        StringValueConverter<?> converter = factory.get(LocalTime.class);
        assertNotNull(converter);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalTime now = LocalTime.now();
        String date = formatter.format(now);
        assertEquals(LocalTime.parse(date, formatter), converter.fromString(date));
    }

    @Test
    void testCustomFormatLocalTimeValueConverterFromStringNull() throws LarJsonConversionException {
        String format = "HH_mm_ss__SSS";
        StringValueConverterFactory factory = getFactoryCustomLocalTimeFormat(format);
        StringValueConverter<?> converter = factory.get(LocalTime.class);
        assertNotNull(converter);
        assertEquals(null, converter.fromString(null));
    }

    @Test
    void testCustomFormatLocalTimeValueConverterFromStringInvalid() {
        String format = "HH_mm_ss__SSS";
        StringValueConverterFactory factory = getFactoryCustomLocalTimeFormat(format);
        StringValueConverter<?> converter = factory.get(LocalTime.class);
        assertNotNull(converter);
        try {
            converter.fromString("hello");
            fail();
        } catch (LarJsonConversionException expected) {
        }
    }

    @Test
    void testCustomFormatLocalTimeValueConverterToString() {
        String format = "HH_mm_ss__SSS";
        StringValueConverterFactory factory = getFactoryCustomLocalTimeFormat(format);
        StringValueConverter<LocalTime> converter = factory.get(LocalTime.class);
        assertNotNull(converter);
        LocalTime now = LocalTime.now();
        assertEquals(DateTimeFormatter.ofPattern(format).format(now), converter.toString(now));
    }

    @Test
    void testCustomFormatLocalTimeValueConverterToStringNull() {
        String format = "HH_mm_ss__SSS";
        StringValueConverterFactory factory = getFactoryCustomLocalTimeFormat(format);
        StringValueConverter<?> converter = factory.get(LocalTime.class);
        assertNotNull(converter);
        try {
            converter.toString(null);
            fail();
        } catch (NullPointerException expected) {
        }
    }

    @Test
    void testCustomFormatterLocalTimeValueConverterFromString() throws LarJsonConversionException {
        String format = "HH_mm_ss__SSS";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        StringValueConverterFactory factory = getFactoryCustomLocalTimeFormatter(formatter);
        StringValueConverter<?> converter = factory.get(LocalTime.class);
        assertNotNull(converter);
        LocalTime now = LocalTime.now();
        String date = formatter.format(now);
        assertEquals(LocalTime.parse(date, formatter), converter.fromString(date));
    }

    @Test
    void testCustomFormatterLocalTimeValueConverterFromStringNull() throws LarJsonConversionException {
        String format = "HH_mm_ss__SSS";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        StringValueConverterFactory factory = getFactoryCustomLocalTimeFormatter(formatter);
        StringValueConverter<?> converter = factory.get(LocalTime.class);
        assertNotNull(converter);
        assertEquals(null, converter.fromString(null));
    }

    @Test
    void testCustomFormatterLocalTimeValueConverterFromStringInvalid() {
        String format = "HH_mm_ss__SSS";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        StringValueConverterFactory factory = getFactoryCustomLocalTimeFormatter(formatter);
        StringValueConverter<?> converter = factory.get(LocalTime.class);
        assertNotNull(converter);
        try {
            converter.fromString("hello");
            fail();
        } catch (LarJsonConversionException expected) {
        }
    }

    @Test
    void testCustomFormatterLocalTimeValueConverterToString() {
        String format = "HH_mm_ss__SSS";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        StringValueConverterFactory factory = getFactoryCustomLocalTimeFormatter(formatter);
        StringValueConverter<LocalTime> converter = factory.get(LocalTime.class);
        assertNotNull(converter);
        LocalTime now = LocalTime.now();
        assertEquals(formatter.format(now), converter.toString(now));
    }

    @Test
    void testCustomFormatterLocalTimeValueConverterToStringNull() {
        String format = "HH_mm_ss__SSS";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        StringValueConverterFactory factory = getFactoryCustomLocalTimeFormatter(formatter);
        StringValueConverter<?> converter = factory.get(LocalTime.class);
        assertNotNull(converter);
        try {
            converter.toString(null);
            fail();
        } catch (NullPointerException expected) {
        }
    }

    @Test
    void testDefaultZonedDateTimeValueConverterFromString() throws LarJsonConversionException {
        StringValueConverterFactory factory = getFactoryDefault();
        StringValueConverter<?> converter = factory.get(ZonedDateTime.class);
        assertNotNull(converter);
        ZonedDateTime now = ZonedDateTime.now();
        assertEquals(now, converter.fromString(now.toString()));
    }

    @Test
    void testDefaultZonedDateTimeValueConverterFromStringNull() throws LarJsonConversionException {
        StringValueConverterFactory factory = getFactoryDefault();
        StringValueConverter<?> converter = factory.get(ZonedDateTime.class);
        assertNotNull(converter);
        assertEquals(null, converter.fromString(null));
    }

    @Test
    void testDefaultZonedDateTimeValueConverterFromStringInvalid() {
        StringValueConverterFactory factory = getFactoryDefault();
        StringValueConverter<?> converter = factory.get(ZonedDateTime.class);
        assertNotNull(converter);
        try {
            converter.fromString("hello");
            fail();
        } catch (LarJsonConversionException expected) {
        }
    }

    @Test
    void testDefaultZonedDateTimeValueConverterToString() {
        StringValueConverterFactory factory = getFactoryDefault();
        StringValueConverter<ZonedDateTime> converter = factory.get(ZonedDateTime.class);
        assertNotNull(converter);
        ZonedDateTime now = ZonedDateTime.now();
        assertEquals(now.toString(), converter.toString(now));
    }

    @Test
    void testDefaultZonedDateTimeValueConverterToStringNull() {
        StringValueConverterFactory factory = getFactoryDefault();
        StringValueConverter<?> converter = factory.get(ZonedDateTime.class);
        assertNotNull(converter);
        try {
            converter.toString(null);
            fail();
        } catch (NullPointerException expected) {
        }
    }

    @Test
    void testCustomFormatZonedDateTimeValueConverterFromString() throws LarJsonConversionException {
        String format = "yyyy.MM.dd G 'at' HH:mm:ss Z";
        StringValueConverterFactory factory = getFactoryCustomZonedDateTimeFormat(format);
        StringValueConverter<?> converter = factory.get(ZonedDateTime.class);
        assertNotNull(converter);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        ZonedDateTime now = ZonedDateTime.now();
        String date = formatter.format(now);
        assertEquals(ZonedDateTime.parse(date, formatter), converter.fromString(date));
    }

    @Test
    void testCustomFormatZonedDateTimeValueConverterFromStringNull() throws LarJsonConversionException {
        String format = "yyyy.MM.dd G 'at' HH:mm:ss Z";
        StringValueConverterFactory factory = getFactoryCustomZonedDateTimeFormat(format);
        StringValueConverter<?> converter = factory.get(ZonedDateTime.class);
        assertNotNull(converter);
        assertEquals(null, converter.fromString(null));
    }

    @Test
    void testCustomFormatZonedDateTimeValueConverterFromStringInvalid() {
        String format = "yyyy.MM.dd G 'at' HH:mm:ss Z";
        StringValueConverterFactory factory = getFactoryCustomZonedDateTimeFormat(format);
        StringValueConverter<?> converter = factory.get(ZonedDateTime.class);
        assertNotNull(converter);
        try {
            converter.fromString("hello");
            fail();
        } catch (LarJsonConversionException expected) {
        }
    }

    @Test
    void testCustomFormatZonedDateTimeValueConverterToString() {
        String format = "yyyy.MM.dd G 'at' HH:mm:ss Z";
        StringValueConverterFactory factory = getFactoryCustomZonedDateTimeFormat(format);
        StringValueConverter<ZonedDateTime> converter = factory.get(ZonedDateTime.class);
        assertNotNull(converter);
        ZonedDateTime now = ZonedDateTime.now();
        assertEquals(DateTimeFormatter.ofPattern(format).format(now), converter.toString(now));
    }

    @Test
    void testCustomFormatZonedDateTimeValueConverterToStringNull() {
        String format = "yyyy.MM.dd G 'at' HH:mm:ss Z";
        StringValueConverterFactory factory = getFactoryCustomZonedDateTimeFormat(format);
        StringValueConverter<?> converter = factory.get(ZonedDateTime.class);
        assertNotNull(converter);
        try {
            converter.toString(null);
            fail();
        } catch (NullPointerException expected) {
        }
    }

    @Test
    void testCustomFormatterZonedDateTimeValueConverterFromString() throws LarJsonConversionException {
        String format = "yyyy.MM.dd G 'at' HH:mm:ss Z";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        StringValueConverterFactory factory = getFactoryCustomZonedDateTimeFormatter(formatter);
        StringValueConverter<?> converter = factory.get(ZonedDateTime.class);
        assertNotNull(converter);
        ZonedDateTime now = ZonedDateTime.now();
        String date = formatter.format(now);
        assertEquals(ZonedDateTime.parse(date, formatter), converter.fromString(date));
    }

    @Test
    void testCustomFormatterZonedDateTimeValueConverterFromStringNull() throws LarJsonConversionException {
        String format = "yyyy.MM.dd G 'at' HH:mm:ss Z";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        StringValueConverterFactory factory = getFactoryCustomZonedDateTimeFormatter(formatter);
        StringValueConverter<?> converter = factory.get(ZonedDateTime.class);
        assertNotNull(converter);
        assertEquals(null, converter.fromString(null));
    }

    @Test
    void testCustomFormatterZonedDateTimeValueConverterFromStringInvalid() {
        String format = "yyyy.MM.dd G 'at' HH:mm:ss Z";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        StringValueConverterFactory factory = getFactoryCustomZonedDateTimeFormatter(formatter);
        StringValueConverter<?> converter = factory.get(ZonedDateTime.class);
        assertNotNull(converter);
        try {
            converter.fromString("hello");
            fail();
        } catch (LarJsonConversionException expected) {
        }
    }

    @Test
    void testCustomFormatterZonedDateTimeValueConverterToString() {
        String format = "yyyy.MM.dd G 'at' HH:mm:ss Z";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        StringValueConverterFactory factory = getFactoryCustomZonedDateTimeFormatter(formatter);
        StringValueConverter<ZonedDateTime> converter = factory.get(ZonedDateTime.class);
        assertNotNull(converter);
        ZonedDateTime now = ZonedDateTime.now();
        assertEquals(formatter.format(now), converter.toString(now));
    }

    @Test
    void testCustomFormatterZonedDateTimeValueConverterToStringNull() {
        String format = "yyyy.MM.dd G 'at' HH:mm:ss Z";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        StringValueConverterFactory factory = getFactoryCustomZonedDateTimeFormatter(formatter);
        StringValueConverter<?> converter = factory.get(ZonedDateTime.class);
        assertNotNull(converter);
        try {
            converter.toString(null);
            fail();
        } catch (NullPointerException expected) {
        }
    }
}
