package com.aminebag.larjson.jsonmapper;

import java.io.Closeable;
import java.util.List;

public interface CloseableList<T> extends List<T>, Closeable {
}
