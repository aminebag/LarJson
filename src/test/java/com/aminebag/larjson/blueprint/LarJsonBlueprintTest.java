package com.aminebag.larjson.blueprint;

import com.aminebag.larjson.utils.TemporaryFileFactory;
import com.aminebag.larjson.resource.ResourceFactory;
import com.aminebag.larjson.utils.LongList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Amine Bagdouri
 */
public abstract class LarJsonBlueprintTest {

    private final static AtomicInteger fileCounter = new AtomicInteger(0);

    @Test
    void testBlueprintPutNegative(@TempDir Path tempDir) throws IOException {
        testBlueprintPutIllegalValue(tempDir, -1L);
    }

    @Test
    void testBlueprintPutZero(@TempDir Path tempDir) throws IOException {
        testBlueprintPut(tempDir, 0L);
    }

    @Test
    void testBlueprintPutByte(@TempDir Path tempDir) throws IOException {
        testBlueprintPut(tempDir, 87L);
    }

    @Test
    void testBlueprintPutMaxByte(@TempDir Path tempDir) throws IOException {
        testBlueprintPut(tempDir, Byte.MAX_VALUE);
    }

    @Test
    void testBlueprintPutShort(@TempDir Path tempDir) throws IOException {
        testBlueprintPut(tempDir, 8754);
    }

    @Test
    void testBlueprintPutMaxShort(@TempDir Path tempDir) throws IOException {
        testBlueprintPut(tempDir, Short.MAX_VALUE);
    }

    @Test
    void testBlueprintPutInt(@TempDir Path tempDir) throws IOException {
        testBlueprintPut(tempDir, 8_754_578);
    }

    @Test
    void testBlueprintPutMaxInteger(@TempDir Path tempDir) throws IOException {
        testBlueprintPut(tempDir, Integer.MAX_VALUE);
    }

    @Test
    void testBlueprintPutLong(@TempDir Path tempDir) throws IOException {
        testBlueprintPut(tempDir, 8_754_578_870_745L);
    }

    @Test
    void testBlueprintPutMaxSupportedLong(@TempDir Path tempDir) throws IOException {
        testBlueprintPut(tempDir, LarJsonBlueprintWriter.MAX_LONG_VALUE);
    }

    @Test
    void testBlueprintPutUnupportedLong(@TempDir Path tempDir) throws IOException {
        testBlueprintPutIllegalValue(tempDir, LarJsonBlueprintWriter.MAX_LONG_VALUE + 1);
    }

    @Test
    void testBlueprintPutMaxLong(@TempDir Path tempDir) throws IOException {
        testBlueprintPutIllegalValue(tempDir, Long.MAX_VALUE);
    }

    @Test
    void testBlueprintPutListZeros(@TempDir Path tempDir) throws IOException {
        testBlueprintPutList(tempDir, Arrays.asList(0L, 0L, 0L, 0L, 0L));
    }

    @Test
    void testBlueprintPutListSmallBytes(@TempDir Path tempDir) throws IOException {
        testBlueprintPutList(tempDir, Arrays.asList(6L, 1L, 32L, 15L, 0L));
    }

    @Test
    void testBlueprintPutListBigBytes(@TempDir Path tempDir) throws IOException {
        testBlueprintPutList(tempDir, Arrays.asList(112L, 1L, (long)Byte.MAX_VALUE, 120L, 0L));
    }

    @Test
    void testBlueprintPutListSmallShorts(@TempDir Path tempDir) throws IOException {
        testBlueprintPutList(tempDir, Arrays.asList(6L, 8571L, 325L, 15L, 0L));
    }

    @Test
    void testBlueprintPutListBigShorts(@TempDir Path tempDir) throws IOException {
        testBlueprintPutList(tempDir, Arrays.asList(27_580L, 1L, (long)Short.MAX_VALUE, 12_470L, 0L));
    }

    @Test
    void testBlueprintPutListSmallInts(@TempDir Path tempDir) throws IOException {
        testBlueprintPutList(tempDir, Arrays.asList(6L, 85_571L, 320_85L, 15_874_852L, 0L));
    }

    @Test
    void testBlueprintPutListBigInts(@TempDir Path tempDir) throws IOException {
        testBlueprintPutList(tempDir, Arrays.asList(1_580_582L, 1L, (long)Integer.MAX_VALUE, 2_000_000L, 0L));
    }

    @Test
    void testBlueprintPutListSmallLongs(@TempDir Path tempDir) throws IOException {
        testBlueprintPutList(tempDir, Arrays.asList(LarJsonBlueprintWriter.MAX_LONG_VALUE,
                7_127_885_571L, 320_85L, 87_127_885_571L, 0L));
    }

    @Test
    void testBlueprintPutListBigLongs(@TempDir Path tempDir) throws IOException {
        testBlueprintPutList(tempDir, Arrays.asList(LarJsonBlueprintWriter.MAX_LONG_VALUE + 1, 1L, Long.MAX_VALUE,
                2_000_000L, 0L));
    }

    @Test
    void testBlueprintPutListEmpty(@TempDir Path tempDir) throws IOException {
        testBlueprintPutList(tempDir, Arrays.asList());
    }

    @Test
    void testBlueprintPutMix(@TempDir Path tempDir) throws IOException {
        List<Long> list0 = Arrays.asList(1_580_582L, 1L, (long)Integer.MAX_VALUE, 2_000_000L, 0L);
        List<Long> list1 = Arrays.asList(112L, 1L, (long)Byte.MAX_VALUE, 120L, 0L);
        List<Long> list2 = Arrays.asList(0L, 0L, 0L, 0L, 0L);
        List<Long> list3 = Arrays.asList(LarJsonBlueprintWriter.MAX_LONG_VALUE + 1, 1L, Long.MAX_VALUE, 2_000_000L, 0L);
        List<Long> list4 = Arrays.asList(6L, 1L, 32L, 15L, 0L);
        List<Long> list5 = Arrays.asList();
        List<Long> list6 = Arrays.asList(LarJsonBlueprintWriter.MAX_LONG_VALUE, 7_127_885_571L, 320_85L,
                87_127_885_571L, 0L);
        List<Long> list7 = Arrays.asList(6L, 85_571L, 320_85L, 15_874_852L, 0L);
        List<Long> list8 = Arrays.asList(6L, 8571L, 325L, 15L, 0L);
        List<Long> list9 = Arrays.asList(27_580L, 1L, (long)Short.MAX_VALUE, 12_470L, 0L);
        testBlueprintPut(tempDir, blueprintWriter->{
                    blueprintWriter.put(87L);
                    blueprintWriter.put(LarJsonBlueprintWriter.MAX_LONG_VALUE);
                    blueprintWriterPutList(blueprintWriter, list0);
                    blueprintWriter.put(8_754_578_870_745L);
                    blueprintWriterPutList(blueprintWriter, list1);
                    blueprintWriterPutList(blueprintWriter, list2);
                    blueprintWriter.put(8754);
                    blueprintWriter.put(0L);
                    blueprintWriter.put(Integer.MAX_VALUE);
                    blueprintWriterPutList(blueprintWriter, list3);
                    blueprintWriterPutList(blueprintWriter, list4);
                    blueprintWriterPutList(blueprintWriter, list5);
                    blueprintWriterPutList(blueprintWriter, list6);
                    blueprintWriter.put(8_754_578);
                    blueprintWriterPutList(blueprintWriter, list7);
                    blueprintWriter.put(Short.MAX_VALUE);
                    blueprintWriter.put(Byte.MAX_VALUE);
                    blueprintWriterPutList(blueprintWriter, list8);
                    blueprintWriterPutList(blueprintWriter, list9);
                },
                blueprintReader-> {
                    assertBlueprintReaderGetList(blueprintReader, list9);
                    assertBlueprintReaderGetList(blueprintReader, list8);
                    assertEquals(Byte.MAX_VALUE, blueprintReader.get());
                    assertEquals(Short.MAX_VALUE, blueprintReader.get());
                    assertBlueprintReaderGetList(blueprintReader, list7);
                    assertEquals(8_754_578, blueprintReader.get());
                    assertBlueprintReaderGetList(blueprintReader, list6);
                    assertBlueprintReaderGetList(blueprintReader, list5);
                    assertBlueprintReaderGetList(blueprintReader, list4);
                    assertBlueprintReaderGetList(blueprintReader, list3);
                    assertEquals(Integer.MAX_VALUE, blueprintReader.get());
                    assertEquals(0L, blueprintReader.get());
                    assertEquals(8754, blueprintReader.get());
                    assertBlueprintReaderGetList(blueprintReader, list2);
                    assertBlueprintReaderGetList(blueprintReader, list1);
                    assertEquals(8_754_578_870_745L, blueprintReader.get());
                    assertBlueprintReaderGetList(blueprintReader, list0);
                    assertEquals(LarJsonBlueprintWriter.MAX_LONG_VALUE, blueprintReader.get());
                    assertEquals(87L, blueprintReader.get());
                }, true);
    }

    @Test
    void testBlueprintPutManyValues(@TempDir Path tempDir) throws IOException {
        int iterations = 20_000;
        List<Long> values = new ArrayList<>(iterations);
        Random random = new Random();
        for(int i = 0; i<iterations; i++) {
            values.add((long)(random.nextDouble() * (LarJsonBlueprintWriter.MAX_LONG_VALUE + 1)));
        }
        testBlueprintPut(tempDir, blueprintWriter -> {
            for(long value : values) {
                blueprintWriter.put(value);
            }
        }, blueprintReader -> {
            for(int i = 0; i<iterations; i++) {
                assertEquals(values.get(values.size() - i - 1), blueprintReader.get(), "i="+i);
            }
        }, true);
    }

    @Test
    void testBlueprintReadMiddle(@TempDir Path tempDir) throws IOException {
        testBlueprintReadMiddle(tempDir, 11_384);
    }

    @Test
    void testBlueprintReadNearEnd(@TempDir Path tempDir) throws IOException {
        testBlueprintReadMiddle(tempDir, 19_987);
    }

    @Test
    void testBlueprintReadNearStart(@TempDir Path tempDir) throws IOException {
        testBlueprintReadMiddle(tempDir, 6);
    }

    @Test
    void testBlueprintReaderNegativePosition(@TempDir Path tempDir) throws IOException {
        testBlueprintPut(tempDir, blueprintWriter -> {
            blueprintWriter.put(7_875_140L);
        }, blueprintReader -> {
            try {
                blueprintReader.position(-1L);
                fail();
            } catch (IllegalArgumentException expected) {
            }
            blueprintReader.position(0L);
        }, false);
    }

    @Test
    void testBlueprintReaderOverflowPosition(@TempDir Path tempDir) throws IOException {
        testBlueprintPut(tempDir, blueprintWriter -> {
            blueprintWriter.put(7_875_140L);
        }, blueprintReader -> {
            try {
                blueprintReader.position(blueprintReader.length() + 1);
                fail();
            } catch (IllegalArgumentException expected) {
            }
            blueprintReader.position(0L);
        }, false);
    }

    @Test
    void testBlueprintReaderInvalidListElementSize(@TempDir Path tempDir) throws IOException {
        byte[] array = new byte[]{6, 7, 12, 88, 32, 7, 5};
        try(LarJsonBlueprintReader blueprintReader = blueprintReader(array, tempDir)) {
            try {
                blueprintReader.position(blueprintReader.length());
                blueprintReader.getList();
                fail();
            } catch (IllegalArgumentException expected) {
            }
        }
    }

    protected abstract LarJsonBlueprintReader blueprintReader(byte[] array, Path tempDir) throws IOException;

    private void testBlueprintReadMiddle(Path tempDir, int startRead) throws IOException {
        int iterations = 20_000;
        List<Long> values = new ArrayList<>(iterations);
        for(int i = 0; i<iterations; i++) {
            values.add((long)i);
        }
        AtomicLong position = new AtomicLong(-1L);
        testBlueprintPut(tempDir, blueprintWriter -> {
            for(int i=0;i<values.size(); i++) {
                long value = values.get(i);
                if(i == startRead) {
                    position.set(blueprintWriter.position());
                }
                blueprintWriter.put(value);
            }
        }, blueprintReader -> {
            blueprintReader.position(blueprintReader.length());
            assertEquals(iterations-1, blueprintReader.get());
            blueprintReader.position(position.get());
            for(int i = iterations - startRead; i<iterations; i++) {
                assertEquals(values.get(values.size() - i - 1), blueprintReader.get(), "i="+i);
            }
        }, false);
    }

    private void testBlueprintPut(Path tempDir, IOConsumer<LarJsonBlueprintWriter> putAction,
                                       IOConsumer<LarJsonBlueprintReader> getAction, boolean readFromTheStart)
            throws IOException {
        AtomicReference<File> fileReference = new AtomicReference<>(null);
        ResourceFactory<? extends LarJsonBlueprintReader> readerFactory;
        try(LarJsonBlueprintWriter blueprintWriter = new LarJsonBlueprintWriter(maxMemoryBlueprintSize(),
                () -> {
                    File file = fileFactory(tempDir).createTemporaryFile();
                    fileReference.set(file);
                    return file;
                })) {
            putAction.accept(blueprintWriter);
            assertTrue(blueprintWriter.position() > 0);
            readerFactory = blueprintWriter.getReaderFactory();
        }
        try {
            try (LarJsonBlueprintReader blueprintReader = readerFactory.create()) {
                if(readFromTheStart) {
                    try {
                        getAction.accept(blueprintReader);
                        fail();
                    } catch (IndexOutOfBoundsException expected) {
                    }
                    blueprintReader.position(blueprintReader.length());
                }
                getAction.accept(blueprintReader);
                try {
                    blueprintReader.get();
                    fail();
                } catch (IndexOutOfBoundsException expected) {
                }
                try {
                    blueprintReader.getList();
                    fail();
                } catch (IndexOutOfBoundsException expected) {
                }
                assertEquals(0, blueprintReader.position());
            }
            assertBlueprintFile(fileReference.get());
        } finally {
            readerFactory.close();
        }
        if(fileReference.get() != null) {
            assertFalse(fileReference.get().exists());
        }
    }

    private void testBlueprintPut(Path tempDir, long value) throws IOException {
        testBlueprintPut(tempDir, blueprintWriter->blueprintWriter.put(value),
                blueprintReader->assertEquals(value, blueprintReader.get()), true);
    }

    private void blueprintWriterPutList(LarJsonBlueprintWriter blueprintWriter, List<Long> list) throws IOException {
        long max = list.stream().max(Comparator.naturalOrder()).orElse(0L);
        blueprintWriter.putList(list, max);
    }

    private void assertBlueprintReaderGetList(LarJsonBlueprintReader blueprintReader, List<Long> list) throws IOException {
        LongList longList = blueprintReader.getList();
        assertEquals(list.size(), longList.size());
        for(int i=0; i<list.size(); i++) {
            assertEquals(list.get(i), longList.get(i));
        }
        try {
            longList.get(-1);
            fail();
        } catch (IndexOutOfBoundsException expected) {
        }
        try {
            longList.get(list.size());
            fail();
        } catch (IndexOutOfBoundsException expected) {
        }
    }

    private void testBlueprintPutList(Path tempDir, List<Long> list) throws IOException {
        testBlueprintPut(tempDir, blueprintWriter->blueprintWriterPutList(blueprintWriter, list),
                blueprintReader-> {
                    assertBlueprintReaderGetList(blueprintReader, list);
                }, true);
    }

    private void testBlueprintPutIllegalValue(Path tempDir, IOConsumer<LarJsonBlueprintWriter> action)
            throws IOException {
        try(LarJsonBlueprintWriter blueprintWriter = new LarJsonBlueprintWriter(
                maxMemoryBlueprintSize(), fileFactory(tempDir))) {
            try {
                action.accept(blueprintWriter);
                fail();
            } catch (IllegalArgumentException expected) {
            }
        }
    }

    private void testBlueprintPutIllegalValue(Path tempDir, long value) throws IOException {
        testBlueprintPutIllegalValue(tempDir, blueprintWriter->blueprintWriter.put(value));
    }

    protected abstract int maxMemoryBlueprintSize();

    protected abstract void assertBlueprintFile(File file);

    protected static TemporaryFileFactory fileFactory(Path tempDir) {
        return () -> tempDir.resolve("test" + fileCounter.getAndIncrement() + ".larjson").toFile();
    }

    interface IOConsumer<T> {
        void accept(T t) throws IOException;
    }
}
