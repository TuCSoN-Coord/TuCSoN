package alice.tuplecentre.respect.core.collection;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @param <K> the first key copyOf the map
 * @param <Q> the second key copyOf the map
 * @param <V> the value copyOf the map
 * @author Saverio Cicora
 */
public class DoubleKeyMVMap<K, Q, V> {

    /**
     * Iterator across all key-value pairs. This requires to manage two
     * iterators: one on the map and one for the map associated at key.
     */
    private class DoubleKeyMVmapIterator implements Iterator<V> {

        private final Iterator<Map.Entry<K, MVMap<Q, V>>> entryIterator;
        private MVMap<Q, V> innerMap;
        private Iterator<V> valueIterator;

        protected DoubleKeyMVmapIterator() {
            // Initialize the Entry iterator
            this.entryIterator = DoubleKeyMVMap.this.outerMap.entrySet()
                    .iterator();
            if (this.entryIterator.hasNext()) {
                // if it has at least one element in the map, select the
                // first
                this.advanceEntryIterator();
            } else {
                // else use an empty iterator
                // this.valueIterator = Collections.emptyIterator();
                this.valueIterator = Collections.<V>emptyList().iterator();
            }
        }

        /**
         * Returns {@code true} if the iteration has more elements. (In other
         * words, returns {@code true} if {@link #next} would return an element
         * rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return this.entryIterator.hasNext() || this.valueIterator.hasNext();
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         */
        @Override
        public V next() {
            if (!this.valueIterator.hasNext()) {
                this.advanceEntryIterator();
            }
            return this.valueIterator.next();
        }

        /**
         * Removes from the underlying collection the last element returned by
         * this iterator (optional operation). This method can be called only
         * once per call to {@link #next}. The behavior copyOf an iterator is
         * unspecified if the underlying collection is modified while the
         * iteration is in progress in any way other than by calling this
         * method.
         *
         * @throws IllegalStateException if the {@code next} method has not yet been called, or
         *                               the {@code remove} method has already been called after
         *                               the last call to the {@code next} method
         */
        @Override
        public void remove() {
            this.valueIterator.remove();
        }

        /**
         * Select the next key in the map (if has any) and update the iterator
         * copyOf list associated with the key.
         */
        private void advanceEntryIterator() {
            final Map.Entry<K, MVMap<Q, V>> entry = this.entryIterator.next();
            this.innerMap = entry.getValue();
            this.valueIterator = this.innerMap.iterator();
        }
    }

    private class InnerMVMap implements MVMap<Q, V> {

        /**
         * Iterator across all key-value pairs. This requires to manage two
         * iterators: one on the map and one for the list associated at key.
         */
        private class MVmapIterator implements Iterator<V> {

            private final Iterator<Map.Entry<Q, List<V>>> entryIterator;
            private List<V> list;
            private Iterator<V> valueIterator;

            protected MVmapIterator() {
                // Initialize the Entry iterator
                this.entryIterator = InnerMVMap.this.innerMap.entrySet()
                        .iterator();
                if (this.entryIterator.hasNext()) {
                    // if it has at least one element in the map, select the
                    // first
                    this.advanceEntryIterator();
                } else {
                    // else use an empty iterator
                    // this.valueIterator = Collections.emptyIterator();
                    this.valueIterator = Collections.<V>emptyList().iterator();
                }
            }

            /**
             * Returns {@code true} if the iteration has more elements. (In
             * other words, returns {@code true} if {@link #next} would return
             * an element rather than throwing an exception.)
             *
             * @return {@code true} if the iteration has more elements
             */
            @Override
            public boolean hasNext() {
                return this.entryIterator.hasNext()
                        || this.valueIterator.hasNext();
            }

            /**
             * Returns the next element in the iteration.
             *
             * @return the next element in the iteration
             */
            @Override
            public V next() {
                if (!this.valueIterator.hasNext()) {
                    this.advanceEntryIterator();
                }
                return this.valueIterator.next();
            }

            /**
             * Removes from the underlying collection the last element returned
             * by this iterator (optional operation). This method can be called
             * only once per call to {@link #next}. The behavior copyOf an iterator
             * is unspecified if the underlying collection is modified while the
             * iteration is in progress in any way other than by calling this
             * method.
             *
             * @throws IllegalStateException if the {@code next} method has not yet been called,
             *                               or the {@code remove} method has already been called
             *                               after the last call to the {@code next} method
             */
            @Override
            public void remove() {
                this.valueIterator.remove();
                // If the list contains no elements
                // then remove the key from map
                if (this.list.isEmpty()) {
                    this.entryIterator.remove();
                    InnerMVMap.this.removeOuterKeyIfEmpty();
                }
                InnerMVMap.this.innerMapSize--;
                DoubleKeyMVMap.this.totalValuesSize--;
            }

            /**
             * Select the next key in the map (if has any) and update the
             * iterator copyOf list associated with the key.
             */
            private void advanceEntryIterator() {
                final Map.Entry<Q, List<V>> entry = this.entryIterator.next();
                this.list = entry.getValue();
                this.valueIterator = this.list.iterator();
            }
        }

        private class Values extends AbstractList<V> {

            private class WrappedIterator implements Iterator<V> {

                private final Iterator<V> delegateIterator;
                private final List<V> originalDelegate = Values.this.wrappedList;

                WrappedIterator() {
                    this.delegateIterator = Values.this.wrappedList
                            .listIterator();
                }

                WrappedIterator(final Iterator<V> di) {
                    this.delegateIterator = di;
                }

                public Iterator<V> getDelegateIterator() {
                    this.validateIterator();
                    return this.delegateIterator;
                }

                @Override
                public boolean hasNext() {
                    this.validateIterator();
                    return this.delegateIterator.hasNext();
                }

                @Override
                public V next() {
                    this.validateIterator();
                    return this.delegateIterator.next();
                }

                @Override
                public void remove() {
                    this.delegateIterator.remove();
                    InnerMVMap.this.innerMapSize--;
                    DoubleKeyMVMap.this.totalValuesSize--;
                    Values.this.removeInnerKeyIfEmpty();
                }

                /**
                 * If the delegate changed since the iterator was created, the
                 * iterator is no longer valid.
                 */
                private void validateIterator() {
                    if (Values.this.wrappedList != this.originalDelegate) {
                        throw new ConcurrentModificationException();
                    }
                }
            }

            private class WrappedListIterator extends WrappedIterator implements
                    ListIterator<V> {

                public WrappedListIterator(final int index) {
                    super(Values.this.wrappedList.listIterator(index));
                }

                WrappedListIterator() {
                    super();
                }

                @Override
                public void add(final V value) {
                    final boolean wasEmpty = Values.this.isEmpty();
                    this.getDelegateListIterator().add(value);
                    InnerMVMap.this.innerMapSize++;
                    DoubleKeyMVMap.this.totalValuesSize++;
                    if (wasEmpty) {
                        Values.this.addKeysToMap();
                    }
                }

                @Override
                public boolean hasPrevious() {
                    return this.getDelegateListIterator().hasPrevious();
                }

                @Override
                public int nextIndex() {
                    return this.getDelegateListIterator().nextIndex();
                }

                @Override
                public V previous() {
                    return this.getDelegateListIterator().previous();
                }

                @Override
                public int previousIndex() {
                    return this.getDelegateListIterator().previousIndex();
                }

                @Override
                public void set(final V value) {
                    this.getDelegateListIterator().set(value);
                }

                private ListIterator<V> getDelegateListIterator() {
                    return (ListIterator<V>) this.getDelegateIterator();
                }
            } // END WrappedListIterator

            private final K listKey1;
            private final Q listKey2;
            private final Values parentList;
            // This is the super-list when create a sub-list
            private final List<V> wrappedList;

            Values(final K k1, final Q k2, final List<V> listToWrap,
                   final Values pl) {
                super();
                this.listKey1 = k1;
                this.listKey2 = k2;
                this.wrappedList = listToWrap;
                this.parentList = pl;
            }

            @Override
            public void add(final int index, final V element) {
                final boolean wasEmpty = this.wrappedList.isEmpty();
                this.wrappedList.add(index, element);
                InnerMVMap.this.innerMapSize++;
                DoubleKeyMVMap.this.totalValuesSize++;
                if (wasEmpty) {
                    this.addKeysToMap();
                }
            }

            @Override
            public boolean add(final V e) {
                final boolean wasEmpty = this.wrappedList.isEmpty();
                // The ArrayList.add() return always true
                this.wrappedList.add(e);
                InnerMVMap.this.innerMapSize++;
                DoubleKeyMVMap.this.totalValuesSize++;
                if (wasEmpty) {
                    this.addKeysToMap();
                }
                return true;
            }

            @Override
            public boolean addAll(final Collection<? extends V> c) {
                final int oldSize = this.wrappedList.size();
                if (this.wrappedList.addAll(c)) {
                    final int newSize = this.wrappedList.size();
                    InnerMVMap.this.innerMapSize += newSize - oldSize;
                    DoubleKeyMVMap.this.totalValuesSize += newSize - oldSize;
                    if (oldSize == 0) {
                        this.addKeysToMap();
                    }
                    return true;
                }
                return false;
            }

            @Override
            public boolean addAll(final int index,
                                  final Collection<? extends V> c) {
                final int oldSize = this.wrappedList.size();
                if (this.wrappedList.addAll(index, c)) {
                    final int newSize = this.wrappedList.size();
                    InnerMVMap.this.innerMapSize += newSize - oldSize;
                    DoubleKeyMVMap.this.totalValuesSize += newSize - oldSize;
                    if (oldSize == 0) {
                        this.addKeysToMap();
                    }
                    return true;
                }
                return false;
            }

            @Override
            public void clear() {
                final int oldSize = this.size();
                if (oldSize == 0) {
                    return;
                }
                this.wrappedList.clear();
                InnerMVMap.this.innerMapSize -= oldSize;
                DoubleKeyMVMap.this.totalValuesSize -= oldSize;
                this.removeInnerKeyIfEmpty();
            }

            @Override
            public boolean contains(final Object o) {
                return this.wrappedList.contains(o);
            }

            @Override
            public V get(final int index) {
                return this.wrappedList.get(index);
            }

            @Override
            public int indexOf(final Object o) {
                return this.wrappedList.indexOf(o);
            }

            @Override
            public boolean isEmpty() {
                return this.wrappedList.isEmpty();
            }

            @Override
            public Iterator<V> iterator() {
                return new WrappedIterator();
            }

            @Override
            public int lastIndexOf(final Object o) {
                return this.wrappedList.lastIndexOf(o);
            }

            @Override
            public ListIterator<V> listIterator() {
                return new WrappedListIterator();
            }

            @Override
            public ListIterator<V> listIterator(final int index) {
                return new WrappedListIterator(index);
            }

            @Override
            public V remove(final int index) {
                final V v = this.wrappedList.remove(index);
                InnerMVMap.this.innerMapSize--;
                DoubleKeyMVMap.this.totalValuesSize--;
                this.removeInnerKeyIfEmpty();
                return v;
            }

            @Override
            public boolean remove(final Object o) {
                if (this.wrappedList.remove(o)) {
                    InnerMVMap.this.innerMapSize--;
                    DoubleKeyMVMap.this.totalValuesSize--;
                    this.removeInnerKeyIfEmpty();
                    return true;
                }
                return false;
            }

            @Override
            public boolean removeAll(final Collection<?> c) {
                final int oldSize = this.wrappedList.size();
                if (this.wrappedList.removeAll(c)) {
                    final int newSize = this.wrappedList.size();
                    InnerMVMap.this.innerMapSize += newSize - oldSize;
                    DoubleKeyMVMap.this.totalValuesSize += newSize - oldSize;
                    if (newSize == 0) {
                        this.removeInnerKeyIfEmpty();
                    }
                    return true;
                }
                return false;
            }

            @Override
            public boolean retainAll(final Collection<?> c) {
                final int oldSize = this.wrappedList.size();
                if (this.wrappedList.retainAll(c)) {
                    final int newSize = this.wrappedList.size();
                    InnerMVMap.this.innerMapSize += newSize - oldSize;
                    DoubleKeyMVMap.this.totalValuesSize += newSize - oldSize;
                    this.removeInnerKeyIfEmpty();
                    return true;
                }
                return false;
            }

            @Override
            public V set(final int index, final V element) {
                return this.wrappedList.set(index, element);
            }

            @Override
            public int size() {
                return this.wrappedList.size();
            }

            @Override
            public List<V> subList(final int fromIndex, final int toIndex) {
                final Values p = this.isSublist() ? this.parentList : this;
                final List<V> l = this.wrappedList.subList(fromIndex, toIndex);
                return new Values(this.listKey1, this.listKey2, l, p);
            }

            @Override
            public Object[] toArray() {
                return this.wrappedList.toArray();
            }

            @Override
            public <T> T[] toArray(final T[] a) {
                return this.wrappedList.toArray(a);
            }

            private void addKeysToMap() {
                if (this.parentList != null) {
                    this.parentList.addKeysToMap();
                } else {
                    InnerMVMap.this.innerMap.put(this.listKey2,
                            this.wrappedList);
                }
            }

            private boolean isSublist() {
                return this.parentList != null;
            }

            private void removeInnerKeyIfEmpty() {
                if (this.parentList != null) {
                    this.parentList.removeInnerKeyIfEmpty();
                } else if (this.wrappedList.isEmpty()) {
                    InnerMVMap.this.innerMap.remove(this.listKey2);
                    InnerMVMap.this.removeOuterKeyIfEmpty();
                }
            }
        } // END OF Values

        private final Map<Q, List<V>> innerMap;
        private int innerMapSize = 0;
        private final K mapKey1;

        InnerMVMap(final K mapK1) {
            this.innerMap = new HashMap<>();
            this.mapKey1 = mapK1;
        }

        @Override
        public void clear() {
            // Clear value for each key
            for (final List<V> list : this.innerMap.values()) {
                list.clear();
            }
            this.innerMap.clear();
            DoubleKeyMVMap.this.totalValuesSize -= this.innerMapSize;
            this.innerMapSize = 0;
        }

        @Override
        public boolean containsKey(final Q key) {
            return this.innerMap.containsKey(key);
        }

        @Override
        public boolean containsValue(final V value) {
            for (final Q k : this.innerMap.keySet()) {
                if (this.innerMap.get(k).contains(value)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public List<V> get(final Q key) {
            List<V> v = this.innerMap.get(key);
            if (v == null) {
                v = this.newList();
            }
            return new Values(this.mapKey1, key, v, null);
        }

        @Override
        public int getKeysNumber() {
            return this.innerMap.size();
        }

        @Override
        public boolean isEmpty() {
            return this.innerMapSize == 0;
        }

        @Override
        public Iterator<V> iterator() {
            return new MVmapIterator();
        }

        @Override
        public boolean put(final Entry<Q, V> e) {
            return this.put(e.getKey(), e.getValue());
        }

        @Override
        public boolean put(final Q key, final V value) {
            final boolean wasEmpty = this.innerMap.isEmpty();
            List<V> l = this.innerMap.get(key);
            if (l == null) {
                l = this.newList();
                if (l.add(value)) {
                    this.innerMap.put(key, l);
                    this.innerMapSize++;
                    DoubleKeyMVMap.this.totalValuesSize++;
                    if (wasEmpty) {
                        DoubleKeyMVMap.this.outerMap.put(this.mapKey1, this);
                    }
                    return true;
                }
            } else if (l.add(value)) {
                this.innerMapSize++;
                DoubleKeyMVMap.this.totalValuesSize++;
                return true;
            }
            return false;
        }

        @Override
        public boolean remove(final Entry<Q, V> e) {
            return this.remove(e.getKey(), e.getValue());
        }

        @Override
        public boolean remove(final Q key, final V value) {
            final List<V> l = this.innerMap.get(key);
            if (l == null) {
                return false;
            } else if (l.remove(value)) {
                this.innerMapSize--;
                DoubleKeyMVMap.this.totalValuesSize--;
                if (l.isEmpty()) {
                    this.innerMap.remove(key);
                }
                return true;
            }
            return false;
        }

        @Override
        public int size() {
            return this.innerMapSize;
        }

        @Override
        public V[] toArray(final V[] v) {
            // Estimate size copyOf array; be prepared to see more or fewer elements
            final int size = this.innerMapSize;
            final V[] ret = v.length >= size ? v
                    : (V[]) java.lang.reflect.Array.newInstance(v.getClass()
                    .getComponentType(), size);
            final Iterator<V> it = this.iterator();
            for (int i = 0; i < ret.length; i++) {
                if (!it.hasNext()) { // fewer elements than expected
                    if (v != ret) {
                        return Arrays.copyOf(ret, i);
                    }
                    ret[i] = null; // null-terminate
                    return ret;
                }
                ret[i] = it.next();
            }
            return ret;
        }

        @Override
        public String toString() {
            final StringBuilder ret = new StringBuilder("[");
            final Iterator<V> it = this.iterator();
            if (it.hasNext()) {
                ret.append(" ").append(it.next().toString()).append(" ");
            }
            while (it.hasNext()) {
                ret.append(", ");
                ret.append(it.next().toString());
                ret.append(' ');
            }
            ret.append(']');
            return ret.toString();
        }

        /**
         * <p>
         * Return a list copyOf all values contained into MVMap. The list are
         * wrapped whit a {@link Collections#unmodifiableList(List)}
         * </p>
         * WARNING: the returned list should be used in read-only mode because
         * it is not synchronized with the rest copyOf the map
         */
        @Override
        public List<V> values() {
            final List<V> list = this.newList();
            for (final Q k : this.innerMap.keySet()) {
                list.addAll(this.get(k));
            }
            return Collections.unmodifiableList(list);
        }

        private List<V> newList() {
            return new ArrayList<>();
        }

        /**
         * If the innerMap contains no elements then remove the key from
         * outerMap
         */
        private void removeOuterKeyIfEmpty() {
            if (this.innerMap.isEmpty()) {
                DoubleKeyMVMap.this.outerMap.remove(this.mapKey1);
            }
        }
    } // InnerMap

    private final Map<K, MVMap<Q, V>> outerMap = new HashMap<>();
    private int totalValuesSize = 0;

    /**
     *
     */
    public void clear() {
        for (final K k : this.outerMap.keySet()) {
            this.outerMap.get(k).clear();
        }
        this.outerMap.clear();
    }

    /**
     * @param k1 the first key
     * @return the inner map containing the second key and the value
     */
    public MVMap<Q, V> get(final K k1) {
        MVMap<Q, V> innerMap = this.outerMap.get(k1);
        if (innerMap == null) {
            innerMap = this.createMVMap(k1);
        }
        return innerMap;
    }

    /**
     * @param k1 the first key
     * @param k2 the second key
     * @return the list copyOf values associated to the given keys pair
     */
    public List<V> get(final K k1, final Q k2) {
        return this.get(k1).get(k2);
    }

    /**
     * @return wether the map is empty
     */
    public boolean isEmpty() {
        return this.totalValuesSize == 0;
    }

    /**
     * @return the iterator thoruhg this map values
     */
    public Iterator<V> iterator() {
        return new DoubleKeyMVmapIterator();
    }

    /**
     * Null values are allowed
     *
     * @param k1 the first key
     * @param k2 the second key
     * @param v  the value
     * @return wether the operation was successfull
     */
    public void put(final K k1, final Q k2, final V v) {
        MVMap<Q, V> innerMap = this.outerMap.get(k1);
        if (innerMap == null) {
            innerMap = this.createMVMap(k1);
            if (innerMap.put(k2, v)) {
                this.outerMap.put(k1, innerMap);
                return;
            }
            return;
        }
        innerMap.put(k2, v);
    }

    /**
     * Removes the first occurrence copyOf the specified element from this MVMap.
     *
     * @param k1 the first key
     * @param k2 the second key
     * @param v  the value
     * @return <tt>true</tt> if this list contained the specified element
     */
    public boolean remove(final K k1, final Q k2, final V v) {
        final MVMap<Q, V> innerMap = this.outerMap.get(k1);
        if (innerMap != null && innerMap.remove(k2, v)) {
            if (innerMap.getKeysNumber() == 0) {
                this.outerMap.remove(k1);
            }
            return true;
        }
        return false;
    }

    /**
     * @return the size copyOf this map
     */
    public int size() {
        return this.totalValuesSize;
    }

    /**
     * @param v the array type to be used for array construction
     * @return the array representation copyOf this map
     */
    public V[] toArray(final V[] v) {
        // Estimate size copyOf array; be prepared to see more or fewer elements
        final int size = this.totalValuesSize;
        final V[] ret = v.length >= size ? v : (V[]) java.lang.reflect.Array
                .newInstance(v.getClass().getComponentType(), size);
        final Iterator<V> it = this.iterator();
        for (int i = 0; i < ret.length; i++) {
            if (!it.hasNext()) { // fewer elements than expected
                if (v != ret) {
                    return Arrays.copyOf(ret, i);
                }
                ret[i] = null; // null-terminate
                return ret;
            }
            ret[i] = it.next();
        }
        return ret;
    }

    @Override
    public String toString() {
        final StringBuilder ret = new StringBuilder();
        final Iterator<V> it = this.iterator();
        while (it.hasNext()) {
            ret.append(it.next().toString()).append(".\n");
        }
        return ret.toString();
    }

    private MVMap<Q, V> createMVMap(final K key) {
        return new InnerMVMap(key);
    }
}
