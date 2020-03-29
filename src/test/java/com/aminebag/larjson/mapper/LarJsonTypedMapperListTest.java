package com.aminebag.larjson.mapper;

import com.aminebag.larjson.exception.LarJsonException;
import com.aminebag.larjson.configuration.LarJsonTypedReadConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.aminebag.larjson.mapper.LarJsonMapperTestUtils.jsonToFile;
import static com.aminebag.larjson.mapper.LarJsonTypedMapperTestModels.ModelWithStringLarJsonList;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Amine Bagdouri
 */
public abstract class LarJsonTypedMapperListTest {

    @Test
    void testListGet(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            assertEquals("hello", list.get(0));
            assertEquals("salut", list.get(1));
            assertEquals(null, list.get(2));
            assertEquals("null", list.get(3));
            assertEquals("salut", list.get(4));
            assertListIsValid(list);
        });
    }

    @Test
    void testListSize(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            assertEquals(5, list.size());
            assertListIsValid(list);
        });
    }

    @Test
    void testListIsEmpty(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            assertFalse(list.isEmpty());
            assertListIsValid(list);
        });
    }

    @Test
    void testEmptyListIsEmpty(@TempDir Path tempDir) throws IOException, LarJsonException {
        testEmptyList(tempDir, list->{
            assertTrue(list.isEmpty());
            assertEmptyListIsValid(list);
        });
    }

    @Test
    void testListContains(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            assertTrue(list.contains("salut"));
            assertFalse(list.contains("bonjour"));
            assertListIsValid(list);
        });
    }

    @Test
    void testListIteratorNext(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            Iterator<String> iterator = list.iterator();
            testIteratorNext(list, iterator);
        });
    }

    @Test
    void testListIteratorForEachReamaining(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            Iterator<String> iterator = list.iterator();
            testIteratorForEachRemaining(iterator);
        });
    }

    @Test
    void testListToArray(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            Object[] toArray = list.toArray();
            assertNotNull(toArray);
            assertEquals(validFilledList(), Arrays.asList(toArray));
            assertListIsValid(list);
        });
    }

    @Test
    void testListToArrayWithSmallArrayArg(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            String[] toArray = list.toArray(new String[1]);
            assertNotNull(toArray);
            assertEquals(validFilledList(), Arrays.asList(toArray));
            assertListIsValid(list);
        });
    }

    @Test
    void testListToArrayWithBigArrayArg(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            String[] toArray = list.toArray(new String[6]);
            assertNotNull(toArray);
            assertEquals(Arrays.asList("hello", "salut", null, "null", "salut", null), Arrays.asList(toArray));
            assertListIsValid(list);
        });
    }

    @Test
    void testListContainsAll(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            assertTrue(list.containsAll(new HashSet<>()));
            assertTrue(list.containsAll(Arrays.asList(null, "null", "hello")));
            assertTrue(list.containsAll(validFilledList()));
            assertFalse(list.containsAll(Arrays.asList("bonjour", "null", "hello")));
            assertListIsValid(list);
        });
    }

    @Test
    void testListEquals(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            assertTrue(validFilledList().equals(list));
            assertTrue(list.equals(validFilledList()));
            assertFalse(Arrays.asList("salut", "hello", null, "null", "salut").equals(list));
            assertFalse(list.equals(Arrays.asList("salut", "hello", null, "null", "salut")));
            assertListIsValid(list);
        });
    }

    @Test
    void testListEqualsSelf(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            assertTrue(list.equals(list));
            assertListIsValid(list);
        });
    }

    @Test
    void testListHashCode(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            assertEquals(validFilledList().hashCode(), list.hashCode());
            assertListIsValid(list);
        });
    }

    @Test
    void testEmptyListHashCode(@TempDir Path tempDir) throws IOException, LarJsonException {
        testEmptyList(tempDir, list->{
            assertEquals(Collections.emptyList().hashCode(), list.hashCode());
            assertEmptyListIsValid(list);
        });
    }

    @Test
    void testListIndexOf(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            assertEquals(2, list.indexOf(null));
            assertEquals(1, list.indexOf("salut"));
            assertEquals(0, list.indexOf("hello"));
            assertEquals(3, list.indexOf("null"));
            assertEquals(-1, list.indexOf("bonjour"));
            assertListIsValid(list);
        });
    }

    @Test
    void testListLastIndexOf(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            assertEquals(2, list.lastIndexOf(null));
            assertEquals(4, list.lastIndexOf("salut"));
            assertEquals(0, list.lastIndexOf("hello"));
            assertEquals(3, list.lastIndexOf("null"));
            assertEquals(-1, list.lastIndexOf("bonjour"));
            assertListIsValid(list);
        });
    }

    @Test
    void testListListIteratorNext(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            ListIterator<String> iterator = list.listIterator();
            testIteratorNext(list, iterator);
        });
    }

    @Test
    void testListListIteratorForEachRemaining(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            ListIterator<String> iterator = list.listIterator();
            testIteratorForEachRemaining(iterator);
        });
    }

    @Test
    void testListListIteratorPrevious(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            ListIterator<String> iterator = list.listIterator();
            testListIteratorPrevious(list, iterator);
        });
    }

    @Test
    void testListListIteratorNextIndex(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            ListIterator<String> iterator = list.listIterator();
            assertEquals(0, iterator.nextIndex());
            iterator.next();
            assertEquals(1, iterator.nextIndex());
            iterator.next();
            assertEquals(2, iterator.nextIndex());
            iterator.previous();
            assertEquals(1, iterator.nextIndex());
            while(iterator.hasNext()) {
                iterator.next();
            }
            assertEquals(5, iterator.nextIndex());
            assertListIsValid(list);
        });
    }

    @Test
    void testListListIteratorPreviousIndex(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            ListIterator<String> iterator = list.listIterator();
            assertEquals(-1, iterator.previousIndex());
            iterator.next();
            assertEquals(0, iterator.previousIndex());
            iterator.next();
            assertEquals(1, iterator.previousIndex());
            iterator.previous();
            assertEquals(0, iterator.previousIndex());
            while(iterator.hasNext()) {
                iterator.next();
            }
            assertEquals(4, iterator.previousIndex());
            assertListIsValid(list);
        });
    }

    @Test
    void testListListIteratorWithIndexNext(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            ListIterator<String> iterator = list.listIterator(3);
            assertNotNull(iterator);
            assertTrue(iterator.hasNext());
            assertEquals("null", iterator.next());
            assertTrue(iterator.hasNext());
            assertEquals("salut", iterator.next());
            assertFalse(iterator.hasNext());
            try {
                iterator.next();
                fail();
            } catch (NoSuchElementException expected) {
            }
            assertListIsValid(list);
        });
    }

    @Test
    void testListListIteratorWithIndexPrevious(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            ListIterator<String> iterator = list.listIterator(3);
            assertNotNull(iterator);
            assertTrue(iterator.hasPrevious());
            assertEquals(null, iterator.previous());
            assertTrue(iterator.hasPrevious());
            assertEquals("salut", iterator.previous());
            assertTrue(iterator.hasPrevious());
            assertEquals("hello", iterator.previous());
            testListIteratorPrevious(list, iterator);
        });
    }

    @Test
    void testListListIteratorWithIndexNextIndex(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            ListIterator<String> iterator = list.listIterator(2);
            assertEquals(2, iterator.nextIndex());
            iterator.next();
            assertEquals(3, iterator.nextIndex());
            iterator.next();
            assertEquals(4, iterator.nextIndex());
            iterator.previous();
            assertEquals(3, iterator.nextIndex());
            while(iterator.hasNext()) {
                iterator.next();
            }
            assertEquals(5, iterator.nextIndex());
            assertListIsValid(list);
        });
    }

    @Test
    void testListListIteratorWithIndexPreviousIndex(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            ListIterator<String> iterator = list.listIterator(2);
            assertEquals(1, iterator.previousIndex());
            iterator.next();
            assertEquals(2, iterator.previousIndex());
            iterator.next();
            assertEquals(3, iterator.previousIndex());
            iterator.previous();
            assertEquals(2, iterator.previousIndex());
            while(iterator.hasPrevious()) {
                iterator.previous();
            }
            assertEquals(-1, iterator.previousIndex());
            assertListIsValid(list);
        });
    }

    @Test
    void testListListIteratorWithIndexForEachRemaining(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            ListIterator<String> iterator = list.listIterator(3);
            assertNotNull(iterator);
            iterator.previous();
            List<String> otherList = new ArrayList<>();
            iterator.forEachRemaining(e->{
                otherList.add(e);
            });
            assertFalse(iterator.hasNext());
            assertEquals(Arrays.asList(null, "null", "salut"), otherList);
            while(iterator.hasPrevious()) {
                iterator.previous();
            }
            testIteratorForEachRemaining(iterator);
        });
    }

    @Test
    void testListSubList(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            List<String> subList = list.subList(1,4);
            assertEquals(Arrays.asList("salut", null, "null"), subList);
            assertListIsValid(list);
        });
    }

    @Test
    void testListSpliterator(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            Spliterator<String> spliterator = list.spliterator();
            assertEquals(5L, spliterator.getExactSizeIfKnown());
            spliterator.tryAdvance(e->{
                assertEquals("hello", e);
            });
            List<String> otherList = new ArrayList<>();
            spliterator.forEachRemaining(e->{
                otherList.add(e);
            });
            assertFalse(spliterator.tryAdvance(e->{
                fail();
            }));
            assertEquals(Arrays.asList("salut", null, "null", "salut"), otherList);
            spliterator.trySplit();
            spliterator.estimateSize();
        });
    }

    @Test
    void testListStream(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            Stream<String> stream = list.stream();
            testStream(list, stream);
        });
    }

    @Test
    void testListParallelStream(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            Stream<String> stream = list.parallelStream();
            testStream(list, stream);
        });
    }

    @Test
    void testListToString(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            assertEquals("[hello, salut, null, null, salut]", list.toString());
            assertListIsValid(list);
        });
    }

    @Test
    void testListForEach(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            List<String> otherList = new ArrayList<>();
            list.forEach(s->otherList.add(s));
            assertEquals(validFilledList(), otherList);
            assertListIsValid(list);
        });
    }

    public static List<String> validFilledList() {
        return Arrays.asList("hello", "salut", null, "null", "salut");
    }

    public static List<String> validFilledNullFreeList() {
        return Arrays.asList("hello", "salut", "null", "salut");
    }

    public static void assertListIsValid(List<String> list) {
        assertEquals(validFilledList(), list);
    }

    public static void assertEmptyListIsValid(List<String> list) {
        assertEquals(Collections.emptyList(), list);
    }

    public static void assertNullFreeListIsValid(List<String> list) {
        assertEquals(validFilledNullFreeList(), list);
    }

    protected final void testList(@TempDir Path tempDir, Consumer<List<String>> test)
            throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithStringLarJsonList> mapper =
                new LarJsonTypedMapper<>(ModelWithStringLarJsonList.class, configuration());
        String json = defaultJsonArray();
        try (ModelWithStringLarJsonList model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertNotNull(model.getWhatever());
            test.accept(defaultList(model.getWhatever()));
        }
    }

    protected final void testEmptyList(@TempDir Path tempDir, Consumer<List<String>> test)
            throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithStringLarJsonList> mapper =
                new LarJsonTypedMapper<>(ModelWithStringLarJsonList.class, configuration());
        String json = emptyJsonArray();
        try (ModelWithStringLarJsonList model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertNotNull(model.getWhatever());
            test.accept(emptyList(model.getWhatever()));
        }
    }

    protected final void testNullFreeList(@TempDir Path tempDir, Consumer<List<String>> test)
            throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithStringLarJsonList> mapper =
                new LarJsonTypedMapper<>(ModelWithStringLarJsonList.class, configuration());
        String json = nullFreeJsonArray();
        try (ModelWithStringLarJsonList model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertNotNull(model.getWhatever());
            test.accept(nullFreeList(model.getWhatever()));
        }
    }

    protected List<String> defaultList(List<String> list) {
        return list;
    }

    protected List<String> emptyList(List<String> list) {
        return list;
    }

    protected List<String> nullFreeList(List<String> list) {
        return list;
    }

    protected String defaultJsonArray() {
        return "{\"whatever\" : [\"hello\", \"salut\", null, \"null\", \"salut\"]}";
    }

    protected String emptyJsonArray() {
        return "{\"whatever\" : []}";
    }

    protected String nullFreeJsonArray() {
        return "{\"whatever\" : [\"hello\", \"salut\", \"null\", \"salut\"]}";
    }

    public static void testIteratorNext(List<String> list, Iterator<String> iterator) {
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        assertEquals("hello", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("salut", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(null, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("null", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("salut", iterator.next());
        assertFalse(iterator.hasNext());
        try {
            iterator.next();
            fail();
        } catch (NoSuchElementException expected) {
        }
        assertListIsValid(list);
    }

    public static void testIteratorForEachRemaining(Iterator<String> iterator) {
        assertNotNull(iterator);
        iterator.next();
        List<String> otherList = new ArrayList<>();
        iterator.forEachRemaining(e->{
            otherList.add(e);
        });
        assertFalse(iterator.hasNext());
        assertEquals(Arrays.asList("salut", null, "null", "salut"), otherList);
    }

    public static void testListIteratorPrevious(List<String> list, ListIterator<String> iterator) {
        assertNotNull(iterator);
        assertFalse(iterator.hasPrevious());
        iterator.next();
        assertTrue(iterator.hasPrevious());
        assertEquals("hello", iterator.previous());
        assertFalse(iterator.hasPrevious());
        while(iterator.hasNext()) {
            iterator.next();
        }
        assertTrue(iterator.hasPrevious());
        assertEquals("salut", iterator.previous());
        assertTrue(iterator.hasPrevious());
        assertEquals("null", iterator.previous());
        assertTrue(iterator.hasPrevious());
        assertEquals(null, iterator.previous());
        assertTrue(iterator.hasPrevious());
        assertEquals("salut", iterator.previous());
        assertTrue(iterator.hasPrevious());
        assertEquals("hello", iterator.previous());
        assertFalse(iterator.hasPrevious());
        try {
            iterator.previous();
            fail();
        } catch (NoSuchElementException expected) {
        }
        assertListIsValid(list);
    }

    public static void testStream(List<String> list, Stream<String> stream) {
        assertNotNull(stream);
        List<String> result = stream.filter(s->s != null).sorted().distinct().collect(Collectors.toList());
        assertNotNull(result);
        assertEquals(Arrays.asList("hello", "null", "salut"), result);
        assertListIsValid(list);
    }

    protected abstract LarJsonTypedReadConfiguration configuration();

}
