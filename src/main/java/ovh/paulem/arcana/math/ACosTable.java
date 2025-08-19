package ovh.paulem.arcana.math;

import lombok.Getter;

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

    public double getAcos(double x) {
        if (x < -1.0) x = -1.0;
        if (x > 1.0) x = 1.0;
        int index = (int) Math.round((x + 1.0) * 1000);
        return acos[index];
    }
}