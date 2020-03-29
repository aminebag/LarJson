package com.aminebag.larjson.valueconverter;

import com.aminebag.larjson.exception.LarJsonConversionException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.function.Supplier;

/**
 * @author Amine Bagdouri
 *
 * Converts a {@link Date} object from/to {@link String}
 */
public class DateValueConverter implements StringValueConverter<Date> {
    private final Supplier<DateFormat> dateFormatFactory;

    public DateValueConverter(Supplier<DateFormat> dateFormatFactory) {
        this.dateFormatFactory = dateFormatFactory;
        this.dateFormatFactory.get(); //verify cretaed instances
    }

    public DateValueConverter(String pattern, Locale locale, ZoneId zoneId) {
        this(()->{
            SimpleDateFormat dateFormat = locale == null ?
                    new SimpleDateFormat(pattern) :
                    new SimpleDateFormat(pattern, locale);
            if(zoneId != null) {
                dateFormat.setTimeZone(TimeZone.getTimeZone(zoneId));
            }
            return dateFormat;
        });
    }

    @Override
    public Date fromString(String s) throws LarJsonConversionException {
        if(s == null) {
            return null;
        }
        try {
            return dateFormatFactory.get().parse(s);
        } catch (ParseException e) {
            throw new LarJsonConversionException(e);
        }
    }

    @Override
    public String toString(Date value) {
        return dateFormatFactory.get().format(value);
    }
}
