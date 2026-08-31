package net.paulem.arcana.utils;

/**
 * Utility methods for converting Minecraft server ticks to time units.
 */
public class TickUtils {
    /**
     * Converts a number of server ticks to whole seconds, assuming 20 ticks per second.
     *
     * @param ticks the number of ticks
     * @return the equivalent number of seconds, rounded to the nearest whole number
     */
    public static long toSecond(long ticks) {
        return Math.round((float) ticks / 20);
    }

    /**
     * Converts a number of server ticks to milliseconds, assuming 20 ticks per second (50ms per tick).
     *
     * @param ticks the number of ticks
     * @return the equivalent number of milliseconds
     */
    public static long toMilliseconds(long ticks) {
        return Math.round((float) ticks * 50);
    }
}
