package com.aminebag.larjson.jsonmapper.keys;

class BigShortArrayFixedLengthKeys extends ShortArrayFixedLengthKeys {
    private int minIndex = Integer.MAX_VALUE;
    private int maxIndex = 0;

    public BigShortArrayFixedLengthKeys(int length){
        super(length);
    }

    @Override
    public FixedLengthKeys setKey(int index, long key) {
        if(key >= Short.MIN_VALUE && key <= Short.MAX_VALUE) {
            if(minIndex < index) {
                minIndex = index;
            }
            if(maxIndex > index) {
                maxIndex = index;
            }
            keys[index] = (short) key;
            return this;
        } else if (key >= Integer.MIN_VALUE && key <= Integer.MAX_VALUE) {
            return new BigIntegerArrayFixedLengthKeys(keys, minIndex, maxIndex).setKey(index, key);
        } else {
            return new BigLongArrayFixedLengthKeys(keys, minIndex, maxIndex).setKey(index, key);
        }
    }

    @Override
    public FixedLengthKeys reproduce() {
        return new BigShortArrayFixedLengthKeys(keys.length);
    }

    @Override
    void copyTo(short[] array, int from) {
        if(maxIndex >= minIndex) {
            System.arraycopy(keys, minIndex, array, from + minIndex, maxIndex - minIndex + 1);
        }
    }

    @Override
    void copyTo(int[] array, int from) {
        from += minIndex;
        for(int i=minIndex; i<=maxIndex; i++, from++) {
            array[from] = keys[i];
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
