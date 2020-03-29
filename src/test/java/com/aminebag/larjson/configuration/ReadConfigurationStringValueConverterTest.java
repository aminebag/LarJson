package com.aminebag.larjson.configuration;

import com.aminebag.larjson.valueconverter.StringValueConverter;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Amine Bagdouri
 */
public class ReadConfigurationStringValueConverterTest extends StringValueConverterTest {

    @Override
    protected StringValueConverterFactory getFactoryDefault() {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder().build();
        return configuration.getStringValueConverterFactory();
    }

    @Override
    protected StringValueConverterFactory getFactoryCustomDateFormat(String format) {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .setDateFormat(format).build();
        return configuration.getStringValueConverterFactory();
    }

    @Override
    protected StringValueConverterFactory getFactoryCustomLocalDateTimeFormat(String format) {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .setLocalDateTimeFormat(format).build();
        return configuration.getStringValueConverterFactory();
    }

    @Override
    protected StringValueConverterFactory getFactoryCustomLocalDateTimeFormatter(DateTimeFormatter formatter) {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .setLocalDateTimeFormatter(formatter).build();
        return configuration.getStringValueConverterFactory();
    }

    @Override
    protected StringValueConverterFactory getFactoryCustomLocalDateFormat(String format) {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .setLocalDateFormat(format).build();
        return configuration.getStringValueConverterFactory();
    }

    @Override
    protected StringValueConverterFactory getFactoryCustomLocalDateFormatter(DateTimeFormatter formatter) {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .setLocalDateFormatter(formatter).build();
        return configuration.getStringValueConverterFactory();
    }

    @Override
    protected StringValueConverterFactory getFactoryCustomLocalTimeFormat(String format) {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .setLocalTimeFormat(format).build();
        return configuration.getStringValueConverterFactory();
    }

    @Override
    protected StringValueConverterFactory getFactoryCustomLocalTimeFormatter(DateTimeFormatter formatter) {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .setLocalTimeFormatter(formatter).build();
        return configuration.getStringValueConverterFactory();
    }

    @Override
    protected StringValueConverterFactory getFactoryCustomZonedDateTimeFormat(String format) {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .setZonedDateTimeFormat(format).build();
        return configuration.getStringValueConverterFactory();
    }

    @Override
    protected StringValueConverterFactory getFactoryCustomZonedDateTimeFormatter(DateTimeFormatter formatter) {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .setZonedDateTimeFormatter(formatter).build();
        return configuration.getStringValueConverterFactory();
    }

    @Test
    void testCustomDateValueConverter() {
        StringValueConverter<Date> converter = new StringValueConverter<Date>() {
            @Override
            public Date fromString(String s) {
                return null;
            }

            @Override
            public String toString(Date value) {
                return null;
            }
        };
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .setStringValueConverter(Date.class, converter)
                .setDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z")
                .setStringValueConverter(Date.class, converter).build();
        assertEquals(converter, configuration.getStringValueConverterFactory().get(Date.class));
    }

    @Test
    void testCustomLocalDateTimeValueConverter() {
        StringValueConverter<LocalDateTime> converter = new StringValueConverter<LocalDateTime>() {
            @Override
            public LocalDateTime fromString(String s) {
                return null;
            }

            @Override
            public String toString(LocalDateTime value) {
                return null;
            }
        };
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .setStringValueConverter(LocalDateTime.class, converter)
                .setLocalDateTimeFormat("yyyy.MM.dd G 'at' HH:mm:ss")
                .setLocalDateTimeFormatter(DateTimeFormatter.ofPattern("yyyy.MM.dd G 'at' HH:mm:ss"))
                .setStringValueConverter(LocalDateTime.class, converter).build();
        assertEquals(converter, configuration.getStringValueConverterFactory().get(LocalDateTime.class));
    }

    @Test
    void testCustomLocalDateValueConverter() {
        StringValueConverter<LocalDate> converter = new StringValueConverter<LocalDate>() {
            @Override
            public LocalDate fromString(String s) {
                return null;
            }

            @Override
            public String toString(LocalDate value) {
                return null;
            }
        };
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .setStringValueConverter(LocalDate.class, converter)
                .setLocalDateFormat("yyyy.MM.dd G 'at' HH:mm:ss")
                .setLocalDateFormatter(DateTimeFormatter.ofPattern("yyyy.MM.dd G 'at' HH:mm:ss"))
                .setStringValueConverter(LocalDate.class, converter).build();
        assertEquals(converter, configuration.getStringValueConverterFactory().get(LocalDate.class));
    }

    @Test
    void testCustomLocalTimeValueConverter() {
        StringValueConverter<LocalTime> converter = new StringValueConverter<LocalTime>() {
            @Override
            public LocalTime fromString(String s) {
                return null;
            }

            @Override
            public String toString(LocalTime value) {
                return null;
            }
        };
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .setStringValueConverter(LocalTime.class, converter)
                .setLocalTimeFormat("yyyy.MM.dd G 'at' HH:mm:ss")
                .setLocalTimeFormatter(DateTimeFormatter.ofPattern("yyyy.MM.dd G 'at' HH:mm:ss"))
                .setStringValueConverter(LocalTime.class, converter).build();
        assertEquals(converter, configuration.getStringValueConverterFactory().get(LocalTime.class));
    }

    @Test
    void testCustomZonedDateTimeValueConverter() {
        StringValueConverter<ZonedDateTime> converter = new StringValueConverter<ZonedDateTime>() {
            @Override
            public ZonedDateTime fromString(String s) {
                return null;
            }

            @Override
            public String toString(ZonedDateTime value) {
                return null;
            }
        };
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder()
                .setStringValueConverter(ZonedDateTime.class, converter)
                .setZonedDateTimeFormat("yyyy.MM.dd G 'at' HH:mm:ss")
                .setZonedDateTimeFormatter(DateTimeFormatter.ofPattern("yyyy.MM.dd G 'at' HH:mm:ss"))
                .setStringValueConverter(ZonedDateTime.class, converter).build();
        assertEquals(converter, configuration.getStringValueConverterFactory().get(ZonedDateTime.class));
    }
}
