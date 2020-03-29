package com.aminebag.larjson.resource;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Amine Bagdouri
 */
public class SafeResourceCloserTest {

    @Test
    void testCloseWithoutException() throws IOException {
        AtomicInteger counter = new AtomicInteger();
        new SafeResourceCloser()
                .add(() -> counter.getAndIncrement())
                .add(() -> counter.getAndAdd(2))
                .add(Arrays.asList(
                        () -> counter.getAndAdd(3),
                        () -> counter.getAndAdd(4)
                )).close();
        assertEquals(10, counter.get());
    }

    @Test
    void testCloseOneIOException() {
        AtomicInteger counter = new AtomicInteger();
        IOException ioException = new IOException();
        try {
            new SafeResourceCloser()
                    .add(() -> counter.getAndIncrement())
                    .add(() -> {
                        throw ioException;
                    })
                    .add(() -> counter.getAndAdd(2))
                    .add(Arrays.asList(
                            () -> counter.getAndAdd(3),
                            () -> counter.getAndAdd(4)
                    )).close();
            fail();
        } catch (IOException e) {
            assertTrue(ioException == e);
        }
        assertEquals(10, counter.get());
    }

    @Test
    void testCloseMultipleIOExceptions() throws IOException {
        AtomicInteger counter = new AtomicInteger();
        IOException ioException0 = new IOException();
        IOException ioException1 = new IOException();
        try {
            new SafeResourceCloser()
                    .add(() -> counter.getAndIncrement())
                    .add(() -> {
                        throw ioException0;
                    })
                    .add(() -> counter.getAndAdd(2))
                    .add(Arrays.asList(
                            () -> counter.getAndAdd(3),
                            () -> {
                                throw ioException1;
                            },
                            () -> counter.getAndAdd(4)
                    )).close();
            fail();
        } catch (MultipleResourcesIOException e) {
            assertEquals(Arrays.asList(ioException0, ioException1), e.getIOExceptions());
            assertEquals(Arrays.asList(), e.getRuntimeExceptions());
        }
        assertEquals(10, counter.get());
    }

    @Test
    void testCloseOneRuntimeException() throws IOException {
        AtomicInteger counter = new AtomicInteger();
        NumberFormatException numberFormatException = new NumberFormatException();
        try {
            new SafeResourceCloser()
                    .add(() -> counter.getAndIncrement())
                    .add(() -> {
                        throw numberFormatException;
                    })
                    .add(() -> counter.getAndAdd(2))
                    .add(Arrays.asList(
                            () -> counter.getAndAdd(3),
                            () -> counter.getAndAdd(4)
                    )).close();
            fail();
        } catch (NumberFormatException e) {
            assertTrue(numberFormatException == e);
        }
        assertEquals(10, counter.get());
    }

    @Test
    void testCloseMultipleRuntimeExceptions() throws IOException {
        AtomicInteger counter = new AtomicInteger();
        NumberFormatException numberFormatException = new NumberFormatException();
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException();
        try {
            new SafeResourceCloser()
                    .add(() -> counter.getAndIncrement())
                    .add(() -> {
                        throw numberFormatException;
                    })
                    .add(() -> counter.getAndAdd(2))
                    .add(Arrays.asList(
                            () -> counter.getAndAdd(3),
                            () -> {
                                throw illegalArgumentException;
                            },
                            () -> counter.getAndAdd(4)
                    )).close();
            fail();
        } catch (MultipleResourcesRuntimeException e) {
            assertEquals(Arrays.asList(numberFormatException, illegalArgumentException), e.getRuntimeExceptions());
        }
        assertEquals(10, counter.get());
    }

    @Test
    void testCloseMultipleIOExceptionsOneRuntimeException() throws IOException {
        AtomicInteger counter = new AtomicInteger();
        IOException ioException0 = new IOException();
        IOException ioException1 = new IOException();
        NumberFormatException numberFormatException = new NumberFormatException();
        try {
            new SafeResourceCloser()
                    .add(() -> counter.getAndIncrement())
                    .add(() -> {
                        throw ioException0;
                    })
                    .add(() -> counter.getAndAdd(2))
                    .add(Arrays.asList(
                            () -> {
                                throw numberFormatException;
                            },
                            () -> counter.getAndAdd(3),
                            () -> {
                                throw ioException1;
                            },
                            () -> counter.getAndAdd(4)
                    )).close();
            fail();
        } catch (MultipleResourcesIOException e) {
            assertEquals(Arrays.asList(ioException0, ioException1), e.getIOExceptions());
            assertEquals(Arrays.asList(numberFormatException), e.getRuntimeExceptions());
        }
        assertEquals(10, counter.get());
    }

    @Test
    void testCloseOneIOExceptionOneRuntimeException() throws IOException {
        AtomicInteger counter = new AtomicInteger();
        IOException ioException = new IOException();
        NumberFormatException numberFormatException = new NumberFormatException();
        try {
            new SafeResourceCloser()
                    .add(() -> counter.getAndIncrement())
                    .add(() -> {
                        throw ioException;
                    })
                    .add(() -> counter.getAndAdd(2))
                    .add(Arrays.asList(
                            () -> {
                                throw numberFormatException;
                            },
                            () -> counter.getAndAdd(3),
                            () -> counter.getAndAdd(4)
                    )).close();
            fail();
        } catch (MultipleResourcesIOException e) {
            assertEquals(Arrays.asList(ioException), e.getIOExceptions());
            assertEquals(Arrays.asList(numberFormatException), e.getRuntimeExceptions());
        }
        assertEquals(10, counter.get());
    }

    @Test
    void testCloseMultipleIOExceptionsMultipleRuntimeExceptions() throws IOException {
        closeMultipleIOExceptionsMultipleRuntimeExceptions();
    }

    @Test
    void testCloseOneIOExceptionMultipleRuntimeExceptions() throws IOException {
        AtomicInteger counter = new AtomicInteger();
        IOException ioException = new IOException();
        NumberFormatException numberFormatException = new NumberFormatException();
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException();
        try {
            new SafeResourceCloser()
                    .add(() -> counter.getAndIncrement())
                    .add(() -> {
                        throw ioException;
                    })
                    .add(() -> counter.getAndAdd(2))
                    .add(Arrays.asList(
                            () -> {
                                throw numberFormatException;
                            },
                            () -> counter.getAndAdd(3),
                            () -> {
                                throw illegalArgumentException;
                            },
                            () -> counter.getAndAdd(4)
                    )).close();
            fail();
        } catch (MultipleResourcesIOException e) {
            assertEquals(Arrays.asList(ioException), e.getIOExceptions());
            assertEquals(Arrays.asList(numberFormatException, illegalArgumentException), e.getRuntimeExceptions());
        }
        assertEquals(10, counter.get());
    }

    @Test
    void testCloseTwice() throws IOException {
        SafeResourceCloser safeResourceCloser = closeMultipleIOExceptionsMultipleRuntimeExceptions();
        safeResourceCloser.close();
    }

    private SafeResourceCloser closeMultipleIOExceptionsMultipleRuntimeExceptions() throws IOException {
        AtomicInteger counter = new AtomicInteger();
        IOException ioException0 = new IOException();
        IOException ioException1 = new IOException();
        NumberFormatException numberFormatException = new NumberFormatException();
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException();
        SafeResourceCloser safeResourceCloser = null;
        try {
            safeResourceCloser = new SafeResourceCloser()
                    .add(() -> {
                        throw numberFormatException;
                    })
                    .add(() -> counter.getAndIncrement())
                    .add(() -> {
                        throw ioException0;
                    })
                    .add(() -> counter.getAndAdd(2))
                    .add(Arrays.asList(
                            () -> {
                                throw ioException1;
                            },
                            () -> counter.getAndAdd(3),
                            () -> {
                                throw illegalArgumentException;
                            },
                            () -> counter.getAndAdd(4)
                    ));
            safeResourceCloser.close();
            fail();
        } catch (MultipleResourcesIOException e) {
            assertEquals(Arrays.asList(ioException0, ioException1), e.getIOExceptions());
            assertEquals(Arrays.asList(numberFormatException, illegalArgumentException), e.getRuntimeExceptions());
        }
        assertEquals(10, counter.get());
        return safeResourceCloser;
    }
}
