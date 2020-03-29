package com.aminebag.larjson.blueprint;

import com.aminebag.larjson.resource.SafeResourceCloser;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author Amine Bagdouri
 *
 * {@inheritDoc}
 * This reader reads bytes from a file.
 */
class FileBinaryReader implements BinaryReader {

    private final BufferBinaryReader buffer = new BufferBinaryReader(8192);
    private final RandomAccessFile randomAccessFile;
    private final long fileLength;
    private long filePosition = -1;

    public FileBinaryReader(File file) throws IOException {
        this.randomAccessFile = new RandomAccessFile(file, "r");
        this.fileLength = randomAccessFile.length();
    }

    @Override
    public byte getByteBackwards() throws IOException {
        ensureRemainingBackwards(Byte.BYTES);
        return buffer.getByteBackwards();
    }

    @Override
    public short getShortBackwards() throws IOException {
        ensureRemainingBackwards(Short.BYTES);
        return buffer.getShortBackwards();
    }

    @Override
    public int getIntBackwards() throws IOException {
        ensureRemainingBackwards(Integer.BYTES);
        return buffer.getIntBackwards();
    }

    @Override
    public long getLongBackwards() throws IOException {
        ensureRemainingBackwards(Long.BYTES);
        return buffer.getLongBackwards();
    }

    private void ensureRemainingBackwards(int remaining) throws IOException {
        if(buffer.position() < remaining) {
            if(filePosition + buffer.position() - remaining < 0) {
                throw new IndexOutOfBoundsException();
            }
            loadData(filePosition + buffer.position());
        }
    }

    @Override
    public void position(long position) throws IOException {
        if(position < 0 || position > fileLength) {
            throw new IllegalArgumentException(
                    "position = " + position + ", fileLength = " + fileLength);
        }
        if(filePosition < 0 || position < filePosition || position > filePosition + buffer.capacity()) {
            loadData(position);
        } else {
            buffer.position((int)(position - filePosition));
        }
    }

    private void loadData(long position) throws IOException {
        filePosition = Math.max(0L, position - (buffer.capacity() >> 1));
        randomAccessFile.seek(filePosition);
        buffer.load(randomAccessFile);
        buffer.position((int)(position - filePosition));
    }

    @Override
    public long position() {
        return filePosition + buffer.position();
    }

    @Override
    public long length() {
        return fileLength;
    }

    @Override
    public void close() throws IOException {
        new SafeResourceCloser()
                .add(buffer)
                .add(randomAccessFile)
                .close();
    }
}
