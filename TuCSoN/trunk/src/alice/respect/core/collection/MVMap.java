package alice.respect.core.collection;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A MVmap (multi-value map) is similar to a {@link Map} but, unlike this, it
 * allows to map multiple values ​​for the same {@code key}. This value are
 * stored in a {@link List} associated whit the {@code key}. You can have
 * duplicate values ​​associated with the same key.
 * 
 * @author ste (mailto: s.mariani@unibo.it) on 22/lug/2013
 * 
 * @param <K>
 *            the type of the key
 * @param <V>
 *            the type of the value
 */
public interface MVMap<K, V> extends Iterable<V> {

    /** Remove all element from the MVMap */
    void clear();

    /**
     * Returns {@code true} if the MVMap contain at least one value for the
     * specified key.
     * 
     * @param key
     *            - key to search
     * @return {@code true} - if the MVMap contain at least one value for the
     *         specified key
     */
    boolean containsKey(K key);

    /**
     * Returns {@code true} if the MVMap contains the specified value for any
     * key.
     * 
     * @param value
     *            - value to search
     * @return {@code true} - if the MVMap contains the specified value for any
     *         key
     */
    boolean containsValue(V value);

    @Override
    boolean equals(Object object);

    /**
     * Returns a {@link List} that contain the value mapped at specified key. If
     * the key does not contain values (or not existing) it return an empty List
     * 
     * Changes to the {@code List} are allowed and they are reflected on the
     * MVMap and vice versa.
     * 
     * @param key
     *            - key to search
     * @return {@code List<V>} whit the value mapped at specified key if any or
     *         return an empty list
     * */
    List<V> get(K key);

    /** Return a number of keys collected into MVMap */
    int getKeysNumber();

    @Override
    int hashCode();

    /**
     * Returns {@code true} if this map contains no key-value mappings.
     * 
     * @return {@code true} if this map contains no key-value mappings
     */
    boolean isEmpty();

    Iterator<V> iterator();

    /**
     * Associates the specified {@code value} with the specified {@code key} in
     * this MVMap. Multiple value for each {@code key} are allowed.
     * 
     * @param key
     *            - the key
     * @param value
     *            - the value
     * @return {@code true} if the {@code value} are correctly added at the
     *         MVMap. This depend of the specific implementation of List that is
     *         used.
     */
    boolean put(K key, V value);

    /**
     * Is the same of the {@link #put(Object key, Object value)} but the
     * key-value pair is provided by a {@link java.util.Map.Entry} object.
     * 
     * @param e
     *            the key-value pair
     * @return {@code true} if the {@code value} are correctly added at the
     *         BucketMap. This depend of the specific implementation of List
     *         that is used.
     */
    boolean put(Map.Entry<K, V> e);

    /**
     * Removes the first occurrence of the specified element from this MVMap.
     * 
     * @return <tt>true</tt> if this list contained the specified element
     */
    boolean remove(K key, V value);

    /**
     * Removes the first occurrence of the specified element from this MVMap.
     * 
     * @return <tt>true</tt> if this list contained the specified element
     */
    boolean remove(Map.Entry<K, V> e);

    /**
     * Returns the number of value contains in this Multi-Value Map.
     * 
     * The max number of key permitted is {@code Integer.MAX_VALUE} and each key
     * can contain {@code Integer.MAX_VALUE} value.
     * 
     * @return the number of values contained in this map
     */
    int size();

    V[] toArray(V[] v);

    @Override
    String toString();

    /**
     * <p>
     * Return a list of all values contained into MVMap
     * </p>
     * WARNING: the returned list should be used in read-only mode because it is
     * not synchronized with the rest of the map
     */
    List<V> values();

}
