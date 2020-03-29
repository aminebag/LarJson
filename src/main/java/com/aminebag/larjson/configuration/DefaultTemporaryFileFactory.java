package com.aminebag.larjson.configuration;

import com.aminebag.larjson.utils.TemporaryFileFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Amine Bagdouri
 */
public class DefaultTemporaryFileFactory implements TemporaryFileFactory {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss_");

    @Override
    public File createTemporaryFile() throws IOException {
        return Files.createTempFile(LocalDateTime.now().format(DATE_TIME_FORMATTER), ".larjson").toFile();
    }

}
