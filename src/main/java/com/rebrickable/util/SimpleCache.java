package com.rebrickable.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Very simple cache implementation.
 *
 * Store elements for up to 300 seconds.
 */
public class SimpleCache<T> {

    static class Element<U> {
        long lastAccess;
        U value;
    }

    private final Map<String, Element<T>> storage = new HashMap<>();

    public T get(String key) {
        Element<T> element = storage.get(key);
        if (element == null) {
            return null;
        }
        long now = System.currentTimeMillis();
        if (element.lastAccess + 300 * 1000 < now) {
            storage.remove(key);
            return null;
        }
        element.lastAccess = now;
        return element.value;
    }

    public void put(String key, T value) {
        var element = new Element<T>();
        element.lastAccess = System.currentTimeMillis();
        element.value = value;
        storage.put(key, element);
    }

}
