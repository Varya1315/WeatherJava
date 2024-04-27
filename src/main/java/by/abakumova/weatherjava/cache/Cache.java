package by.abakumova.weatherjava.cache;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Cache<K, V> {
    private final Map<K, V> cacheMap = new ConcurrentHashMap<>();

    private static final int MAX_SIZE = 100;

    public void put(final K key, final V value) {
        if (cacheMap.containsKey(key)) {
            cacheMap.put(key, value);
        } else {
            cacheMap.put(key, value);
            if (cacheMap.size() >= MAX_SIZE) {
                cacheMap.clear();
            }
        }
    }

    public V get(final K key) {
        return cacheMap.get(key);
    }

    public void remove(final K key) {
        cacheMap.remove(key);
    }

}