package net.paulem.arcana.regions;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * Represents the coordinates of a chunk, identified by its x and z chunk coordinates.
 * Provides utility methods for converting to and from chunks, locations, block coordinates,
 * and a compact packed-long representation.
 */
public class ChunkKey {
    private final int x;
    private final int z;

    private ChunkKey(int x, int z) {
        this.x = x;
        this.z = z;
    }

    /**
     * Creates a {@code ChunkKey} from the coordinates of the given chunk.
     *
     * @param chunk the chunk to create the key from
     * @return a new {@code ChunkKey} matching the chunk's coordinates
     */
    public static ChunkKey fromChunk(Chunk chunk) {
        return new ChunkKey(chunk.getX(), chunk.getZ());
    }

    /**
     * Creates a {@code ChunkKey} for the chunk containing the given location.
     *
     * @param location the location to derive the chunk coordinates from
     * @return a new {@code ChunkKey} for the chunk containing the location
     */
    public static ChunkKey fromLocation(Location location) {
        return new ChunkKey(location.getBlockX() >> 4, location.getBlockZ() >> 4);
    }

    /**
     * Creates a {@code ChunkKey} from block coordinates, converting them to chunk coordinates.
     *
     * @param x the block x coordinate
     * @param z the block z coordinate
     * @return a new {@code ChunkKey} for the chunk containing the given block coordinates
     */
    public static ChunkKey fromCoordinates(int x, int z) {
        return new ChunkKey(x >> 4, z >> 4);
    }

    /**
     * Creates a {@code ChunkKey} from a packed long representation, as produced by {@link #asLong()}.
     *
     * @param key the packed chunk coordinates
     * @return a new {@code ChunkKey} decoded from the packed value
     */
    public static ChunkKey fromLong(long key) {
        return new ChunkKey((int) (key >> 32), (int) (key & 0xFFFFFFFFL));
    }

    /**
     * @return the x coordinate of the chunk
     */
    public int getChunkX() {
        return x;
    }

    /**
     * @return the z coordinate of the chunk
     */
    public int getChunkZ() {
        return z;
    }

    /**
     * Checks whether this key refers to the same coordinates as the given chunk.
     *
     * @param chunk the chunk to compare against
     * @return {@code true} if the chunk has the same x and z coordinates as this key
     */
    public boolean isInChunk(Chunk chunk) {
        return chunk.getX() == x && chunk.getZ() == z;
    }

    /**
     * Creates a new {@code ChunkKey} offset from this one by the given amount of chunks.
     *
     * @param x the x offset, in chunks
     * @param z the z offset, in chunks
     * @return a new {@code ChunkKey} relative to this one
     */
    public ChunkKey getRelative(int x, int z) {
        return new ChunkKey(this.x + x, this.z + z);
    }

    /**
     * Computes the Manhattan distance, in chunks, between this key and another.
     *
     * @param other the other chunk key
     * @return the sum of the absolute differences of the x and z chunk coordinates
     */
    public int distanceTo(ChunkKey other) {
        return Math.abs(other.x - x) + Math.abs(other.z - z);
    }

    /**
     * @return the block x coordinate of this chunk's origin (its minimum x coordinate)
     */
    public int getBlockX() {
        return getChunkX() << 4;
    }

    /**
     * @return the block z coordinate of this chunk's origin (its minimum z coordinate)
     */
    public int getBlockZ() {
        return getChunkZ() << 4;
    }

    /**
     * Compares this key to another object for equality based on chunk x and z coordinates.
     *
     * @param o the object to compare against
     * @return {@code true} if the object is a {@code ChunkKey} with the same coordinates
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChunkKey chunkKey = (ChunkKey) o;
        return x == chunkKey.x && z == chunkKey.z;
    }

    /**
     * @return a hash code derived from the chunk x and z coordinates
     */
    @Override
    public int hashCode() {
        return 31 * x + z;
    }

    /**
     * Resolves this key to the actual {@link Chunk} in the given world.
     *
     * @param world the world to resolve the chunk in
     * @return the chunk at this key's coordinates in the given world
     */
    public Chunk toChunk(World world) {
        return world.getChunkAt(x, z);
    }

    /**
     * Packs this key's x and z coordinates into a single long value, suitable for
     * use as a compact map key or for reconstruction via {@link #fromLong(long)}.
     *
     * @return the packed representation of this chunk key
     */
    public long asLong() {
        return (long) x << 32 | (long) z & 0xFFFFFFFFL;
    }

    /**
     * @return a string representation of this chunk key, including its x and z coordinates
     */
    @Override
    public String toString() {
        return "ChunkKey{" +
                "x=" + x +
                ", z=" + z +
                '}';
    }
}