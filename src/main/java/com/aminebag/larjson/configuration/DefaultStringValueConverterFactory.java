package com.aminebag.larjson.configuration;

import com.aminebag.larjson.valueconverter.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Amine Bagdouri
 */
class DefaultStringValueConverterFactory implements StringValueConverterFactory {

    private final Map<Class<?>, StringValueConverter> converters = new HashMap<>();

    DefaultStringValueConverterFactory() {
        converters.put(Date.class, new DateValueConverter(SimpleDateFormat::new));
        converters.put(LocalDateTime.class, new LocalDateTimeValueConverter(null));
        converters.put(LocalDate.class, new LocalDateValueConverter(null));
        converters.put(LocalTime.class, new LocalTimeValueConverter(null));
        converters.put(ZonedDateTime.class, new ZonedDateTimeValueConverter(null));
    }

    <T> void setStringValueConverter(Class<T> clazz, StringValueConverter<T> converter) {
        converters.put(clazz, converter);
    }

    @Override
    public <T> StringValueConverter<T> get(Class<T> clazz) {
        return converters.get(clazz);
    }
}
