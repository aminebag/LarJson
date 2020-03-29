package com.aminebag.larjson.valueconverter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * @author Amine Bagdouri
 *
 * Converts a {@link LocalDateTime} object from/to {@link String}
 */
public class LocalDateTimeValueConverter extends TemporalAccessorValueConverter<LocalDateTime> {
    public LocalDateTimeValueConverter(DateTimeFormatter formatter) {
        super(formatter);
    }

    public LocalDateTimeValueConverter(String pattern, Locale locale, ZoneId zoneId) {
        super(pattern, locale, zoneId);
    }

    @Override
    protected LocalDateTime parse(String s) {
        return LocalDateTime.parse(s);
    }

    @Override
    protected LocalDateTime parse(String s, DateTimeFormatter formatter) {
        return LocalDateTime.parse(s, formatter);
    }
}
