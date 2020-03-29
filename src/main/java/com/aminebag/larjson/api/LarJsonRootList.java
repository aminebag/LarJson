package com.aminebag.larjson.api;

/**
 * @author Amine Bagdouri
 *
 * A class implements this interface to indicate that it represents a JSON root array whose elements are mapped to a
 * model interface.
 */
public interface LarJsonRootList<T> extends LarJsonList<T>, LarJsonRootTypedElement {
}
