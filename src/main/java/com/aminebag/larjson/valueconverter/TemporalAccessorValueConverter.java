package com.aminebag.larjson.valueconverter;

import com.aminebag.larjson.exception.LarJsonConversionException;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;

/**
 * @author Amine Bagdouri
 *
 * Converts a {@link TemporalAccessor} object from/to {@link String}
 */
abstract class TemporalAccessorValueConverter<T extends TemporalAccessor> implements StringValueConverter<T> {
    private final DateTimeFormatter formatter;

    public TemporalAccessorValueConverter(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    public TemporalAccessorValueConverter(String pattern, Locale locale, ZoneId zoneId) {
        DateTimeFormatter formatter = locale == null ? DateTimeFormatter.ofPattern(pattern) :
                DateTimeFormatter.ofPattern(pattern, locale);
        if(zoneId != null) {
            formatter = formatter.withZone(zoneId);
        }
        this.formatter = formatter;
    }

    @Override
    public T fromString(String s) throws LarJsonConversionException {
        if(s == null) {
            return null;
        }
        try {
            return formatter == null ? parse(s) : parse(s, formatter);
        } catch (DateTimeParseException e) {
            throw new LarJsonConversionException("Failed to parse value '" + s + "' " +
                    (formatter == null ? "using default formatter" : "using formatter " + formatter), e);
        }
    }

    protected abstract T parse(String s);
    protected abstract T parse(String s, DateTimeFormatter formatter);

    @Override
    public String toString(T value) {
        return formatter == null ? value.toString() : formatter.format(value);
    }
}
