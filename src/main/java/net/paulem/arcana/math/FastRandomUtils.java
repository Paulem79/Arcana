package net.paulem.arcana.math;

import org.apache.commons.lang3.Validate;

/**
 * Utility class for generating random values using {@link FastRandom}.<br>
 * This class provides methods for generating random booleans, bytes, integers, longs, doubles, and floats
 * with optional range constraints and probability checks.<br>
 * Because {@link FastRandom} is not thread-safe, this class keeps one {@link FastRandom} instance per thread
 * in a {@link ThreadLocal}, so all static methods here can safely be called concurrently from multiple threads.
 */
public class FastRandomUtils {
    // FastRandom is explicitly not thread-safe; give every thread its own instance
    // instead of sharing mutable RNG state across threads.
    private static final ThreadLocal<FastRandom> RANDOM_HOLDER = ThreadLocal.withInitial(FastRandom::of);

    /**
     * Returns the {@link FastRandom} instance for the calling thread, creating one on first use.
     * @return the calling thread's {@link FastRandom} instance
     */
    private static FastRandom random() {
        return RANDOM_HOLDER.get();
    }

    /// START PROBABILITY

    /**
     * Determines if an event occurs based on a given probability (float).<br>
     * This method is less precise than {@link FastRandomUtils#doubleProbability}, but consumes less memory
     *
     * @param probability the probability of the event occurring (0.0 to 1.0)
     * @return true if the event occurs, false otherwise
     * @throws IllegalArgumentException if the probability is negative or greater than 1
     */
    public static boolean floatProbability(float probability) {
        Validate.isTrue(probability >= 0, "Probability cannot be negative.");
        Validate.isTrue(probability <= 1, "Probability cannot be greater than 1.");

        return random().nextFloat() < probability;
    }

    /**
     * Determines if an event occurs based on a given probability (double).
     *
     * @param probability the probability of the event occurring (0.0 to 1.0)
     * @return true if the event occurs, false otherwise
     * @throws IllegalArgumentException if the probability is negative or greater than 1
     */
    public static boolean doubleProbability(double probability) {
        Validate.isTrue(probability >= 0, "Probability cannot be negative.");
        Validate.isTrue(probability <= 1, "Probability cannot be greater than 1.");

        return random().nextDouble() < probability;
    }

    /// END PROBABILITY

    /**
     * Generates a random boolean value.<br>
     * Can be used to simulate a coin flip or other random boolean events.
     *
     * @return a random boolean
     */
    public static boolean nextBoolean() {
        return random().nextBoolean();
    }

    /**
     * Generates an array of random bytes.
     *
     * @param count the number of random bytes to generate
     * @return an array of random bytes
     * @throws IllegalArgumentException if the count is negative
     */
    public static byte[] nextBytes(int count) {
        Validate.isTrue(count >= 0, "Count cannot be negative.");
        byte[] result = new byte[count];
        random().nextBytes(result);
        return result;
    }

    /**
     * Generates a random integer within a specified range.
     *
     * @param startInclusive the inclusive lower bound of the range
     * @param endExclusive   the exclusive upper bound of the range
     * @return a random integer within the range
     * @throws IllegalArgumentException if the range is invalid or negative
     */
    public static int nextInt(int startInclusive, int endExclusive) {
        Validate.isTrue(endExclusive >= startInclusive, "Start value must be smaller or equal to end value.");
        Validate.isTrue(startInclusive >= 0, "Both range values must be non-negative.");
        return startInclusive == endExclusive ? startInclusive : startInclusive + random().nextInt(endExclusive - startInclusive);
    }

    /**
     * Generates a random integer within the range [0, Integer.MAX_VALUE).
     *
     * @return a random integer
     */
    public static int nextInt() {
        return nextInt(0, Integer.MAX_VALUE);
    }

    /**
     * Generates a random long within a specified range.
     *
     * @param startInclusive the inclusive lower bound of the range
     * @param endExclusive   the exclusive upper bound of the range
     * @return a random long within the range
     * @throws IllegalArgumentException if the range is invalid or negative
     */
    public static long nextLong(long startInclusive, long endExclusive) {
        Validate.isTrue(endExclusive >= startInclusive, "Start value must be smaller or equal to end value.");
        Validate.isTrue(startInclusive >= 0L, "Both range values must be non-negative.");
        return startInclusive == endExclusive ? startInclusive : startInclusive + nextLong(endExclusive - startInclusive);
    }

    /**
     * Generates a random long within the range [0, Long.MAX_VALUE).
     *
     * @return a random long
     */
    public static long nextLong() {
        return nextLong(Long.MAX_VALUE);
    }

    /**
     * Generates a random long within the range [0, n).
     *
     * @param n the exclusive upper bound of the range
     * @return a random long within the range
     */
    private static long nextLong(long n) {
        long bits;
        long val;
        do {
            bits = random().nextLong() >>> 1;
            val = bits % n;
        } while (bits - val + (n - 1L) < 0L);

        return val;
    }

    /**
     * Generates a random double within a specified range.
     *
     * @param startInclusive the inclusive lower bound of the range
     * @param endExclusive   the exclusive upper bound of the range
     * @return a random double within the range
     * @throws IllegalArgumentException if the range is invalid or negative
     */
    public static double nextDouble(double startInclusive, double endExclusive) {
        Validate.isTrue(endExclusive >= startInclusive, "Start value must be smaller or equal to end value.");
        Validate.isTrue(startInclusive >= (double) 0.0F, "Both range values must be non-negative.");
        return startInclusive == endExclusive ? startInclusive : startInclusive + (endExclusive - startInclusive) * random().nextDouble();
    }

    /**
     * Generates a random double within the range [0.0, Double.MAX_VALUE).
     *
     * @return a random double
     */
    public static double nextDouble() {
        return nextDouble(0.0F, Double.MAX_VALUE);
    }

    /**
     * Generates a random float within a specified range.
     *
     * @param startInclusive the inclusive lower bound of the range
     * @param endExclusive   the exclusive upper bound of the range
     * @return a random float within the range
     * @throws IllegalArgumentException if the range is invalid or negative
     */
    public static float nextFloat(float startInclusive, float endExclusive) {
        Validate.isTrue(endExclusive >= startInclusive, "Start value must be smaller or equal to end value.");
        Validate.isTrue(startInclusive >= 0.0F, "Both range values must be non-negative.");
        return startInclusive == endExclusive ? startInclusive : startInclusive + (endExclusive - startInclusive) * random().nextFloat();
    }

    /**
     * Generates a random float within the range [0.0, Float.MAX_VALUE).
     *
     * @return a random float
     */
    public static float nextFloat() {
        return nextFloat(0.0F, Float.MAX_VALUE);
    }
}