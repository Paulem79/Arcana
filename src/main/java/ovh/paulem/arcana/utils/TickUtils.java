package ovh.paulem.arcana.utils;

public class TickUtils {
    public static long toSecond(long ticks) {
        return Math.round((float) ticks / 20);
    }

    public static long toMilliseconds(long ticks) {
        return Math.round((float) ticks * 50);
    }
}
