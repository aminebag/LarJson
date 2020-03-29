package com.aminebag.larjson.blueprint;

import com.aminebag.larjson.resource.ResourceFactory;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author Amine Bagdouri
 *
 * A binary writer that can store bytes in a binary source. It can be used, once closed, to generate blueprint
 * readers.
 */
interface BinaryWriter extends Closeable {

    void write(ByteBuffer buffer) throws IOException;
    ResourceFactory<? extends LarJsonBlueprintReader> getBlueprintReaderFactory();
    Closeable getPostErrorCleaner();
}
