package net.paulem.arcana.regions.container.blocks;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.paulem.arcana.regions.Holder;
import net.paulem.arcana.regions.Tracked;
import net.paulem.arcana.regions.WorldKey;

/**
 * A {@link SectionContainer} implementation backed by a sparse {@link Int2ObjectOpenHashMap}.
 * Well-suited for sections with few occupied positions, since it only allocates entries for
 * positions that are actually in use.
 *
 * @param <S> the type of tracked objects, which must implement the {@link Tracked} interface
 * @param <K> the type of the key associated with the tracked object
 */
public class MapContainer<S extends Tracked<K>, K extends WorldKey> implements SectionContainer<S, K> {
    private final Int2ObjectOpenHashMap<Holder<S, K>> map = new Int2ObjectOpenHashMap<>();

    /**
     * Retrieves the holder stored at the given position.
     *
     * @param position the index to look up
     * @return the holder at the position, or {@code null} if empty
     */
    @Override
    public Holder<S, K> get(int position) {
        return map.get(position);
    }

    /**
     * Stores or clears the holder at the given position.
     *
     * @param position the index to store the holder at
     * @param holder the holder to store, or {@code null} to clear the position
     */
    @Override
    public void set(int position, Holder<S, K> holder) {
        map.put(position, holder);
    }

    /**
     * @return a shallow copy of the underlying map of position to holder
     */
    @Override
    public Int2ObjectOpenHashMap<Holder<S, K>> getAll() {
        return map.clone();
    }

    /**
     * @return the number of occupied positions in this container
     */
    @Override
    public int size() {
        return map.size();
    }

    /**
     * @return {@code true} if no positions are currently occupied
     */
    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }
}