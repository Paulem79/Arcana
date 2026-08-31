package net.paulem.arcana.regions;

/**
 * Represents an object that can be tracked and associated with a unique key.
 * Implementations are typically stored in a {@link Tracker} and its underlying block containers.
 *
 * @param <K> the type of the key used to identify this object
 */
public interface Tracked<K> {
    /**
     * @return the key used to identify and locate this object
     */
    K getKey();

    /**
     * Called when the chunk containing this object is unloaded, allowing implementations
     * to persist or clean up their state.
     */
    void onUnload();
}
