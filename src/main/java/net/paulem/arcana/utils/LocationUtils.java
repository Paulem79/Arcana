package net.paulem.arcana.utils;

import org.bukkit.Location;

/**
 * Utility methods for working with locations.
 */
public class LocationUtils {
    /**
     * Computes a series of locations between {@code base} and {@code end}, evenly spaced along the Y axis
     * by {@code stepsInterval} blocks. The X and Z coordinates of {@code base} are kept for every intermediary
     * location. Works whether {@code end} is above or below {@code base}: the step direction is chosen based
     * on the sign of the Y difference, and the number of steps is derived from the absolute Y distance divided
     * by {@code stepsInterval}, rounded up. The returned array always starts at {@code base}'s Y and ends at
     * or past {@code end}'s Y.
     *
     * @param base the starting location
     * @param end the ending location
     * @param stepsInterval the vertical distance, in blocks, between each intermediary location
     * @return an array of locations from {@code base} to {@code end}, spaced by {@code stepsInterval} on the Y axis
     */
    public static Location[] getIntermediary(Location base, Location end, double stepsInterval) {
        double deltaY = end.getY() - base.getY();
        int steps = (int) Math.ceil(Math.abs(deltaY) / stepsInterval);
        double signedInterval = deltaY < 0 ? -stepsInterval : stepsInterval;
        Location[] targetLocs = new Location[steps + 1];

        for (int i = 0; i <= steps; i++) {
            double y = base.getY() + i * signedInterval;
            Location baseClone = base.clone();
            baseClone.setY(y);
            targetLocs[i] = baseClone;
        }

        return targetLocs;
    }
}
