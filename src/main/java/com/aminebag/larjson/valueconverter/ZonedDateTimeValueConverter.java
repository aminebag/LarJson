package com.aminebag.larjson.valueconverter;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * @author Amine Bagdouri
 *
 * Converts a {@link ZonedDateTime} object from/to {@link String}
 */
public class ZonedDateTimeValueConverter extends TemporalAccessorValueConverter<ZonedDateTime> {
    public ZonedDateTimeValueConverter(DateTimeFormatter formatter) {
        super(formatter);
    }

    public ZonedDateTimeValueConverter(String pattern, Locale locale, ZoneId zoneId) {
        super(pattern, locale, zoneId);
    }

    @Override
    protected ZonedDateTime parse(String s) {
        return ZonedDateTime.parse(s);
    }

    @Override
    protected ZonedDateTime parse(String s, DateTimeFormatter formatter) {
        return ZonedDateTime.parse(s, formatter);
    }
}
