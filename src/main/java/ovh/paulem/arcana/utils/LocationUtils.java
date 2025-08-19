package ovh.paulem.arcana.utils;

import org.bukkit.Location;

public class LocationUtils {
    public static Location[] getIntermediary(Location base, Location end, double stepsInterval) {
        int steps = (int) Math.ceil((end.getY() - base.getY()) / stepsInterval);
        Location[] targetLocs = new Location[steps + 1];

        for (int i = 0; i <= steps; i++) {
            double y = base.getY() + i * 0.1;
            Location baseClone = base.clone();
            baseClone.setY(y);
            targetLocs[i] = baseClone;
        }

        return targetLocs;
    }
}
