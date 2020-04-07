package com.aminebag.larjson.jsonmapper.keys;

class SmallIntegerArrayFixedLengthKeys extends IntegerArrayFixedLengthKeys {

    SmallIntegerArrayFixedLengthKeys(int length) {
        super(length);
    }

    SmallIntegerArrayFixedLengthKeys(short[] shortKeys) {
        super(shortKeys.length);
        for(int i=0; i<shortKeys.length; i++) {
            this.keys[i] = shortKeys[i];
        }
    }

    SmallIntegerArrayFixedLengthKeys(int[] keys){
        super(keys);
    }

    @Override
    public long getKey(int index) {
        return keys[index];
    }

    @Override
    public FixedLengthKeys setKey(int index, long key) {
        if(key >= Integer.MIN_VALUE && key <= Integer.MAX_VALUE) {
            keys[index] = (int) key;
            return this;
        } else {
            return new SmallLongArrayFixedLengthKeys(keys).setKey(index, key);
        }
    }

    @Override
    FixedLengthKeys reproduce() {
        return new SmallIntegerArrayFixedLengthKeys(keys.length);
    }

    @Override
    void copyTo(int[] array, int from) {
        System.arraycopy(keys, 0, array, from, keys.length);
    }

    @Override
    void copyTo(long[] array, int from) {
        for(int i=0; i<keys.length; i++, from++) {
            array[from] = keys[i];
        }
    }

}
