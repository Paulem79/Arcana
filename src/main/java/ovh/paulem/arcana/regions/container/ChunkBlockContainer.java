package ovh.paulem.arcana.regions.container;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import ovh.paulem.arcana.regions.ChunkKey;
import ovh.paulem.arcana.regions.Holder;
import ovh.paulem.arcana.regions.Tracked;
import ovh.paulem.arcana.regions.WorldKey;

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

    public static<S extends Tracked<K>, K extends WorldKey> ChunkBlockContainer<S, K> of(WorldBlockContainer<S, K> parent, ChunkKey chunkKey) {
        return new ChunkBlockContainer<>(parent, chunkKey);
    }

    public ChunkSectionBlockContainer<S, K> getSection(int sectionY) {
        return sections[sectionY];
    }

    public ChunkSectionBlockContainer<S, K> getOrCreateSection(int sectionY) {
        ChunkSectionBlockContainer<S, K> section = getSection(sectionY);

        if (section == null) {
            section = ChunkSectionBlockContainer.of(this, sectionY);
            sections[sectionY] = section;
        }

        return section;
    }

    public void removeSection(ChunkSectionBlockContainer<S, K> section) {
        sections[section.getSectionY()] = null;
        notifyParent();
    }

    public void clear() {
        for (ChunkSectionBlockContainer<S, K> section : sections) {
            section.clear();
        }
    }

    @Override
    @Nullable
    public S getHolder(int x, int y, int z) {
        if (y < MIN_HEIGHT || y > MAX_HEIGHT) {
            return null;
        }

        ChunkSectionBlockContainer<S, K> section = getOrCreateSection(y / SECTION_SIZE);

        if (section == null) {
            return null;
        }

        return section.getHolder(x, y, z);
    }

    @Override
    public void setHolder(int x, int y, int z, S holder) {
        if (y < MIN_HEIGHT || y > MAX_HEIGHT) {
            return;
        }

        ChunkSectionBlockContainer<S, K> section = getOrCreateSection(y / SECTION_SIZE);

        if (section == null) {
            return;
        }

        section.setHolder(x, y, z, holder);
    }

    @Override
    public void removeHolder(int x, int y, int z) {
        if (y < MIN_HEIGHT || y > MAX_HEIGHT) {
            return;
        }

        ChunkSectionBlockContainer<S, K> section = getOrCreateSection(y / SECTION_SIZE);

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