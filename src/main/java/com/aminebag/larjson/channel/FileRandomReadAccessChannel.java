package com.aminebag.larjson.channel;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileRandomReadAccessChannel implements RandomReadAccessChannel {

    private final FileChannel fileChannel;

    public FileRandomReadAccessChannel(RandomAccessFile randomAccessFile) {
        this.fileChannel = randomAccessFile.getChannel();
    }

    @Override
    public long read(ByteBuffer[] dsts, int offset, int length) throws IOException {
        return fileChannel.read(dsts, offset, length);
    }

    @Override
    public long position() throws IOException {
        return fileChannel.position();
    }

    @Override
    public void seek(long newPosition) throws IOException {
        fileChannel.position(newPosition);
    }

    @Override
    public long length() throws IOException {
        return fileChannel.size();
    }

    @Override
    public void close() throws IOException {
        fileChannel.close();
    }
}
