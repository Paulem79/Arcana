package ovh.paulem.arcana.regions.container;

import lombok.Getter;
import ovh.paulem.arcana.regions.Holder;
import ovh.paulem.arcana.regions.Tracked;
import ovh.paulem.arcana.regions.WorldKey;
import ovh.paulem.arcana.regions.container.blocks.DynamicSectionContainer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents a container for managing blocks within a specific vertical section of a chunk.
 * Each ChunkSectionBlockContainer instance corresponds to a single section, providing management
 * for tracked entities within that section. This container is optimized for organizing and accessing
 * data based on three-dimensional coordinates (x, y, z) relative to the chunk section.
 *
 * @param <S> The type of entities being tracked and managed, which extends {@link Tracked}.
 * @param <K> The type of the key associated with the tracked entities, which extends {@link WorldKey}.
 */
public class ChunkSectionBlockContainer<S extends Tracked<K>, K extends WorldKey> implements SubContainerHolderAccessor<S, K> {

    private static final int SIZE = 16;
    private static final int VOLUME = SIZE * SIZE * SIZE;

    @Getter
    private final ChunkBlockContainer<S, K> parent;
    private final DynamicSectionContainer<S, K> holders;
    @Getter
    private final int sectionY;
    private int validCount;

    private ChunkSectionBlockContainer(ChunkBlockContainer<S, K> parent, int sectionY) {
        this.parent = parent;
        this.holders = DynamicSectionContainer.create(VOLUME);
        this.sectionY = sectionY;
    }

    public static<S extends Tracked<K>, K extends WorldKey> ChunkSectionBlockContainer<S, K> of(ChunkBlockContainer<S, K> parent, int sectionY) {
        return new ChunkSectionBlockContainer<>(parent, sectionY);
    }

    @Override
    public S getHolder(int x, int y, int z) {
        Holder<S, K> holder = holders.get(key(x, y, z));
        return holder == null ? null : holder.getData();
    }

    @Override
    public void setHolder(int x, int y, int z, S holder) {
        Holder<S, K> oldHolder = holders.get(key(x, y, z));
        holders.set(key(x, y, z), new Holder<>(x, y, z, holder));

        if (oldHolder == null) {
            validCount++;
        }
    }

    @Override
    public void removeHolder(int x, int y, int z) {
        Holder<S, K> holder = holders.get(key(x, y, z));

        if (holder == null) {
            return;
        }

        holders.set(key(x, y, z), null);
        validCount--;

        notifyParent();
    }

    public void clear() {
        for (int index = 0; index < VOLUME; index++) {
            holders.set(index, null);
        }

        validCount = 0;
    }

    // Utility methods

    private int key(int x, int y, int z) {
        x = x % SIZE;
        y = y % SIZE;
        z = z % SIZE;

        return Math.abs(x + (y * SIZE) + (z * SIZE * SIZE));
    }

    private void notifyParent() {
        if (validCount == 0) {
            parent.removeSection(this);
        }
    }

    @Override
    public Collection<Holder<S, K>> getAllHolders() {
        List<Holder<S, K>> holders = new ArrayList<>();

        for (int index = 0; index < VOLUME; index++) {
            Holder<S, K> holder = this.holders.get(index);

            if (holder == null) {
                continue;
            }

            holders.add(holder);
        }

        return holders;
    }
}