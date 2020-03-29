package com.aminebag.larjson.channel;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;

/**
 * @author Amine Bagdouri
 *
 * A channel for reading a byte resource. This channel must a provide means to read bytes from any given byte position
 */
public interface RandomReadAccessChannel extends Closeable {

    /**
     * Reads a sequence of bytes from this channel into a subsequence of the
     * given buffers.
     *
     * <p> An invocation of this method attempts to read up to <i>r</i> bytes
     * from this channel, where <i>r</i> is the total number of bytes remaining
     * the specified subsequence of the given buffer array, that is,
     *
     * <blockquote><pre>
     * dsts[offset].remaining()
     *     + dsts[offset+1].remaining()
     *     + ... + dsts[offset+length-1].remaining()</pre></blockquote>
     *
     * at the moment that this method is invoked.
     *
     * <p> Suppose that a byte sequence of length <i>n</i> is read, where
     * <tt>0</tt>&nbsp;<tt>&lt;=</tt>&nbsp;<i>n</i>&nbsp;<tt>&lt;=</tt>&nbsp;<i>r</i>.
     * Up to the first <tt>dsts[offset].remaining()</tt> bytes of this sequence
     * are transferred into buffer <tt>dsts[offset]</tt>, up to the next
     * <tt>dsts[offset+1].remaining()</tt> bytes are transferred into buffer
     * <tt>dsts[offset+1]</tt>, and so forth, until the entire byte sequence
     * is transferred into the given buffers.  As many bytes as possible are
     * transferred into each buffer, hence the final position of each updated
     * buffer, except the last updated buffer, is guaranteed to be equal to
     * that buffer's limit.
     *
     * <p> This method may be invoked at any time.  If another thread has
     * already initiated a read operation upon this channel, however, then an
     * invocation of this method will block until the first operation is
     * complete. </p>
     *
     * @param  dsts
     *         The buffers into which bytes are to be transferred
     *
     * @param  offset
     *         The offset within the buffer array of the first buffer into
     *         which bytes are to be transferred; must be non-negative and no
     *         larger than <tt>dsts.length</tt>
     *
     * @param  length
     *         The maximum number of buffers to be accessed; must be
     *         non-negative and no larger than
     *         <tt>dsts.length</tt>&nbsp;-&nbsp;<tt>offset</tt>
     *
     * @return The number of bytes read, possibly zero,
     *         or <tt>-1</tt> if the channel has reached end-of-stream
     *
     * @throws  IndexOutOfBoundsException
     *          If the preconditions on the <tt>offset</tt> and <tt>length</tt>
     *          parameters do not hold
     *
     * @throws NonReadableChannelException
     *          If this channel was not opened for reading
     *
     * @throws ClosedChannelException
     *          If this channel is closed
     *
     * @throws AsynchronousCloseException
     *          If another thread closes this channel
     *          while the read operation is in progress
     *
     * @throws ClosedByInterruptException
     *          If another thread interrupts the current thread
     *          while the read operation is in progress, thereby
     *          closing the channel and setting the current thread's
     *          interrupt status
     *
     * @throws  IOException
     *          If some other I/O error occurs
     *
     * @see ScatteringByteChannel#read(ByteBuffer[], int, int)
     */
    long read(ByteBuffer[] dsts, int offset, int length) throws IOException;

    /**
     * Returns this channel's position.
     *
     * @return  This channel's position,
     *          a non-negative integer counting the number of bytes
     *          from the beginning of the entity to the current position
     *
     * @throws  ClosedChannelException
     *          If this channel is closed
     * @throws  IOException
     *          If some other I/O error occurs
     * @see SeekableByteChannel#position()
     */
    long position() throws IOException;

    /**
     * Sets this channel's position.
     *
     * <p> Setting the position to a value that is greater than the current size
     * is legal but does not change the size of the entity.  A later attempt to
     * read bytes at such a position will immediately return an end-of-file
     * indication.  A later attempt to write bytes at such a position will cause
     * the entity to grow to accommodate the new bytes; the values of any bytes
     * between the previous end-of-file and the newly-written bytes are
     * unspecified.
     *
     * <p> Setting the channel's position is not recommended when connected to
     * an entity, typically a file, that is opened with the {@link
     * java.nio.file.StandardOpenOption#APPEND APPEND} option. When opened for
     * append, the position is first advanced to the end before writing.
     *
     * @param  newPosition
     *         The new position, a non-negative integer counting
     *         the number of bytes from the beginning of the entity
     *
     * @throws  ClosedChannelException
     *          If this channel is closed
     * @throws  IllegalArgumentException
     *          If the new position is negative
     * @throws  IOException
     *          If some other I/O error occurs
     * @see SeekableByteChannel#position(long)
     */
    void seek(long newPosition) throws IOException;

    /**
     * Returns the current size of entity to which this channel is connected.
     *
     * @return  The current size, measured in bytes
     *
     * @throws  ClosedChannelException
     *          If this channel is closed
     * @throws  IOException
     *          If some other I/O error occurs
     * @see SeekableByteChannel#size()
     */
    long length() throws IOException;
}
