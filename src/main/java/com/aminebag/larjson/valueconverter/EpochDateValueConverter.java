package com.aminebag.larjson.valueconverter;

import com.aminebag.larjson.exception.LarJsonConversionException;

import java.util.Date;

/**
 * @author Amine Bagdouri
 *
 * Converts a {@link Date} object from/to {@link String} based on an epoch milliseconds value
 */
public class EpochDateValueConverter implements StringValueConverter<Date> {

    @Override
    public Date fromString(String s) throws LarJsonConversionException {
        if(s == null) {
            return null;
        }

        try {
            long millis = Long.parseLong(s);
            return new Date(millis);
        } catch (NumberFormatException e) {
            throw new LarJsonConversionException("Invalid number of milliseconds", e);
        }
    }

    @Override
    public String toString(Date value) {
        return Long.toString(value.getTime());
    }
}
