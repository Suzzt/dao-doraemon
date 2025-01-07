package org.dao.doraemon.database.crypto.util;

import java.util.Arrays;

/**
 * 固定大小的简单 hashMap，该 hashmap
 * 主要用于多元模式下的并发编程，请注意使用场景
 *
 * @param <K>
 * @param <V>
 * @author wuzhenhong
 * @SInCE 1.0
 */
public class SimpleHashMap<K, V> {

    private final Entry<K, V>[] buckets;
    private final int indexMask;
    public final static int DEFAULT_SIZE = 8192;

    public SimpleHashMap() {
        this(DEFAULT_SIZE);
    }

    public SimpleHashMap(int tableSize) {
        this.indexMask = tableSize - 1;
        this.buckets = new Entry[tableSize];
    }

    public final V get(K key) {
        final int hash = this.hash(key);
        final int bucket = hash & indexMask;

        for (Entry<K, V> entry = buckets[bucket]; entry != null; entry = entry.next) {
            if (key == entry.key || key.equals(entry.key)) {
                return entry.value;
            }
        }

        return null;
    }

    public boolean put(K key, V value) {
        final int hash = this.hash(key);
        final int bucket = hash & indexMask;

        for (Entry<K, V> entry = buckets[bucket]; entry != null; entry = entry.next) {
            if (key == entry.key || key.equals(entry.key)) {
                entry.value = value;
                return true;
            }
        }

        Entry<K, V> entry = new Entry<K, V>(key, value, hash, buckets[bucket]);
        // 如果该HashMap被用于多线程，这里会产生覆盖，所以最好使用在那种可以覆盖的场景
        // 比如根据className创建对象，这种多元模式下
        buckets[bucket] = entry;

        return false;
    }

    public void clear() {
        Arrays.fill(this.buckets, null);
    }

    public final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private static final class Entry<K, V> {

        public final int hash;
        public final K key;
        public V value;

        public final Entry<K, V> next;

        public Entry(K key, V value, int hash, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
            this.hash = hash;
        }
    }
}
