package com.aminebag.larjson.configuration;

/**
 * @author Amine Bagdouri
 *
 * A class that implements this interface can be used as a substitue for the default implementation of
 * {@link #equals(Object)} and {@link #hashCode()} of a LarJson object, and it must honour the same
 * contract as that of {@link #equals(Object)} and {@link #hashCode()} methods.
 */
public interface EqualsDelegate<T> {

    /**
     * Indicates whether the two objects are equal. It must honour the same contract as that of
     * {@link #equals(Object)} method.
     * @return {@code true} if the two arguments are equal, {@code false} otherwise.
     * @see #equals(Object)
     */
    boolean equals(T thisInstance, Object otherInstance);

    /**
     * Returns a hash code value for the object. It must honour the same contract as that of {@link #hashCode()} method.
     * @return a hash code value for this object.
     * @see #hashCode()
     */
    int hashCode(T thisInstance);
}
