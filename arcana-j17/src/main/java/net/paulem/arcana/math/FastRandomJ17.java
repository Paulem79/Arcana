package net.paulem.arcana.math;

import java.io.Serial;
import java.io.Serializable;
import java.util.random.RandomGenerator;

/**
 * Java 17 extension of {@link FastRandom} that implements the JDK's {@link RandomGenerator} interface,
 * so instances can be used anywhere a standard {@code RandomGenerator} is expected, and marks itself
 * with {@link Serial} for serialization.
 */
public class FastRandomJ17 extends FastRandom implements RandomGenerator, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance, seeded using {@link FastRandom}'s default seeding strategy.
     */
    public FastRandomJ17() {
        super();
    }

    /**
     * Creates a new instance seeded from the given seed value.
     * @param seed the seed used to initialize this generator
     */
    public FastRandomJ17(long seed) {
        super(seed);
    }
}
