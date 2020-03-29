package com.aminebag.larjson.resource;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author Amine Bagdouri
 *
 * A closeable factory of resources
 */
public interface ResourceFactory<T> extends Closeable {

    T create() throws IOException;
}
