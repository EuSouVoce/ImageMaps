package net.craftcitizen.imagemaps.clcore.util;

public class Tuple<K, V> {
    private final K key;
    private final V value;

    public Tuple(final K key, final V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return this.key;
    }

    public V getValue() {
        return this.value;
    }
}
