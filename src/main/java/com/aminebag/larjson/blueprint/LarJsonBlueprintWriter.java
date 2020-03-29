package com.aminebag.larjson.blueprint;

import com.aminebag.larjson.resource.ResourceFactory;
import com.aminebag.larjson.utils.ByteBufferUtils;
import com.aminebag.larjson.utils.TemporaryFileFactory;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

/**
 * @author Amine Bagdouri
 *
 * LarJsonBlueprintWriter allows writing binary values in a LarJson blueprint. Depending on configuration and on the
 * final size of the blueprint, the latter might be stored in a file or in memory.
 */
public class LarJsonBlueprintWriter implements Closeable {

    static final byte MAX_BYTE_VALUE =      0b00000000_00000000_00000000_00000000_00000000_00000000_00000000_01111111;
    static final short MAX_SHORT_VALUE =    0b00000000_00000000_00000000_00000000_00000000_00000000_00111111_11111111;
    static final int MAX_INT_VALUE =        0b00000000_00000000_00000000_00000000_00011111_11111111_11111111_11111111;
    static final long MAX_LONG_VALUE =      0b00001111_11111111_11111111_11111111_11111111_11111111_11111111_11111111L;
    static final long SHORT_FLAG =          0b00000000_00000000_00000000_00000000_00000000_00000000_10000000_00000000L;
    static final long INT_FLAG =            0b00000000_00000000_00000000_00000000_11000000_00000000_00000000_00000000L;
    static final long LONG_FLAG =           0b11100000_00000000_00000000_00000000_00000000_00000000_00000000_00000000L;
    private final ByteBuffer buffer = ByteBuffer.allocate(8192);
    private final BinaryWriter binaryWriter;
    private long position = 0L;

    public LarJsonBlueprintWriter(long maxMemoryBlueprintSize, TemporaryFileFactory temporaryFileFactory)
            throws IOException {
        this.binaryWriter = new SimpleBinaryWriter(maxMemoryBlueprintSize, temporaryFileFactory);
        this.buffer.order(ByteOrder.LITTLE_ENDIAN);
    }

    public void put(long value) throws IOException {
        if(value < 0L) {
            throw new IllegalArgumentException("Value cannot be negative : " + value);
        } else if(value <= MAX_BYTE_VALUE) {
            ensureRemaining(Byte.BYTES);
            buffer.put((byte)value);
        } else if(value <= MAX_SHORT_VALUE) {
            ensureRemaining(Short.BYTES);
            buffer.putShort((short) (SHORT_FLAG | value));
        } else if(value <= MAX_INT_VALUE) {
            ensureRemaining(Integer.BYTES);
            buffer.putInt((int) (INT_FLAG | value));
        } else if(value <= MAX_LONG_VALUE) {
            ensureRemaining(Long.BYTES);
            buffer.putLong(LONG_FLAG | value);
        } else {
            throw new IllegalArgumentException("Value " + value + " is greater than the max value (" + MAX_LONG_VALUE + ")");
        }
    }

    private void ensureRemaining(int bytes) throws IOException {
        if(buffer.remaining() < bytes) {
            ByteBufferUtils.flip(buffer);
            binaryWriter.write(buffer);
            ByteBufferUtils.clear(buffer);
        }
        position += bytes;
    }

    public void putList(List<Long> values, long maxValue) throws IOException {
        if(values.size() > 0) {
            if (maxValue <= Byte.MAX_VALUE) {
                for (int i = values.size() - 1; i >= 0; i--) {
                    ensureRemaining(Byte.BYTES);
                    buffer.put(values.get(i).byteValue());
                }
                ensureRemaining(Byte.BYTES);
                buffer.put((byte)Byte.BYTES);
            } else if (maxValue <= Short.MAX_VALUE) {
                for (int i = values.size() - 1; i >= 0; i--) {
                    ensureRemaining(Short.BYTES);
                    buffer.putShort(values.get(i).shortValue());
                }
                ensureRemaining(Byte.BYTES);
                buffer.put((byte)Short.BYTES);
            } else if (maxValue <= Integer.MAX_VALUE) {
                for (int i = values.size() - 1; i >= 0; i--) {
                    ensureRemaining(Integer.BYTES);
                    buffer.putInt(values.get(i).intValue());
                }
                ensureRemaining(Byte.BYTES);
                buffer.put((byte)Integer.BYTES);
            } else {
                for (int i = values.size() - 1; i >= 0; i--) {
                    ensureRemaining(Long.BYTES);
                    buffer.putLong(values.get(i));
                }
                ensureRemaining(Byte.BYTES);
                buffer.put((byte)Long.BYTES);
            }
        }
        put(values.size());
    }

    public long position() {
        return position;
    }

    @Override
    public void close() throws IOException {
        ByteBufferUtils.flip(buffer);
        binaryWriter.write(buffer);
        ByteBufferUtils.clear(buffer);
        binaryWriter.close();
    }

    public ResourceFactory<? extends LarJsonBlueprintReader> getReaderFactory() {
        return binaryWriter.getBlueprintReaderFactory();
    }

    public Closeable getOnErrorCleaner() {
        return binaryWriter.getPostErrorCleaner();
    }
}
