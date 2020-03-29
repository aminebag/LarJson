package com.aminebag.larjson.blueprint;

import com.aminebag.larjson.utils.LongList;

import java.io.IOException;

/**
 * @author Amine Bagdouri
 *
 * {@inheritDoc}
 */
class BufferedBlueprintReader extends AbstractBlueprintReader {

    private final BinaryReader binaryReader;

    BufferedBlueprintReader(BinaryReader binaryReader) {
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
            case Byte.BYTES: {
                byte[] array = new byte[size];
                for(int i=0;i<size;i++) {
                    array[i] = binaryReader.getByteBackwards();
                }
                return new ByteArrayLongList(array);
            }
            case Short.BYTES: {
                short[] array = new short[size];
                for(int i=0;i<size;i++) {
                    array[i] = binaryReader.getShortBackwards();
                }
                return new ShortArrayLongList(array);
            }
            case Integer.BYTES: {
                int[] array = new int[size];
                for(int i=0;i<size;i++) {
                    array[i] = binaryReader.getIntBackwards();
                }
                return new IntArrayLongList(array);
            }
            case Long.BYTES: {
                long[] array = new long[size];
                for(int i=0;i<size;i++) {
                    array[i] = binaryReader.getLongBackwards();
                }
                return new LongArrayLongList(array);
            }
            default: {
                throw new IllegalArgumentException("bytes : " + bytes);
            }
        }
    }

    @Override
    protected BinaryReader getBinaryReader() {
        return binaryReader;
    }

    private static class ByteArrayLongList implements LongList {
        private final byte[] array;

        public ByteArrayLongList(byte[] array) {
            this.array = array;
        }

        @Override
        public int size() {
            return array.length;
        }

        @Override
        public long get(int index) {
            if(index < 0 || index >= size()) {
                throw new IndexOutOfBoundsException();
            }
            return array[index];
        }
    }

    private static class ShortArrayLongList implements LongList {
        private final short[] array;

        public ShortArrayLongList(short[] array) {
            this.array = array;
        }

        @Override
        public int size() {
            return array.length;
        }

        @Override
        public long get(int index) {
            if(index < 0 || index >= size()) {
                throw new IndexOutOfBoundsException();
            }
            return array[index];
        }
    }

    private static class IntArrayLongList implements LongList {
        private final int[] array;

        public IntArrayLongList(int[] array) {
            this.array = array;
        }

        @Override
        public int size() {
            return array.length;
        }

        @Override
        public long get(int index) {
            if(index < 0 || index >= size()) {
                throw new IndexOutOfBoundsException();
            }
            return array[index];
        }
    }

    private static class LongArrayLongList implements LongList {
        private final long[] array;

        public LongArrayLongList(long[] array) {
            this.array = array;
        }

        @Override
        public int size() {
            return array.length;
        }

        @Override
        public long get(int index) {
            if(index < 0 || index >= size()) {
                throw new IndexOutOfBoundsException();
            }
            return array[index];
        }
    }
}
