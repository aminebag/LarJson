package com.aminebag.larjson.mapper;

import com.aminebag.larjson.exception.LarJsonException;
import com.aminebag.larjson.configuration.LarJsonTypedReadConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.*;

import static com.aminebag.larjson.mapper.LarJsonMapperTestUtils.jsonToFile;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author Amine Bagdouri
 */
public class LarJsonTypedMapperThreadSafeOverwriteTest extends LarJsonTypedMapperOverwriteTest {

    @Test
    void testOverwriteThreadSafe(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<LarJsonTypedMapperTestModels.ModelWithStringMutable> mapper = new LarJsonTypedMapper<>(LarJsonTypedMapperTestModels.ModelWithStringMutable.class,
                enrichConfigBuilder(new LarJsonTypedReadConfiguration.Builder().setMutable(true)).build());
        String json = "{\"whatever\": \"hello\"}";
        try(LarJsonTypedMapperTestModels.ModelWithStringMutable model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals("hello", model.getWhatever());
            model.setWhatever("bonjour");
            assertEquals("bonjour", model.getWhatever());
            waitForAsyncTask(()->{
                assertEquals("bonjour", model.getWhatever());
                model.setWhatever("salut");
            });
            assertEquals("salut", model.getWhatever());
        }
    }

    private static void waitForAsyncTask(Runnable task) {
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

    @Override
    protected LarJsonTypedReadConfiguration.Builder enrichConfigBuilder(LarJsonTypedReadConfiguration.Builder builder) {
        return super.enrichConfigBuilder(builder).setThreadSafe(true);
    }
}
