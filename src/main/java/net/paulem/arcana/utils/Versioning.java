package net.paulem.arcana.utils;

import org.bukkit.Bukkit;

/**
 * Utility methods for detecting the running server's Minecraft version and server software.
 */
public class Versioning {
    private static String[] getMcParts() {
        if (mcParts != null) return mcParts;

        String version = Bukkit.getVersion();
        String[] parts = version.substring(version.indexOf("MC: ") + 4, version.length() - 1).split("\\.");

        // 1.21 is 1.21.0
        if (parts.length < 3) {
            parts = new String[]{parts[0], parts[1], "0"};
        }

        return parts;
    }    private static final String[] mcParts = getMcParts();

    /**
     * Checks whether the running server's Minecraft minor version is at least {@code v.1}
     * (i.e. strictly after minor version {@code v}, or minor version {@code v} with a patch of 1 or higher).
     *
     * @param v the minor version to compare against
     * @return {@code true} if the server's Minecraft version is at or after {@code v.1}
     */
    public static boolean isPost(int v) {
        String[] mcParts = getMcParts();
        return Integer.parseInt(mcParts[1]) > v || (Integer.parseInt(mcParts[1]) == v && Integer.parseInt(mcParts[2]) >= 1);
    }

    /**
     * Checks whether the running server's Minecraft version is strictly after {@code v.r}.
     *
     * @param v the minor version to compare against
     * @param r the patch version to compare against
     * @return {@code true} if the server's Minecraft version is strictly after {@code v.r}
     */
    public static boolean isPost(int v, int r) {
        String[] mcParts = getMcParts();
        return Integer.parseInt(mcParts[1]) > v || (Integer.parseInt(mcParts[1]) == v && Integer.parseInt(mcParts[2]) > r);
    }

    /**
     * Checks whether the server is running Paper (or a fork of it), by testing for the
     * presence of a Paper-specific class on the classpath.
     *
     * @return {@code true} if the server is running Paper or a Paper fork, {@code false} otherwise
     */
    public static boolean isPaper() {
        try {
            Class.forName("com.destroystokyo.paper.ParticleBuilder");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }


}
