package com.aminebag.larjson.jsonmapper.propertymapper;

import com.aminebag.larjson.jsonmapper.LarJsonBranch;
import com.aminebag.larjson.jsonparser.LarJsonParseException;
import com.aminebag.larjson.jsonparser.LarJsonToken;
import com.aminebag.larjson.jsonparser.LarJsonTokenParser;

import java.io.IOException;

public class SmallEnumPropertyMapper<T extends Enum<T>> extends SimpleValuePropertyMapper<T> {
    public static final int MAX_VALUES = Short.MAX_VALUE;
    private final Class<T> enumClazz;
    private final T[] values;

    public SmallEnumPropertyMapper(int index, Class<T> enumClazz, T[] values) {
        super(index);
        this.enumClazz = enumClazz;
        this.values = values;
    }

    @Override
    T calculateValue(LarJsonBranch branch, long key) {
        if (key > values.length) {
            return illegalKey(key);
        } else {
            return values[(int)key - 1];
        }
    }

    @Override
    long calculateKey(LarJsonToken token, LarJsonTokenParser parser, long position)
            throws IOException, LarJsonParseException {

        String value = parser.nextString();
        try {
            return Enum.valueOf(enumClazz, value).ordinal() + 1;
        } catch (IllegalArgumentException e) {
            throw new LarJsonParseException("Failed to parse enum value '" + value +
                    "' of type " + enumClazz.getName(), e);
        }
    }

    public static <T extends Enum<T>> boolean isSmallEnum(T[] values) {
        return values.length <= Short.MAX_VALUE;
    }
}
