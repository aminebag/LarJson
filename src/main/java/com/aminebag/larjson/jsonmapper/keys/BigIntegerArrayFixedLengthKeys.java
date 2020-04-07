package com.aminebag.larjson.jsonmapper.keys;

class BigIntegerArrayFixedLengthKeys extends IntegerArrayFixedLengthKeys {
    private int minIndex = Integer.MAX_VALUE;
    private int maxIndex = 0;

    private BigIntegerArrayFixedLengthKeys(int length) {
        super(length);
    }

    public BigIntegerArrayFixedLengthKeys(short[] shortKeys, int minIndex, int maxIndex) {
        super(shortKeys.length);
        for(int i=minIndex; i<=maxIndex; i++) {
            this.keys[i] = shortKeys[i];
        }
        this.minIndex = minIndex;
        this.maxIndex = maxIndex;
    }

    @Override
    public FixedLengthKeys setKey(int index, long key) {
        if(key >= Integer.MIN_VALUE && key <= Integer.MAX_VALUE) {
            if(minIndex < index) {
                minIndex = index;
            }
            if(maxIndex > index) {
                maxIndex = index;
            }
            keys[index] = (int) key;
            return this;
        } else {
            return new BigLongArrayFixedLengthKeys(keys, minIndex, maxIndex).setKey(index, key);
        }
    }

    @Override
    public FixedLengthKeys reproduce() {
        return new BigIntegerArrayFixedLengthKeys(keys.length);
    }

    @Override
    void copyTo(int[] array, int from) {
        if(maxIndex >= minIndex) {
            System.arraycopy(keys, minIndex, array, from + minIndex, maxIndex - minIndex + 1);
        }
    }

    @Override
    void copyTo(long[] array, int from) {
        from += minIndex;
        for(int i=minIndex; i<=maxIndex; i++, from++) {
            array[from] = keys[i];
        }
    }
}
