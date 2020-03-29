package com.aminebag.larjson.mapper;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static com.aminebag.larjson.mapper.LarJsonTypedMapperListTest.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Amine Bagdouri
 */
public class LarJsonMapperThreadSafeListTestUtils {

    public static Consumer<List<String>> testThreadSafeListGet() {
        return list->{
            waitForAsyncTask(list, ()->{
                assertEquals("salut", list.get(1));
                assertListIsValid(list);
            });
            assertListIsValid(list);
        };
    }

    public static Consumer<List<String>> testThreadSafeListSize() {
        return list->{
            waitForAsyncTask(list, ()->{
                assertEquals(5, list.size());
                assertListIsValid(list);
            });
            assertListIsValid(list);
        };
    }

    public static Consumer<List<String>> testThreadSafeListIsEmpty() {
        return list->{
            waitForAsyncTask(list, ()->{
                assertTrue(list.isEmpty());
                assertEmptyListIsValid(list);
            });
            assertEmptyListIsValid(list);
        };
    }

    public static Consumer<List<String>> testThreadSafeListIsNotEmpty() {
        return list->{
            waitForAsyncTask(list, ()->{
                assertFalse(list.isEmpty());
                assertListIsValid(list);
            });
            assertListIsValid(list);
        };
    }

    public static Consumer<List<String>> testThreadSafeListContains() {
        return list->{
            waitForAsyncTask(list, ()->{
                assertTrue(list.contains("salut"));
                assertFalse(list.contains("bonjour"));
                assertListIsValid(list);
            });
            assertListIsValid(list);
        };
    }

    public static Consumer<List<String>> testThreadSafeListIteratorNext() {
        return list->{
            waitForAsyncTask(list, ()->{
                Iterator<String> iterator = list.iterator();
                LarJsonTypedMapperListTest.testIteratorNext(list, iterator);
            });
            assertListIsValid(list);
        };
    }

    public static Consumer<List<String>> testThreadSafeListIteratorForEachReamaining() {
        return list->{
            waitForAsyncTask(list, ()->{
                Iterator<String> iterator = list.iterator();
                LarJsonTypedMapperListTest.testIteratorForEachRemaining(iterator);
            });
            assertListIsValid(list);
        };
    }

    public static Consumer<List<String>> testThreadSafeListToArray() {
        return list->{
            waitForAsyncTask(list, ()->{
                Object[] toArray = list.toArray();
                assertNotNull(toArray);
                assertEquals(validFilledList(), Arrays.asList(toArray));
                assertListIsValid(list);
            });
            assertListIsValid(list);
        };
    }

    public static Consumer<List<String>> testThreadSafeListToArrayWithSmallArrayArg() {
        return list->{
            waitForAsyncTask(list, ()->{
                String[] toArray = list.toArray(new String[1]);
                assertNotNull(toArray);
                assertEquals(validFilledList(), Arrays.asList(toArray));
                assertListIsValid(list);
            });
            assertListIsValid(list);
        };
    }

    public static Consumer<List<String>> testThreadSafeListToArrayWithBigArrayArg() {
        return list->{
            waitForAsyncTask(list, ()->{
                String[] toArray = list.toArray(new String[6]);
                assertNotNull(toArray);
                assertEquals(Arrays.asList("hello", "salut", null, "null", "salut", null), Arrays.asList(toArray));
                assertListIsValid(list);
            });
            assertListIsValid(list);
        };
    }

    public static Consumer<List<String>> testThreadSafeListContainsAll() {
        return list->{
            waitForAsyncTask(list, ()->{
                assertTrue(list.containsAll(new HashSet<>()));
                assertTrue(list.containsAll(Arrays.asList(null, "null", "hello")));
                assertTrue(list.containsAll(validFilledList()));
                assertFalse(list.containsAll(Arrays.asList("bonjour", "null", "hello")));
                assertListIsValid(list);
            });
            assertListIsValid(list);
        };
    }

    public static Consumer<List<String>> testThreadSafeListEquals() {
        return list->{
            waitForAsyncTask(list, ()->{
                assertTrue(validFilledList().equals(list));
                assertTrue(list.equals(validFilledList()));
                assertFalse(Arrays.asList("salut", "hello", null, "null", "salut").equals(list));
                assertFalse(list.equals(Arrays.asList("salut", "hello", null, "null", "salut")));
                assertListIsValid(list);
            });
            assertListIsValid(list);
        };
    }

    public static Consumer<List<String>> testThreadSafeListHashCode() {
        return list->{
            waitForAsyncTask(list, ()->{
                assertEquals(validFilledList().hashCode(), list.hashCode());
                assertListIsValid(list);
            });
            assertListIsValid(list);
        };
    }

    public static Consumer<List<String>> testThreadSafeListIndexOf() {
        return list->{
            waitForAsyncTask(list, ()->{
                assertEquals(2, list.indexOf(null));
                assertEquals(1, list.indexOf("salut"));
                assertEquals(0, list.indexOf("hello"));
                assertEquals(3, list.indexOf("null"));
                assertEquals(-1, list.indexOf("bonjour"));
                assertListIsValid(list);
            });
            assertListIsValid(list);
        };
    }

    public static Consumer<List<String>> testThreadSafeListLastIndexOf() {
        return list->{
            waitForAsyncTask(list, ()->{
                assertEquals(2, list.lastIndexOf(null));
                assertEquals(4, list.lastIndexOf("salut"));
                assertEquals(0, list.lastIndexOf("hello"));
                assertEquals(3, list.lastIndexOf("null"));
                assertEquals(-1, list.lastIndexOf("bonjour"));
                assertListIsValid(list);
            });
            assertListIsValid(list);
        };
    }

    public static Consumer<List<String>> testThreadSafeListListIteratorNext() {
        return list->{
            waitForAsyncTask(list, ()->{
                ListIterator<String> iterator = list.listIterator();
                testIteratorNext(list, iterator);
            });
            assertListIsValid(list);
        };
    }

    public static Consumer<List<String>> testThreadSafeListForEachRemaining() {
        return list->{
            waitForAsyncTask(list, ()->{
                ListIterator<String> iterator = list.listIterator();
                testIteratorForEachRemaining(iterator);
            });
            assertListIsValid(list);
        };
    }

    public static Consumer<List<String>> testThreadSafeListListIteratorPrevious() {
        return list->{
            waitForAsyncTask(list, ()->{
                ListIterator<String> iterator = list.listIterator();
                testListIteratorPrevious(list, iterator);
            });
            assertListIsValid(list);
        };
    }

    public static Consumer<List<String>> testThreadSafeListListIteratorNextIndex() {
        return list->{
            waitForAsyncTask(list, ()->{
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
            assertListIsValid(list);
        };
    }

    public static Consumer<List<String>> testThreadSafeListListIteratorPreviousIndex() {
        return list->{
            waitForAsyncTask(list, ()->{
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
            assertListIsValid(list);
        };
    }

    public static Consumer<List<String>> testThreadSafeListListIteratorWithIndexNext() {
        return list->{
            waitForAsyncTask(list, ()->{
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
            assertListIsValid(list);
        };
    }

    public static Consumer<List<String>> testThreadSafeListListIteratorWithIndexPrevious() {
        return list->{
            waitForAsyncTask(list, ()->{
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
            assertListIsValid(list);
        };
    }

    public static Consumer<List<String>> testThreadSafeListListIteratorWithIndexNextIndex() {
        return list->{
            waitForAsyncTask(list, ()->{
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
            assertListIsValid(list);
        };
    }

    public static Consumer<List<String>> testThreadSafeListListIteratorWithIndexPreviousIndex() {
        return list->{
            waitForAsyncTask(list, ()->{
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
            assertListIsValid(list);
        };
    }

    public static Consumer<List<String>> testThreadSafeListListIteratorWithIndexForEachRemaining() {
        return list->{
            waitForAsyncTask(list, ()->{
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
            assertListIsValid(list);
        };
    }

    public static Consumer<List<String>> testThreadSafeListSubList() {
        return list->{
            waitForAsyncTask(list, ()->{
                List<String> subList = list.subList(1,4);
                assertEquals(Arrays.asList("salut", null, "null"), subList);
                assertListIsValid(list);
            });
            assertListIsValid(list);
        };
    }

    public static Consumer<List<String>> testThreadSafeListSpliterator() {
        return list->{
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
        };
    }

    public static Consumer<List<String>> testThreadSafeListStream() {
        return list->{
            Stream<String> stream = list.stream();
            testStream(list, stream);
        };
    }

    public static Consumer<List<String>> testThreadSafeListToString() {
        return list->{
            assertEquals("[hello, salut, null, null, salut]", list.toString());
            assertListIsValid(list);
        };
    }

    public static Consumer<List<String>> testThreadSafeListForEach() {
        return list->{
            List<String> otherList = new ArrayList<>();
            list.forEach(s->otherList.add(s));
            assertEquals(validFilledList(), otherList);
            assertListIsValid(list);
        };
    }

    public static void waitForAsyncTask(List<String> list, Runnable task) {
        assertEquals(list, new ArrayList<>(list));
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<?> future = executorService.submit(task);
        try {
            future.get(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            fail(e);
        } catch (ExecutionException e) {
            if(e.getCause() instanceof AssertionError) {
                throw ((AssertionError)e.getCause());
            }
            fail(e.getCause());
        } catch (TimeoutException e) {
            fail(e);
        }
    }
}
