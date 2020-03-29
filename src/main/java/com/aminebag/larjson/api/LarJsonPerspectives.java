package com.aminebag.larjson.api;

import com.aminebag.larjson.exception.LarJsonException;
import com.aminebag.larjson.configuration.LarJsonTypedWriteConfiguration;
import com.aminebag.larjson.exception.LarJsonWriteException;

import java.io.IOException;
import java.io.Writer;

/**
 * @author Amine Bagdouri
 *
 * Provide access to functionalities of LarJson objects.
 */
public class LarJsonPerspectives {
    private LarJsonPerspectives() {

    }

    /**
     * Provided that the {@code o} argument is an instance of {@link LarJsonTypedWriteable}, write the current
     * instance in the provided {@link Writer} argument.
     * @param writer the writer
     * @param writeConfiguration the configuration used for the serialization
     * @throws IOException if a resource access error is encountered
     * @throws LarJsonWriteException if a JSON write related error is encountered
     * @throws LarJsonException if another JSON related error is encountered
     * @throws IllegalArgumentException if {@code o} is not an instance of {@link LarJsonTypedWriteable}
     * @see LarJsonTypedWriteable#write(Writer, LarJsonTypedWriteConfiguration)
     */
    public static void write(Object o, Writer writer, LarJsonTypedWriteConfiguration writeConfiguration)
            throws IOException, LarJsonException {
        LarJsonTypedWriteable writeable = checkType(LarJsonTypedWriteable.class, o);
        writeable.write(writer, writeConfiguration);
    }

    /**
     * Provided that the {@code o} argument is an instance of {@link LarJsonWriteable}, write the current instance
     * in the provided {@link Writer} argument.
     * @param writer the writer
     * @throws IOException if a resource access error is encountered
     * @throws LarJsonWriteException if a JSON write related error is encountered
     * @throws LarJsonException if another JSON related error is encountered
     * @throws IllegalArgumentException if {@code o} is not an instance of {@link LarJsonWriteable}
     * @see LarJsonWriteable#write(Writer)
     */
    public static void write(Object o, Writer writer) throws IOException, LarJsonException {
        LarJsonWriteable writeable = checkType(LarJsonWriteable.class, o);
        writeable.write(writer);
    }

    /**
     * Provided that the {@code o} argument is an instance of {@link LarJsonPath}, return its JSON path
     * @return the JSON path to the provided element from the root
     * @throws IllegalArgumentException if {@code o} is not an instance of {@link LarJsonPath}
     * @see LarJsonPath#getLarJsonPath()
     */
    public static String getPath(Object o) {
        LarJsonPath larJsonPath = checkType(LarJsonPath.class, o);
        return larJsonPath.getLarJsonPath();
    }

    /**
     * Provided that the {@code o} argument is an instance of {@link LarJsonPath}, return its parent JSON path
     * @return this element's parent path, or {@code null} if {@code o} is root
     * @throws IllegalArgumentException if {@code o} is not an instance of {@link LarJsonPath}
     * @see LarJsonPath#getParentLarJsonPath()
     */
    public static LarJsonPath getParentPath(Object o) {
        LarJsonPath larJsonPath = checkType(LarJsonPath.class, o);
        return larJsonPath.getParentLarJsonPath();
    }

    /**
     * Provided that the {@code o} argument is an instance of {@link LarJsonCloneable}, return a clone of the
     * provided object
     * @return a clone of the provided object
     * @throws IllegalArgumentException if {@code o} is not an instance of {@link LarJsonCloneable}
     * @see LarJsonCloneable#clone()
     */
    public static Object clone(Object o) {
        LarJsonCloneable cloneable = checkType(LarJsonCloneable.class, o);
        return cloneable.clone();
    }

    private static <T> T checkType(Class<T> type, Object o) {
        if(!type.isInstance(o)) {
            throw new IllegalArgumentException("Object is not " + type.getName());
        }
        return (T) o;
    }
}
