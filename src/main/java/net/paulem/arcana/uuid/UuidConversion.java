package net.paulem.arcana.uuid;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * Utility methods for converting {@link UUID}s to and from their raw 16-byte representation.
 */
public class UuidConversion {
    /**
     * Reconstructs a {@link UUID} from a 16-byte array, as produced by {@link #asBytes(UUID)}.
     * The first 8 bytes are read as the most significant bits and the last 8 as the least significant bits.
     *
     * @param bytes the 16-byte array to convert
     * @return the {@link UUID} represented by {@code bytes}
     */
    public static UUID asUuid(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        long firstLong = bb.getLong();
        long secondLong = bb.getLong();
        return new UUID(firstLong, secondLong);
    }

    /**
     * Converts a {@link UUID} to its raw 16-byte representation, with the most significant bits
     * written first, followed by the least significant bits.
     *
     * @param uuid the UUID to convert
     * @return the 16-byte array representing {@code uuid}
     */
    public static byte[] asBytes(UUID uuid) {
        ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }
}