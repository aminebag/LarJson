package com.aminebag.larjson.benchmark;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * @author Amine Bagdouri
 */
public class JsonDataGenerator {

    private static final int AVG_BEER_OBJECT_SIZE = 1937;

    public void run(long targetFileSizeKb) throws IOException {
        long targetFileSize = targetFileSizeKb * 1024;
        long approximateNbBeers = (targetFileSizeKb * 1024) / AVG_BEER_OBJECT_SIZE;
        long root = (long) Math.sqrt(approximateNbBeers);
        List<String> jsonArray = new ArrayList<>();
        Files.list(getPoolFolderPath()).forEach(f->{

            try {
                JSONArray a = new JSONArray(new String(Files.readAllBytes(f.toAbsolutePath()), StandardCharsets.UTF_8));
                for(Object o : a) {
                    JSONObject jsonObject = (JSONObject) o;
                    jsonArray.add(jsonObject.toString());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                getDataFile("beers-" + formatSize(targetFileSizeKb) + ".json")), StandardCharsets.UTF_8))) {

            String suffix = "]}]}";
            long fileSize = suffix.length();
            fileSize += write(writer, "{\"beers\":[{\"group\":[");
            long next = (long) ((0.5 + (Math.random()/2)) * root);
            for(long i=0 ; true ; i++) {
                boolean newArray = i == next;
                if(newArray) {
                    fileSize += write(writer, "]},{\"group\":[");
                    next = i + (long) ((0.5 + (Math.random()/2)) * root);
                }
                String beer = jsonArray.get(new Random().nextInt(jsonArray.size()));
                fileSize += write(writer, beer);
                if(fileSize < targetFileSize) {
                    if (i + 1 != next) {
                        fileSize += write(writer, ",");
                    }
                } else {
                    break;
                }
            }
            write(writer, suffix);
        }
    }

    int write(Writer writer, String s) throws IOException {
        writer.write(s);
        return s.length();
    }

    String formatSize(long sizeInKb) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

        symbols.setGroupingSeparator('_');
        formatter.setDecimalFormatSymbols(symbols);
        return formatter.format(sizeInKb) + "-kb";
    }

    private Path getPoolFolderPath() throws UnsupportedEncodingException {
        ClassLoader classLoader = getClass().getClassLoader();
        String path = "json-pool";
        URL url = classLoader.getResource(path);
        if(url == null) {
            throw new IllegalArgumentException("JSON pool folder not found : " + path);
        }
        return new File(URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8.name())).toPath();
    }

    private File getDataFile(String fileName) throws UnsupportedEncodingException {
        ClassLoader classLoader = getClass().getClassLoader();
        String path = JsonMappersBenchmark.DATA_FOLDER;
        URL url = classLoader.getResource(path);
        if(url == null) {
            throw new IllegalArgumentException("JSON data folder not found : " + path);
        }
        File dataFolder = new File(URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8.name()));
        if(!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        return new File(dataFolder, fileName);
    }

    public static void main(String... args) throws Exception {
        if(args.length != 1) {
            throw new IllegalArgumentException("The following argument must be provided : <targetFileSizeKb>");
        }
        long targetFileSizeKb = Long.parseLong(args[0]);
        new JsonDataGenerator().run(targetFileSizeKb);
    }
}
