package com.aminebag.larjson.api;

/**
 * @author Amine Bagdouri
 *
 * A class implements this interface to indicate that its instances can be cloned via the {@link #clone()} method
 */
public interface LarJsonCloneable extends Cloneable {

    /**
     * Returns an independent copy of the current instance. The returned copy must be equal to the current instance
     * until one of them is updated. Updates to the copy must not be reflected on the current instance and vice versa.
     * An implementation of this interface might choose to return the current instance if it's immutable.
     * @return a copy of the current instance
     */
    Object clone();
}
