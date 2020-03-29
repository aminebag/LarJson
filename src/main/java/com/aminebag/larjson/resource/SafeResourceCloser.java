package com.aminebag.larjson.resource;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Amine Bagdouri
 *
 * Closes a list of resources with a guarantee that all resources will be closed even if the closing of any of the
 * resources fails.
 */
public class SafeResourceCloser implements Closeable {

    private List<Closeable> closeables = new ArrayList<>();

    /**
     * Adds a list of {@link Closeable}s to the closer
     * @return this instance
     */
    public SafeResourceCloser add(Collection<? extends Closeable> closeables) {
        this.closeables.addAll(closeables);
        return this;
    }

    /**
     * Adds a {@link Closeable}s to the closer
     * @return this instance
     */
    public SafeResourceCloser add(Closeable closeable) {
        this.closeables.add(closeable);
        return this;
    }

    /**
     * Closes all the resources that were added to this closer while respecting insertion order and ensuring all
     * resources are closed.
     * @throws IOException if a single resource fails because of a {@link IOException}
     * @throws RuntimeException if a single resource fails because of a {@link RuntimeException}
     * @throws MultipleResourcesRuntimeException if multiple resources fail because of {@link RuntimeException}s only
     * @throws MultipleResourcesIOException if multiple resources fail because of {@link IOException}s and possibly
     *                                      {@link RuntimeException}s
     */
    @Override
    public void close() throws IOException {
        if(closeables == null) {
            return;
        }
        List<IOException> ioExceptions = new ArrayList<>();
        List<RuntimeException> runtimeExceptions = new ArrayList<>();
        for(Closeable closeable : closeables) {
            try {
                closeable.close();
            } catch (IOException e) {
                ioExceptions.add(e);
            } catch (RuntimeException e) {
                runtimeExceptions.add(e);
            }
        }
        closeables = null;
        if(ioExceptions.size() > 0) {
            if(ioExceptions.size() > 1 || runtimeExceptions.size() > 0) {
                throw new MultipleResourcesIOException(ioExceptions, runtimeExceptions);
            } else {
                throw ioExceptions.get(0);
            }
        } else if (runtimeExceptions.size() > 1) {
            throw new MultipleResourcesRuntimeException(runtimeExceptions);
        } else if (runtimeExceptions.size() == 1) {
            throw runtimeExceptions.get(0);
        }
    }
}
