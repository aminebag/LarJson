package com.aminebag.larjson.blueprint;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Amine Bagdouri
 */
public class MultiArrayBlueprintTest extends LarJsonBlueprintTest {
    @Override
    protected LarJsonBlueprintReader blueprintReader(byte[] array, Path tempDir) {
        int size = array.length;
        boolean zeroMod = (size % 8) == 0;
        byte[][] arrays = new byte[zeroMod ? size / 8 : (size / 8) + 1][];
        for(int i=0;i<arrays.length-1;i++) {
            arrays[i] = new byte[8];
        }
        arrays[arrays.length-1] = new byte[zeroMod ? 8 : size % 8];
        for(int i=0; i<array.length; i++) {
            arrays[i/8][i%8] = array[i];
        }
        return new SimpleBlueprintReader(new MultiArrayBinaryReader(arrays, size, 3));
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
