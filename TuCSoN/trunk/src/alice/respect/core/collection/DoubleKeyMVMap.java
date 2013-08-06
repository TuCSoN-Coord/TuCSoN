package alice.respect.core.collection;

import java.util.AbstractCollection;
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
import java.util.NoSuchElementException;

public class DoubleKeyMVMap<K1, K2, V> {

    /**
     * Iterator across all key-value pairs. This requires to manage two
     * iterators: one on the map and one for the map associated at key.
     */
    private class DoubleKeyMVmapIterator implements Iterator<V> {

        final Iterator<Map.Entry<K1, MVMap<K2, V>>> entryIterator;
        MVMap<K2, V> innerMap;
        Iterator<V> valueIterator;

        protected DoubleKeyMVmapIterator() {
            // Initialize the Entry iterator
            this.entryIterator =
                    DoubleKeyMVMap.this.outerMap.entrySet().iterator();
            if (this.entryIterator.hasNext()) {
                // if it has at least one element in the map, select the
                // first
                this.advanceEntryIterator();
            } else {
                // else use an empty iterator
                this.valueIterator = Collections.emptyIterator();
            }
        }

        /**
         * Returns {@code true} if the iteration has more elements. (In other
         * words, returns {@code true} if {@link #next} would return an element
         * rather than throwing an exception.)
         * 
         * @return {@code true} if the iteration has more elements
         */
        public boolean hasNext() {
            return this.entryIterator.hasNext() || this.valueIterator.hasNext();
        }

        /**
         * Returns the next element in the iteration.
         * 
         * @return the next element in the iteration
         * @throws NoSuchElementException
         *             if the iteration has no more elements
         */
        public V next() {
            if (!this.valueIterator.hasNext()) {
                this.advanceEntryIterator();
            }
            return this.valueIterator.next();
        }

        /**
         * Removes from the underlying collection the last element returned by
         * this iterator (optional operation). This method can be called only
         * once per call to {@link #next}. The behavior of an iterator is
         * unspecified if the underlying collection is modified while the
         * iteration is in progress in any way other than by calling this
         * method.
         * 
         * @throws IllegalStateException
         *             if the {@code next} method has not yet been called, or
         *             the {@code remove} method has already been called after
         *             the last call to the {@code next} method
         */
        public void remove() {
            this.valueIterator.remove();
            // If the list contains no elements
            // then remove the key from map
            if (this.innerMap.isEmpty()) {
                this.entryIterator.remove();
            }
            DoubleKeyMVMap.this.totalValuesSize--;
        }

        /**
         * Select the next key in the map (if has any) and update the iterator
         * of list associated with the key.
         */
        private void advanceEntryIterator() {
            final Map.Entry<K1, MVMap<K2, V>> entry = this.entryIterator.next();
            this.innerMap = entry.getValue();
            this.valueIterator = this.innerMap.iterator();
        }
    }

    private class InnerMVMap implements MVMap<K2, V> {

        /**
         * Iterator across all key-value pairs. This requires to manage two
         * iterators: one on the map and one for the list associated at key.
         */
        private class MVmapIterator implements Iterator<V> {

            final Iterator<Map.Entry<K2, List<V>>> entryIterator;
            List<V> list;
            Iterator<V> valueIterator;

            protected MVmapIterator() {
                // Initialize the Entry iterator
                this.entryIterator =
                        InnerMVMap.this.innerMap.entrySet().iterator();
                if (this.entryIterator.hasNext()) {
                    // if it has at least one element in the map, select the
                    // first
                    this.advanceEntryIterator();
                } else {
                    // else use an empty iterator
                    this.valueIterator = Collections.emptyIterator();
                }
            }

            /**
             * Returns {@code true} if the iteration has more elements. (In
             * other words, returns {@code true} if {@link #next} would return
             * an element rather than throwing an exception.)
             * 
             * @return {@code true} if the iteration has more elements
             */
            public boolean hasNext() {
                return this.entryIterator.hasNext()
                        || this.valueIterator.hasNext();
            }

            /**
             * Returns the next element in the iteration.
             * 
             * @return the next element in the iteration
             * @throws NoSuchElementException
             *             if the iteration has no more elements
             */
            public V next() {
                if (!this.valueIterator.hasNext()) {
                    this.advanceEntryIterator();
                }
                return this.valueIterator.next();
            }

            /**
             * Removes from the underlying collection the last element returned
             * by this iterator (optional operation). This method can be called
             * only once per call to {@link #next}. The behavior of an iterator
             * is unspecified if the underlying collection is modified while the
             * iteration is in progress in any way other than by calling this
             * method.
             * 
             * @throws IllegalStateException
             *             if the {@code next} method has not yet been called,
             *             or the {@code remove} method has already been called
             *             after the last call to the {@code next} method
             */
            public void remove() {
                this.valueIterator.remove();
                // If the list contains no elements
                // then remove the key from map
                if (this.list.isEmpty()) {
                    this.entryIterator.remove();
                }
                DoubleKeyMVMap.this.totalValuesSize--;
            }

            /**
             * Select the next key in the map (if has any) and update the
             * iterator of list associated with the key.
             */
            private void advanceEntryIterator() {
                final Map.Entry<K2, List<V>> entry = this.entryIterator.next();
                this.list = entry.getValue();
                this.valueIterator = this.list.iterator();
            }
        }

        private class Values extends AbstractList<V> {

            private class WrappedIterator implements Iterator<V> {
                final Iterator<V> delegateIterator;
                final List<V> originalDelegate = Values.this.wrappedList;

                WrappedIterator() {
                    this.delegateIterator =
                            Values.this.wrappedList.listIterator();
                }

                WrappedIterator(final Iterator<V> di) {
                    this.delegateIterator = di;
                }

                public boolean hasNext() {
                    this.validateIterator();
                    return this.delegateIterator.hasNext();
                }

                public V next() {
                    this.validateIterator();
                    return this.delegateIterator.next();
                }

                public void remove() {
                    this.delegateIterator.remove();
                    InnerMVMap.this.innerMapSize--;
                    DoubleKeyMVMap.this.totalValuesSize--;
                    Values.this.removeInnerKeyIfEmpty();
                }

                Iterator<V> getDelegateIterator() {
                    this.validateIterator();
                    return this.delegateIterator;
                }

                /**
                 * If the delegate changed since the iterator was created, the
                 * iterator is no longer valid.
                 */
                void validateIterator() {
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
                }

                public void add(final V value) {
                    final boolean wasEmpty = Values.this.isEmpty();
                    this.getDelegateListIterator().add(value);
                    InnerMVMap.this.innerMapSize++;
                    DoubleKeyMVMap.this.totalValuesSize++;
                    if (wasEmpty) {
                        Values.this.addKeysToMap();
                    }
                }

                public boolean hasPrevious() {
                    return this.getDelegateListIterator().hasPrevious();
                }

                public int nextIndex() {
                    return this.getDelegateListIterator().nextIndex();
                }

                public V previous() {
                    return this.getDelegateListIterator().previous();
                }

                public int previousIndex() {
                    return this.getDelegateListIterator().previousIndex();
                }

                public void set(final V value) {
                    this.getDelegateListIterator().set(value);
                }

                private ListIterator<V> getDelegateListIterator() {
                    return (ListIterator<V>) this.getDelegateIterator();
                }
            }// END WrappedListIterator

            final K1 listKey1;
            final K2 listKey2;

            final Values parentList;

            // This is the super-list when create a sub-list
            final List<V> wrappedList;

            Values(final K1 k1, final K2 k2, final List<V> listToWrap,
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
                    InnerMVMap.this.innerMapSize += (newSize - oldSize);
                    DoubleKeyMVMap.this.totalValuesSize += (newSize - oldSize);
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
                    InnerMVMap.this.innerMapSize += (newSize - oldSize);
                    DoubleKeyMVMap.this.totalValuesSize += (newSize - oldSize);
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
                    InnerMVMap.this.innerMapSize += (newSize - oldSize);
                    DoubleKeyMVMap.this.totalValuesSize += (newSize - oldSize);
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
                    InnerMVMap.this.innerMapSize += (newSize - oldSize);
                    DoubleKeyMVMap.this.totalValuesSize += (newSize - oldSize);
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
                return (this.parentList != null);
            }

            private void removeInnerKeyIfEmpty() {
                if (this.parentList != null) {
                    this.parentList.removeInnerKeyIfEmpty();
                } else if (this.wrappedList.isEmpty()) {
                    InnerMVMap.this.innerMap.remove(this.listKey2);
                }
            }

        } // END OF Values

        private final Map<K2, List<V>> innerMap;

        private int innerMapSize = 0;

        final K1 mapKey1;

        InnerMVMap(final K1 mapK1) {
            this.innerMap = new HashMap<K2, List<V>>();
            this.mapKey1 = mapK1;
        }

        public void clear() {
            // Clear value for each key
            for (final List<V> list : this.innerMap.values()) {
                list.clear();
            }
            this.innerMap.clear();
            DoubleKeyMVMap.this.totalValuesSize -= this.innerMapSize;
            this.innerMapSize = 0;
        }

        public boolean containsKey(final K2 key) {
            return this.innerMap.containsKey(key);
        }

        public boolean containsValue(final V value) {
            for (final K2 k : this.innerMap.keySet()) {
                if (this.innerMap.get(k).contains(value)) {
                    return true;
                }
            }
            return false;
        }

        public List<V> get(final K2 key) {
            List<V> v = this.innerMap.get(key);
            if (v == null) {
                v = this.newList();
            }
            return new Values(this.mapKey1, key, v, null);
        }

        public int getKeysNumber() {
            return this.innerMap.size();
        }

        public boolean isEmpty() {
            return (this.innerMapSize == 0);
        }

        public Iterator<V> iterator() {
            return new MVmapIterator();
        }

        public boolean put(final Entry<K2, V> e) {
            return this.put(e.getKey(), e.getValue());
        }

        public boolean put(final K2 key, final V value) {
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

        public boolean remove(final Entry<K2, V> e) {
            return this.remove(e.getKey(), e.getValue());
        }

        public boolean remove(final K2 key, final V value) {
            final List<V> l = this.innerMap.get(key);
            if (l == null) {
                return false;
            } else if (l.remove(value)) {
                this.innerMapSize--;
                DoubleKeyMVMap.this.totalValuesSize--;
                if (l.size() == 0) {
                    this.innerMap.remove(key);
                }
                return true;
            }
            return false;
        }

        public int size() {
            return this.innerMapSize;
        }

        /** See {@link AbstractCollection#toArray()} */
        public V[] toArray(final V[] v) {
            // Estimate size of array; be prepared to see more or fewer elements
            final int size = this.innerMapSize;
            final V[] ret =
                    v.length >= size ? v : (V[]) java.lang.reflect.Array
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
            final StringBuffer ret = new StringBuffer("[");
            final Iterator<V> it = this.iterator();
            if (it.hasNext()) {
                ret.append(" " + it.next().toString() + " ");
            }
            while (it.hasNext()) {
                ret.append(", ");
                ret.append(it.next().toString() + " ");
            }
            ret.append("]");
            return ret.toString();
        }

        /**
         * <p>
         * Return a list of all values contained into MVMap. The list are
         * wrapped whit a {@link Collections#unmodifiableList()}
         * </p>
         * WARNING: the returned list should be used in read-only mode because
         * it is not synchronized with the rest of the map
         */
        public List<V> values() {
            final List<V> list = this.newList();
            for (final K2 k : this.innerMap.keySet()) {
                list.addAll(this.get(k));
            }
            return Collections.unmodifiableList(list);
        }

        private List<V> newList() {
            return new ArrayList<V>();
        }

    } // InnerMap

    private int totalValuesSize = 0;

    HashMap<K1, MVMap<K2, V>> outerMap = new HashMap<K1, MVMap<K2, V>>();

    public void clear() {
        for (final K1 k : this.outerMap.keySet()) {
            this.outerMap.get(k).clear();
        }
        this.outerMap.clear();
    }

    public MVMap<K2, V> get(final K1 k1) {
        MVMap<K2, V> innerMap = this.outerMap.get(k1);
        if (innerMap == null) {
            innerMap = this.createMVMap(k1);
        }
        return innerMap;
    }

    public List<V> get(final K1 k1, final K2 k2) {
        return this.get(k1).get(k2);
    }

    public boolean isEmpty() {
        return (this.totalValuesSize == 0);
    }

    public Iterator<V> iterator() {
        return new DoubleKeyMVmapIterator();
    }

    /** Null value are allowed */
    public boolean put(final K1 k1, final K2 k2, final V v) {
        MVMap<K2, V> innerMap = this.outerMap.get(k1);
        if (innerMap == null) {
            innerMap = this.createMVMap(k1);
            if (innerMap.put(k2, v)) {
                this.outerMap.put(k1, innerMap);
                return true;
            }
            return false;
        }
        return innerMap.put(k2, v);
    }

    /**
     * Removes the first occurrence of the specified element from this MVMap.
     * 
     * @return <tt>true</tt> if this list contained the specified element
     */
    public boolean remove(final K1 k1, final K2 k2, final V v) {
        final MVMap<K2, V> innerMap = this.outerMap.get(k1);
        if (innerMap != null) {
            if (innerMap.remove(k2, v)) {
                if (innerMap.getKeysNumber() == 0) {
                    this.outerMap.remove(k1);
                }
                return true;
            }
        }
        return false;
    }

    public int size() {
        return this.totalValuesSize;
    }

    @SuppressWarnings("unchecked")
    public V[] toArray(final V[] v) {
        // Estimate size of array; be prepared to see more or fewer elements
        final int size = this.totalValuesSize;
        final V[] ret =
                v.length >= size ? v : (V[]) java.lang.reflect.Array
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
        final StringBuffer ret = new StringBuffer("[");
        final Iterator<V> it = this.iterator();
        if (it.hasNext()) {
            ret.append(" " + it.next().toString() + " ");
        }
        while (it.hasNext()) {
            ret.append(", ");
            ret.append(it.next().toString() + " ");
        }
        ret.append("]");
        return ret.toString();
    }

    private MVMap<K2, V> createMVMap(final K1 key) {
        return new InnerMVMap(key);
    }

}
