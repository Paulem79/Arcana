package ovh.paulem.arcana.utils;

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

public class EntityUtils {
    public static boolean areEquals(@Nullable Entity entity, @Nullable Entity entity1) {
        if (entity == null || entity1 == null) return false;

        return entity.getUniqueId().equals(entity1.getUniqueId());
    }

    @Nullable
    public static <T extends Entity> T getNearestEntity(@NotNull Entity entity, double dist, Class<T> filterClass) {
        List<T> entities = entity.getNearbyEntities(dist, dist, dist)
                .stream()
                .filter(entity1 -> filterClass.isInstance(entity1) && !areEquals(entity1, entity))
                .map(entity1 -> (T) entity1)
                .collect(Collectors.toList());

        if (entities.isEmpty()) {
            return null;
        }

        entities.sort(Comparator.comparingDouble(o -> o.getLocation().distanceSquared(entity.getLocation())));

        return entities.get(0);
    }

    public static List<LivingEntity> getEntitiesTargeting(@NotNull Player player, double dist) {
        return player.getNearbyEntities(dist, dist, dist)
                .stream()
                .filter(entity -> entity instanceof LivingEntity && isLookingAt(player, (LivingEntity) entity))
                .map(entity -> (LivingEntity) entity)
                .collect(Collectors.toList());
    }

    public static boolean isLookingAt(Player player, LivingEntity livingEntity) {
        Location eye = player.getEyeLocation();
        Vector toEntity = livingEntity.getLocation().toVector().subtract(eye.toVector());
        Vector crossProduct = toEntity.normalize().crossProduct(eye.getDirection());

        Vector boundingBox = getSize(livingEntity.getBoundingBox());

        return crossProduct.getX() < boundingBox.getX() && crossProduct.getY() < boundingBox.getY() && crossProduct.getZ() < boundingBox.getZ();
    }

    private static Vector getSize(BoundingBox boundingBox) {
        return new Vector(boundingBox.getWidthX(), boundingBox.getHeight(), boundingBox.getWidthZ());
    }

    public static void tpToGround(@NotNull Entity entity) {
        Location location = getBottomGround(entity.getLocation());

        entity.teleport(location);
    }

    public static Location getBottomGround(Location location) {
        for (int y = location.getBlockY(); y >= 0; y--) {
            if (!location.getWorld().getBlockAt(location.getBlockX(), y, location.getBlockZ()).isEmpty()) {
                Location clone = location.clone();
                clone.setY(y + 1);

                return clone;
            }
        }

        return location.getWorld().getHighestBlockAt(location).getLocation();
    }
}
