package ovh.paulem.arcana.regions.container;

import org.bukkit.World;
import org.jetbrains.annotations.Nullable;
import ovh.paulem.arcana.regions.Holder;
import ovh.paulem.arcana.regions.WorldKey;
import ovh.paulem.arcana.regions.Tracked;

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

    public static <S extends Tracked<K>, K extends WorldKey> GlobalBlockContainer<S, K> of() {
        return new GlobalBlockContainer<>();
    }

    @Nullable
    public WorldBlockContainer<S, K> getWorldContainer(UUID worldId) {
        return worldContainers.get(worldId);
    }

    public WorldBlockContainer<S, K> getOrCreateWorldContainer(UUID worldId) {
        WorldBlockContainer<S, K> container = getWorldContainer(worldId);

        if (container == null) {
            container = WorldBlockContainer.of(worldId);
            worldContainers.put(worldId, container);
        }

        return container;
    }

    public void removeWorldContainer(UUID worldId) {
        worldContainers.remove(worldId);
    }

    @Nullable
    public WorldBlockContainer<S, K> getWorldContainer(World world) {
        return getWorldContainer(world.getUID());
    }

    public WorldBlockContainer<S, K> getOrCreateWorldContainer(World world) {
        return getOrCreateWorldContainer(world.getUID());
    }

    @Nullable
    public S getHolder(K key) {
        WorldBlockContainer<S, K> container = getWorldContainer(key.getWorld());

        if (container == null) {
            return null;
        }

        return container.getHolder(key.getX(), key.getY(), key.getZ());
    }

    public void setHolder(K key, S tracked) {
        WorldBlockContainer<S, K> container = getOrCreateWorldContainer(key.getWorld());
        container.setHolder(key.getX(), key.getY(), key.getZ(), tracked);
    }

    public void removeHolder(K key) {
        WorldBlockContainer<S, K> container = getWorldContainer(key.getWorld());

        if (container == null) {
            return;
        }

        container.removeHolder(key.getX(), key.getY(), key.getZ());
    }

    public List<Holder<S, K>> getAllHolders() {
        List<Holder<S, K>> holders = new ArrayList<>();

        for (WorldBlockContainer<S, K> container : worldContainers.values()) {
            holders.addAll(container.getAllHolders());
        }

        return holders;
    }

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