package by.abakumova.weatherjava.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
 class CacheTest {

    private Cache<String, String> cache;

    @BeforeEach
    void setUp() {
        cache = new Cache<>();
    }

    @Test
    void putAndGet() {
        cache.put("key1", "value1");
        assertEquals("value1", cache.get("key1"));
    }

    @Test
    void putTwiceAndGet() {
        cache.put("key1", "value1");
        cache.put("key1", "value2");
        assertEquals("value2", cache.get("key1"));
    }

    @Test
    void remove() {
        cache.put("key1", "value1");
        cache.remove("key1");
        assertNull(cache.get("key1"));
    }

    @Test
    void clearWhenSizeExceeds() {
        for (int i = 0; i < 110; i++) {
            cache.put("key" + i, "value" + i);
        }
        assertNull(cache.get("key0")); // The oldest entry should have been removed
        assertNotNull(cache.get("key100")); // The latest entry should still exist
    }

    @Test
    void getNativeCache() {
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");

        int count = 0;
        for (var entry : cache.getNativeCache()) {
            count++;
            assertTrue(entry.getKey().startsWith("key"));
            assertTrue(entry.getValue().startsWith("value"));
        }
        assertEquals(3, count);
    }
}
