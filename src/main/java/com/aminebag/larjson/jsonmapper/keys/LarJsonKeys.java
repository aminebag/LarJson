package com.aminebag.larjson.jsonmapper.keys;

public abstract class LarJsonKeys {

    private static final int BIG_ARRAY_MIN_LENGTH = 1024 * 1024;
    public static final LarJsonKeys EMPTY_KEYS = new EmptyKeys();

    LarJsonKeys() {
    }

    public abstract long getKey(int index);
    public abstract int size();

    public static FixedLengthKeys createFixedLength(int length){
        if(length >= BIG_ARRAY_MIN_LENGTH) {
            return new BigShortArrayFixedLengthKeys(length);
        } else {
            return new SmallShortArrayFixedLengthKeys(length);
        }
    }

    public static DynamicLengthKeys createDynamicLength(){
        return new ShortArrayDynamicLengthKeys();
    }
}
