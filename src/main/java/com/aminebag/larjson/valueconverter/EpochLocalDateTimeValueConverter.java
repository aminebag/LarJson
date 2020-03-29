package com.aminebag.larjson.valueconverter;

import com.aminebag.larjson.exception.LarJsonConversionException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author Amine Bagdouri
 *
 * Converts a {@link LocalDateTime} object from/to {@link String} based on an epoch milliseconds value
 */
public class EpochLocalDateTimeValueConverter implements StringValueConverter<LocalDateTime> {

    @Override
    public LocalDateTime fromString(String s) throws LarJsonConversionException {
        if(s == null) {
            return null;
        }

        try {
            long millis = Long.parseLong(s);
            return LocalDateTime.ofEpochSecond(millis/1000, (int)(millis%1000), ZoneOffset.UTC);
        } catch (NumberFormatException e) {
            throw new LarJsonConversionException("Invalid number of milliseconds", e);
        }
    }

    @Override
    public String toString(LocalDateTime value) {
        return Long.toString(value.toInstant(ZoneOffset.UTC).toEpochMilli());
    }
}
