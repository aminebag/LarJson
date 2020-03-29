package com.aminebag.larjson.blueprint;

import java.io.IOException;

import static com.aminebag.larjson.blueprint.LarJsonBlueprintWriter.*;

/**
 * @author Amine Bagdouri
 *
 * {@inheritDoc}
 */
abstract class AbstractBlueprintReader implements LarJsonBlueprintReader {

    protected abstract BinaryReader getBinaryReader();

    @Override
    public void position(long position) throws IOException {
        getBinaryReader().position(position);
    }

    @Override
    public long position() {
        return getBinaryReader().position();
    }

    @Override
    public long get() throws IOException {
        BinaryReader binaryReader = getBinaryReader();
        long value = binaryReader.getByteBackwards() & 0xFF;
        if(value <= MAX_BYTE_VALUE) {
            return value;
        }

        value = (value << 8 | (binaryReader.getByteBackwards() & 0xFF)) & ~SHORT_FLAG;
        if(value <= MAX_SHORT_VALUE) {
            return value;
        }

        value = (value << 16 | (binaryReader.getShortBackwards() & 0xFFFF)) & ~INT_FLAG;
        if(value <= MAX_INT_VALUE) {
            return value;
        }

        return (value << 32 | binaryReader.getIntBackwards() & 0xFFFFFFFFL) & ~LONG_FLAG;
    }

    @Override
    public long length() throws IOException {
        return getBinaryReader().length();
    }

    @Override
    public void close() throws IOException {
        getBinaryReader().close();
    }
}
