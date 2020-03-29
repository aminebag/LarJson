package com.aminebag.larjson.mapper.element;

import com.aminebag.larjson.api.LarJsonList;
import com.aminebag.larjson.api.LarJsonPath;
import com.aminebag.larjson.configuration.LarJsonTypedReadConfiguration;
import com.aminebag.larjson.configuration.LarJsonTypedWriteConfiguration;
import com.aminebag.larjson.configuration.PropertyResolver;
import com.aminebag.larjson.exception.LarJsonException;
import com.aminebag.larjson.exception.LarJsonRuntimeException;
import com.aminebag.larjson.exception.LarJsonWriteException;
import com.aminebag.larjson.mapper.LarJsonMapperUtils;
import com.aminebag.larjson.mapper.propertymapper.LarJsonPropertyMapper;
import com.aminebag.larjson.mapper.valueoverwriter.ValueOverwriter;
import com.aminebag.larjson.utils.LongList;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * @author Amine Bagdouri
 *
 * The base implementation of a typed LarJson array
 */
public abstract class AbstractLarJsonList<T> extends AbstractList<T> implements LarJsonList<T> {
    private final LarJsonPropertyMapper<?> mapper;
    private final long jsonPosition;
    private final long blueprintPosition;
    private final LongList keys;
    private final List<T> originalList = new AbstractList<T>() {
        @Override
        public int size() {
            getContext().checkClosed();
            return keys.size();
        }

        @Override
        public T get(int index) {
            getContext().checkClosed();
            Method currentMethod = new Object(){}.getClass().getEnclosingMethod();
            return getValue(index, currentMethod);
        }
    };

    public AbstractLarJsonList(LarJsonPropertyMapper<?> mapper, long jsonPosition, long blueprintPosition, LongList keys) {
        this.mapper = mapper;
        this.jsonPosition = jsonPosition;
        this.blueprintPosition = blueprintPosition;
        this.keys = keys;
    }

    private List<T> getList() {
        return (List<T>) getValueOverwriter().getListOrDefault(jsonPosition, originalList);
    }

    @Override
    public int size() {
        getContext().checkClosed();
        return getList().size();
    }

    @Override
    public T get(int index) {
        getContext().checkClosed();
        return getList().get(index);
    }

    private T getValue(int index, Method currentMethod) {
        String pathElement = "[" + index + "]";
        try {
            long key = keys.get(index);
            if (key == 0L) {
                return (T) mapper.nullValue();
            }
            return (T) mapper.calculateValue(getContext(), key - 1, jsonPosition, blueprintPosition, this, pathElement);
        } catch (IOException | LarJsonException e) {
            StringBuilder pathBuilder = new StringBuilder();
            getLarJsonPath(pathBuilder);
            pathBuilder.append(pathElement);
            return (T) onCheckedException(currentMethod, new Object[]{index}, pathBuilder.toString(), e);
        }
    }

    private List<T> putListIfAbsent() {
        return (List<T>) getValueOverwriter().putListIfAbsent(jsonPosition, originalList);
    }

    @Override
    public T set(int index, T element) {
        getContext().checkClosed();
        List<T> l = putListIfAbsent();
        return l.set(index, element);
    }

    @Override
    public void add(int index, T element) {
        getContext().checkClosed();
        List<T> l = putListIfAbsent();
        l.add(index, element);
    }

    @Override
    public T remove(int index) {
        getContext().checkClosed();
        List<T> l = putListIfAbsent();
        return l.remove(index);
    }

    @Override
    public boolean add(T t) {
        getContext().checkClosed();
        List<T> l = putListIfAbsent();
        return l.add(t);
    }

    @Override
    public int indexOf(Object o) {
        getContext().checkClosed();
        return getList().indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        getContext().checkClosed();
        return getList().lastIndexOf(o);
    }

    @Override
    public void clear() {
        getContext().checkClosed();
        List<T> l = putListIfAbsent();
        l.clear();
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        getContext().checkClosed();
        List<T> l = putListIfAbsent();
        return l.addAll(index, c);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        getContext().checkClosed();
        LarJsonTypedReadConfiguration configuration = getContext().getReadConfiguration();
        if(configuration.isMutable() && configuration.isThreadSafe()) {
            //TODO could be optimized
            List<T> l = putListIfAbsent();
            return l.subList(fromIndex, toIndex);
        } else if(configuration.isMutable()){
            return new RandomAccessSubList(fromIndex, toIndex);
        } else {
            return originalList.subList(fromIndex, toIndex);
        }
    }

    @Override
    public boolean equals(Object o) {
        getContext().checkClosed();
        if (this == o) {
            return true;
        }
        LarJsonTypedReadConfiguration configuration = getContext().getReadConfiguration();
        if(configuration.isMutable() && configuration.isThreadSafe()) {
            //TODO could be optimized
            List<T> l = putListIfAbsent();
            return l.equals(o);
        } else {
            return getList().equals(o);
        }
    }

    @Override
    public int hashCode() {
        getContext().checkClosed();
        LarJsonTypedReadConfiguration configuration = getContext().getReadConfiguration();
        if(configuration.isMutable() && configuration.isThreadSafe()) {
            //TODO could be optimized
            List<T> l = putListIfAbsent();
            return l.hashCode();
        } else {
            return getList().hashCode();
        }
    }

    @Override
    public boolean isEmpty() {
        getContext().checkClosed();
        return getList().isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        getContext().checkClosed();
        LarJsonTypedReadConfiguration configuration = getContext().getReadConfiguration();
        if(configuration.isMutable() && configuration.isThreadSafe()) {
            //TODO could be optimized
            List<T> l = putListIfAbsent();
            return l.contains(o);
        } else {
            return getList().contains(o);
        }
    }

    @Override
    public Object[] toArray() {
        getContext().checkClosed();
        LarJsonTypedReadConfiguration configuration = getContext().getReadConfiguration();
        if(configuration.isMutable() && configuration.isThreadSafe()) {
            //TODO could be optimized
            List<T> l = putListIfAbsent();
            return l.toArray();
        } else {
            return getList().toArray();
        }
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        getContext().checkClosed();
        LarJsonTypedReadConfiguration configuration = getContext().getReadConfiguration();
        if(configuration.isMutable() && configuration.isThreadSafe()) {
            //TODO could be optimized
            List<T> l = putListIfAbsent();
            return l.toArray(a);
        } else {
            return getList().toArray(a);
        }
    }

    @Override
    public boolean remove(Object o) {
        getContext().checkClosed();
        List<T> l = putListIfAbsent();
        return l.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        getContext().checkClosed();
        LarJsonTypedReadConfiguration configuration = getContext().getReadConfiguration();
        if(configuration.isMutable() && configuration.isThreadSafe()) {
            //TODO could be optimized
            List<T> l = putListIfAbsent();
            return l.containsAll(c);
        } else {
            return getList().containsAll(c);
        }
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        getContext().checkClosed();
        List<T> l = putListIfAbsent();
        return l.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        getContext().checkClosed();
        List<T> l = putListIfAbsent();
        return l.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        getContext().checkClosed();
        List<T> l = putListIfAbsent();
        return l.retainAll(c);
    }

    @Override
    public String toString() {
        getContext().checkClosed();
        LarJsonTypedReadConfiguration configuration = getContext().getReadConfiguration();
        if(configuration.isMutable() && configuration.isThreadSafe()) {
            //TODO could be optimized
            List<T> l = putListIfAbsent();
            return l.toString();
        } else {
            return getList().toString();
        }
    }

    @Override
    public void replaceAll(UnaryOperator<T> operator) {
        getContext().checkClosed();
        List<T> l = putListIfAbsent();
        l.replaceAll(operator);
    }

    @Override
    public void sort(Comparator<? super T> c) {
        getContext().checkClosed();
        List<T> l = putListIfAbsent();
        l.sort(c);
    }

    @Override
    public Spliterator<T> spliterator() {
        getContext().checkClosed();
        LarJsonTypedReadConfiguration configuration = getContext().getReadConfiguration();
        if(configuration.isMutable() && configuration.isThreadSafe()) {
            //TODO could be optimized
            List<T> l = putListIfAbsent();
            return l.spliterator();
        } else {
            return getList().spliterator();
        }
    }

    @Override
    public Iterator<T> iterator() {
        getContext().checkClosed();
        LarJsonTypedReadConfiguration configuration = getContext().getReadConfiguration();
        if(configuration.isMutable() && configuration.isThreadSafe()) {
            //TODO could be optimized
            List<T> l = putListIfAbsent();
            return l.iterator();
        } else if(configuration.isMutable()) {
            return new Itr();
        } else {
            return originalList.iterator();
        }
    }

    @Override
    public ListIterator<T> listIterator() {
        return listIterator(0);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        getContext().checkClosed();
        LarJsonTypedReadConfiguration configuration = getContext().getReadConfiguration();
        if(configuration.isMutable() && configuration.isThreadSafe()) {
            //TODO could be optimized
            List<T> l = putListIfAbsent();
            return l.listIterator(index);
        } else if(configuration.isMutable()) {
            return new ListItr(index);
        } else {
            return originalList.listIterator(index);
        }
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        getContext().checkClosed();
        List<T> l = putListIfAbsent();
        return l.removeIf(filter);
    }

    @Override
    public Stream<T> stream() {
        getContext().checkClosed();
        LarJsonTypedReadConfiguration configuration = getContext().getReadConfiguration();
        if(configuration.isMutable() && configuration.isThreadSafe()) {
            //TODO could be optimized
            List<T> l = putListIfAbsent();
            return l.stream();
        } else {
            return getList().stream();
        }
    }

    @Override
    public Stream<T> parallelStream() {
        getContext().checkClosed();
        LarJsonTypedReadConfiguration configuration = getContext().getReadConfiguration();
        if(configuration.isMutable() && configuration.isThreadSafe()) {
            //TODO could be optimized
            List<T> l = putListIfAbsent();
            return l.parallelStream();
        } else {
            return getList().parallelStream();
        }
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        getContext().checkClosed();
        LarJsonTypedReadConfiguration configuration = getContext().getReadConfiguration();
        if(configuration.isMutable() && configuration.isThreadSafe()) {
            //TODO could be optimized
            List<T> l = putListIfAbsent();
            l.forEach(action);
        } else {
            getList().forEach(action);
        }
    }

    protected abstract LarJsonContext getContext();
    protected abstract ValueOverwriter getValueOverwriter();

    @Override
    public void write(JsonWriter jsonWriter, LarJsonTypedWriteConfiguration writeConfiguration)
            throws IOException, LarJsonException {
        getContext().checkClosed();
        try {
            jsonWriter.beginArray();
            List<T> l = (List<T>) getValueOverwriter().getListOrDefault(jsonPosition, null);
            if (l != null) {
                for (T t : l) {
                    LarJsonMapperUtils.writeValue(jsonWriter, t, writeConfiguration);
                }
            } else {
                for (int i = 0; i < keys.size(); i++) {
                    long key = keys.get(i);
                    if (key == 0L) {
                        jsonWriter.nullValue();
                        continue;
                    }
                    PropertyResolver propertyResolver = writeConfiguration.getPropertyResolverFactory()
                            .get(getContext().getRootInterface());
                    mapper.write(getContext(), key - 1, jsonPosition, blueprintPosition, jsonWriter, getValueOverwriter(),
                            writeConfiguration, propertyResolver);
                }
            }
            jsonWriter.endArray();
        }catch (IllegalArgumentException | IllegalStateException | LarJsonRuntimeException e) {
            throw new LarJsonWriteException(e);
        }
    }

    @Override
    public void write(JsonWriter jsonWriter) throws IOException, LarJsonException {
        write(jsonWriter, getContext().getReadConfiguration().toWriteConfiguration());
    }

    @Override
    public void write(Writer writer) throws IOException, LarJsonException {
        getContext().checkClosed();
        write(writer, getContext().getReadConfiguration().toWriteConfiguration());
    }

    @Override
    public List<T> clone() {
        getContext().checkClosed();
        if(!getContext().getReadConfiguration().isMutable()) {
            return this;
        }
        try {
            return new CloneLarJsonList<>(mapper, jsonPosition, blueprintPosition, keys, getValueOverwriter(),
                    getContext(), getParentLarJsonPath(), getPathElement());
        } catch (IOException e) {
            Method currentMethod = new Object(){}.getClass().getEnclosingMethod(); //TODO verify
            return (List<T>) onCheckedException(currentMethod, new Object[]{}, getLarJsonPath(), e);
        }
    }

    private Object onCheckedException(Method currentMethod, Object[] args, String path, Exception e) {
        return getContext().getReadConfiguration().getValueReadFailedBehavior()
                .onValueReadFailed(this, currentMethod, args, path, e);
    }

    abstract String getPathElement();

    @Override
    public void getLarJsonPath(StringBuilder sb) {
        getContext().checkClosed();
        LarJsonPath parentPath = getParentLarJsonPath();
        if(parentPath != null) {
            parentPath.getLarJsonPath(sb);
        }
        sb.append(getPathElement());
    }

    private class Itr implements Iterator<T> {
        /**
         * Index of element to be returned by subsequent call to next.
         */
        int cursor = 0;

        /**
         * Index of element returned by most recent call to next or
         * previous.  Reset to -1 if this element is deleted by a call
         * to remove.
         */
        int lastRet = -1;

        /**
         * The modCount value that the iterator believes that the backing
         * List should have.  If this expectation is violated, the iterator
         * has detected concurrent modification.
         */
        int expectedModCount = modCount;

        public boolean hasNext() {
            return cursor != size();
        }

        public T next() {
            checkForComodification();
            try {
                int i = cursor;
                T next = get(i);
                lastRet = i;
                cursor = i + 1;
                return next;
            } catch (IndexOutOfBoundsException e) {
                checkForComodification();
                throw new NoSuchElementException();
            }
        }

        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
            checkForComodification();

            try {
                AbstractLarJsonList.this.remove(lastRet);
                if (lastRet < cursor)
                    cursor--;
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException e) {
                throw new ConcurrentModificationException();
            }
        }

        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }

    private class ListItr extends Itr implements ListIterator<T> {
        ListItr(int index) {
            if (index < 0 || index > size()) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
            }
            cursor = index;
        }

        public boolean hasPrevious() {
            return cursor != 0;
        }

        public T previous() {
            checkForComodification();
            try {
                int i = cursor - 1;
                T previous = get(i);
                lastRet = cursor = i;
                return previous;
            } catch (IndexOutOfBoundsException e) {
                checkForComodification();
                throw new NoSuchElementException();
            }
        }

        public int nextIndex() {
            return cursor;
        }

        public int previousIndex() {
            return cursor-1;
        }

        public void set(T e) {
            if (lastRet < 0)
                throw new IllegalStateException();
            checkForComodification();

            try {
                AbstractLarJsonList.this.set(lastRet, e);
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        public void add(T e) {
            checkForComodification();

            try {
                int i = cursor;
                AbstractLarJsonList.this.add(i, e);
                lastRet = -1;
                cursor = i + 1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }

    private class RandomAccessSubList extends AbstractList<T> implements RandomAccess {
        private final int offset;
        private int size;

        RandomAccessSubList(int fromIndex, int toIndex) {
            if (fromIndex < 0)
                throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
            if (toIndex > AbstractLarJsonList.this.size())
                throw new IndexOutOfBoundsException("toIndex = " + toIndex);
            if (fromIndex > toIndex)
                throw new IllegalArgumentException("fromIndex(" + fromIndex +
                        ") > toIndex(" + toIndex + ")");
            offset = fromIndex;
            size = toIndex - fromIndex;
        }

        public T set(int index, T element) {
            rangeCheck(index);
            checkForComodification();
            return AbstractLarJsonList.this.set(index+offset, element);
        }

        public T get(int index) {
            rangeCheck(index);
            checkForComodification();
            return AbstractLarJsonList.this.get(index+offset);
        }

        public int size() {
            checkForComodification();
            return size;
        }

        public void add(int index, T element) {
            rangeCheckForAdd(index);
            checkForComodification();
            AbstractLarJsonList.this.add(index+offset, element);
            this.modCount = AbstractLarJsonList.this.modCount;
            size++;
        }

        public T remove(int index) {
            rangeCheck(index);
            checkForComodification();
            T result = AbstractLarJsonList.this.remove(index+offset);
            this.modCount = AbstractLarJsonList.this.modCount;
            size--;
            return result;
        }

        protected void removeRange(int fromIndex, int toIndex) {
            checkForComodification();
            AbstractLarJsonList.this.removeRange(fromIndex+offset, toIndex+offset);
            this.modCount = AbstractLarJsonList.this.modCount;
            size -= (toIndex-fromIndex);
        }

        public boolean addAll(Collection<? extends T> c) {
            return addAll(size, c);
        }

        public boolean addAll(int index, Collection<? extends T> c) {
            rangeCheckForAdd(index);
            int cSize = c.size();
            if (cSize==0)
                return false;

            checkForComodification();
            AbstractLarJsonList.this.addAll(offset+index, c);
            this.modCount = AbstractLarJsonList.this.modCount;
            size += cSize;
            return true;
        }

        public Iterator<T> iterator() {
            return listIterator();
        }

        public ListIterator<T> listIterator(final int index) {
            checkForComodification();
            rangeCheckForAdd(index);

            return new ListIterator<T>() {
                private final ListIterator<T> i = AbstractLarJsonList.this.listIterator(index+offset);

                public boolean hasNext() {
                    return nextIndex() < size;
                }

                public T next() {
                    if (hasNext())
                        return i.next();
                    else
                        throw new NoSuchElementException();
                }

                public boolean hasPrevious() {
                    return previousIndex() >= 0;
                }

                public T previous() {
                    if (hasPrevious())
                        return i.previous();
                    else
                        throw new NoSuchElementException();
                }

                public int nextIndex() {
                    return i.nextIndex() - offset;
                }

                public int previousIndex() {
                    return i.previousIndex() - offset;
                }

                public void remove() {
                    i.remove();
                    RandomAccessSubList.this.modCount = AbstractLarJsonList.this.modCount;
                    size--;
                }

                public void set(T e) {
                    i.set(e);
                }

                public void add(T e) {
                    i.add(e);
                    RandomAccessSubList.this.modCount = AbstractLarJsonList.this.modCount;
                    size++;
                }
            };
        }

        public List<T> subList(int fromIndex, int toIndex) {
            return new RandomAccessSubList(this.offset + fromIndex, this.offset + toIndex);
        }

        private void rangeCheck(int index) {
            if (index < 0 || index >= size)
                throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }

        private void rangeCheckForAdd(int index) {
            if (index < 0 || index > size)
                throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }

        private String outOfBoundsMsg(int index) {
            return "Index: "+index+", Size: "+size;
        }

        private void checkForComodification() {
            if (this.modCount != RandomAccessSubList.this.modCount)
                throw new ConcurrentModificationException();
        }
    }
}
