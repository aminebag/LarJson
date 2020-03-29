package com.aminebag.larjson.resource;

import org.junit.jupiter.api.Test;

import java.io.Closeable;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author Amine Bagdouri
 */
public class ResourcePoolTest {

    @Test
    void testResourcePoolClose() throws InterruptedException, IOException {
        AtomicInteger factoryClose = new AtomicInteger(0);
        ResourcePool<Resource> resourcePool = new ResourcePool<>(new ResourceFactory<Resource>() {
            AtomicInteger resourceCounter = new AtomicInteger();
            @Override
            public Resource create() throws IOException {
                int id = resourceCounter.getAndIncrement();
                if(id == 7) {
                    return new IOCorruptResource();
                } else if(id == 13) {
                    return new RuntimeCorruptResource();
                } else {
                    return new Resource();
                }
            }

            @Override
            public void close() throws IOException {
                factoryClose.getAndIncrement();
            }
        });
        Set<Resource> resources = ConcurrentHashMap.newKeySet();
        int nbResources = 20;
        CountDownLatch countDownLatch = new CountDownLatch(nbResources);
        for(int i=0 ; i<nbResources; i++) {
            new Thread(()->{
                try {
                    resources.add(resourcePool.get());
                } catch (IOException e) {
                    fail(e);
                } finally {
                    countDownLatch.countDown();
                }
            }).start();
        }
        countDownLatch.await(1, TimeUnit.MINUTES);
        assertEquals(nbResources, resources.size());
        try {
            resourcePool.close();
            fail();
        } catch (MultipleResourcesIOException expected) {

        }
        assertEquals(1, factoryClose.get());
        assertEquals(resources.stream().mapToInt(r->r.close).sum(), nbResources - 2);
    }

    static class Resource implements Closeable {

        int close = 0;

        @Override
        public void close() throws IOException {
            close++;
        }
    }

    static class IOCorruptResource extends Resource {

        @Override
        public void close() throws IOException {
            throw new IOException();
        }
    }

    static class RuntimeCorruptResource extends Resource {

        @Override
        public void close() throws IOException {
            throw new NumberFormatException();
        }
    }
}
