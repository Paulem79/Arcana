package ovh.paulem.arcana.hook;

import java.util.HashMap;
import java.util.Map;

public class HookCondition<K, V> {
    private final Map<K, V> hooks;

    public HookCondition(Map<K, V> hooks) {
        this.hooks = hooks;
    }

    public V get(K key) {
        return hooks.get(key);
    }

    public static class HookConditionBuilder<K, V> {
        private final Map<K, V> hooks = new HashMap<>();

        public HookConditionBuilder<K, V> when(K key, V value) {
            hooks.put(key, value);
            return this;
        }

        public HookCondition<K, V> build() {
            return new HookCondition<>(hooks);
        }
    }
}
