package com.aminebag.larjson.blueprint;

import com.aminebag.larjson.utils.LongList;

import java.io.IOException;

/**
 * @author Amine Bagdouri
 *
 * {@inheritDoc}
 */
class SimpleBlueprintReader extends AbstractBlueprintReader {

    private final BinaryReader binaryReader;

    SimpleBlueprintReader(BinaryReader binaryReader) {
        this.binaryReader = binaryReader;
    }

    @Override
    public LongList getList() throws IOException {
        int size = (int) get();
        if(size <= 0) {
            return LongList.EMPTY;
        }
        byte bytes = binaryReader.getByteBackwards();

        switch (bytes) {
            case Byte.BYTES : {
                return new ByteBinaryReaderLongList(binaryReader, size);
            }
            case Short.BYTES : {
                return new ShortBinaryReaderLongList(binaryReader, size);
            }
            case Integer.BYTES : {
                return new IntBinaryReaderLongList(binaryReader, size);
            }
            case Long.BYTES : {
                return new LongBinaryReaderLongList(binaryReader, size);
            }
            default: throw new IllegalArgumentException("bytes : " + bytes);
        }
    }

    @Override
    protected BinaryReader getBinaryReader() {
        return binaryReader;
    }

    private abstract static class BinaryReaderLongList implements LongList {

        private final BinaryReader binaryReader;
        private final long position;
        private final int size;

        protected BinaryReaderLongList(BinaryReader binaryReader, int size) {
            this.binaryReader = binaryReader;
            this.position = binaryReader.position();
            this.size = size;
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public long get(int index) throws IOException {
            if(index < 0 || index >= size) {
                throw new IndexOutOfBoundsException();
            }
            binaryReader.position(position - index * bytes());
            return readBackwards(binaryReader);
        }

        abstract int bytes();

        abstract long readBackwards(BinaryReader binaryReader) throws IOException;
    }

    private static class ByteBinaryReaderLongList extends BinaryReaderLongList {

        protected ByteBinaryReaderLongList(BinaryReader binaryReader, int size) {
            super(binaryReader, size);
        }

        @Override
        int bytes() {
            return Byte.BYTES;
        }

        @Override
        long readBackwards(BinaryReader binaryReader) throws IOException {
            return binaryReader.getByteBackwards();
        }
    }

    private static class ShortBinaryReaderLongList extends BinaryReaderLongList {

        protected ShortBinaryReaderLongList(BinaryReader binaryReader, int size) {
            super(binaryReader, size);
        }

        @Override
        int bytes() {
            return Short.BYTES;
        }

        @Override
        long readBackwards(BinaryReader binaryReader) throws IOException {
            return binaryReader.getShortBackwards();
        }
    }

    private static class IntBinaryReaderLongList extends BinaryReaderLongList {

        protected IntBinaryReaderLongList(BinaryReader binaryReader, int size) {
            super(binaryReader, size);
        }

        @Override
        int bytes() {
            return Integer.BYTES;
        }

        @Override
        long readBackwards(BinaryReader binaryReader) throws IOException {
            return binaryReader.getIntBackwards();
        }
    }

    private static class LongBinaryReaderLongList extends BinaryReaderLongList {

        protected LongBinaryReaderLongList(BinaryReader binaryReader, int size) {
            super(binaryReader, size);
        }

        @Override
        int bytes() {
            return Long.BYTES;
        }

        @Override
        long readBackwards(BinaryReader binaryReader) throws IOException {
            return binaryReader.getLongBackwards();
        }
    }
}
