package com.aminebag.larjson.mapper;

import com.aminebag.larjson.exception.LarJsonException;
import com.aminebag.larjson.configuration.LarJsonTypedReadConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

import static com.aminebag.larjson.mapper.LarJsonMapperTestUtils.jsonToFile;
import static com.aminebag.larjson.mapper.LarJsonTypedMapperTestModels.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Amine Bagdouri
 */
public class LarJsonTypedMapperOverwriteTest {

    @Test
    void testOverwriteConfigurationImmutable(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithStringMutable> mapper = new LarJsonTypedMapper<>(ModelWithStringMutable.class,
                enrichConfigBuilder(new LarJsonTypedReadConfiguration.Builder().setUnsupportedMethodAllowed(true)).build());
        String json = "{\"whatever\": \"hello\"}";
        try(ModelWithStringMutable model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            model.setWhatever("bonjour");
            fail();
        }catch (UnsupportedOperationException expected) {
        }
    }

    @Test
    void testOverwriteString(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithStringMutable> mapper = new LarJsonTypedMapper<>(ModelWithStringMutable.class,
                enrichConfigBuilder(new LarJsonTypedReadConfiguration.Builder().setMutable(true)).build());
        String json = "{\"whatever\": \"hello\"}";
        try(ModelWithStringMutable model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals("hello", model.getWhatever());
            model.setWhatever("bonjour");
            assertEquals("bonjour", model.getWhatever());
        }
    }

    @Test
    void testOverwriteNull(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithStringMutable> mapper = new LarJsonTypedMapper<>(ModelWithStringMutable.class,
                enrichConfigBuilder(new LarJsonTypedReadConfiguration.Builder().setMutable(true)).build());
        String json = "{\"whatever\": null}";
        try(ModelWithStringMutable model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(null, model.getWhatever());
            model.setWhatever("bonjour");
            assertEquals("bonjour", model.getWhatever());
        }
    }

    @Test
    void testOverwriteMissing(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithStringMutable> mapper = new LarJsonTypedMapper<>(ModelWithStringMutable.class,
                enrichConfigBuilder(new LarJsonTypedReadConfiguration.Builder().setMutable(true)).build());
        String json = "{}";
        try(ModelWithStringMutable model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(null, model.getWhatever());
            model.setWhatever("bonjour");
            assertEquals("bonjour", model.getWhatever());
        }
    }

    @Test
    void testOverwriteWithNull(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithStringMutable> mapper = new LarJsonTypedMapper<>(ModelWithStringMutable.class,
                enrichConfigBuilder(new LarJsonTypedReadConfiguration.Builder().setMutable(true)).build());
        String json = "{\"whatever\": \"hello\"}";
        try(ModelWithStringMutable model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals("hello", model.getWhatever());
            model.setWhatever(null);
            assertEquals(null, model.getWhatever());
        }
    }

    @Test
    void testOverwriteObject(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithObjectMutable> mapper = new LarJsonTypedMapper<>(ModelWithObjectMutable.class,
                enrichConfigBuilder(new LarJsonTypedReadConfiguration.Builder().setMutable(true)).build());
        String json = "{\"something\":{\"whatever\": \"hello\"}}";
        ModelWithStringMutable oldStringModel;
        ModelWithStringMutable newStringModel = new ModelWithStringMutableImpl();
        try(ModelWithObjectMutable model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertNotNull(model.getSomething());
            assertEquals("hello", model.getSomething().getWhatever());
            model.getSomething().setWhatever("salut");
            assertEquals("salut", model.getSomething().getWhatever());
            oldStringModel = model.getSomething();
            model.setSomething(newStringModel);
            assertTrue(newStringModel == model.getSomething());
            assertEquals("salut", oldStringModel.getWhatever());
        }
    }

    protected LarJsonTypedReadConfiguration.Builder enrichConfigBuilder(LarJsonTypedReadConfiguration.Builder builder) {
        return builder;
    }
}
