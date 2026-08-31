package net.paulem.arcana.regions;

import org.bukkit.World;

/**
 * Represents a key that locates a tracked object by its world and block coordinates.
 */
public interface WorldKey {
    /**
     * @return the world this key is located in
     */
    World getWorld();

    /**
     * @return the x block coordinate
     */
    int getX();

    /**
     * @return the y block coordinate
     */
    int getY();

    /**
     * @return the z block coordinate
     */
    int getZ();
}
