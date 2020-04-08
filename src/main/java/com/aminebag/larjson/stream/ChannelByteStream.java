package com.aminebag.larjson.stream;

import com.aminebag.larjson.channel.RandomReadAccessChannel;

import java.io.IOException;
import java.nio.ByteBuffer;

public class ChannelByteStream implements ByteStream {

    private static final int DEFAULT_BUFFER_SIZE = 4096;

    private final ByteBuffer buffer;
    private final ByteBuffer[] dsts;
    private final RandomReadAccessChannel channel;
    private long fileLength = -1;
    private long currentPosition = 0;

    public ChannelByteStream(RandomReadAccessChannel channel) {
        this.channel = channel;
        this.buffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
        this.dsts = new ByteBuffer[]{buffer};
    }

    public void seek(long position) throws IOException {
        channel.seek(currentPosition = position);
        buffer.clear();
    }

    @Override
    public byte nextByte() throws IOException {
        if(!hasAtLeastRemainingBytes(Byte.BYTES)){
            throw new IllegalStateException();
        }
        if(buffer.remaining() < Byte.BYTES){
            buffer.clear();
            channel.read(dsts, buffer.position(), buffer.remaining());
        }
        return buffer.get();
    }

    private void ensureRemaining(int bytes) throws IOException {
        if (!hasAtLeastRemainingBytes(bytes)) {
            throw new IllegalStateException();
        }
        if (buffer.remaining() < bytes) {
            buffer.compact();
            channel.read(dsts, buffer.position(), buffer.remaining());
        }
    }

    @Override
    public short nextShort() throws IOException {
        ensureRemaining(Short.BYTES);
        return buffer.getShort();
    }

    @Override
    public int nextInt() throws IOException {
        ensureRemaining(Integer.BYTES);
        return buffer.getInt();
    }

    @Override
    public long nextLong() throws IOException {
        ensureRemaining(Integer.BYTES);
        return buffer.getLong();
    }

    @Override
    public boolean hasAtLeastRemainingBytes(int bytes) throws IOException {
        if(fileLength < 0) {
            fileLength = channel.length();
        }
        return buffer.remaining() - currentPosition + fileLength >= bytes;
    }
}
