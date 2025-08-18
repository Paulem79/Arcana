package ovh.paulem.arcana.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MapUtils {
    public static<K, V> Map<K, V> copyOf(Map<K, V> map) {
        return Collections.unmodifiableMap(new HashMap<>(map));
    }
}
