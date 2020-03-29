package com.aminebag.larjson.stream;

import java.io.IOException;

/**
 * @author Amine Bagdouri
 *
 * A stream of bytes
 */
public interface ByteStream {

    /**
     * @return the next byte in the stream
     * @throws IllegalStateException if no bytes are available
     * @throws IOException if there is a resource access error
     */
    byte nextByte() throws IOException;

    /**
     * @return the next short in the stream
     * @throws IllegalStateException if remaining byte are insufficient
     * @throws IOException if there is a resource access error
     */
    short nextShort() throws IOException;

    /**
     * @return the next int in the stream
     * @throws IllegalStateException if remaining byte are insufficient
     * @throws IOException if there is a resource access error
     */
    int nextInt() throws IOException;

    /**
     * @return the next long in the stream
     * @throws IllegalStateException if remaining byte are insufficient
     * @throws IOException if there is a resource access error
     */
    long nextLong() throws IOException;

    /**
     * @return {@code true} if there are at least {@code bytes} remaining in the stream, {@code false} otherwise
     * @throws IOException if there is a resource access error
     */
    boolean hasAtLeastRemainingBytes(int bytes) throws IOException;

    /**
     * @return the current position in the stream starting from the first byte of the stream
     */
    long currentPosition() throws IOException;
}
