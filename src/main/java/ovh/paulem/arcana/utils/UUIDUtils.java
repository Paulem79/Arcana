package ovh.paulem.arcana.utils;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class UUIDUtils {
    public static boolean areEquals(@Nullable Entity entity, @Nullable Entity entity1) {
        return areEquals(entity == null ? null : entity.getUniqueId(), entity1 == null ? null : entity1.getUniqueId());
    }

    public static boolean areEquals(@Nullable Entity entity, @Nullable UUID uuid1) {
        return areEquals(entity == null ? null : entity.getUniqueId(), uuid1);
    }

    public static boolean areEquals(@Nullable UUID uuid, @Nullable Entity entity1) {
        return areEquals(uuid, entity1 == null ? null : entity1.getUniqueId());
    }

    public static boolean areEquals(@Nullable UUID uuid, @Nullable UUID uuid1) {
        if (uuid == null || uuid1 == null) return false;

        return uuid.equals(uuid1);
    }
}
