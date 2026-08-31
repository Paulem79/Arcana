package net.paulem.arcana.regions.container.blocks;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.paulem.arcana.regions.Holder;
import net.paulem.arcana.regions.Tracked;
import net.paulem.arcana.regions.WorldKey;

/**
 * A container implementation that dynamically transitions between a {@link MapContainer} and
 * an {@link ArraySectionContainer} based on the load factor and size constraints. This class is
 * designed to optimize memory usage and performance by starting with an efficient map-based
 * container and expanding into an array-based container when necessary.
 *
 * @param <S> the type of tracked objects, which must implement the {@link Tracked} interface
 * @param <K> the type of the key associated with the tracked object
 */
// TODO: If you're fancy about it you can also implement a putAll method that pre-expands and yeets it all for a bit more efficiency
public class DynamicSectionContainer<S extends Tracked<K>, K extends WorldKey> implements SectionContainer<S, K> {
    private static final float DEFAULT_LOAD_FACTOR = 0.5f;

    private final float loadFactor;
    private final int maxSize;

    private SectionContainer<S, K> currentContainer;
    private boolean expanded = false;

    private DynamicSectionContainer(float loadFactor, int maxSize) {
        this.loadFactor = loadFactor;
        this.maxSize = maxSize;
        this.currentContainer = new MapContainer<>();
    }

    /**
     * Creates a new {@code DynamicSectionContainer} with the given load factor and capacity.
     *
     * @param loadFactor the fraction of {@code maxSize} that, once reached, triggers expansion to an array-backed container
     * @param maxSize the maximum number of positions this container can hold once expanded
     * @param <S> the type of tracked objects
     * @param <K> the type of the key associated with the tracked object
     * @return a new {@code DynamicSectionContainer} instance
     */
    public static<S extends Tracked<K>, K extends WorldKey> DynamicSectionContainer<S, K> create(float loadFactor, int maxSize) {
        return new DynamicSectionContainer<>(loadFactor, maxSize);
    }

    /**
     * Creates a new {@code DynamicSectionContainer} with the given capacity and the default load factor.
     *
     * @param maxSize the maximum number of positions this container can hold once expanded
     * @param <S> the type of tracked objects
     * @param <K> the type of the key associated with the tracked object
     * @return a new {@code DynamicSectionContainer} instance
     */
    public static<S extends Tracked<K>, K extends WorldKey> DynamicSectionContainer<S, K> create(int maxSize) {
        return create(DEFAULT_LOAD_FACTOR, maxSize);
    }

    /**
     * Retrieves the holder at the given position from the currently active underlying container.
     *
     * @param position the index to look up
     * @return the holder at the position, or {@code null} if empty
     */
    @Override
    public Holder<S, K> get(int position) {
        return currentContainer.get(position);
    }

    /**
     * Stores or clears the holder at the given position in the currently active underlying container,
     * expanding to an array-backed container first if the load factor is now exceeded.
     *
     * @param position the index to store the holder at
     * @param holder the holder to store, or {@code null} to clear the position
     */
    @Override
    public void set(int position, Holder<S, K> holder) {
        currentContainer.set(position, holder);
        recalculate();
    }

    /**
     * @return a map of position to holder for every non-null entry in the currently active underlying container
     */
    @Override
    public Int2ObjectOpenHashMap<Holder<S, K>> getAll() {
        return currentContainer.getAll();
    }

    /**
     * Copies every entry from the given container into this one, expanding to an array-backed
     * container first if adding them would exceed the load factor.
     *
     * @param other the container whose entries should be copied into this one
     */
    public void putAll(SectionContainer<S, K> other) {
        if (!expanded && ((float) (currentContainer.size() + other.size()) / maxSize) >= loadFactor) {
            expand();
        }

        other.getAll().forEach((pos, holder) -> currentContainer.set(pos, holder));
        recalculate();
    }

    private float calculateLoadFactor() {
        return (float) currentContainer.size() / maxSize;
    }

    private void recalculate() {
        if (expanded) {
            return;
        }

        if (calculateLoadFactor() < loadFactor) {
            return;
        }

        expand();
    }

    private void expand() {
        SectionContainer<S, K> old = currentContainer;
        SectionContainer<S, K> newContainer = ArraySectionContainer.create(maxSize);

        old.copyTo(newContainer);
        this.currentContainer = newContainer;
        this.expanded = true;
    }
}