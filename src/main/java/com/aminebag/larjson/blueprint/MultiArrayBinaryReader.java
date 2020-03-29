package com.aminebag.larjson.blueprint;

/**
 * @author Amine Bagdouri
 *
 * {@inheritDoc}
 * This reader reads bytes from a file into multiple byte arrays.
 */
class MultiArrayBinaryReader extends AbstractBinaryReader {

    private final int bits;
    private final int maxLength ;
    private final int mask;
    private final byte[][] arrays;
    private final long size;
    private long position = -1;

    MultiArrayBinaryReader(byte[][] arrays, long size, int bits) {
        this.bits = bits;
        this.maxLength = 1 << bits;
        this.mask = maxLength - 1;
        this.arrays = arrays;
        this.size = size;
    }

    @Override
    public void position(long position) {
        if(position < 0 || position > size) {
            throw new IllegalArgumentException("position : " + position + ", size : " + size);
        }
        this.position = position;
    }

    @Override
    public long position() {
        return position;
    }

    @Override
    public long length() {
        return size;
    }

    @Override
    public void close() {

    }

    @Override
    protected byte getBackwards() {
        int mod = (int) (position & mask);
        byte b;
        if(mod == 0) {
            byte[] array = arrays[(int)(position >>> bits) - 1];
            b = array[maxLength - 1];
        } else {
            byte[] array = arrays[(int)(position >>> bits)];
            b = array[mod - 1];
        }
        position--;
        return b;
    }
}
