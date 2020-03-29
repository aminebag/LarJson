package com.aminebag.larjson.api;

import com.aminebag.larjson.exception.LarJsonException;
import com.aminebag.larjson.mapper.LarJsonMapperUtils;
import com.aminebag.larjson.configuration.LarJsonTypedWriteConfiguration;
import com.aminebag.larjson.exception.LarJsonWriteException;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.Writer;

/**
 * @author Amine Bagdouri
 *
 * A class implements this interface to indicate that its instances can be serialized in a JSON format.
 */
public interface LarJsonTypedWriteable extends LarJsonWriteable {

    /**
     * Write the current instance in the provided {@link JsonWriter} argument. This method doesn't close the writer.
     * The serialization can be customizd using the provided {@link LarJsonTypedWriteConfiguration}
     * @param jsonWriter the JSON writer
     * @param writeConfiguration the configuration used for the serialization
     * @throws IOException if a resource access error is encountered
     * @throws LarJsonWriteException if a JSON write related error is encountered
     * @throws LarJsonException if another JSON related error is encountered
     */
    void write(JsonWriter jsonWriter, LarJsonTypedWriteConfiguration writeConfiguration) throws IOException, LarJsonException;

    /**
     * Write the current instance in the provided {@link Writer} argument. This method doesn't close the writer.
     * The serialization can be customizd using the provided {@link LarJsonTypedWriteConfiguration}
     * @param writer the writer
     * @param writeConfiguration the configuration used for the serialization
     * @throws IOException if a resource access error is encountered
     * @throws LarJsonWriteException if a JSON write related error is encountered
     * @throws LarJsonException if another JSON related error is encountered
     */
    default void write(Writer writer, LarJsonTypedWriteConfiguration writeConfiguration) throws IOException,
            LarJsonException{
        JsonWriter jsonWriter = LarJsonMapperUtils.getJsonWriter(writer, writeConfiguration);
        write(jsonWriter, writeConfiguration);
    }
}
