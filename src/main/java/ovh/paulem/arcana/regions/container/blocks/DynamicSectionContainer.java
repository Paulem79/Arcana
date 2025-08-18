package ovh.paulem.arcana.regions.container.blocks;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import ovh.paulem.arcana.regions.Holder;
import ovh.paulem.arcana.regions.Tracked;
import ovh.paulem.arcana.regions.WorldKey;

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

    public static<S extends Tracked<K>, K extends WorldKey> DynamicSectionContainer<S, K> create(float loadFactor, int maxSize) {
        return new DynamicSectionContainer<>(loadFactor, maxSize);
    }

    public static<S extends Tracked<K>, K extends WorldKey> DynamicSectionContainer<S, K> create(int maxSize) {
        return create(DEFAULT_LOAD_FACTOR, maxSize);
    }

    @Override
    public Holder<S, K> get(int position) {
        return currentContainer.get(position);
    }

    @Override
    public void set(int position, Holder<S, K> holder) {
        currentContainer.set(position, holder);
        recalculate();
    }

    @Override
    public Int2ObjectOpenHashMap<Holder<S, K>> getAll() {
        return currentContainer.getAll();
    }

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