package net.paulem.arcana.utils;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility methods for working with entities, such as finding nearby entities,
 * checking line-of-sight, and teleporting to the ground.
 */
public class EntityUtils {

    /**
     * Finds the closest entity of type {@code filterClass} to {@code entity} within {@code dist} blocks.
     * The entity itself is never returned as its own nearest entity.
     *
     * @param entity the entity to search around
     * @param dist the search radius, in blocks, on each axis
     * @param filterClass the type of entity to look for
     * @param <T> the type of entity to look for
     * @return the nearest matching entity, or {@code null} if none was found
     */
    @Nullable
    public static <T extends Entity> T getNearestEntity(@NotNull Entity entity, double dist, Class<T> filterClass) {
        List<T> entities = entity.getNearbyEntities(dist, dist, dist)
                .stream()
                .filter(entity1 -> filterClass.isInstance(entity1) && !UUIDUtils.areEquals(entity1, entity))
                .map(entity1 -> (T) entity1)
                .collect(Collectors.toList());

        if (entities.isEmpty()) {
            return null;
        }

        entities.sort(Comparator.comparingDouble(o -> o.getLocation().distanceSquared(entity.getLocation())));

        return entities.get(0);
    }

    /**
     * Finds all living entities within {@code dist} blocks of {@code player} that the player is currently looking at.
     *
     * @param player the player whose look direction is checked
     * @param dist the search radius, in blocks, on each axis
     * @return the list of living entities the player is looking at
     */
    public static List<LivingEntity> getEntitiesTargeting(@NotNull Player player, double dist) {
        return player.getNearbyEntities(dist, dist, dist)
                .stream()
                .filter(entity -> entity instanceof LivingEntity && isLookingAt(player, (LivingEntity) entity))
                .map(entity -> (LivingEntity) entity)
                .collect(Collectors.toList());
    }

    /**
     * Checks whether {@code player} is looking at {@code livingEntity}.
     * The direction from the player's eye to the entity is compared to the player's look direction
     * using a dot product; the resulting angle is then compared to the angular size that the entity's
     * bounding box occupies at that distance, so that larger or closer entities are easier to be looking at.
     * An entity occupying the exact same position as the player's eye is always considered looked at.
     *
     * @param player the player whose look direction is checked
     * @param livingEntity the entity to check
     * @return {@code true} if the player is looking at the entity, {@code false} otherwise
     */
    public static boolean isLookingAt(Player player, LivingEntity livingEntity) {
        Location eye = player.getEyeLocation();
        Vector toEntity = livingEntity.getLocation().toVector().subtract(eye.toVector());
        double distance = toEntity.length();

        if (distance == 0) {
            return true;
        }

        double dot = toEntity.normalize().dot(eye.getDirection().normalize());

        if (dot <= 0) {
            return false;
        }

        Vector boundingBox = getSize(livingEntity.getBoundingBox());
        double maxSize = Math.max(boundingBox.getX(), Math.max(boundingBox.getY(), boundingBox.getZ()));
        double angularSize = Math.atan2(maxSize, distance);
        double angle = Math.acos(Math.min(1.0, dot));

        return angle <= angularSize;
    }

    private static Vector getSize(BoundingBox boundingBox) {
        return new Vector(boundingBox.getWidthX(), boundingBox.getHeight(), boundingBox.getWidthZ());
    }

    /**
     * Teleports the given entity down to the ground below its current location.
     *
     * @param entity the entity to teleport
     */
    public static void tpToGround(@NotNull Entity entity) {
        Location location = getBottomGround(entity.getLocation());

        entity.teleport(location);
    }

    /**
     * Finds the location of the first solid ground below the given location, scanning downward
     * from its current Y position to the world's minimum height. If no solid block is found,
     * the world's highest block at that column is used instead.
     *
     * @param location the location to scan downward from
     * @return the location just above the first solid block found below {@code location}
     */
    public static Location getBottomGround(Location location) {
        int minHeight = location.getWorld().getMinHeight();

        for (int y = location.getBlockY(); y >= minHeight; y--) {
            if (!location.getWorld().getBlockAt(location.getBlockX(), y, location.getBlockZ()).isEmpty()) {
                Location clone = location.clone();
                clone.setY(y + 1);

                return clone;
            }
        }

        return location.getWorld().getHighestBlockAt(location).getLocation();
    }
}
