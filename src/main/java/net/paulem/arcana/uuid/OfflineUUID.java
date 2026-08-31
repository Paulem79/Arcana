package net.paulem.arcana.uuid;

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

    /**
     * Creates an {@link OfflineUUID} for the given player, based on their name.
     *
     * @param player the player to create the offline UUID for
     * @return an {@link OfflineUUID} instance for {@code player}'s name
     */
    public static OfflineUUID of(Player player) {
        return of(player.getName());
    }

    /**
     * Creates an {@link OfflineUUID} for the given player name.
     *
     * @param name the player name to create the offline UUID for
     * @return an {@link OfflineUUID} instance for {@code name}
     */
    public static OfflineUUID of(String name) {
        return new OfflineUUID(name);
    }

    /**
     * Computes the offline-mode UUID for the given player, based on their name.
     *
     * @param player the player to compute the offline UUID for
     * @return the offline UUID derived from {@code player}'s name
     */
    public static UUID get(Player player) {
        return of(player).get();
    }

    /**
     * Computes the offline-mode UUID for the given player name.
     *
     * @param name the player name to compute the offline UUID for
     * @return the offline UUID derived from {@code name}
     */
    public static UUID get(String name) {
        return of(name).get();
    }

    /**
     * Computes the offline-mode UUID derived from this instance's player name,
     * the same way vanilla Minecraft derives UUIDs for offline-mode servers.
     *
     * @return the offline UUID derived from the player name
     */
    public UUID get() {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(StandardCharsets.UTF_8));
    }
}