package com.aminebag.larjson.jsonmapper.keys;

class SmallLongArrayFixedLengthKeys extends LongArrayFixedLengthKeys {

    SmallLongArrayFixedLengthKeys(int length) {
        super(length);
    }

    SmallLongArrayFixedLengthKeys(long[] shortKeys) {
        super(shortKeys);
    }

    SmallLongArrayFixedLengthKeys(int[] intKeys) {
        super(intKeys.length);
        for(int i=0; i<intKeys.length; i++) {
            this.keys[i] = intKeys[i];
        }
    }

    SmallLongArrayFixedLengthKeys(short[] shortKeys) {
        super(shortKeys.length);
        for(int i=0; i<shortKeys.length; i++) {
            this.keys[i] = shortKeys[i];
        }
    }

    @Override
    public FixedLengthKeys setKey(int index, long key) {
        keys[index] = key;
        return this;
    }

    @Override
    public FixedLengthKeys reproduce() {
        return new SmallLongArrayFixedLengthKeys(keys.length);
    }

    @Override
    void copyTo(long[] array, int from) {
        System.arraycopy(keys, 0, array, from, keys.length);
    }
}
