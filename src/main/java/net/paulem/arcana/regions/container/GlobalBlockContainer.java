package net.paulem.arcana.regions.container;

import org.bukkit.World;
import org.jetbrains.annotations.Nullable;
import net.paulem.arcana.regions.Holder;
import net.paulem.arcana.regions.WorldKey;
import net.paulem.arcana.regions.Tracked;

import java.util.*;

/**
 * A container that holds all the block containers for all worlds.
 * This increases performance when dealing with many worlds, chunks and sections and avoid iterating over every single object.
 * @param <S> the type of the object to track
 * @param <K> the key used to identify the object
 */
public class GlobalBlockContainer<S extends Tracked<K>, K extends WorldKey> {
    private final Map<UUID, WorldBlockContainer<S, K>> worldContainers;

    private GlobalBlockContainer() {
        this.worldContainers = new HashMap<>();
    }

    /**
     * Creates a new, empty {@code GlobalBlockContainer}.
     *
     * @param <S> the type of the object to track
     * @param <K> the key used to identify the object
     * @return a new {@code GlobalBlockContainer} instance
     */
    public static <S extends Tracked<K>, K extends WorldKey> GlobalBlockContainer<S, K> of() {
        return new GlobalBlockContainer<>();
    }

    /**
     * Retrieves the container for the given world, without creating it.
     *
     * @param worldId the UUID of the world
     * @return the world's container, or {@code null} if it doesn't exist yet
     */
    @Nullable
    public WorldBlockContainer<S, K> getWorldContainer(UUID worldId) {
        return worldContainers.get(worldId);
    }

    /**
     * Retrieves the container for the given world, creating it first if it doesn't exist yet.
     *
     * @param worldId the UUID of the world
     * @return the world's container, never {@code null}
     */
    public WorldBlockContainer<S, K> getOrCreateWorldContainer(UUID worldId) {
        WorldBlockContainer<S, K> container = getWorldContainer(worldId);

        if (container == null) {
            container = WorldBlockContainer.of(worldId);
            worldContainers.put(worldId, container);
        }

        return container;
    }

    /**
     * Removes the container for the given world, if present.
     *
     * @param worldId the UUID of the world to remove
     */
    public void removeWorldContainer(UUID worldId) {
        worldContainers.remove(worldId);
    }

    /**
     * Retrieves the container for the given world, without creating it.
     *
     * @param world the world
     * @return the world's container, or {@code null} if it doesn't exist yet
     */
    @Nullable
    public WorldBlockContainer<S, K> getWorldContainer(World world) {
        return getWorldContainer(world.getUID());
    }

    /**
     * Retrieves the container for the given world, creating it first if it doesn't exist yet.
     *
     * @param world the world
     * @return the world's container, never {@code null}
     */
    public WorldBlockContainer<S, K> getOrCreateWorldContainer(World world) {
        return getOrCreateWorldContainer(world.getUID());
    }

    /**
     * Retrieves the tracked object located at the given key, across all worlds.
     *
     * @param key the key identifying the world and coordinates to look up
     * @return the tracked object at the key, or {@code null} if none is registered or its world has no container
     */
    @Nullable
    public S getHolder(K key) {
        WorldBlockContainer<S, K> container = getWorldContainer(key.getWorld());

        if (container == null) {
            return null;
        }

        return container.getHolder(key.getX(), key.getY(), key.getZ());
    }

    /**
     * Stores a tracked object at the given key, creating the containers for its world/chunk/section as needed.
     *
     * @param key the key identifying the world and coordinates to store the object at
     * @param tracked the object to store
     */
    public void setHolder(K key, S tracked) {
        WorldBlockContainer<S, K> container = getOrCreateWorldContainer(key.getWorld());
        container.setHolder(key.getX(), key.getY(), key.getZ(), tracked);
    }

    /**
     * Removes the tracked object located at the given key, if any.
     *
     * @param key the key identifying the world and coordinates to remove
     */
    public void removeHolder(K key) {
        WorldBlockContainer<S, K> container = getWorldContainer(key.getWorld());

        if (container == null) {
            return;
        }

        container.removeHolder(key.getX(), key.getY(), key.getZ());
    }

    /**
     * @return a collection of all holders stored across every world container
     */
    public List<Holder<S, K>> getAllHolders() {
        List<Holder<S, K>> holders = new ArrayList<>();

        for (WorldBlockContainer<S, K> container : worldContainers.values()) {
            holders.addAll(container.getAllHolders());
        }

        return holders;
    }

    /**
     * Retrieves the data of every holder across all world containers whose data is an instance of the given type.
     *
     * @param type the class used to filter and cast each holder's data
     * @param <T> the expected type of the holder's data
     * @return a list of the data objects matching the given type
     */
    public <T extends Holder<S, K>> List<T> getAllHolders(Class<T> type) {
        List<T> blocks = new ArrayList<>();

        for (Holder<S, K> holder : getAllHolders()) {
            if (type.isInstance(holder.getData())) {
                blocks.add(type.cast(holder.getData()));
            }
        }

        return blocks;
    }
}