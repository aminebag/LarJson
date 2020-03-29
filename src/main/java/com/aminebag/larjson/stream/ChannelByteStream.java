package com.aminebag.larjson.stream;

import com.aminebag.larjson.channel.RandomReadAccessChannel;
import com.aminebag.larjson.utils.ByteBufferUtils;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author Amine Bagdouri
 *
 * {@inheritDoc}
 */
public class ChannelByteStream implements ByteStream {

    private static final int DEFAULT_BUFFER_SIZE = 8192;

    private final ByteBuffer buffer;
    private final ByteBuffer[] dsts;
    private final RandomReadAccessChannel channel;
    private long fileLength = -1;
    private long currentPosition = 0;
    private long filePosition = -1;

    public ChannelByteStream(RandomReadAccessChannel channel) {
        this.channel = channel;
        this.buffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
        this.dsts = new ByteBuffer[]{buffer};
        ByteBufferUtils.limit(this.buffer, 0);
        ByteBufferUtils.position(this.buffer, 0);
    }

    void seek(long position) throws IOException {
        currentPosition = position;
        if(filePosition >= 0 && position >= filePosition && position < filePosition + buffer.limit()) {
            ByteBufferUtils.position(this.buffer, (int)(currentPosition - filePosition));
        } else {
            channel.seek(currentPosition);
            ByteBufferUtils.limit(this.buffer, 0);
            ByteBufferUtils.position(this.buffer, 0);
        }
    }

    @Override
    public byte nextByte() throws IOException {
        if(!hasAtLeastRemainingBytes(Byte.BYTES)){
            throw new IllegalStateException();
        }
        if(buffer.remaining() < Byte.BYTES){
            ByteBufferUtils.clear(buffer);
            filePosition = channel.position();
            while (buffer.position() < Byte.BYTES) {
                long read = channel.read(dsts, 0, 1);
                if(read < 0) {
                    throw unexpectedEof();
                }
            }
            ByteBufferUtils.flip(buffer);
        }
        currentPosition++;
        return buffer.get();
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
        ensureRemaining(Long.BYTES);
        return buffer.getLong();
    }

    private void ensureRemaining(int bytes) throws IOException {
        if (!hasAtLeastRemainingBytes(bytes)) {
            throw new IllegalStateException();
        }
        if (buffer.remaining() < bytes) {
            buffer.compact();
            filePosition = channel.position() - buffer.remaining();
            while (buffer.position() < bytes) {
                long read = channel.read(dsts, 0, 1);
                if(read < 0) {
                    throw unexpectedEof();
                }
            }
            ByteBufferUtils.flip(buffer);
        }
        currentPosition += bytes;
    }

    private IOException unexpectedEof() throws IOException {
        throw new IOException("Unexpected end of file reached before consuming a number of bytes corresponding " +
                "to the file's initial size");
    }

    @Override
    public boolean hasAtLeastRemainingBytes(int bytes) throws IOException {
        if(fileLength < 0) {
            fileLength = channel.length();
        }
        return buffer.remaining() - currentPosition + fileLength >= bytes;
    }

    @Override
    public long currentPosition() {
        return currentPosition;
    }
}
