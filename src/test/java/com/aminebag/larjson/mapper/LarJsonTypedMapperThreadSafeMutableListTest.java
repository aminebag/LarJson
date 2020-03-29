package com.aminebag.larjson.mapper;

import com.aminebag.larjson.exception.LarJsonException;
import com.aminebag.larjson.configuration.LarJsonTypedReadConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;

import static com.aminebag.larjson.mapper.LarJsonMapperThreadSafeListTestUtils.waitForAsyncTask;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Amine Bagdouri
 */
public class LarJsonTypedMapperThreadSafeMutableListTest extends LarJsonTypedMapperMutableListTest {

    @Test
    void testListIteratorRemove(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            Iterator<String> iterator = list.iterator();
            testIteratorRemove(iterator);
        });
    }

    @Test
    void testListListIteratorRemove(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            ListIterator<String> iterator = list.listIterator();
            testIteratorRemove(iterator);
        });
    }

    @Test
    void testListListIteratorWithIndexRemove(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            ListIterator<String> iterator = list.listIterator(1);
            testIteratorRemove(iterator);
        });
    }

    @Test
    void testListListIteratorAdd(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            ListIterator<String> iterator = list.listIterator();
            testIteratorAdd(iterator);
        });
    }

    @Test
    void testListListIteratorWithIndexAdd(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            ListIterator<String> iterator = list.listIterator(2);
            testIteratorAdd(iterator);
        });
    }

    @Test
    void testListListIteratorSet(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            ListIterator<String> iterator = list.listIterator();
            testIteratorSet(iterator);
        });
    }

    @Test
    void testListListIteratorWithIndexSet(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            ListIterator<String> iterator = list.listIterator(2);
            testIteratorSet(iterator);
        });
    }

    @Test
    void testThreadSafeListAdd(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            assertTrue(list.add("yes"));
            waitForAsyncTask(list, ()->{
                assertEquals(Arrays.asList("hello", "salut", null, "null", "salut", "yes"), list);
                assertTrue(list.add("no"));
            });
            assertEquals(Arrays.asList("hello", "salut", null, "null", "salut", "yes", "no"), list);
        });
    }

    @Test
    void testThreadSafeListAddWithIndex(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            list.add(2, "yes");
            waitForAsyncTask(list, ()->{
                assertEquals(Arrays.asList("hello", "salut", "yes", null, "null", "salut"), list);
                list.add(1, "no");
            });
            assertEquals(Arrays.asList("hello", "no", "salut", "yes", null, "null", "salut"), list);
        });
    }

    @Test
    void testThreadSafeListRemove(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            assertTrue(list.remove("null"));
            waitForAsyncTask(list, ()->{
                assertEquals(Arrays.asList("hello", "salut", null, "salut"), list);
                assertTrue(list.remove("salut"));
            });
            assertEquals(Arrays.asList("hello", null, "salut"), list);
        });
    }

    @Test
    void testThreadSafeListRemoveWithIndex(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            assertEquals("null", list.remove(3));
            waitForAsyncTask(list, ()->{
                assertEquals(Arrays.asList("hello", "salut", null, "salut"), list);
                assertEquals("salut", list.remove(1));
            });
            assertEquals(Arrays.asList("hello", null, "salut"), list);
        });
    }

    @Test
    void testThreadSafeListAddAll(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            assertTrue(list.addAll(Arrays.asList("yes", "no")));
            waitForAsyncTask(list, ()->{
                assertEquals(Arrays.asList("hello", "salut", null, "null", "salut", "yes", "no"), list);
                assertTrue(list.addAll(Arrays.asList("hi", "you")));
            });
            assertEquals(Arrays.asList("hello", "salut", null, "null", "salut", "yes", "no", "hi", "you"), list);
        });
    }

    @Test
    void testThreadSafeListAddAllWithIndex(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            assertTrue(list.addAll(1, Arrays.asList("yes", "no")));
            waitForAsyncTask(list, ()->{
                assertEquals(Arrays.asList("hello", "yes", "no", "salut", null, "null", "salut"), list);
                assertTrue(list.addAll(4, Arrays.asList("hi", "you")));
            });
            assertEquals(Arrays.asList("hello", "yes", "no", "salut", "hi", "you", null, "null", "salut"), list);
        });
    }

    @Test
    void testThreadSafeListRemoveAll(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            assertTrue(list.removeAll(Arrays.asList("salut", "bonjour")));
            waitForAsyncTask(list, ()->{
                assertEquals(Arrays.asList("hello", null, "null"), list);
                assertTrue(list.removeAll(Arrays.asList("null")));
            });
            assertEquals(Arrays.asList("hello", null), list);
        });
    }

    @Test
    void testThreadSafeListRetainAll(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            assertTrue(list.retainAll(Arrays.asList("salut", "hello")));
            waitForAsyncTask(list, ()->{
                assertEquals(Arrays.asList("hello", "salut", "salut"), list);
                assertTrue(list.retainAll(Arrays.asList("salut", null)));
            });
            assertEquals(Arrays.asList("salut", "salut"), list);
        });
    }

    @Test
    void testThreadSafeListReplaceAll(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            list.replaceAll(s->s==null ? "nada" : s.charAt(0) + "");
            waitForAsyncTask(list, ()->{
                assertEquals(Arrays.asList("h", "s", "nada", "n", "s"), list);
                list.replaceAll(s->s+"i");
            });
            assertEquals(Arrays.asList("hi", "si", "nadai", "ni", "si"), list);
        });
    }

    @Test
    void testThreadSafeListSortNull(@TempDir Path tempDir) throws IOException, LarJsonException {
        testNullFreeList(tempDir, list->{
            list.sort(null);
            waitForAsyncTask(list, ()->{
                assertEquals(Arrays.asList("hello", "null", "salut", "salut"), list);
                list.set(0, "zebra");
                list.sort(null);
            });
            assertEquals(Arrays.asList("null", "salut", "salut", "zebra"), list);
        });
    }

    @Test
    void testThreadSafeListSortComparator(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            list.sort(Comparator.nullsLast(Comparator.naturalOrder()));
            waitForAsyncTask(list, ()->{
                assertEquals(Arrays.asList("hello", "null", "salut", "salut", null), list);
                list.set(0, "zebra");
                list.set(2, null);
                list.sort(Comparator.nullsLast(Comparator.naturalOrder()));
            });
            assertEquals(Arrays.asList("null", "salut", "zebra", null, null), list);
        });
    }

    @Test
    void testThreadSafeListClear(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            list.clear();
            list.add("first");
            waitForAsyncTask(list, ()->{
                assertEquals(Arrays.asList("first"), list);
                list.clear();
            });
            assertEquals(Arrays.asList(), list);
        });
    }

    @Test
    void testThreadSafeListSet(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            assertEquals("null", list.set(3, "bonjour"));
            waitForAsyncTask(list, ()->{
                assertEquals(Arrays.asList("hello", "salut", null, "bonjour", "salut"), list);
                list.set(0, "zebra");
            });
            assertEquals(Arrays.asList("zebra", "salut", null, "bonjour", "salut"), list);
        });
    }

    @Test
    void testThreadSafeListRemoveIf(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            assertTrue(list.removeIf(s->"null".equals(s)));
            waitForAsyncTask(list, ()->{
                assertEquals(Arrays.asList("hello", "salut", null, "salut"), list);
                assertTrue(list.removeIf(s->"salut".equals(s)));
            });
            assertEquals(Arrays.asList("hello", null), list);
        });
    }

    @Test
    void testThreadSafeListGet(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListGet());
    }

    @Test
    void testThreadSafeListSize(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListSize());
    }

    @Test
    void testThreadSafeListIsEmpty(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListIsNotEmpty());
    }

    @Test
    void testThreadSafeEmptyListIsEmpty(@TempDir Path tempDir) throws IOException, LarJsonException {
        testEmptyList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListIsEmpty());
    }

    @Test
    void testThreadSafeListContains(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListContains());
    }

    @Test
    void testThreadSafeListIteratorNext(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListIteratorNext());
    }

    @Test
    void testThreadSafeListIteratorForEachReamaining(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListIteratorForEachReamaining());
    }

    @Test
    void testThreadSafeListToArray(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListToArray());
    }

    @Test
    void testThreadSafeListToArrayWithSmallArrayArg(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListToArrayWithSmallArrayArg());
    }

    @Test
    void testThreadSafeListToArrayWithBigArrayArg(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListToArrayWithBigArrayArg());
    }

    @Test
    void testThreadSafeListContainsAll(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListContainsAll());
    }

    @Test
    void testThreadSafeListEquals(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListEquals());
    }

    @Test
    void testThreadSafeListHashCode(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListHashCode());
    }

    @Test
    void testThreadSafeListIndexOf(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListIndexOf());
    }

    @Test
    void testThreadSafeListLastIndexOf(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListLastIndexOf());
    }

    @Test
    void testThreadSafeListListIteratorNext(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListListIteratorNext());
    }

    @Test
    void testThreadSafeListForEachRemaining(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListForEachRemaining());
    }

    @Test
    void testThreadSafeListListIteratorPrevious(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListListIteratorPrevious());
    }

    @Test
    void testThreadSafeListListIteratorNextIndex(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListListIteratorNextIndex());
    }

    @Test
    void testThreadSafeListListIteratorPreviousIndex(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListListIteratorPreviousIndex());
    }

    @Test
    void testThreadSafeListListIteratorWithIndexNext(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListListIteratorWithIndexNext());
    }

    @Test
    void testThreadSafeListListIteratorWithIndexPrevious(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListListIteratorWithIndexPrevious());
    }

    @Test
    void testThreadSafeListListIteratorWithIndexNextIndex(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListListIteratorWithIndexNextIndex());
    }

    @Test
    void testThreadSafeListListIteratorWithIndexPreviousIndex(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListListIteratorWithIndexPreviousIndex());
    }

    @Test
    void testThreadSafeListListIteratorWithIndexForEachRemaining(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListListIteratorWithIndexForEachRemaining());
    }

    @Test
    void testThreadSafeListSubList(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListSubList());
    }

    @Test
    void testThreadSafeListSpliterator(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListSpliterator());
    }

    @Test
    void testThreadSafeListStream(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListStream());
    }

    @Test
    void testThreadSafeListToString(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListToString());
    }

    @Test
    void testThreadSafeListForEach(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, LarJsonMapperThreadSafeListTestUtils.testThreadSafeListForEach());
    }

    private void testIteratorRemove(Iterator<String> iterator) {
        assertNotNull(iterator);
        iterator.next();
        try {
            iterator.remove();
            fail();
        } catch (UnsupportedOperationException expected) {
        }
    }

    private void testIteratorAdd(ListIterator<String> iterator) {
        assertNotNull(iterator);
        iterator.next();
        try {
            iterator.add("hi");
            fail();
        } catch (UnsupportedOperationException expected) {
        }
    }

    private void testIteratorSet(ListIterator<String> iterator) {
        assertNotNull(iterator);
        iterator.next();
        try {
            iterator.set("hi");
            fail();
        } catch (UnsupportedOperationException expected) {
        }
    }

    @Override
    protected LarJsonTypedReadConfiguration configuration() {
        return new LarJsonTypedReadConfiguration.Builder().setMutable(true).setThreadSafe(true).build();
    }
}
