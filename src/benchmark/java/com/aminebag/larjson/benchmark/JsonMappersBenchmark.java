package com.aminebag.larjson.benchmark;

import com.aminebag.larjson.benchmark.mapper.GsonMapper;
import com.aminebag.larjson.benchmark.mapper.JacksonMapper;
import com.aminebag.larjson.benchmark.mapper.JsonMapper;
import com.aminebag.larjson.benchmark.mapper.LarJsonMapper;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * @author Amine Bagdouri
 */
public class JsonMappersBenchmark {

    public static final String DATA_FOLDER = "json-data";

    public void run(String mapperId, String fileName) throws Exception {
        JsonMapper mapper = getMapper(mapperId);
        File outputFile = File.createTempFile(mapperId + "_", ".json");
        try {
            File file = getFile(fileName);
            long startReading = System.currentTimeMillis();
            System.out.println("Memory before reading, in bytes : " +
                    String.format(Locale.US, "%,d", getUsedMemory()));
            System.out.println("Start reading...");
            Object value = mapper.readObject(file);
            try {
                long endReading = System.currentTimeMillis();
                System.out.println("Reading ended...");
                System.out.println("Reading duration, in millis : " +
                        String.format(Locale.US, "%,d", endReading - startReading));
                System.out.println("Memory after reading, in bytes : " +
                        String.format(Locale.US, "%,d", getUsedMemory()));

                System.out.println("Start writing...");
                long startWriting = System.currentTimeMillis();
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                    mapper.writeObject(value, writer);
                }
                long endWriting = System.currentTimeMillis();
                System.out.println("Writing ended...");
                System.out.println("Writing duration, in millis : " +
                        String.format(Locale.US, "%,d", endWriting - startWriting));
                System.out.println("Memory after writing, in bytes : " +
                        String.format(Locale.US, "%,d", getUsedMemory()));
                System.out.println("Output file size, in kb : " +
                        String.format(Locale.US, "%,d", outputFile.length() / 1024));
            } finally {
                mapper.closeObject(value);
            }
        } finally {
            if(outputFile.exists()) {
                outputFile.delete();
            }
        }
    }

    private long getUsedMemory() {
        System.gc(); //Request a GC (hopefully the request will be honoured)
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    private File getFile(String fileName) throws UnsupportedEncodingException {
        ClassLoader classLoader = getClass().getClassLoader();
        String path = DATA_FOLDER + "/" + fileName;
        URL url = classLoader.getResource(path);
        if(url == null) {
            throw new IllegalArgumentException("File not found : " + path);
        }
        return new File(URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8.name()));
    }

    public JsonMapper getMapper(String mapperId) throws IOException {
        switch (mapperId) {
            case "larjsonMemoryBlueprint": return new LarJsonMapper(Long.MAX_VALUE);
            case "larjsonFileBlueprint": return new LarJsonMapper(0);
            case "gson": return new GsonMapper();
            case "jackson": return new JacksonMapper();
            default: throw new IllegalArgumentException("Unknown mapper : " + mapperId);
        }
    }

    public static void main(String... args) throws Exception {
        if(args.length != 2) {
            throw new IllegalArgumentException("The following arguments must be provided : <mapperId> <jsonFileName>");
        }
        new JsonMappersBenchmark().run(args[0], args[1]);
    }
}
