package ovh.paulem.arcana.uuid;

import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Represents an offline UUID for a player based on their name.
 * This is useful if you want datas linked with uuid to stay correct even if the server switches from online to offline mode.
 * @author Paulem
 */
public class OfflineUUID {
    private final String name;

    private OfflineUUID(String name) {
        this.name = name;
    }

    public static OfflineUUID of(Player player) {
        return of(player.getName());
    }

    public static OfflineUUID of(String name) {
        return new OfflineUUID(name);
    }

    public static UUID get(Player player) {
        return of(player).get();
    }

    public static UUID get(String name) {
        return of(name).get();
    }

    public UUID get() {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(StandardCharsets.UTF_8));
    }
}