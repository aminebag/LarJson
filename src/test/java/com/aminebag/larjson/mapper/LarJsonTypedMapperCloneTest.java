package com.aminebag.larjson.mapper;

import com.aminebag.larjson.exception.LarJsonException;
import com.aminebag.larjson.mapper.LarJsonTypedMapperTestModels.*;
import com.aminebag.larjson.api.LarJsonList;
import com.aminebag.larjson.api.LarJsonRootList;
import com.aminebag.larjson.configuration.LarJsonTypedReadConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

import static com.aminebag.larjson.mapper.LarJsonMapperTestUtils.jsonToFile;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Amine Bagdouri
 */
public class LarJsonTypedMapperCloneTest {

    @Test
    void testCloneImmutableCloneable(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithObjectCloneable> mapper = new LarJsonTypedMapper<>(ModelWithObjectCloneable.class,
                enrichConfigBuilder(new LarJsonTypedReadConfiguration.Builder()).build());
        String json = "{\"something\": {\"whatever\" : \"hello\"}}";
        try(ModelWithObjectCloneable model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            ModelWithObjectCloneable clone = model.clone();
            assertTrue(clone == model);
        }
    }

    @Test
    void testCloneImmutableRootObject(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithObject> mapper = new LarJsonTypedMapper<>(ModelWithObject.class,
                enrichConfigBuilder(new LarJsonTypedReadConfiguration.Builder()).build());
        String json = "{\"something\": {\"whatever\" : \"hello\"}}";
        try(ModelWithObject model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            Object clone = model.clone();
            assertTrue(clone == model);
        }
    }

    @Test
    void testCloneImmutableChildObject(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithObject> mapper = new LarJsonTypedMapper<>(ModelWithObject.class,
                enrichConfigBuilder(new LarJsonTypedReadConfiguration.Builder()).build());
        String json = "{\"something\": {\"whatever\" : \"hello\"}}";
        try(ModelWithObject model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertNotNull(model.getSomething());
            Object clone = model.getSomething().clone();
            assertTrue(clone == model.getSomething());
        }
    }

    @Test
    void testCloneImmutableRootList(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithObject> mapper = new LarJsonTypedMapper<>(ModelWithObject.class,
                enrichConfigBuilder(new LarJsonTypedReadConfiguration.Builder()).build());
        String json = "[{\"something\": {\"whatever\" : \"hello\"}}, {}, null]";
        try(LarJsonRootList<ModelWithObject> model = mapper.readArray(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            Object clone = model.clone();
            assertTrue(clone == model);
        }
    }

    @Test
    void testCloneImmutableChildList(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithObjectList> mapper = new LarJsonTypedMapper<>(ModelWithObjectList.class,
                enrichConfigBuilder(new LarJsonTypedReadConfiguration.Builder()).build());
        String json = "{\"something\":[{\"whatever\" : \"hello\"}, null, {}]}";
        try(ModelWithObjectList model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertNotNull(model.getSomething());
            Object clone = model.getSomething().clone();
            assertTrue(clone == model.getSomething());
        }
    }

    @Test
    void testCloneMutableCloneable(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithObjectCloneable> mapper = new LarJsonTypedMapper<>(ModelWithObjectCloneable.class,
                enrichConfigBuilder(new LarJsonTypedReadConfiguration.Builder().setMutable(true)).build());
        String json = "{\"something\": {\"whatever\" : \"hello\"}}";
        try(ModelWithObjectCloneable model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            ModelWithObjectCloneable clone = model.clone();
            assertTrue(model.equals(clone));
            assertTrue(clone.equals(model));
            assertEquals(model.hashCode(), clone.hashCode());
            assertEquals(model.getSomething(), clone.getSomething());
            assertEquals("hello", model.getSomething().getWhatever());
            assertEquals("hello", clone.getSomething().getWhatever());
        }
    }

    @Test
    void testCloneMutableRootObjectUpdateBeforeClone(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithObjectMutable> mapper = new LarJsonTypedMapper<>(ModelWithObjectMutable.class,
                enrichConfigBuilder(new LarJsonTypedReadConfiguration.Builder().setMutable(true)).build());
        String json = "{\"something\": {\"whatever\" : \"hello\"}}";
        try(ModelWithObjectMutable model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            model.setSomething(new ModelWithStringMutableImpl());
            ModelWithObjectMutable clone = (ModelWithObjectMutable) model.clone();
            assertFalse(clone == model);
            assertTrue(model.equals(clone));
            assertTrue(clone.equals(model));
            assertEquals(model.hashCode(), clone.hashCode());
            assertEquals(model.getSomething(), clone.getSomething());
            assertEquals(null, model.getSomething().getWhatever());
            assertEquals(null, clone.getSomething().getWhatever());
        }
    }

    @Test
    void testCloneMutableRootObjectUpdateOriginal(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithObjectMutable> mapper = new LarJsonTypedMapper<>(ModelWithObjectMutable.class,
                enrichConfigBuilder(new LarJsonTypedReadConfiguration.Builder().setMutable(true)).build());
        String json = "{\"something\": {\"whatever\" : \"hello\"}}";
        try(ModelWithObjectMutable model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            ModelWithObjectMutable clone = (ModelWithObjectMutable) model.clone();
            assertFalse(clone == model);
            assertTrue(model.equals(clone));
            assertTrue(clone.equals(model));
            assertEquals(model.hashCode(), clone.hashCode());
            model.setSomething(new ModelWithStringMutableImpl());
            assertFalse(model.equals(clone));
            assertFalse(clone.equals(model));
            assertNotEquals(model.hashCode(), clone.hashCode());
            assertNotEquals(model.getSomething(), clone.getSomething());
            assertEquals(null, model.getSomething().getWhatever());
            assertEquals("hello", clone.getSomething().getWhatever());
        }
    }

    @Test
    void testCloneMutableRootObjectUpdateClone(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithObjectMutable> mapper = new LarJsonTypedMapper<>(ModelWithObjectMutable.class,
                enrichConfigBuilder(new LarJsonTypedReadConfiguration.Builder().setMutable(true)).build());
        String json = "{\"something\": {\"whatever\" : \"hello\"}}";
        try(ModelWithObjectMutable model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            ModelWithObjectMutable clone = (ModelWithObjectMutable) model.clone();
            assertFalse(clone == model);
            assertTrue(model.equals(clone));
            assertTrue(clone.equals(model));
            assertEquals(model.hashCode(), clone.hashCode());
            clone.setSomething(new ModelWithStringMutableImpl());
            assertFalse(model.equals(clone));
            assertFalse(clone.equals(model));
            assertNotEquals(model.getSomething(), clone.getSomething());
            assertEquals("hello", model.getSomething().getWhatever());
            assertEquals(null, clone.getSomething().getWhatever());
        }
    }

    @Test
    void testCloneMutableRootObjectClone(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithObjectMutable> mapper = new LarJsonTypedMapper<>(ModelWithObjectMutable.class,
                enrichConfigBuilder(new LarJsonTypedReadConfiguration.Builder().setMutable(true)).build());
        String json = "{\"something\": {\"whatever\" : \"hello\"}}";
        try(ModelWithObjectMutable model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            ModelWithObjectMutable clone = (ModelWithObjectMutable) model.clone();
            ModelWithObjectMutable cloneClone = (ModelWithObjectMutable) clone.clone();
            assertFalse(cloneClone == model);
            assertFalse(cloneClone == clone);
            assertTrue(model.equals(cloneClone));
            assertTrue(cloneClone.equals(model));
            assertEquals(cloneClone.hashCode(), clone.hashCode());
            assertTrue(clone.equals(cloneClone));
            assertTrue(cloneClone.equals(clone));
            assertEquals(cloneClone.hashCode(), model.hashCode());
            model.setSomething(new ModelWithStringMutableImpl());
            assertTrue(clone.equals(cloneClone));
            assertTrue(cloneClone.equals(clone));
            assertEquals(cloneClone.hashCode(), clone.hashCode());
            assertFalse(model.equals(cloneClone));
            assertFalse(cloneClone.equals(model));
            assertNotEquals(cloneClone.hashCode(), model.hashCode());
            clone.setSomething(new ModelWithStringMutableImpl());
            assertFalse(clone.equals(cloneClone));
            assertFalse(cloneClone.equals(clone));
            assertNotEquals(cloneClone.hashCode(), clone.hashCode());
            assertFalse(model.equals(cloneClone));
            assertFalse(cloneClone.equals(model));
            assertNotEquals(cloneClone.hashCode(), model.hashCode());
            cloneClone.setSomething(new ModelWithStringMutableImpl());
            assertTrue(clone.equals(cloneClone));
            assertTrue(cloneClone.equals(clone));
            assertEquals(cloneClone.hashCode(), clone.hashCode());
            assertTrue(model.equals(cloneClone));
            assertTrue(cloneClone.equals(model));
            assertEquals(cloneClone.hashCode(), clone.hashCode());
        }
    }

    @Test
    void testCloneMutableChildObjectUpdateBeforeClone(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithObjectMutable> mapper = new LarJsonTypedMapper<>(ModelWithObjectMutable.class,
                enrichConfigBuilder(new LarJsonTypedReadConfiguration.Builder().setMutable(true)).build());
        String json = "{\"something\": {\"whatever\" : \"hello\"}}";
        try(ModelWithObjectMutable model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertNotNull(model.getSomething());
            model.getSomething().setWhatever("yes");
            ModelWithStringMutable clone = (ModelWithStringMutable) model.getSomething().clone();
            assertFalse(clone == model.getSomething());
            assertEquals(model.getSomething(), clone);
            assertEquals(model.getSomething().hashCode(), clone.hashCode());
            assertEquals("yes", model.getSomething().getWhatever());
            assertEquals("yes", clone.getWhatever());
        }
    }

    @Test
    void testCloneMutableChildObjectUpdateOriginal(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithObjectMutable> mapper = new LarJsonTypedMapper<>(ModelWithObjectMutable.class,
                enrichConfigBuilder(new LarJsonTypedReadConfiguration.Builder().setMutable(true)).build());
        String json = "{\"something\": {\"whatever\" : \"hello\"}}";
        try(ModelWithObjectMutable model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertNotNull(model.getSomething());
            ModelWithStringMutable clone = (ModelWithStringMutable) model.getSomething().clone();
            assertFalse(clone == model.getSomething());
            assertEquals(model.getSomething(), clone);
            assertEquals(model.getSomething().hashCode(), clone.hashCode());
            model.getSomething().setWhatever("yes");
            assertNotEquals(model.getSomething(), clone);
            assertNotEquals(model.getSomething().hashCode(), clone.hashCode());
            assertEquals("yes", model.getSomething().getWhatever());
            assertEquals("hello", clone.getWhatever());
        }
    }

    @Test
    void testCloneMutableChildObjectUpdateClone(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithObjectMutable> mapper = new LarJsonTypedMapper<>(ModelWithObjectMutable.class,
                enrichConfigBuilder(new LarJsonTypedReadConfiguration.Builder().setMutable(true)).build());
        String json = "{\"something\": {\"whatever\" : \"hello\"}}";
        try(ModelWithObjectMutable model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertNotNull(model.getSomething());
            ModelWithStringMutable clone = (ModelWithStringMutable) model.getSomething().clone();
            assertFalse(clone == model.getSomething());
            assertEquals(model.getSomething(), clone);
            assertEquals(model.getSomething().hashCode(), clone.hashCode());
            clone.setWhatever("yes");
            assertNotEquals(model.getSomething(), clone);
            assertNotEquals(model.getSomething().hashCode(), clone.hashCode());
            assertEquals("hello", model.getSomething().getWhatever());
            assertEquals("yes", clone.getWhatever());
        }
    }

    @Test
    void testCloneMutableChildObjectClone(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithObjectMutable> mapper = new LarJsonTypedMapper<>(ModelWithObjectMutable.class,
                enrichConfigBuilder(new LarJsonTypedReadConfiguration.Builder().setMutable(true)).build());
        String json = "{\"something\": {\"whatever\" : \"hello\"}}";
        try(ModelWithObjectMutable model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            ModelWithStringMutable clone = (ModelWithStringMutable) model.getSomething().clone();
            ModelWithStringMutable cloneClone = (ModelWithStringMutable) clone.clone();
            assertFalse(cloneClone == model.getSomething());
            assertFalse(cloneClone == clone);
            assertTrue(model.getSomething().equals(cloneClone));
            assertTrue(cloneClone.equals(model.getSomething()));
            assertEquals(model.getSomething().hashCode(), cloneClone.hashCode());
            assertTrue(clone.equals(cloneClone));
            assertTrue(cloneClone.equals(clone));
            assertEquals(clone.hashCode(), cloneClone.hashCode());
            model.getSomething().setWhatever("me");
            assertFalse(model.getSomething().equals(cloneClone));
            assertFalse(cloneClone.equals(model.getSomething()));
            assertNotEquals(model.getSomething().hashCode(), cloneClone.hashCode());
            assertTrue(clone.equals(cloneClone));
            assertTrue(cloneClone.equals(clone));
            assertEquals(clone.hashCode(), cloneClone.hashCode());
            clone.setWhatever("what");
            assertFalse(model.getSomething().equals(cloneClone));
            assertFalse(cloneClone.equals(model.getSomething()));
            assertNotEquals(model.getSomething().hashCode(), cloneClone.hashCode());
            assertFalse(clone.equals(cloneClone));
            assertFalse(cloneClone.equals(clone));
            assertNotEquals(clone.hashCode(), cloneClone.hashCode());
            cloneClone.setWhatever("me");
            assertTrue(model.getSomething().equals(cloneClone));
            assertTrue(cloneClone.equals(model.getSomething()));
            assertEquals(model.getSomething().hashCode(), cloneClone.hashCode());
            assertFalse(clone.equals(cloneClone));
            assertFalse(cloneClone.equals(clone));
            assertNotEquals(clone.hashCode(), cloneClone.hashCode());
        }
    }

    @Test
    void testCloneMutableRootListUpdateBeforeClone(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithObjectMutable> mapper = new LarJsonTypedMapper<>(ModelWithObjectMutable.class,
                enrichConfigBuilder(new LarJsonTypedReadConfiguration.Builder().setMutable(true)).build());
        String json = "[{\"something\": {\"whatever\" : \"hello\"}}, {}, null]";
        try(LarJsonRootList<ModelWithObjectMutable> model = mapper.readArray(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            model.remove(1);
            LarJsonList<ModelWithObjectMutable> clone = (LarJsonList<ModelWithObjectMutable>) model.clone();
            assertFalse(clone == model);
            assertTrue(clone.equals(model));
            assertTrue(model.equals(clone));
            assertEquals(model.hashCode(), clone.hashCode());
            assertEquals(2, clone.size());
            assertEquals(2, model.size());
            assertNotNull(clone.get(0));
            assertNotNull(model.get(0));
            assertNotNull(clone.get(0).getSomething());
            assertNotNull(model.get(0).getSomething());
            assertEquals("hello", clone.get(0).getSomething().getWhatever());
            assertEquals("hello", model.get(0).getSomething().getWhatever());
            assertEquals(null, clone.get(1));
            assertEquals(null, model.get(1));
        }
    }

    @Test
    void testCloneMutableRootListUpdateOriginal(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithObjectMutable> mapper = new LarJsonTypedMapper<>(ModelWithObjectMutable.class,
                enrichConfigBuilder(new LarJsonTypedReadConfiguration.Builder().setMutable(true)).build());
        String json = "[{\"something\": {\"whatever\" : \"hello\"}}, {}, null]";
        try(LarJsonRootList<ModelWithObjectMutable> model = mapper.readArray(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            LarJsonList<ModelWithObjectMutable> clone = (LarJsonList<ModelWithObjectMutable>) model.clone();
            assertFalse(clone == model);
            assertTrue(clone.equals(model));
            assertTrue(model.equals(clone));
            assertEquals(model.hashCode(), clone.hashCode());
            model.remove(1);
            assertFalse(clone.equals(model));
            assertFalse(model.equals(clone));
            assertNotEquals(model.hashCode(), clone.hashCode());
            assertEquals(3, clone.size());
            assertEquals(2, model.size());
            assertNotNull(clone.get(0));
            assertNotNull(model.get(0));
            assertNotNull(clone.get(0).getSomething());
            assertNotNull(model.get(0).getSomething());
            assertEquals("hello", clone.get(0).getSomething().getWhatever());
            assertEquals("hello", model.get(0).getSomething().getWhatever());
            assertNotNull(clone.get(1));
            assertEquals(null, clone.get(1).getSomething());
            assertEquals(null, model.get(1));
            assertEquals(null, clone.get(2));
        }
    }

    @Test
    void testCloneMutableRootListUpdateClone(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithObjectMutable> mapper = new LarJsonTypedMapper<>(ModelWithObjectMutable.class,
                enrichConfigBuilder(new LarJsonTypedReadConfiguration.Builder().setMutable(true)).build());
        String json = "[{\"something\": {\"whatever\" : \"hello\"}}, {}, null]";
        try(LarJsonRootList<ModelWithObjectMutable> model = mapper.readArray(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            LarJsonList<ModelWithObjectMutable> clone = (LarJsonList<ModelWithObjectMutable>) model.clone();
            assertFalse(clone == model);
            assertTrue(clone.equals(model));
            assertTrue(model.equals(clone));
            assertEquals(model.hashCode(), clone.hashCode());
            clone.remove(1);
            assertFalse(clone.equals(model));
            assertFalse(model.equals(clone));
            assertNotEquals(model.hashCode(), clone.hashCode());
            assertEquals(2, clone.size());
            assertEquals(3, model.size());
            assertNotNull(clone.get(0));
            assertNotNull(model.get(0));
            assertNotNull(clone.get(0).getSomething());
            assertNotNull(model.get(0).getSomething());
            assertEquals("hello", clone.get(0).getSomething().getWhatever());
            assertEquals("hello", model.get(0).getSomething().getWhatever());
            assertEquals(null, clone.get(1));
            assertNotNull(model.get(1));
            assertEquals(null, model.get(1).getSomething());
            assertEquals(null, model.get(2));
        }
    }

    @Test
    void testCloneMutableRootListClone(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithObjectMutable> mapper = new LarJsonTypedMapper<>(ModelWithObjectMutable.class,
                enrichConfigBuilder(new LarJsonTypedReadConfiguration.Builder().setMutable(true)).build());
        String json = "[{\"something\": {\"whatever\" : \"hello\"}}, {}, null]";
        try(LarJsonRootList<ModelWithObjectMutable> model = mapper.readArray(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            LarJsonList<ModelWithObjectMutable> clone = (LarJsonList<ModelWithObjectMutable>) model.clone();
            LarJsonList<ModelWithObjectMutable> cloneClone = (LarJsonList<ModelWithObjectMutable>) clone.clone();
            assertFalse(cloneClone == model);
            assertFalse(cloneClone == clone);
            assertTrue(cloneClone.equals(model));
            assertTrue(model.equals(cloneClone));
            assertEquals(model.hashCode(), cloneClone.hashCode());
            assertTrue(cloneClone.equals(clone));
            assertTrue(clone.equals(cloneClone));
            assertEquals(clone.hashCode(), cloneClone.hashCode());
            model.remove(1);
            assertFalse(cloneClone.equals(model));
            assertFalse(model.equals(cloneClone));
            assertNotEquals(model.hashCode(), cloneClone.hashCode());
            assertTrue(cloneClone.equals(clone));
            assertTrue(clone.equals(cloneClone));
            assertEquals(clone.hashCode(), cloneClone.hashCode());
            clone.remove(0);
            assertFalse(cloneClone.equals(model));
            assertFalse(model.equals(cloneClone));
            assertNotEquals(model.hashCode(), cloneClone.hashCode());
            assertFalse(cloneClone.equals(clone));
            assertFalse(clone.equals(cloneClone));
            assertNotEquals(clone.hashCode(), cloneClone.hashCode());
            cloneClone.remove(1);
            assertTrue(cloneClone.equals(model));
            assertTrue(model.equals(cloneClone));
            assertEquals(model.hashCode(), cloneClone.hashCode());
            assertFalse(cloneClone.equals(clone));
            assertFalse(clone.equals(cloneClone));
            assertNotEquals(clone.hashCode(), cloneClone.hashCode());
        }
    }

    @Test
    void testCloneMutableChildListUpdateBeforeClone(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithObjectList> mapper = new LarJsonTypedMapper<>(ModelWithObjectList.class,
                enrichConfigBuilder(new LarJsonTypedReadConfiguration.Builder().setMutable(true)).build());
        String json = "{\"something\":[{\"whatever\" : \"hello\"}, null, {}]}";
        try(ModelWithObjectList model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertNotNull(model.getSomething());
            model.getSomething().remove(2);
            LarJsonList<ModelWithString> clone = (LarJsonList<ModelWithString>) model.getSomething().clone();
            assertFalse(clone == model.getSomething());
            assertTrue(clone.equals(model.getSomething()));
            assertTrue(model.getSomething().equals(clone));
            assertEquals(model.getSomething().hashCode(), clone.hashCode());
            assertEquals(2, clone.size());
            assertEquals(2, model.getSomething().size());
            assertNotNull(clone.get(0));
            assertNotNull(model.getSomething().get(0));
            assertEquals("hello", clone.get(0).getWhatever());
            assertEquals("hello", model.getSomething().get(0).getWhatever());
            assertEquals(null, clone.get(1));
            assertEquals(null, model.getSomething().get(1));
        }
    }

    @Test
    void testCloneMutableChildListUpdateOriginal(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithObjectList> mapper = new LarJsonTypedMapper<>(ModelWithObjectList.class,
                enrichConfigBuilder(new LarJsonTypedReadConfiguration.Builder().setMutable(true)).build());
        String json = "{\"something\":[{\"whatever\" : \"hello\"}, null, {}]}";
        try(ModelWithObjectList model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertNotNull(model.getSomething());
            LarJsonList<ModelWithString> clone = (LarJsonList<ModelWithString>) model.getSomething().clone();
            assertFalse(clone == model.getSomething());
            assertTrue(clone.equals(model.getSomething()));
            assertTrue(model.getSomething().equals(clone));
            assertEquals(model.getSomething().hashCode(), clone.hashCode());
            model.getSomething().remove(2);
            assertFalse(clone.equals(model.getSomething()));
            assertFalse(model.getSomething().equals(clone));
            assertNotEquals(model.getSomething().hashCode(), clone.hashCode());
            assertEquals(3, clone.size());
            assertEquals(2, model.getSomething().size());
            assertNotNull(clone.get(0));
            assertNotNull(model.getSomething().get(0));
            assertEquals("hello", clone.get(0).getWhatever());
            assertEquals("hello", model.getSomething().get(0).getWhatever());
            assertEquals(null, clone.get(1));
            assertEquals(null, model.getSomething().get(1));
            assertNotNull(clone.get(2));
            assertEquals(null, clone.get(2).getWhatever());
        }
    }

    @Test
    void testCloneMutableChildListUpdateClone(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithObjectList> mapper = new LarJsonTypedMapper<>(ModelWithObjectList.class,
                enrichConfigBuilder(new LarJsonTypedReadConfiguration.Builder().setMutable(true)).build());
        String json = "{\"something\":[{\"whatever\" : \"hello\"}, null, {}]}";
        try(ModelWithObjectList model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertNotNull(model.getSomething());
            LarJsonList<ModelWithString> clone = (LarJsonList<ModelWithString>) model.getSomething().clone();
            assertFalse(clone == model.getSomething());
            assertTrue(clone.equals(model.getSomething()));
            assertTrue(model.getSomething().equals(clone));
            assertEquals(model.getSomething().hashCode(), clone.hashCode());
            clone.remove(2);
            assertFalse(clone.equals(model.getSomething()));
            assertFalse(model.getSomething().equals(clone));
            assertNotEquals(model.getSomething().hashCode(), clone.hashCode());
            assertEquals(2, clone.size());
            assertEquals(3, model.getSomething().size());
            assertNotNull(clone.get(0));
            assertNotNull(model.getSomething().get(0));
            assertEquals("hello", clone.get(0).getWhatever());
            assertEquals("hello", model.getSomething().get(0).getWhatever());
            assertEquals(null, clone.get(1));
            assertEquals(null, model.getSomething().get(1));
            assertNotNull(model.getSomething().get(2));
            assertEquals(null, model.getSomething().get(2).getWhatever());
        }
    }

    @Test
    void testCloneMutableChildListClone(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithObjectList> mapper = new LarJsonTypedMapper<>(ModelWithObjectList.class,
                enrichConfigBuilder(new LarJsonTypedReadConfiguration.Builder().setMutable(true)).build());
        String json = "{\"something\":[{\"whatever\" : \"hello\"}, null, {}]}";
        try(ModelWithObjectList model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertNotNull(model.getSomething());
            LarJsonList<ModelWithString> clone = (LarJsonList<ModelWithString>) model.getSomething().clone();
            LarJsonList<ModelWithString> cloneClone = (LarJsonList<ModelWithString>) clone.clone();
            assertFalse(cloneClone == model.getSomething());
            assertFalse(cloneClone == clone);
            assertTrue(cloneClone.equals(model.getSomething()));
            assertTrue(model.getSomething().equals(cloneClone));
            assertEquals(model.getSomething().hashCode(), cloneClone.hashCode());
            assertTrue(cloneClone.equals(clone));
            assertTrue(clone.equals(cloneClone));
            assertEquals(clone.hashCode(), cloneClone.hashCode());
            model.getSomething().remove(1);
            assertFalse(cloneClone.equals(model.getSomething()));
            assertFalse(model.getSomething().equals(cloneClone));
            assertNotEquals(model.getSomething().hashCode(), cloneClone.hashCode());
            assertTrue(cloneClone.equals(clone));
            assertTrue(clone.equals(cloneClone));
            assertEquals(clone.hashCode(), cloneClone.hashCode());
            clone.remove(0);
            assertFalse(cloneClone.equals(model.getSomething()));
            assertFalse(model.getSomething().equals(cloneClone));
            assertNotEquals(model.getSomething().hashCode(), cloneClone.hashCode());
            assertFalse(cloneClone.equals(clone));
            assertFalse(clone.equals(cloneClone));
            assertNotEquals(clone.hashCode(), cloneClone.hashCode());
            cloneClone.remove(1);
            assertTrue(cloneClone.equals(model.getSomething()));
            assertTrue(model.getSomething().equals(cloneClone));
            assertEquals(model.getSomething().hashCode(), cloneClone.hashCode());
            assertFalse(cloneClone.equals(clone));
            assertFalse(clone.equals(cloneClone));
            assertNotEquals(clone.hashCode(), cloneClone.hashCode());
        }
    }

    @Test
    void testCloneTwoMutableChildLists(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithTwoObjectLists> mapper = new LarJsonTypedMapper<>(ModelWithTwoObjectLists.class,
                enrichConfigBuilder(new LarJsonTypedReadConfiguration.Builder().setMutable(true)).build());
        String json = "{\"something\":[{\"whatever\" : \"hello\"}, null, {}], " +
                "\"another\":[{\"whatever\" : \"salut\"}, {}, {}, {}]}";
        try(ModelWithTwoObjectLists model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertNotNull(model.getSomething());
            assertNotNull(model.getAnother());

            LarJsonList<ModelWithString> somethingClone = (LarJsonList<ModelWithString>) model.getSomething().clone();
            assertFalse(somethingClone == model.getSomething());
            assertEquals(model.getSomething(), somethingClone);
            model.getSomething().remove(2);
            assertNotEquals(model.getSomething(), somethingClone);
            assertEquals(3, somethingClone.size());
            assertEquals(2, model.getSomething().size());

            LarJsonList<ModelWithString> anotherClone = (LarJsonList<ModelWithString>) model.getAnother().clone();
            assertFalse(anotherClone == model.getAnother());
            assertEquals(model.getAnother(), anotherClone);
            model.getAnother().add(0, null);
            assertNotEquals(model.getAnother(), somethingClone);
            assertEquals(4, anotherClone.size());
            assertEquals(5, model.getAnother().size());
        }
    }

    protected LarJsonTypedReadConfiguration.Builder enrichConfigBuilder(LarJsonTypedReadConfiguration.Builder builder) {
        return builder;
    }
}
