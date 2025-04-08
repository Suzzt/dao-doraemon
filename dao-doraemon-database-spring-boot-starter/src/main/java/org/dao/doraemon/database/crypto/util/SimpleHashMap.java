package org.dao.doraemon.database.crypto.util;

import java.util.Arrays;

/**
 * 注意：
 * 该 hashmap 是固定大小的简单 hashMap，该 hashmap
 * 主要用于多元模式下的并发编程，请注意使用场景
 * 有些兄弟看到这个集合的时候非常疑惑，它线程不安全啊，
 * 首先该集合固定大小不会扩容，第二在修改方面，它只有 put 的逻辑，没有删除的逻辑
 * 也就是说只有在 put 的时候会发生线程安全问题，但是这个线程安全问题只是发生覆盖，
 * 所以这个时候你要明确你的使用场景，比如我的使用场景是用于缓存策略实例，className -》instance
 * 那么在并发情况下，会出现多个线程同时构建同一个className，然后往里放的情况，但是对于我缓存来说
 * 并不会出现错误。之前是使用 ConcurrentHashMap 后边我感觉它要加锁，对性能有影响，所以想这种全局的缓存
 * 我觉得使用 SimpleHashMap 是比较合适的
 *
 * @param <K>
 * @param <V>
 * @author wuzhenhong
 * @since 1.0
 */
public class SimpleHashMap<K, V> {

    public final static int DEFAULT_SIZE = 8192;
    private final Entry<K, V>[] buckets;
    private final int indexMask;

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
        public final Entry<K, V> next;
        public V value;

        public Entry(K key, V value, int hash, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
            this.hash = hash;
        }
    }
}
