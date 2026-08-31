package net.paulem.arcana.regions.container;

import net.paulem.arcana.regions.Holder;
import net.paulem.arcana.regions.Tracked;
import net.paulem.arcana.regions.WorldKey;

import java.util.Collection;

/**
 * Common accessor contract for containers that store {@link Holder} instances at
 * three-dimensional block coordinates, regardless of how they are internally organized.
 *
 * @param <S> the type of tracked entities being managed, which extends {@link Tracked}
 * @param <K> the type of the key associated with the tracked entities, which extends {@link WorldKey}
 */
public interface SubContainerHolderAccessor<S extends Tracked<K>, K extends WorldKey> {
    /**
     * Retrieves the tracked object at the given block coordinates.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @return the tracked object at the given coordinates, or {@code null} if none is present
     */
    S getHolder(int x, int y, int z);

    /**
     * Stores a tracked object at the given block coordinates.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @param holder the tracked object to store
     */
    void setHolder(int x, int y, int z, S holder);

    /**
     * Removes the tracked object at the given block coordinates, if any.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    void removeHolder(int x, int y, int z);

    /**
     * @return a collection of all holders currently stored in this container
     */
    Collection<Holder<S, K>> getAllHolders();
}
