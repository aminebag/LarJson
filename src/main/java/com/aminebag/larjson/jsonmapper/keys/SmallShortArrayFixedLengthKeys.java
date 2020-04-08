package com.aminebag.larjson.jsonmapper.keys;

class SmallShortArrayFixedLengthKeys extends ShortArrayFixedLengthKeys {

    SmallShortArrayFixedLengthKeys(int length){
        super(length);
    }

    SmallShortArrayFixedLengthKeys(short[] keys){
        super(keys);
    }

    @Override
    public FixedLengthKeys setKey(int index, long key) {
        if(key >= Short.MIN_VALUE && key <= Short.MAX_VALUE) {
            keys[index] = (short) key;
            return this;
        } else if (key >= Integer.MIN_VALUE && key <= Integer.MAX_VALUE) {
            return new SmallIntegerArrayFixedLengthKeys(keys).setKey(index, key);
        } else {
            return new SmallLongArrayFixedLengthKeys(keys).setKey(index, key);
        }
    }

    @Override
    FixedLengthKeys reproduce() {
        return new SmallShortArrayFixedLengthKeys(keys.length);
    }

    @Override
    void copyTo(short[] array, int from) {
        System.arraycopy(keys, 0, array, from, keys.length);
    }

    @Override
    void copyTo(int[] array, int from) {
        for(int i=0; i<keys.length; i++, from++) {
            array[from] = keys[i];
        }
    }

    @Override
    void copyTo(long[] array, int from) {
        for(int i=0; i<keys.length; i++, from++) {
            array[from] = keys[i];
        }
    }
}
