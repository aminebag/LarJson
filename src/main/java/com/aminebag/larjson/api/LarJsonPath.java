package com.aminebag.larjson.api;

/**
 * @author Amine Bagdouri
 *
 * <p>A class implements this interface to indicate that it has an exposeable JSON path.</p>
 * <p>A path is always prefixed by a '$' character representing the JSON root element.
 * The '.x' denotes a JSON object attribute and the '[x]' denotes a JSON array element</p>
 * <p>Example of returned path: <pre>$.trip[3].departureAirport</pre></p>
 */
public interface LarJsonPath {

    /**
     * @return the JSON path to the current element from the root
     */
    default String getLarJsonPath() {
        StringBuilder sb = new StringBuilder();
        getLarJsonPath(sb);
        return sb.toString();
    }

    /**
     * Appends the JSON path of the current element to the provided {@link StringBuilder}
     * @param sb the {@link StringBuilder}
     */
    void getLarJsonPath(StringBuilder sb);

    /**
     * @return this element's parent path, or {@code null} if this is root
     */
    LarJsonPath getParentLarJsonPath();
}
