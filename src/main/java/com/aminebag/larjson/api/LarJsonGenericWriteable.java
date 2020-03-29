//package com.aminebag.larjson.api;
//
//import com.aminebag.larjson.exception.LarJsonException;
//import com.aminebag.larjson.jsonmapper.configuration.LarJsonGenericWriteConfiguration;
//import com.aminebag.larjson.utils.LarJsonUtils;
//import com.google.gson.stream.JsonWriter;
//
//import java.io.IOException;
//import java.io.Writer;
//
///**
// * @author Amine Bagdouri
// */
//public interface LarJsonGenericWriteable extends LarJsonWriteable {
//
//    /**
//     *
//     * @param jsonWriter
//     * @param writeConfiguration
//     * @throws IOException
//     * @throws LarJsonException
//     * @throws com.aminebag.larjson.exception.LarJsonRuntimeException
//     */
//    void write(JsonWriter jsonWriter, LarJsonGenericWriteConfiguration writeConfiguration) throws IOException, LarJsonException;
//
//    /**
//     *
//     * @param writer
//     * @param writeConfiguration
//     * @throws IOException
//     * @throws LarJsonException
//     * @throws com.aminebag.larjson.exception.LarJsonRuntimeException
//     */
//    default void write(Writer writer, LarJsonGenericWriteConfiguration writeConfiguration) throws IOException,
//            LarJsonException{
//        JsonWriter jsonWriter = LarJsonUtils.getJsonWriter(writer, writeConfiguration);
//        write(jsonWriter, writeConfiguration);
//    }
//}
