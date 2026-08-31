package net.paulem.arcana.regions.container.blocks;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.paulem.arcana.regions.Holder;
import net.paulem.arcana.regions.Tracked;
import net.paulem.arcana.regions.WorldKey;

/**
 * A fixed-size {@link SectionContainer} implementation backed by a plain array, indexed
 * directly by position. Offers O(1) access at the cost of always allocating the full
 * capacity upfront, regardless of how many positions are actually occupied.
 *
 * @param <S> the type of tracked objects, which must implement the {@link Tracked} interface
 * @param <K> the type of the key associated with the tracked object
 */
public class ArraySectionContainer<S extends Tracked<K>, K extends WorldKey> implements SectionContainer<S, K> {

    private final Holder<S, K>[] contents;
    private int size;

    private ArraySectionContainer(int size) {
        contents = new Holder[size];
    }

    /**
     * Creates a new, empty {@code ArraySectionContainer} with the given fixed capacity.
     *
     * @param size the number of positions this container can hold
     * @param <S> the type of tracked objects
     * @param <K> the type of the key associated with the tracked object
     * @return a new {@code ArraySectionContainer} instance
     */
    public static<S extends Tracked<K>, K extends WorldKey> ArraySectionContainer<S, K> create(int size) {
        return new ArraySectionContainer<>(size);
    }

    /**
     * Retrieves the holder stored at the given position.
     *
     * @param position the index to look up
     * @return the holder at the position, or {@code null} if empty
     */
    @Override
    public Holder<S, K> get(int position) {
        return contents[position];
    }

    /**
     * Stores or clears the holder at the given position, updating the tracked size accordingly.
     *
     * @param position the index to store the holder at
     * @param holder the holder to store, or {@code null} to clear the position
     */
    @Override
    public void set(int position, Holder<S, K> holder) {
        Holder<S, K> old = get(position);
        contents[position] = holder;

        if (old == null && holder != null) {
            size++;
        } else if (old != null && holder == null) {
            size--;
        }
    }

    /**
     * Builds a map of every occupied position to its holder.
     *
     * @return a map of position to holder for every non-null entry in this container
     */
    @Override
    public Int2ObjectOpenHashMap<Holder<S, K>> getAll() {
        Int2ObjectOpenHashMap<Holder<S, K>> result = new Int2ObjectOpenHashMap<>();

        if (size == 0) {
            return result;
        }

        for (int index = 0; index < contents.length; index++) {
            Holder<S, K> holder = contents[index];

            if (holder == null) {
                continue;
            }

            result.put(index, holder);
        }

        return result;
    }

    /**
     * @return the number of occupied positions in this container
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * @return {@code true} if no positions are currently occupied
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
}