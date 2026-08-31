package net.paulem.arcana.utils;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility methods for working with blocks and locations, such as enumerating
 * nearby locations, walking between two points, and converting block faces to vectors.
 */
public class BlockUtils {
    /**
     * Returns every location inside the cuboid centered on {@code center}, extending
     * {@code radiusX}, {@code radiusY} and {@code radiusZ} blocks on each axis (inclusive).
     * The center location itself is included in the result.
     *
     * @param center the center of the cuboid
     * @param radiusX the radius on the X axis
     * @param radiusY the radius on the Y axis
     * @param radiusZ the radius on the Z axis
     * @return the list of locations contained in the cuboid
     */
    public static List<Location> getBlocksAround(Location center, int radiusX, int radiusY, int radiusZ) {
        List<Location> locations = new ArrayList<>();
        for (int x = -radiusX; x <= radiusX; x++) {
            for (int y = -radiusY; y <= radiusY; y++) {
                for (int z = -radiusZ; z <= radiusZ; z++) {
                    Location pos = center.clone().add(x, y, z);
                    locations.add(pos);
                }
            }
        }
        return locations;
    }

    /**
     * Walks from {@code loc1} towards {@code loc2} and collects the blocks encountered along the way.
     * The straight-line distance between the two locations is split into unit-length steps, and the
     * block at each step is added to the result until the block at {@code loc2} is reached or the
     * computed number of steps is exhausted. If both locations are at the same position, a single-element
     * list containing that block is returned.
     *
     * @param loc1 the starting location
     * @param loc2 the ending location
     * @return the list of blocks encountered while walking from {@code loc1} to {@code loc2}
     */
    public static List<Block> getBlocksBetween(Location loc1, Location loc2) {
        List<Block> blocks = new ArrayList<>();
        int distance = (int) Math.ceil(loc1.distance(loc2));

        if (distance == 0) {
            blocks.add(loc1.getBlock());
            return blocks;
        }

        Vector direction = loc2.toVector().subtract(loc1.toVector());
        direction.setX(Math.ceil((direction.getX() / distance) * 100D) / 100D).setY(direction.getY() / distance).setZ(Math.ceil((direction.getZ() / distance) * 100D) / 100D);
        Vector vec1 = loc1.toVector();
        Block b = vec1.toLocation(loc1.getWorld()).getBlock();
        blocks.add(b);
        for (int x = 0; x <= distance; x++) {
            if (b.equals(loc2.getBlock())) {
                break;
            }
            b = vec1.add(direction).toLocation(loc1.getWorld()).getBlock();
            blocks.add(b);
        }
        return blocks;
    }

    /**
     * Converts a {@link BlockFace} to a unit vector pointing in that direction.
     *
     * @param face the block face to convert
     * @return a vector of length 1 pointing towards {@code face}
     */
    public static Vector faceToVector(BlockFace face) {
        return faceToVector(face, 1);
    }

    /**
     * Converts a {@link BlockFace} to a vector pointing in that direction, scaled by {@code distance}.
     *
     * @param face the block face to convert
     * @param distance the length to scale the resulting vector to
     * @return a vector pointing towards {@code face}, scaled by {@code distance}
     */
    public static Vector faceToVector(BlockFace face, int distance) {
        return new Vector(face.getModX() * distance, face.getModY() * distance, face.getModZ() * distance);
    }
}
