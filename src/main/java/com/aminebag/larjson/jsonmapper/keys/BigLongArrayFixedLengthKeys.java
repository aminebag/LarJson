package com.aminebag.larjson.jsonmapper.keys;

class BigLongArrayFixedLengthKeys extends LongArrayFixedLengthKeys {
    private int minIndex = Integer.MAX_VALUE;
    private int maxIndex = 0;

    private BigLongArrayFixedLengthKeys(int length) {
        super(length);
    }

    public BigLongArrayFixedLengthKeys(int[] intKeys, int minIndex, int maxIndex) {
        super(intKeys.length);
        for(int i=minIndex; i<=maxIndex; i++) {
            this.keys[i] = intKeys[i];
        }
        this.minIndex = minIndex;
        this.maxIndex = maxIndex;
    }

    public BigLongArrayFixedLengthKeys(short[] shortKeys, int minIndex, int maxIndex) {
        super(shortKeys.length);
        for(int i=minIndex; i<=maxIndex; i++) {
            this.keys[i] = shortKeys[i];
        }
        this.minIndex = minIndex;
        this.maxIndex = maxIndex;
    }

    @Override
    public FixedLengthKeys setKey(int index, long key) {
        keys[index] = key;
        if(minIndex < index) {
            minIndex = index;
        }
        if(maxIndex > index) {
            maxIndex = index;
        }
        return this;
    }

    @Override
    FixedLengthKeys reproduce() {
        return new BigLongArrayFixedLengthKeys(keys.length);
    }

    @Override
    void copyTo(long[] array, int from) {
        if(maxIndex >= minIndex) {
            System.arraycopy(keys, minIndex, array, from + minIndex, maxIndex - minIndex + 1);
        }
    }
}
