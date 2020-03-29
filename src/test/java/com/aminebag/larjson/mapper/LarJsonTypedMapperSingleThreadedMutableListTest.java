package com.aminebag.larjson.mapper;

import com.aminebag.larjson.exception.LarJsonException;
import com.aminebag.larjson.configuration.LarJsonTypedReadConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Amine Bagdouri
 */
public class LarJsonTypedMapperSingleThreadedMutableListTest extends LarJsonTypedMapperMutableListTest {

    @Test
    void testListIteratorRemove(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            Iterator<String> iterator = list.iterator();
            assertNotNull(iterator);
            try {
                iterator.remove();
                fail();
            } catch (IllegalStateException expected) {
            }
            iterator.next();
            iterator.next();
            iterator.remove();
            assertEquals(4, list.size());
            assertEquals("hello", list.get(0));
            assertEquals(null, list.get(1));
            assertEquals("null", list.get(2));
            assertEquals("salut", list.get(3));
            iterator = list.iterator();
            while(iterator.hasNext()) {
                iterator.next();
                iterator.remove();
            }
            assertEquals(0, list.size());
        });
    }

    @Test
    void testListListIteratorRemove(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            ListIterator<String> iterator = list.listIterator();
            assertNotNull(iterator);
            try {
                iterator.remove();
                fail();
            } catch (IllegalStateException expected) {
            }
            iterator.next();
            testListIteratorRemove(list, iterator);
        });
    }

    @Test
    void testListListIteratorWithIndexRemove(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            ListIterator<String> iterator = list.listIterator(1);
            assertNotNull(iterator);
            try {
                iterator.remove();
                fail();
            } catch (IllegalStateException expected) {
            }
            testListIteratorRemove(list, iterator);
        });
    }

    @Test
    void testListListIteratorAdd(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            ListIterator<String> iterator = list.listIterator();
            assertNotNull(iterator);
            iterator.add("yes");
            assertEquals("hello", iterator.next());
            iterator.add("no");
            assertEquals("salut", iterator.next());
            assertEquals(null, iterator.next());
            assertEquals(null,  iterator.previous());
            iterator.add("wow");
            assertEquals(8, list.size());
            assertEquals("yes", list.get(0));
            assertEquals("hello", list.get(1));
            assertEquals("no", list.get(2));
            assertEquals("salut", list.get(3));
            assertEquals("wow", list.get(4));
            assertEquals(null, list.get(5));
            assertEquals("null", list.get(6));
            assertEquals("salut", list.get(7));
        });
    }

    @Test
    void testListListIteratorWithIndexAdd(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            ListIterator<String> iterator = list.listIterator(2);
            assertNotNull(iterator);
            iterator.add("yes");
            assertEquals(null, iterator.next());
            iterator.add("no");
            assertEquals("null", iterator.next());
            assertEquals("salut", iterator.next());
            assertEquals("salut",  iterator.previous());
            iterator.add("wow");
            assertEquals(8, list.size());
            assertEquals("hello", list.get(0));
            assertEquals("salut", list.get(1));
            assertEquals("yes", list.get(2));
            assertEquals(null, list.get(3));
            assertEquals("no", list.get(4));
            assertEquals("null", list.get(5));
            assertEquals("wow", list.get(6));
            assertEquals("salut", list.get(7));
        });
    }

    @Test
    void testListListIteratorSet(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            ListIterator<String> iterator = list.listIterator();
            assertNotNull(iterator);
            try {
                iterator.set("now");
                fail();
            } catch (IllegalStateException expected) {
            }
            iterator.next();
            iterator.next();
            testListIteratorSet(list, iterator);
        });
    }

    @Test
    void testListListIteratorWithIndexSet(@TempDir Path tempDir) throws IOException, LarJsonException {
        testList(tempDir, list->{
            ListIterator<String> iterator = list.listIterator(2);
            assertNotNull(iterator);
            try {
                iterator.set("now");
                fail();
            } catch (IllegalStateException expected) {
            }
            testListIteratorSet(list, iterator);
        });
    }

    private void testListIteratorRemove(List<String> list, ListIterator<String> iterator) {
        iterator.next();
        iterator.remove();
        assertEquals(null, iterator.next());
        assertEquals(null, iterator.previous());
        iterator.remove();
        assertEquals(3, list.size());
        assertEquals("hello", list.get(0));
        assertEquals("null", list.get(1));
        assertEquals("salut", list.get(2));
        while (iterator.hasNext()) {
            iterator.next();
        }
        while (iterator.hasPrevious()) {
            iterator.previous();
            iterator.remove();
        }
        assertEquals(0, list.size());
    }

    private void testListIteratorSet(List<String> list, ListIterator<String> iterator) {
        iterator.next();
        iterator.set("hi");
        assertEquals("null", iterator.next());
        assertEquals("null", iterator.previous());
        iterator.set("yes");
        assertEquals(5, list.size());
        assertEquals("hello", list.get(0));
        assertEquals("salut", list.get(1));
        assertEquals("hi", list.get(2));
        assertEquals("yes", list.get(3));
        assertEquals("salut", list.get(4));
    }

    @Override
    protected LarJsonTypedReadConfiguration configuration() {
        return new LarJsonTypedReadConfiguration.Builder().setMutable(true).build();
    }
}
