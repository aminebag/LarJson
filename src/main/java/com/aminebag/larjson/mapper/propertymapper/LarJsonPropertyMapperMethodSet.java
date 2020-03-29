package com.aminebag.larjson.mapper.propertymapper;

import java.lang.reflect.Method;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Amine Bagdouri
 *
 * A set of model property mappers
 */
class LarJsonPropertyMapperMethodSet extends AbstractSet<Method> {

    private final LarJsonPropertyMapper<?>[] mappers;
    private int removed = 0;

    public LarJsonPropertyMapperMethodSet(Collection<? extends LarJsonPropertyMapper<?>> mappers) {
        this.mappers = new LarJsonPropertyMapper<?>[mappers.size()];
        for(LarJsonPropertyMapper<?> mapper : mappers) {
            this.mappers[mapper.getGetterIndex()] = mapper;
        }
    }

    @Override
    public Iterator<Method> iterator() {
        return new MethodIter();
    }

    @Override
    public int size() {
        return mappers.length - removed;
    }

    void removeMapper(LarJsonPropertyMapper<?> mapper) {
        mappers[mapper.getGetterIndex()] = null;
        removed++;
    }

    Iterator<LarJsonPropertyMapper<?>> mapperIterator() {
        return new MapperIter();
    }

    private class MethodIter implements Iterator<Method> {

        int cursor = 0;
        int consumed = 0;

        @Override
        public boolean hasNext() {
            return consumed < size();
        }

        @Override
        public Method next() {
            while (cursor < mappers.length) {
                LarJsonPropertyMapper<?> mapper = mappers[cursor++];
                if(mapper != null) {
                    consumed++;
                    return mapper.getGetterMethod();
                }
            }
            throw new NoSuchElementException();
        }
    }

    private class MapperIter implements Iterator<LarJsonPropertyMapper<?>> {

        int cursor = 0;
        int consumed = 0;

        @Override
        public boolean hasNext() {
            return consumed < size();
        }

        @Override
        public LarJsonPropertyMapper<?> next() {
            while (cursor < mappers.length) {
                LarJsonPropertyMapper<?> mapper = mappers[cursor++];
                if(mapper != null) {
                    consumed++;
                    return mapper;
                }
            }
            throw new NoSuchElementException();
        }
    }
}
