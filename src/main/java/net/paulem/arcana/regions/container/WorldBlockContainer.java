package net.paulem.arcana.regions.container;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;
import net.paulem.arcana.regions.ChunkKey;
import net.paulem.arcana.regions.Holder;
import net.paulem.arcana.regions.Tracked;
import net.paulem.arcana.regions.WorldKey;

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

    /**
     * Creates a new, empty {@code WorldBlockContainer} for the given world.
     *
     * @param worldId the UUID of the world this container manages
     * @param <S> the type of the tracked entities being managed
     * @param <K> the type of the key associated with the tracked entities
     * @return a new {@code WorldBlockContainer} instance
     */
    public static<S extends Tracked<K>, K extends WorldKey> WorldBlockContainer<S, K> of(UUID worldId) {
        return new WorldBlockContainer<>(worldId);
    }

    /**
     * Creates a new, empty {@code WorldBlockContainer} for the given world.
     *
     * @param world the world this container manages
     * @param <S> the type of the tracked entities being managed
     * @param <K> the type of the key associated with the tracked entities
     * @return a new {@code WorldBlockContainer} instance
     */
    public static<S extends Tracked<K>, K extends WorldKey> WorldBlockContainer<S, K> of(World world) {
        return of(world.getUID());
    }

    /**
     * @return the {@link World} this container manages, resolved from its UUID, or {@code null} if that world isn't currently loaded
     */
    public World getWorld() {
        return Bukkit.getWorld(worldId);
    }

    /**
     * Retrieves the container for the given chunk, without creating it.
     *
     * @param key the chunk's key
     * @return the chunk's container, or {@code null} if it doesn't exist yet
     */
    @Nullable
    public ChunkBlockContainer<S, K> getChunkContainer(ChunkKey key) {
        return chunkContainers.get(key);
    }

    /**
     * Retrieves the container for the given chunk, creating it first if it doesn't exist yet.
     *
     * @param key the chunk's key
     * @return the chunk's container, never {@code null}
     */
    public ChunkBlockContainer<S, K> getOrCreateChunkContainer(ChunkKey key) {
        ChunkBlockContainer<S, K> container = getChunkContainer(key);

        if (container == null) {
            container = ChunkBlockContainer.of(this, key);
            chunkContainers.put(key, container);
        }

        return container;
    }

    /**
     * Removes the container for the given chunk, if present.
     *
     * @param key the key of the chunk to remove
     */
    public void removeChunkContainer(ChunkKey key) {
        chunkContainers.remove(key);
    }

    /**
     * Removes the container for the given chunk, if present.
     *
     * @param chunk the chunk to remove
     */
    public void removeChunkContainer(Chunk chunk) {
        removeChunkContainer(ChunkKey.fromChunk(chunk));
    }

    /**
     * Retrieves the tracked object at the given coordinates, using the chunk container that contains them.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @return the tracked object at the given coordinates, or {@code null} if its chunk isn't tracked or no object is stored there
     */
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

    /**
     * Stores a tracked object at the given coordinates, creating the chunk container for those coordinates if needed.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @param holder the tracked object to store
     */
    @Override
    public void setHolder(int x, int y, int z, S holder) {
        ChunkKey key = ChunkKey.fromCoordinates(x, z);
        ChunkBlockContainer<S, K> container = getOrCreateChunkContainer(key);

        container.setHolder(x, y, z, holder);
    }

    /**
     * Removes the tracked object at the given coordinates, if its chunk is tracked.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    @Override
    public void removeHolder(int x, int y, int z) {
        ChunkKey key = ChunkKey.fromCoordinates(x, z);
        ChunkBlockContainer<S, K> container = getChunkContainer(key);

        if (container == null) {
            return;
        }

        container.removeHolder(x, y, z);
    }

    /**
     * @return a collection of all holders stored across every chunk in this world
     */
    @Override
    public Collection<Holder<S, K>> getAllHolders() {
        List<Holder<S, K>> holders = new ArrayList<>();

        for (ChunkBlockContainer<S, K> container : chunkContainers.values()) {
            holders.addAll(container.getAllHolders());
        }

        return holders;
    }
}