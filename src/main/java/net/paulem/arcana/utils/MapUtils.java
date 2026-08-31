package net.paulem.arcana.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility methods for working with maps.
 */
public class MapUtils {
    /**
     * Creates an unmodifiable shallow copy of the given map.
     *
     * @param map the map to copy
     * @param <K> the type of the keys
     * @param <V> the type of the values
     * @return an unmodifiable copy of {@code map}
     */
    public static<K, V> Map<K, V> copyOf(Map<K, V> map) {
        return Collections.unmodifiableMap(new HashMap<>(map));
    }
}
