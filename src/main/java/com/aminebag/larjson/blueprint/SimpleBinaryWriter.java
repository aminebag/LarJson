package com.aminebag.larjson.blueprint;

import com.aminebag.larjson.resource.ResourceFactory;
import com.aminebag.larjson.utils.TemporaryFileFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Amine Bagdouri
 *
 * {@inheritDoc}
 */
public class SimpleBinaryWriter implements BinaryWriter {

    private static final int BITS = 30;
    private static final int MAX_LENGTH = 1 << BITS;
    private static final int MASK = MAX_LENGTH - 1;

    private final long maxMemoryBlueprintSize;
    private final FileChannel channel;
    private final File file;
    private long size;
    private byte[] array = null;
    private byte[][] arrays = null;

    public SimpleBinaryWriter(long maxMemoryBlueprintSize, TemporaryFileFactory temporaryFileFactory) throws IOException {
        this.maxMemoryBlueprintSize = maxMemoryBlueprintSize;
        this.file = temporaryFileFactory.createTemporaryFile();
        try {
            this.channel = new RandomAccessFile(file, "rw").getChannel();
        } catch (FileNotFoundException e) {
            this.file.delete();
            throw e;
        }
    }

    @Override
    public void write(ByteBuffer buffer) throws IOException {
        int bytes = buffer.remaining();
        size += bytes;
        while(buffer.hasRemaining()) {
            channel.write(buffer);
        }
    }

    @Override
    public ResourceFactory<? extends LarJsonBlueprintReader> getBlueprintReaderFactory() {
        return new ResourceFactory<LarJsonBlueprintReader>() {
            @Override
            public LarJsonBlueprintReader create() throws IOException {
                if(array != null) {
                    return new SimpleBlueprintReader(new SingleArrayBinaryReader(array));
                } else if(arrays != null) {
                    return new SimpleBlueprintReader(new MultiArrayBinaryReader(arrays, size, BITS));
                } else {
                    return new BufferedBlueprintReader(new FileBinaryReader(file));
                }
            }

            @Override
            public void close() {
                if(file.exists()) {
                    file.delete();
                }
            }
        };
    }

    @Override
    public Closeable getPostErrorCleaner() {
        return ()->file.delete();
    }

    private static void fillArrayFromFile(RandomAccessFile randomAccessFile, byte[] array) throws IOException {
        int bytes = 0;
        while (bytes < array.length) {
            int read = randomAccessFile.read(array, bytes, array.length - bytes);
            if(read < 0) {
                throw new IOException("Unexpected end of file");
            }
            bytes += read;
        }
    }

    @Override
    public void close() throws IOException {
        channel.close();

        if(size > maxMemoryBlueprintSize) {
            return;
        }

        if(size > Integer.MAX_VALUE) {
            int mod = (int) (size & MASK);
            boolean zeroMod = (mod) == 0;
            int nbArrays = (int)(size >>> BITS) + (zeroMod ? 0 : 1);
            this.arrays = new byte[nbArrays][];
            try(RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
                randomAccessFile.seek(0L);
                for (int i = 0; i < nbArrays - 1; i++) {
                    byte[] array = new byte[MAX_LENGTH];
                    fillArrayFromFile(randomAccessFile, array);
                    this.arrays[i] = array;
                }
                byte[] array = new byte[zeroMod ? MAX_LENGTH : mod];
                fillArrayFromFile(randomAccessFile, array);
                this.arrays[nbArrays - 1] = array;
            }
        } else {
            array = new byte[(int) file.length()];
            try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
                randomAccessFile.seek(0L);
                fillArrayFromFile(randomAccessFile, array);
            }
        }
        file.delete();
    }
}
