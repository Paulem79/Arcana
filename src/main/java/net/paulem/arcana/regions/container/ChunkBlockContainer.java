package net.paulem.arcana.regions.container;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import net.paulem.arcana.regions.ChunkKey;
import net.paulem.arcana.regions.Holder;
import net.paulem.arcana.regions.Tracked;
import net.paulem.arcana.regions.WorldKey;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents a container for managing and organizing data within a specific chunk of the world.
 * This container divides the chunk into sections based on the vertical axis, allowing for more
 * granular management of data associated with tracked entities.
 *
 * @param <S> The type of the tracked entities being managed, which extends {@link Tracked}.
 * @param <K> The type of the key associated with the tracked entities, which extends {@link WorldKey}.
 */
public class ChunkBlockContainer<S extends Tracked<K>, K extends WorldKey> implements SubContainerHolderAccessor<S, K> {
    private static final int SECTION_SIZE = 16;
    private static final int MIN_HEIGHT = -64;
    private static final int MAX_HEIGHT = 256;
    private static final int SECTION_COUNT = (MAX_HEIGHT - MIN_HEIGHT) / SECTION_SIZE;

    @Getter
    private final WorldBlockContainer<S, K> parent;
    @Getter
    private final ChunkKey chunkKey;
    private final ChunkSectionBlockContainer<S, K>[] sections;

    private ChunkBlockContainer(WorldBlockContainer<S, K> parent, ChunkKey chunkKey) {
        this.parent = parent;
        this.chunkKey = chunkKey;
        this.sections = new ChunkSectionBlockContainer[SECTION_COUNT];
    }

    /**
     * Creates a new, empty {@code ChunkBlockContainer} for the given parent world container and chunk.
     *
     * @param parent the world container this chunk container belongs to
     * @param chunkKey the key identifying the chunk this container manages
     * @param <S> the type of the tracked entities being managed
     * @param <K> the type of the key associated with the tracked entities
     * @return a new {@code ChunkBlockContainer} instance
     */
    public static<S extends Tracked<K>, K extends WorldKey> ChunkBlockContainer<S, K> of(WorldBlockContainer<S, K> parent, ChunkKey chunkKey) {
        return new ChunkBlockContainer<>(parent, chunkKey);
    }

    /**
     * Retrieves the vertical section at the given index, without creating it.
     *
     * @param sectionY the section index
     * @return the section at the given index, or {@code null} if it hasn't been created yet
     */
    public ChunkSectionBlockContainer<S, K> getSection(int sectionY) {
        return sections[sectionY];
    }

    /**
     * Retrieves the vertical section at the given index, creating it first if it doesn't exist yet.
     *
     * @param sectionY the section index
     * @return the section at the given index, never {@code null}
     */
    public ChunkSectionBlockContainer<S, K> getOrCreateSection(int sectionY) {
        ChunkSectionBlockContainer<S, K> section = getSection(sectionY);

        if (section == null) {
            section = ChunkSectionBlockContainer.of(this, sectionY);
            sections[sectionY] = section;
        }

        return section;
    }

    /**
     * Removes the given section from this chunk container and notifies the parent world
     * container to remove this chunk if it no longer has any sections left.
     *
     * @param section the section to remove
     */
    public void removeSection(ChunkSectionBlockContainer<S, K> section) {
        sections[section.getSectionY()] = null;
        notifyParent();
    }

    /**
     * Clears all holders from every section in this chunk container.
     */
    public void clear() {
        for (ChunkSectionBlockContainer<S, K> section : sections) {
            if (section != null) {
                section.clear();
            }
        }
    }

    private static int sectionIndex(int y) {
        return (y - MIN_HEIGHT) / SECTION_SIZE;
    }

    /**
     * Retrieves the tracked object at the given coordinates, using the vertical section that y falls into.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @return the tracked object at the given coordinates, or {@code null} if y is outside this chunk's height range or no object is stored there
     */
    @Override
    @Nullable
    public S getHolder(int x, int y, int z) {
        if (y < MIN_HEIGHT || y >= MAX_HEIGHT) {
            return null;
        }

        ChunkSectionBlockContainer<S, K> section = getOrCreateSection(sectionIndex(y));

        if (section == null) {
            return null;
        }

        return section.getHolder(x, y, z);
    }

    /**
     * Stores a tracked object at the given coordinates, using the vertical section that y falls into.
     * Does nothing if y is outside this chunk's height range.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @param holder the tracked object to store
     */
    @Override
    public void setHolder(int x, int y, int z, S holder) {
        if (y < MIN_HEIGHT || y >= MAX_HEIGHT) {
            return;
        }

        ChunkSectionBlockContainer<S, K> section = getOrCreateSection(sectionIndex(y));

        if (section == null) {
            return;
        }

        section.setHolder(x, y, z, holder);
    }

    /**
     * Removes the tracked object at the given coordinates, using the vertical section that y falls into.
     * Does nothing if y is outside this chunk's height range.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    @Override
    public void removeHolder(int x, int y, int z) {
        if (y < MIN_HEIGHT || y >= MAX_HEIGHT) {
            return;
        }

        ChunkSectionBlockContainer<S, K> section = getOrCreateSection(sectionIndex(y));

        if (section == null) {
            return;
        }

        section.removeHolder(x, y, z);
    }

    private void notifyParent() {
        for (ChunkSectionBlockContainer<S, K> section : sections) {
            if (section != null) {
                return;
            }
        }

        parent.removeChunkContainer(chunkKey);
    }

    /**
     * @return a collection of all holders stored across every section of this chunk
     */
    @Override
    public Collection<Holder<S, K>> getAllHolders() {
        List<Holder<S, K>> holders = new ArrayList<>();

        for (ChunkSectionBlockContainer<S, K> section : sections) {
            if (section == null) {
                continue;
            }

            holders.addAll(section.getAllHolders());
        }

        return holders;
    }
}