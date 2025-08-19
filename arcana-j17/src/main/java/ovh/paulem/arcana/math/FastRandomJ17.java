package ovh.paulem.arcana.math;

import java.io.Serial;
import java.io.Serializable;
import java.util.random.RandomGenerator;

/**
 * Java 17 extension of FastRandom that implements RandomGenerator and uses @Serial.
 */
public class FastRandomJ17 extends FastRandom implements RandomGenerator, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public FastRandomJ17() {
        super();
    }

    public FastRandomJ17(long seed) {
        super(seed);
    }
}
