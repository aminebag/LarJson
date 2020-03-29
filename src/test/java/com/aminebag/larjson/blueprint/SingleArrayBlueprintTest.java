package com.aminebag.larjson.blueprint;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Amine Bagdouri
 */
public class SingleArrayBlueprintTest extends LarJsonBlueprintTest {
    @Override
    protected LarJsonBlueprintReader blueprintReader(byte[] array, Path tempDir) {
        return new SimpleBlueprintReader(new SingleArrayBinaryReader(array));
    }

    @Override
    protected int maxMemoryBlueprintSize() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected void assertBlueprintFile(File file) {
        assertNotNull(file);
        assertFalse(file.exists());
    }
}
