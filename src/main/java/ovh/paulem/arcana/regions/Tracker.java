package ovh.paulem.arcana.regions;

import lombok.Getter;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;
import ovh.paulem.arcana.regions.container.ChunkBlockContainer;
import ovh.paulem.arcana.regions.container.GlobalBlockContainer;
import ovh.paulem.arcana.regions.container.WorldBlockContainer;

import java.util.function.Consumer;

/**
 * A class to extend that tracks all the specified S type in the server.
 * @param <S> the type of the object to track
 */
public abstract class Tracker<S extends Tracked<K>, K extends WorldKey> {
    @Getter
    private final GlobalBlockContainer<S, K> globalContainer = GlobalBlockContainer.of();

    public Tracker() {
    }

    @Nullable
    public S getHolderAt(K key) {
        return globalContainer.getHolder(key);
    }

    public void registerHolder(S tracked) {
        globalContainer.setHolder(tracked.getKey(), tracked);
    }

    public void removeHolder(S tracked) {
        globalContainer.removeHolder(tracked.getKey());
    }

    public abstract void handleChunkLoad(Chunk chunk);

    public void handleChunkUnload(Chunk chunk) {
        saveChunk(chunk);

        WorldBlockContainer<S, K> container = globalContainer.getWorldContainer(chunk.getWorld());

        if (container == null) {
            return;
        }

        container.removeChunkContainer(chunk);
    }

    public void saveChunk(Chunk chunk) {
        saveChunk(chunk, holder -> {
            S tracked = holder.getData();

            tracked.onUnload();
        });
    }

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
            S tracked = holder.getData();
            save(tracked);

            callback.accept(holder);
        }
    }

    private void save(S tracked) {
        tracked.onUnload();
    }
}