package net.paulem.arcana.utils;

import org.bukkit.persistence.PersistentDataType;

/**
 * Utility methods for working with Bukkit's {@link PersistentDataType} system.
 */
public class PersistentDataUtils {
    /**
     * Resolves the built-in {@link PersistentDataType} that corresponds to the runtime type of the given value.
     * Supports {@link Byte}, {@link Short}, {@link Integer}, {@link Long}, {@link Float}, {@link Double},
     * {@link Boolean}, {@link String}, {@code byte[]}, {@code int[]} and {@code long[]}.
     *
     * @param constable a sample value whose type determines the returned {@link PersistentDataType}
     * @param <P> the primitive persistent data type
     * @param <C> the complex/runtime type of the value
     * @return the {@link PersistentDataType} matching the runtime type of {@code constable}
     * @throws IllegalArgumentException if {@code constable} is {@code null} or its type is not supported
     */
    public static <P, C> PersistentDataType<P, C> getCorrespondType(C constable) {
        if (constable == null) throw new IllegalArgumentException("Unsupported type: null");
        if (constable instanceof Byte) return (PersistentDataType<P, C>) PersistentDataType.BYTE;
        if (constable instanceof Short) return (PersistentDataType<P, C>) PersistentDataType.SHORT;
        if (constable instanceof Integer) return (PersistentDataType<P, C>) PersistentDataType.INTEGER;
        if (constable instanceof Long) return (PersistentDataType<P, C>) PersistentDataType.LONG;
        if (constable instanceof Float) return (PersistentDataType<P, C>) PersistentDataType.FLOAT;
        if (constable instanceof Double) return (PersistentDataType<P, C>) PersistentDataType.DOUBLE;
        if (constable instanceof Boolean) return (PersistentDataType<P, C>) PersistentDataType.BOOLEAN;
        if (constable instanceof String) return (PersistentDataType<P, C>) PersistentDataType.STRING;
        if (constable instanceof byte[]) return (PersistentDataType<P, C>) PersistentDataType.BYTE_ARRAY;
        if (constable instanceof int[]) return (PersistentDataType<P, C>) PersistentDataType.INTEGER_ARRAY;
        if (constable instanceof long[]) return (PersistentDataType<P, C>) PersistentDataType.LONG_ARRAY;
        throw new IllegalArgumentException("Unsupported type: " + constable.getClass().getName());
    }
}
