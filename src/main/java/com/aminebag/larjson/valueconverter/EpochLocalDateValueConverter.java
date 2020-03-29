package com.aminebag.larjson.valueconverter;

import com.aminebag.larjson.exception.LarJsonConversionException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author Amine Bagdouri
 *
 * Converts a {@link LocalDate} object from/to {@link String} based on an epoch milliseconds value
 */
public class EpochLocalDateValueConverter implements StringValueConverter<LocalDate> {

    @Override
    public LocalDate fromString(String s) throws LarJsonConversionException {
        if(s == null) {
            return null;
        }

        try {
            long millis = Long.parseLong(s);
            return LocalDateTime.ofEpochSecond(millis/1000, (int)(millis%1000), ZoneOffset.UTC)
                    .toLocalDate();
        } catch (NumberFormatException e) {
            throw new LarJsonConversionException("Invalid number of milliseconds", e);
        }
    }

    @Override
    public String toString(LocalDate value) {
        return Long.toString(value.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli());
    }
}
