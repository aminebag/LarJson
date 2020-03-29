package com.aminebag.larjson.resource;

import java.io.Closeable;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

/**
 * @author Amine Bagdouri
 *
 * A closeable pool of resource
 */
public final class ResourcePool<T extends Closeable> implements Closeable {
    private final ThreadLocal<T> localResource = new ThreadLocal<>();
    private final Set<T> openResources = ConcurrentHashMap.newKeySet();
    private final ResourceFactory<T> resourceFactory;
    private final AtomicBoolean closed = new AtomicBoolean(false);
    private final AtomicInteger pending = new AtomicInteger(0);

    public ResourcePool(ResourceFactory<T> factory) {
        this.resourceFactory = factory;
    }

    public T get() throws IOException {
        checkNotClosed();
        pending.getAndIncrement();
        try {
            checkNotClosed();
            T resource = localResource.get();
            if (resource == null) {
                resource = resourceFactory.create();
                openResources.add(resource);
                localResource.set(resource);
            }
            return resource;
        }finally {
            pending.getAndDecrement();
        }
    }

    private void checkNotClosed() throws IOException {
        if(closed.get()) {
            throw new IOException("pool was closed");
        }
    }

    @Override
    public void close() throws IOException {
        if(closed.get()) {
            return;
        }
        closed.set(true);
        while (pending.get() > 0) {
            LockSupport.parkNanos(1L);
        }
        new SafeResourceCloser()
                .add(openResources)
                .add(()->openResources.clear())
                .add(resourceFactory)
                .close();
    }
}
