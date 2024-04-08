package by.abakumova.weatherjava.cache;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Cache<K, V> {
    private final Map<K, V> cacheMap = new ConcurrentHashMap<>();

    /**
     * Constant.
     */
    private static final int MAX_SIZE = 100;

    /**
     * Добавляет элемент в кэш. Если ключ уже существует,
     * обновляет его значение. Если размер кэша превышает максимальный,
     * удаляет самый старый элемент.
     *
     * @param key   ключ элемента
     * @param value значение элемента
     */
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

    /**
     * Возвращает значение из кэша по указанному ключу.
     *
     * @param key ключ для поиска значения в кэше
     * @return значение из кэша, связанное с указанным ключом,
     * или null, если ключ не найден в кэше
     */
    public V get(final K key) {
        return cacheMap.get(key);
    }

    /**
     * Удаляет элемент из кэша по указанному ключу, если он присутствует.
     *
     * @param key ключ элемента, который следует удалить из кэша
     */
    public void remove(final K key) {
        cacheMap.remove(key);
    }

    /**
     * Возвращает представление кэша как коллекцию пар ключ-значение.
     *
     * @return представление кэша как коллекцию пар ключ-значение
     */
    public Iterable<Map.Entry<K, V>> getNativeCache() {
        return cacheMap.entrySet();
    }

}
