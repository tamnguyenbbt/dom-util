package com.github.tamnguyenbbt.dom;

import java.util.Map;

final class MapEntry<K, V> implements Map.Entry<K,V>
{
    private final K key;
    private V value;

    public MapEntry(K key, V value)
    {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey()
    {
        return key;
    }

    @Override
    public V getValue()
    {
        return value;
    }

    @Override
    public V setValue(V value)
    {
        V oldValue = this.value;
        this.value = value;
        return oldValue;
    }
}
