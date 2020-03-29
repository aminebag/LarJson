package com.aminebag.larjson.api;

import java.util.List;

/**
 * @author Amine Bagdouri
 *
 * A class implements this interface to indicate that it represents a JSON array whose elements are mapped to a model
 * interface.
 */
public interface LarJsonList<T> extends List<T>, LarJsonTypedElement {
}
