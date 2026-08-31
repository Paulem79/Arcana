package net.paulem.arcana.math;

import lombok.Getter;

/**
 * A precomputed lookup table for {@link Math#acos(double)}, used to approximate the arc cosine faster than
 * calling {@code Math.acos} directly. Values are cached at a resolution of 0.001 over the [-1.0, 1.0] domain,
 * so results are approximate rather than exact.
 */
public class ACosTable {
    @Getter
    private static final ACosTable table = new ACosTable();
    private final double[] acos = new double[2001];

    private ACosTable() {
        // Précalcule les valeurs de Math.acos pour x de -1.0 à 1.0 par pas de 0.001
        for (int i = 0; i <= 2000; i++) {
            double x = (i - 1000) / 1000.0;
            acos[i] = Math.acos(x);
        }
    }

    /**
     * Returns the approximate arc cosine of the given value, read from the precomputed table.
     * The input is clamped to the [-1.0, 1.0] range before being rounded to the nearest cached step of 0.001.
     *
     * @param x the value whose arc cosine is sought; values outside [-1.0, 1.0] are clamped
     * @return the approximate arc cosine of {@code x}, in radians
     */
    public double getAcos(double x) {
        if (x < -1.0) x = -1.0;
        if (x > 1.0) x = 1.0;
        int index = (int) Math.round((x + 1.0) * 1000);
        return acos[index];
    }
}