package com.aminebag.larjson.valueconverter;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * @author Amine Bagdouri
 *
 * Converts a {@link LocalTime} object from/to {@link String}
 */
public class LocalTimeValueConverter extends TemporalAccessorValueConverter<LocalTime> {
    public LocalTimeValueConverter(DateTimeFormatter formatter) {
        super(formatter);
    }

    public LocalTimeValueConverter(String pattern, Locale locale, ZoneId zoneId) {
        super(pattern, locale, zoneId);
    }

    @Override
    protected LocalTime parse(String s) {
        return LocalTime.parse(s);
    }

    @Override
    protected LocalTime parse(String s, DateTimeFormatter formatter) {
        return LocalTime.parse(s, formatter);
    }
}
