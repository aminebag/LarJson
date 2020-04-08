package com.aminebag.larjson.jsonmapper.keys;

class EmptyKeys extends LarJsonKeys {

    @Override
    public long getKey(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public int size() {
        return 0;
    }
}
