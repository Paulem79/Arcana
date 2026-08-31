package net.paulem.arcana.regions.container.blocks;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.paulem.arcana.regions.Holder;
import net.paulem.arcana.regions.Tracked;
import net.paulem.arcana.regions.WorldKey;

/**
 * Represents a container for managing sections of data associated with spatial positions.
 * Provides methods for accessing, modifying, and retrieving the elements within the container.
 * This container works with elements that are {@link Tracked} and are associated with a key
 * implementing {@link WorldKey}.
 *
 * @param <S> the type of elements stored in the container, which must implement the {@link Tracked} interface
 * @param <K> the type of key associated with each element, which must extend {@link WorldKey}
 */
public interface SectionContainer<S extends Tracked<K>, K extends WorldKey> {

    /**
     * Retrieves the holder stored at the given position.
     *
     * @param position the index to look up
     * @return the holder at the position, or {@code null} if empty
     */
    Holder<S, K> get(int position);

    /**
     * Stores or clears the holder at the given position.
     *
     * @param position the index to store the holder at
     * @param holder the holder to store, or {@code null} to clear the position
     */
    void set(int position, Holder<S, K> holder);

    /**
     * @return a map of position to holder for every non-null entry in this container
     */
    Int2ObjectOpenHashMap<Holder<S, K>> getAll();

    /**
     * @return the number of occupied positions in this container
     */
    default int size() {
        return getAll().size();
    }

    /**
     * @return {@code true} if no positions are currently occupied
     */
    default boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Copies every entry from this container into the given target container.
     *
     * @param target the container to copy entries into
     */
    default void copyTo(SectionContainer<S, K> target) {
        for (Int2ObjectMap.Entry<Holder<S, K>> entry : getAll().int2ObjectEntrySet()) {
            target.set(entry.getIntKey(), entry.getValue());
        }
    }
}