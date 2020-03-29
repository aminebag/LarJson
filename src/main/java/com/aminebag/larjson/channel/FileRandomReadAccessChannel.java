package com.aminebag.larjson.channel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Amine Bagdouri
 *
 * A channel for reading a file. This channel provides means to read bytes from any given byte position
 */
public class FileRandomReadAccessChannel implements RandomReadAccessChannel {

    private final FileChannel fileChannel;

    public FileRandomReadAccessChannel(File file) throws FileNotFoundException {
        this.fileChannel = new RandomAccessFile(file, "r").getChannel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long read(ByteBuffer[] dsts, int offset, int length) throws IOException {
        return fileChannel.read(dsts, offset, length);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long position() throws IOException {
        return fileChannel.position();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void seek(long newPosition) throws IOException {
        fileChannel.position(newPosition);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long length() throws IOException {
        return fileChannel.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        fileChannel.close();
    }
}
