package ovh.paulem.arcana.regions.container.blocks;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import ovh.paulem.arcana.regions.Holder;
import ovh.paulem.arcana.regions.Tracked;
import ovh.paulem.arcana.regions.WorldKey;

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

    Holder<S, K> get(int position);

    void set(int position, Holder<S, K> holder);

    Int2ObjectOpenHashMap<Holder<S, K>> getAll();

    default int size() {
        return getAll().size();
    }

    default boolean isEmpty() {
        return size() == 0;
    }

    default void copyTo(SectionContainer<S, K> target) {
        for (Int2ObjectMap.Entry<Holder<S, K>> entry : target.getAll().int2ObjectEntrySet()) {
            target.set(entry.getIntKey(), entry.getValue());
        }
    }
}