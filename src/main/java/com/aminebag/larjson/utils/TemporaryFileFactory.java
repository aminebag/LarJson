package com.aminebag.larjson.utils;

import java.io.File;
import java.io.IOException;

/**
 * @author Amine Bagdouri
 *
 * A factory of temporary files
 */
public interface TemporaryFileFactory {

    /**
     * Creates a temporary file that should be removed once it's no longer needed. Must return a different file for
     * each call.
     * @return the created temporary file
     * @throws IOException if it fails to create a temporary file
     */
    File createTemporaryFile() throws IOException;
}
