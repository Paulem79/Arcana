package net.paulem.arcana.hook;

import java.util.HashMap;
import java.util.Map;

/**
 * A key-to-value mapping used by {@link Hook} to resolve which implementation to use for a given key.
 * Instances are typically assembled with {@link HookConditionBuilder}.
 *
 * @param <K> the type of the keys
 * @param <V> the type of the values associated with each key
 */
public class HookCondition<K, V> {
    private final Map<K, V> hooks;

    /**
     * Creates a new HookCondition backed by the given map.
     * @param hooks the map of keys to values
     */
    public HookCondition(Map<K, V> hooks) {
        this.hooks = hooks;
    }

    /**
     * Returns the value registered for the given key.
     * @param key the key to look up
     * @return the value associated with the key, or {@code null} if none is registered
     */
    public V get(K key) {
        return hooks.get(key);
    }

    /**
     * A builder for {@link HookCondition} instances.
     *
     * @param <K> the type of the keys
     * @param <V> the type of the values associated with each key
     */
    public static class HookConditionBuilder<K, V> {
        private final Map<K, V> hooks = new HashMap<>();

        /**
         * Registers the value to use for the given key.
         * @param key the key to register
         * @param value the value to associate with the key
         * @return this builder, for chaining
         */
        public HookConditionBuilder<K, V> when(K key, V value) {
            hooks.put(key, value);
            return this;
        }

        /**
         * Builds a new {@link HookCondition} containing all the keys and values registered so far.
         * @return a new HookCondition instance
         */
        public HookCondition<K, V> build() {
            return new HookCondition<>(hooks);
        }
    }
}
