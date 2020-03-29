package com.aminebag.larjson.api;

import com.aminebag.larjson.exception.LarJsonException;
import com.google.gson.stream.JsonWriter;
import com.aminebag.larjson.exception.LarJsonWriteException;

import java.io.IOException;
import java.io.Writer;

/**
 * @author Amine Bagdouri
 *
 * A class implements this interface to indicate that its instances can be serialized in a JSON format.
 */
public interface LarJsonWriteable {

    /**
     * Write the current instance in the provided {@link JsonWriter} argument. This method doesn't close the writer.
     * @param jsonWriter the JSON writer
     * @throws IOException if a resource access error is encountered
     * @throws LarJsonWriteException if a JSON write related error is encountered
     * @throws LarJsonException if another JSON related error is encountered
     */
    void write(JsonWriter jsonWriter) throws IOException, LarJsonException;

    /**
     * Write the current instance in the provided {@link Writer} argument. This method doesn't close the writer.
     * @param writer the writer
     * @throws IOException if a resource access error is encountered
     * @throws LarJsonWriteException if a JSON write related error is encountered
     * @throws LarJsonException if another JSON related error is encountered
     */
    void write(Writer writer) throws IOException, LarJsonException;
}
