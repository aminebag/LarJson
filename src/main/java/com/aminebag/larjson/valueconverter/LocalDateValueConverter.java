package com.aminebag.larjson.valueconverter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * @author Amine Bagdouri
 *
 * Converts a {@link LocalDate} object from/to {@link String}
 */
public class LocalDateValueConverter extends TemporalAccessorValueConverter<LocalDate> {
    public LocalDateValueConverter(DateTimeFormatter formatter) {
        super(formatter);
    }

    public LocalDateValueConverter(String pattern, Locale locale, ZoneId zoneId) {
        super(pattern, locale, zoneId);
    }
    @Override
    protected LocalDate parse(String s) {
        return LocalDate.parse(s);
    }

    @Override
    protected LocalDate parse(String s, DateTimeFormatter formatter) {
        return LocalDate.parse(s, formatter);
    }
}
