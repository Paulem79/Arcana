package ovh.paulem.arcana.regions.container;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;
import ovh.paulem.arcana.regions.ChunkKey;
import ovh.paulem.arcana.regions.Holder;
import ovh.paulem.arcana.regions.Tracked;
import ovh.paulem.arcana.regions.WorldKey;

import java.util.*;

/**
 * Represents a container for managing block-related data across a specific world,
 * organized into smaller chunk-based containers.
 *
 * @param <S> The type of the tracked entities being managed, which extends {@link Tracked}.
 * @param <K> The type of the key associated with the tracked entities, which extends {@link WorldKey}.
 */
public class WorldBlockContainer<S extends Tracked<K>, K extends WorldKey> implements SubContainerHolderAccessor<S, K> {
    @Getter
    private final UUID worldId;
    private final Map<ChunkKey, ChunkBlockContainer<S, K>> chunkContainers;

    private WorldBlockContainer(UUID worldId) {
        this.worldId = worldId;
        this.chunkContainers = new HashMap<>();
    }

    public static<S extends Tracked<K>, K extends WorldKey> WorldBlockContainer<S, K> of(UUID worldId) {
        return new WorldBlockContainer<>(worldId);
    }

    public static<S extends Tracked<K>, K extends WorldKey> WorldBlockContainer<S, K> of(World world) {
        return of(world.getUID());
    }

    public World getWorld() {
        return Bukkit.getWorld(worldId);
    }

    @Nullable
    public ChunkBlockContainer<S, K> getChunkContainer(ChunkKey key) {
        return chunkContainers.get(key);
    }

    public ChunkBlockContainer<S, K> getOrCreateChunkContainer(ChunkKey key) {
        ChunkBlockContainer<S, K> container = getChunkContainer(key);

        if (container == null) {
            container = ChunkBlockContainer.of(this, key);
            chunkContainers.put(key, container);
        }

        return container;
    }

    public void removeChunkContainer(ChunkKey key) {
        chunkContainers.remove(key);
    }

    public void removeChunkContainer(Chunk chunk) {
        removeChunkContainer(ChunkKey.fromChunk(chunk));
    }

    @Override
    @Nullable
    public S getHolder(int x, int y, int z) {
        ChunkKey key = ChunkKey.fromCoordinates(x, z);
        ChunkBlockContainer<S, K> container = getChunkContainer(key);

        if (container == null) {
            return null;
        }

        return container.getHolder(x, y, z);
    }

    @Override
    public void setHolder(int x, int y, int z, S holder) {
        ChunkKey key = ChunkKey.fromCoordinates(x, z);
        ChunkBlockContainer<S, K> container = getOrCreateChunkContainer(key);

        container.setHolder(x, y, z, holder);
    }

    @Override
    public void removeHolder(int x, int y, int z) {
        ChunkKey key = ChunkKey.fromCoordinates(x, z);
        ChunkBlockContainer<S, K> container = getChunkContainer(key);

        if (container == null) {
            return;
        }

        container.removeHolder(x, y, z);
    }

    @Override
    public Collection<Holder<S, K>> getAllHolders() {
        List<Holder<S, K>> holders = new ArrayList<>();

        for (ChunkBlockContainer<S, K> container : chunkContainers.values()) {
            holders.addAll(container.getAllHolders());
        }

        return holders;
    }
}