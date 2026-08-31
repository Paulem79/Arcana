package net.paulem.arcana.utils;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Utility methods for comparing the unique identifiers of entities and UUIDs.
 */
public class UUIDUtils {
    /**
     * Checks whether two entities share the same unique identifier.
     *
     * @param entity the first entity, may be {@code null}
     * @param entity1 the second entity, may be {@code null}
     * @return {@code true} if both entities are non-null and have the same UUID, {@code false} otherwise
     */
    public static boolean areEquals(@Nullable Entity entity, @Nullable Entity entity1) {
        return areEquals(entity == null ? null : entity.getUniqueId(), entity1 == null ? null : entity1.getUniqueId());
    }

    /**
     * Checks whether an entity's unique identifier matches the given UUID.
     *
     * @param entity the entity, may be {@code null}
     * @param uuid1 the UUID to compare against, may be {@code null}
     * @return {@code true} if both are non-null and the entity's UUID equals {@code uuid1}, {@code false} otherwise
     */
    public static boolean areEquals(@Nullable Entity entity, @Nullable UUID uuid1) {
        return areEquals(entity == null ? null : entity.getUniqueId(), uuid1);
    }

    /**
     * Checks whether a UUID matches an entity's unique identifier.
     *
     * @param uuid the UUID to compare against, may be {@code null}
     * @param entity1 the entity, may be {@code null}
     * @return {@code true} if both are non-null and {@code uuid} equals the entity's UUID, {@code false} otherwise
     */
    public static boolean areEquals(@Nullable UUID uuid, @Nullable Entity entity1) {
        return areEquals(uuid, entity1 == null ? null : entity1.getUniqueId());
    }

    /**
     * Checks whether two UUIDs are equal.
     *
     * @param uuid the first UUID, may be {@code null}
     * @param uuid1 the second UUID, may be {@code null}
     * @return {@code true} if both UUIDs are non-null and equal, {@code false} otherwise
     */
    public static boolean areEquals(@Nullable UUID uuid, @Nullable UUID uuid1) {
        if (uuid == null || uuid1 == null) return false;

        return uuid.equals(uuid1);
    }
}
