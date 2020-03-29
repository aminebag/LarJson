package com.aminebag.larjson.blueprint;

/**
 * @author Amine Bagdouri
 *
 * A byte buffer that can be used to read bytes, from an array, backwards
 */
abstract class AbstractBinaryReader implements BinaryReader {

    protected abstract byte getBackwards();

    private void ensureRemainingBackwards(int bytes) {
        if(position() < bytes) {
            throw new IndexOutOfBoundsException("Position : " + position());
        }
    }

    public byte getByteBackwards() {
        ensureRemainingBackwards(Byte.BYTES);
        return getBackwards();
    }

    public short getShortBackwards() {
        ensureRemainingBackwards(Short.BYTES);
        return (short) (getBackwards() << 8 | (getBackwards() & 0xFF));
    }

    public int getIntBackwards() {
        ensureRemainingBackwards(Integer.BYTES);
        return getBackwards() << 24 | (getBackwards() & 0xFF) << 16 | (getBackwards() & 0xFF) << 8
                | (getBackwards() & 0xFF);
    }

    public long getLongBackwards() {
        ensureRemainingBackwards(Long.BYTES);
        return ((long) getBackwards()) << 56 | ((long) (getBackwards() & 0xFF)) << 48
                | ((long) (getBackwards() & 0xFF)) << 40 | ((long) (getBackwards() & 0xFF)) << 32
                | ((long) (getBackwards() & 0xFF)) << 24 | ((long) (getBackwards() & 0xFF)) << 16
                | ((long) (getBackwards() & 0xFF)) << 8 | ((long) (getBackwards() & 0xFF));
    }
}
