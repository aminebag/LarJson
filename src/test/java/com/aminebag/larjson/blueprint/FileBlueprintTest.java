package com.aminebag.larjson.blueprint;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Amine Bagdouri
 */
public class FileBlueprintTest extends LarJsonBlueprintTest {

    @Override
    protected LarJsonBlueprintReader blueprintReader(byte[] array, Path tempDir) throws IOException {
        File file = tempDir.resolve("test.larjson").toFile();
        Files.write(file.toPath(), array);
        return new BufferedBlueprintReader(new FileBinaryReader(file));
    }

    @Override
    protected int maxMemoryBlueprintSize() {
        return 0;
    }

    @Override
    protected void assertBlueprintFile(File file) {
        assertNotNull(file);
        assertTrue(file.exists());
        assertTrue(file.length() > 0);
    }

    @Test
    void testBlueprintOnErrorCleaner(@TempDir Path tempDir)
            throws IOException {
        AtomicReference<File> fileReference = new AtomicReference<>(null);
        Closeable cleaner;
        try(LarJsonBlueprintWriter blueprintWriter = new LarJsonBlueprintWriter(maxMemoryBlueprintSize(),
                () -> {
                    File file = fileFactory(tempDir).createTemporaryFile();
                    fileReference.set(file);
                    return file;
                })) {
            cleaner = blueprintWriter.getOnErrorCleaner();
            blueprintWriter.put(5L);
        }
        cleaner.close();
        assertNotNull(fileReference.get());
        assertFalse(fileReference.get().exists());
    }
}
