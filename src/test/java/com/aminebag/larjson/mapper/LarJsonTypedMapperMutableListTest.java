package com.aminebag.larjson.mapper;

import com.aminebag.larjson.exception.LarJsonException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Amine Bagdouri
 */
public abstract class LarJsonTypedMapperMutableListTest extends LarJsonTypedMapperListTest {

    @Test
    void testListAdd(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            assertTrue(list.add("yes"));
            assertEquals(6, list.size());
            assertEquals("hello", list.get(0));
            assertEquals("salut", list.get(1));
            assertEquals(null, list.get(2));
            assertEquals("null", list.get(3));
            assertEquals("salut", list.get(4));
            assertEquals("yes", list.get(5));
        });
    }

    @Test
    void testListAddWithIndex(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            list.add(1, "yes");
            assertEquals(6, list.size());
            assertEquals("hello", list.get(0));
            assertEquals("yes", list.get(1));
            assertEquals("salut", list.get(2));
            assertEquals(null, list.get(3));
            assertEquals("null", list.get(4));
            assertEquals("salut", list.get(5));
        });
    }

    @Test
    void testListRemove(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            assertTrue(list.remove("salut"));
            assertFalse(list.remove("bonjour"));
            assertEquals(4, list.size());
            assertEquals("hello", list.get(0));
            assertEquals(null, list.get(1));
            assertEquals("null", list.get(2));
            assertEquals("salut", list.get(3));
        });
    }

    @Test
    void testListRemoveWithIndex(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            assertEquals("null", list.remove(3));
            assertEquals(4, list.size());
            assertEquals("hello", list.get(0));
            assertEquals("salut", list.get(1));
            assertEquals(null, list.get(2));
            assertEquals("salut", list.get(3));
        });
    }

    @Test
    void testListAddAll(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            assertTrue(list.addAll(Arrays.asList("yes", "no")));
            assertFalse(list.addAll(Arrays.asList()));
            assertEquals(7, list.size());
            assertEquals("hello", list.get(0));
            assertEquals("salut", list.get(1));
            assertEquals(null, list.get(2));
            assertEquals("null", list.get(3));
            assertEquals("salut", list.get(4));
            assertEquals("yes", list.get(5));
            assertEquals("no", list.get(6));
        });
    }

    @Test
    void testListAddAllWithIndex(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            assertTrue(list.addAll(3, Arrays.asList("yes", "no")));
            assertFalse(list.addAll(2, Arrays.asList()));
            assertEquals(7, list.size());
            assertEquals("hello", list.get(0));
            assertEquals("salut", list.get(1));
            assertEquals(null, list.get(2));
            assertEquals("yes", list.get(3));
            assertEquals("no", list.get(4));
            assertEquals("null", list.get(5));
            assertEquals("salut", list.get(6));
        });
    }

    @Test
    void testListRemoveAll(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            assertTrue(list.removeAll(Arrays.asList("salut", "bonjour")));
            assertFalse(list.removeAll(Arrays.asList("hi")));
            assertEquals(3, list.size());
            assertEquals("hello", list.get(0));
            assertEquals(null, list.get(1));
            assertEquals("null", list.get(2));
        });
    }

    @Test
    void testListRetainAll(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            assertTrue(list.retainAll(Arrays.asList("salut", "null", "bonjour")));
            assertFalse(list.retainAll(Arrays.asList("salut", "null", "bonjour")));
            assertEquals(3, list.size());
            assertEquals("salut", list.get(0));
            assertEquals("null", list.get(1));
            assertEquals("salut", list.get(2));
        });
    }

    @Test
    void testListReplaceAll(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            list.replaceAll(s->{
                if(s == null) {
                    return "NO";
                } else if (s.equals("salut")) {
                    return "YES";
                } else {
                    return s;
                }
            });
            assertEquals(5, list.size());
            assertEquals("hello", list.get(0));
            assertEquals("YES", list.get(1));
            assertEquals("NO", list.get(2));
            assertEquals("null", list.get(3));
            assertEquals("YES", list.get(4));
        });
    }

    @Test
    void testListSortNull(@TempDir Path tempDir) throws IOException, LarJsonException {
        testNullFreeList(tempDir, list->{
            list.sort(null);
            assertEquals(4, list.size());
            assertEquals("hello", list.get(0));
            assertEquals("null", list.get(1));
            assertEquals("salut", list.get(2));
            assertEquals("salut", list.get(3));
        });
    }

    @Test
    void testListSortComparator(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            list.sort(Comparator.nullsLast(Comparator.naturalOrder()));
            assertEquals(5, list.size());
            assertEquals("hello", list.get(0));
            assertEquals("null", list.get(1));
            assertEquals("salut", list.get(2));
            assertEquals("salut", list.get(3));
            assertEquals(null, list.get(4));
        });
    }

    @Test
    void testListClear(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            list.clear();
            assertEquals(0, list.size());
        });
    }

    @Test
    void testListSet(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            assertEquals("null", list.set(3, "bonjour"));
            assertEquals(5, list.size());
            assertEquals("hello", list.get(0));
            assertEquals("salut", list.get(1));
            assertEquals(null, list.get(2));
            assertEquals("bonjour", list.get(3));
            assertEquals("salut", list.get(4));
        });
    }

    @Test
    void testListRemoveIf(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            assertTrue(list.removeIf(s->"salut".equals(s)));
            assertFalse(list.removeIf(s->"salut".equals(s)));
            assertEquals(3, list.size());
            assertEquals("hello", list.get(0));
            assertEquals(null, list.get(1));
            assertEquals("null", list.get(2));
        });
    }
}
