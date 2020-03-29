package com.aminebag.larjson.mapper;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Amine Bagdouri
 */
public class LarJsonMapperTestUtils {

    private final static AtomicInteger counter = new AtomicInteger(0);

    static File jsonToFile(Path tempDir, String json) throws IOException {
        Path path = tempDir.resolve("test" + counter.getAndIncrement() + ".json");
        Files.write(path, json.getBytes(StandardCharsets.UTF_8));
        return path.toFile();
    }
}
