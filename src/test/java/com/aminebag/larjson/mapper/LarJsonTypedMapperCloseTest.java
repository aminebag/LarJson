package com.aminebag.larjson.mapper;

import com.aminebag.larjson.exception.LarJsonException;

import static com.aminebag.larjson.mapper.LarJsonTypedMapperTestModels.*;

import com.aminebag.larjson.api.LarJsonRootList;
import com.aminebag.larjson.configuration.LarJsonTypedReadConfiguration;
import com.aminebag.larjson.configuration.LarJsonTypedWriteConfiguration;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

import static com.aminebag.larjson.mapper.LarJsonMapperTestUtils.jsonToFile;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author Amine Bagdouri
 */
public class LarJsonTypedMapperCloseTest {

    @Test
    void testMapperCloseObjectTwice(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithString> mapper = new LarJsonTypedMapper<>(ModelWithString.class);
        String json = "{\"whatever\" : \"hi\"}";
        ModelWithString model = mapper.readObject(jsonToFile(tempDir, json));
        try {
            assertNotNull(model);
            assertEquals("hi", model.getWhatever());
        } finally {
            model.close();
        }
        model.close();
    }

    @Test
    void testMapperObjectGetAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        ModelWithStringMutable model = readObjectAndClose(tempDir);
        try {
            model.getWhatever();
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperObjectHashCodeAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        ModelWithStringMutable model = readObjectAndClose(tempDir);
        try {
            model.hashCode();
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperObjectEqualsAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        ModelWithStringMutable model = readObjectAndClose(tempDir);
        try {
            model.equals("hi");
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperObjectToStringAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        ModelWithStringMutable model = readObjectAndClose(tempDir);
        try {
            model.toString();
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperObjectSetAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        ModelWithStringMutable model = readObjectAndClose(tempDir);
        try {
            model.setWhatever("salut");
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperObjectWriteAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        ModelWithStringMutable model = readObjectAndClose(tempDir);
        try {
            model.write(new StringWriter());
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperObjectCustomConfigWriteAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        ModelWithStringMutable model = readObjectAndClose(tempDir);
        try {
            model.write(new StringWriter(), new LarJsonTypedWriteConfiguration.Builder().build());
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperObjectJsonWriteAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        ModelWithStringMutable model = readObjectAndClose(tempDir);
        try {
            model.write(new JsonWriter(new StringWriter()));
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperObjectCustomConfigJsonWriteAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        ModelWithStringMutable model = readObjectAndClose(tempDir);
        try {
            model.write(new JsonWriter(new StringWriter()), new LarJsonTypedWriteConfiguration.Builder().build());
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperObjectCloneAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        ModelWithStringMutable model = readObjectAndClose(tempDir);
        try {
            model.clone();
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperObjectGetPathAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        ModelWithStringMutable model = readObjectAndClose(tempDir);
        try {
            model.getLarJsonPath();
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperObjectGetParentPathAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        ModelWithStringMutable model = readObjectAndClose(tempDir);
        try {
            model.getParentLarJsonPath();
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperObjectGetPathStringBuilderAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        ModelWithStringMutable model = readObjectAndClose(tempDir);
        try {
            model.getLarJsonPath(new StringBuilder());
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    private ModelWithStringMutable readObjectAndClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithStringMutable> mapper = new LarJsonTypedMapper<>(ModelWithStringMutable.class,
                new LarJsonTypedReadConfiguration.Builder().setMutable(true).build());
        String json = "{\"whatever\" : \"hi\"}";
        ModelWithStringMutable model = mapper.readObject(jsonToFile(tempDir, json));
        try {
            assertNotNull(model);
            assertEquals("hi", model.getWhatever());
        } finally {
            model.close();
        }
        return model;
    }

    @Test
    void testMapperCloseListTwice(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithStringList> mapper = new LarJsonTypedMapper<>(ModelWithStringList.class);
        String json = "{\"whatever\" : [\"hi\"]}";
        ModelWithStringList model = mapper.readObject(jsonToFile(tempDir, json));
        try {
            assertNotNull(model);
        } finally {
            model.close();
        }
        model.close();
    }

    @Test
    void testMapperListGetAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readListAndClose(tempDir);
        try {
            model.get(0);
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListSizeAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readListAndClose(tempDir);
        try {
            model.size();
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListIsEmptyAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readListAndClose(tempDir);
        try {
            model.isEmpty();
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListStreamAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readListAndClose(tempDir);
        try {
            model.stream();
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListIteratorAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readListAndClose(tempDir);
        try {
            model.iterator();
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListSplititeratorAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readListAndClose(tempDir);
        try {
            model.spliterator();
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListListIteratorAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readListAndClose(tempDir);
        try {
            model.listIterator();
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListListIteratorWithIndexAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readListAndClose(tempDir);
        try {
            model.listIterator(1);
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListSubListAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readListAndClose(tempDir);
        try {
            model.subList(0, 1);
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListEqualsAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readListAndClose(tempDir);
        try {
            model.equals(Collections.singleton("hi"));
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListHashCodeAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readListAndClose(tempDir);
        try {
            model.hashCode();
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListToStringAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readListAndClose(tempDir);
        try {
            model.toString();
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListAddAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readListAndClose(tempDir);
        try {
            model.add(new ModelWithStringImpl());
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListAddWithIndexAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readListAndClose(tempDir);
        try {
            model.add(1, new ModelWithStringImpl());
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListRemoveWithIndexAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readListAndClose(tempDir);
        try {
            model.remove(0);
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListRemoveIfAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readListAndClose(tempDir);
        try {
            model.removeIf(s->true);
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListRemoveAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readListAndClose(tempDir);
        try {
            model.remove("hi");
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListSortAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readListAndClose(tempDir);
        try {
            model.sort(null);
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListToArrayAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readListAndClose(tempDir);
        try {
            model.toArray();
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListToArrayWithArgAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readListAndClose(tempDir);
        try {
            model.toArray(new String[2]);
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListContainsAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readListAndClose(tempDir);
        try {
            model.contains(new ModelWithStringImpl());
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListSetAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readListAndClose(tempDir);
        try {
            model.set(0, new ModelWithStringImpl());
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListAddAllAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readListAndClose(tempDir);
        try {
            model.addAll(Collections.emptyList());
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListAddAllWithIndexAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readListAndClose(tempDir);
        try {
            model.addAll(1, Collections.emptyList());
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListRemoveAllAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readListAndClose(tempDir);
        try {
            model.removeAll(Collections.emptyList());
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListReplaceAllAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readListAndClose(tempDir);
        try {
            model.replaceAll(s->new ModelWithStringImpl());
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListContainsAllAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readListAndClose(tempDir);
        try {
            model.containsAll(Collections.emptyList());
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListRetainAllAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readListAndClose(tempDir);
        try {
            model.retainAll(Collections.emptyList());
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListIndexOfAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readListAndClose(tempDir);
        try {
            model.indexOf("ho");
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListLastIndexOfAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readListAndClose(tempDir);
        try {
            model.lastIndexOf("ho");
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListForEachAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readListAndClose(tempDir);
        try {
            model.forEach(s->{});
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListStreamCountAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readArray(tempDir);
        Stream<ModelWithString> stream;
        try {
            assertNotNull(model);
            stream = model.stream();
        } finally {
            model.close();
        }
        try {
            stream.count();
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListIteratorNextAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readArray(tempDir);
        Iterator<ModelWithString> iterator;
        try {
            assertNotNull(model);
            iterator = model.iterator();
            iterator.next();
        } finally {
            model.close();
        }
        try {
            iterator.next();
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListListIteratorPreviousAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readArray(tempDir);
        ListIterator<ModelWithString> iterator;
        try {
            assertNotNull(model);
            iterator = model.listIterator();
            iterator.next();
        } finally {
            model.close();
        }
        try {
            iterator.previous();
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListListIteratorWithIndexPreviousAfterClose(@TempDir Path tempDir)
            throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readArray(tempDir);
        ListIterator<ModelWithString> iterator;
        try {
            assertNotNull(model);
            iterator = model.listIterator(2);
            iterator.next();
        } finally {
            model.close();
        }
        try {
            iterator.previous();
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListSplititeratorForEachAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readArray(tempDir);
        Spliterator<ModelWithString> spliterator;
        try {
            assertNotNull(model);
            spliterator = model.spliterator();
        } finally {
            model.close();
        }
        try {
            spliterator.forEachRemaining(s->{});
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListSubListGetAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readArray(tempDir);
        List<ModelWithString> subList;
        try {
            assertNotNull(model);
            subList = model.subList(0, 1);
        } finally {
            model.close();
        }
        try {
            subList.get(0);
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListClearAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readListAndClose(tempDir);
        try {
            model.clear();
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListParallelStreamAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readListAndClose(tempDir);
        try {
            model.parallelStream();
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListWriteAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readListAndClose(tempDir);
        try {
            model.write(new StringWriter());
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListCustomConfigWriteAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readListAndClose(tempDir);
        try {
            model.write(new StringWriter(), new LarJsonTypedWriteConfiguration.Builder().build());
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListJsonWriteAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readListAndClose(tempDir);
        try {
            model.write(new JsonWriter(new StringWriter()));
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListCustomConfigJsonWriteAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readListAndClose(tempDir);
        try {
            model.write(new JsonWriter(new StringWriter()), new LarJsonTypedWriteConfiguration.Builder().build());
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListCloneAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readListAndClose(tempDir);
        try {
            model.clone();
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListGetPathAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readListAndClose(tempDir);
        try {
            model.getLarJsonPath();
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListGetParentPathAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readListAndClose(tempDir);
        try {
            model.getParentLarJsonPath();
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    void testMapperListGetPathStringBuilderAfterClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readListAndClose(tempDir);
        try {
            model.getLarJsonPath(new StringBuilder());
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    private LarJsonRootList<ModelWithString> readListAndClose(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonRootList<ModelWithString> model = readArray(tempDir);
        try {
            assertNotNull(model);
        } finally {
            model.close();
        }
        return model;
    }

    private LarJsonRootList<ModelWithString> readArray(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithString> mapper = new LarJsonTypedMapper<>(ModelWithString.class,
                new LarJsonTypedReadConfiguration.Builder().setMutable(true).build());
        String json = "[{\"whatever\" : \"hello\"}, null, {\"whatever\": null}, {}]";
        return mapper.readArray(jsonToFile(tempDir, json));
    }
}
