package net.paulem.arcana.regions;

import lombok.Getter;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;
import net.paulem.arcana.regions.container.ChunkBlockContainer;
import net.paulem.arcana.regions.container.GlobalBlockContainer;
import net.paulem.arcana.regions.container.WorldBlockContainer;

import java.util.function.Consumer;

/**
 * A class to extend that tracks all the specified S type in the server.
 * @param <S> the type of the object to track
 */
public abstract class Tracker<S extends Tracked<K>, K extends WorldKey> {
    @Getter
    private final GlobalBlockContainer<S, K> globalContainer = GlobalBlockContainer.of();

    /**
     * Creates a new tracker with an empty {@link GlobalBlockContainer}.
     */
    public Tracker() {
    }

    /**
     * Retrieves the tracked object registered at the given key, if any.
     *
     * @param key the key to look up
     * @return the tracked object at the key, or {@code null} if none is registered
     */
    @Nullable
    public S getHolderAt(K key) {
        return globalContainer.getHolder(key);
    }

    /**
     * Registers a tracked object in the global container, using its own key.
     *
     * @param tracked the object to register
     */
    public void registerHolder(S tracked) {
        globalContainer.setHolder(tracked.getKey(), tracked);
    }

    /**
     * Removes a tracked object from the global container, using its own key.
     *
     * @param tracked the object to remove
     */
    public void removeHolder(S tracked) {
        globalContainer.removeHolder(tracked.getKey());
    }

    /**
     * Called when a chunk is loaded, allowing implementations to populate the tracker
     * with the tracked objects contained in that chunk.
     *
     * @param chunk the chunk that was loaded
     */
    public abstract void handleChunkLoad(Chunk chunk);

    /**
     * Called when a chunk is unloaded. Saves all tracked objects held in the chunk and
     * removes the chunk's container from its world container.
     *
     * @param chunk the chunk that was unloaded
     */
    public void handleChunkUnload(Chunk chunk) {
        saveChunk(chunk);

        WorldBlockContainer<S, K> container = globalContainer.getWorldContainer(chunk.getWorld());

        if (container == null) {
            return;
        }

        container.removeChunkContainer(chunk);
    }

    /**
     * Saves all tracked objects held within the given chunk by invoking {@link Tracked#onUnload()}
     * on each of them.
     *
     * @param chunk the chunk whose tracked objects should be saved
     */
    public void saveChunk(Chunk chunk) {
        saveChunk(chunk, holder -> {
            S tracked = holder.getData();

            tracked.onUnload();
        });
    }

    /**
     * Invokes the given callback for every holder tracked within the given chunk.
     * Does nothing if the chunk's world or chunk container isn't tracked.
     *
     * @param chunk the chunk whose holders should be visited
     * @param callback the action to perform for each holder in the chunk
     */
    public void saveChunk(Chunk chunk, Consumer<Holder<S, K>> callback) {
        World world = chunk.getWorld();
        ChunkKey key = ChunkKey.fromChunk(chunk);
        WorldBlockContainer<S, K> container = globalContainer.getWorldContainer(world);

        if (container == null) {
            return;
        }

        ChunkBlockContainer<S, K> chunkContainer = container.getChunkContainer(key);

        if (chunkContainer == null) {
            return;
        }

        for (Holder<S, K> holder : chunkContainer.getAllHolders()) {
            callback.accept(holder);
        }
    }
}