package com.aminebag.larjson.mapper;

import com.aminebag.larjson.exception.LarJsonException;
import com.aminebag.larjson.configuration.LarJsonTypedReadConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author Amine Bagdouri
 */
public class LarJsonTypedMapperImmutableListTest extends LarJsonTypedMapperListTest {

    @Test
    void testListAdd(@TempDir Path tempDir) throws IOException, LarJsonException {
        testListMutatingAction(tempDir, list->list.add("yes"));
    }

    @Test
    void testListAddWithIndex(@TempDir Path tempDir) throws IOException, LarJsonException {
        testListMutatingAction(tempDir, list->list.add(1, "yes"));
    }

    @Test
    void testListRemove(@TempDir Path tempDir) throws IOException, LarJsonException {
        testListMutatingAction(tempDir, list->list.remove("salut"));
    }

    @Test
    void testListRemoveWithIndex(@TempDir Path tempDir) throws IOException, LarJsonException {
        testListMutatingAction(tempDir, list->list.remove(2));
    }

    @Test
    void testListAddAll(@TempDir Path tempDir) throws IOException, LarJsonException {
        testListMutatingAction(tempDir, list->list.addAll(Arrays.asList("yes", "no")));
    }

    @Test
    void testListAddAllWithIndex(@TempDir Path tempDir) throws IOException, LarJsonException {
        testListMutatingAction(tempDir, list->list.addAll(Arrays.asList("yes", "no")));
    }

    @Test
    void testListRemoveAll(@TempDir Path tempDir) throws IOException, LarJsonException {
        testListMutatingAction(tempDir, list->list.removeAll(Arrays.asList("salut", "bonjour")));
    }

    @Test
    void testListRetainAll(@TempDir Path tempDir) throws IOException, LarJsonException {
        testListMutatingAction(tempDir, list->list.removeAll(Arrays.asList("salut", "bonjour")));
    }

    @Test
    void testListReplaceAll(@TempDir Path tempDir) throws IOException, LarJsonException {
        testListMutatingAction(tempDir, list->list.replaceAll(s->s+'!'));
    }

    @Test
    void testListSortNull(@TempDir Path tempDir) throws IOException, LarJsonException {
        testNullFreeListMutatingAction(tempDir, list->list.sort(null));
    }

    @Test
    void testListSortComparator(@TempDir Path tempDir) throws IOException, LarJsonException {
        testListMutatingAction(tempDir, list->list.sort(Comparator.nullsLast(Comparator.naturalOrder())));
    }

    @Test
    void testListClear(@TempDir Path tempDir) throws IOException, LarJsonException {
        testListMutatingAction(tempDir, List::clear);
    }

    @Test
    void testListSet(@TempDir Path tempDir) throws IOException, LarJsonException {
        testListMutatingAction(tempDir, list->list.set(2, "bonjour"));
    }

    @Test
    void testListRemoveIf(@TempDir Path tempDir) throws IOException, LarJsonException {
        testListMutatingAction(tempDir, list->list.removeIf(s->"salut".equals(s)));
    }

    @Test
    void testListIteratorRemove(@TempDir Path tempDir) throws IOException, LarJsonException {
        testListMutatingAction(tempDir, list->{
            Iterator<String> iterator = list.iterator();
            iterator.next();
            iterator.remove();
        });
    }

    @Test
    void testListListIteratorRemove(@TempDir Path tempDir) throws IOException, LarJsonException {
        testListMutatingAction(tempDir, list->{
            ListIterator<String> iterator = list.listIterator();
            iterator.next();
            iterator.remove();
        });
    }

    @Test
    void testListListIteratorWithIndexRemove(@TempDir Path tempDir) throws IOException, LarJsonException {
        testListMutatingAction(tempDir, list->{
            ListIterator<String> iterator = list.listIterator(2);
            iterator.next();
            iterator.remove();
        });
    }

    @Test
    void testListListIteratorAdd(@TempDir Path tempDir) throws IOException, LarJsonException {
        testListMutatingAction(tempDir, list->{
            ListIterator<String> iterator = list.listIterator();
            iterator.next();
            iterator.add("yes");
        });
    }

    @Test
    void testListListIteratorWithIndexAdd(@TempDir Path tempDir) throws IOException, LarJsonException {
        testListMutatingAction(tempDir, list->{
            ListIterator<String> iterator = list.listIterator(2);
            iterator.next();
            iterator.add("yes");
        });
    }

    @Test
    void testListListIteratorSet(@TempDir Path tempDir) throws IOException, LarJsonException {
        testListMutatingAction(tempDir, list->{
            ListIterator<String> iterator = list.listIterator();
            iterator.next();
            iterator.set("yes");
        });
    }

    @Test
    void testListListIteratorWithIndexSet(@TempDir Path tempDir) throws IOException, LarJsonException {
        testListMutatingAction(tempDir, list->{
            ListIterator<String> iterator = list.listIterator(2);
            iterator.next();
            iterator.set("yes");
        });
    }

    void testListMutatingAction(Path tempDir, Consumer<List<String>> action) throws IOException, LarJsonException {
        testList(tempDir, list->{
            try {
                action.accept(list);
                fail();
            } catch (UnsupportedOperationException expected) {
            }
            assertListIsValid(list);
        });
    }

    void testNullFreeListMutatingAction(Path tempDir, Consumer<List<String>> action)
            throws IOException, LarJsonException {
        testNullFreeList(tempDir, list->{
            try {
                action.accept(list);
                fail();
            } catch (UnsupportedOperationException expected) {
            }
            assertNullFreeListIsValid(list);
        });
    }

    @Override
    protected LarJsonTypedReadConfiguration configuration() {
        return new LarJsonTypedReadConfiguration.Builder().build();
    }
}
