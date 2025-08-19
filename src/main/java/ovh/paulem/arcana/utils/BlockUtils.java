package ovh.paulem.arcana.utils;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class BlockUtils {
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

    public static List<Block> getBlocksBetween(Location loc1, Location loc2) {
        List<Block> blocks = new ArrayList<>();
        int distance = (int) Math.ceil(loc1.distance(loc2));
        Vector direction = loc2.toVector().subtract(loc1.toVector());
        direction.setX(Math.ceil((direction.getX() / distance) * 100D) / 100D).setY(direction.getY() / distance).setZ(Math.ceil((direction.getZ() / distance) * 100D) / 100D);
        Vector vec1 = loc1.toVector();
        for (int x = 0; x <= distance; x++) {
            Block b = vec1.add(direction).toLocation(loc1.getWorld()).getBlock();
            blocks.add(b);
            if (b.equals(loc2.getBlock())) {
                break;
            }
        }
        return blocks;
    }

    public static Vector faceToVector(BlockFace face) {
        return faceToVector(face, 1);
    }

    public static Vector faceToVector(BlockFace face, int distance) {
        return new Vector(face.getModX() * distance, face.getModY() * distance, face.getModZ() * distance);
    }
}
