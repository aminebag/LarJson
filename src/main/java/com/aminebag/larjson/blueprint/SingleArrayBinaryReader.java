package com.aminebag.larjson.blueprint;

/**
 * @author Amine Bagdouri
 *
 * {@inheritDoc}
 * This reader reads bytes from a file into a single byte array.
 */
class SingleArrayBinaryReader extends AbstractBinaryReader {

    private final byte[] array;
    private int position = -1;

    SingleArrayBinaryReader(byte[] array) {
        this.array = array;
    }

    @Override
    public void position(long position) {
        if(position < 0 || position > array.length) {
            throw new IllegalArgumentException("position : " + position + ", size : " + array.length);
        }
        this.position = (int) position;
    }

    @Override
    public long position() {
        return position;
    }

    @Override
    public long length() {
        return array.length;
    }

    @Override
    public void close() {

    }

    @Override
    protected byte getBackwards() {
        return array[--position];
    }
}
