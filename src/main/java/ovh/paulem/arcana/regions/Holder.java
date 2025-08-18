package ovh.paulem.arcana.regions;

import lombok.Getter;

/**
 * Represents a holder that encapsulates spatial coordinates (x, y, z) and a data object of type T.
 *
 * @param <T> the type of the data object, which must implement the {@link Tracked} interface
 * @param <K> the type of the key associated with the tracked object
 */
@Getter
public final class Holder<T extends Tracked<K>, K> {
    private final int x, y, z;
    private final T data;

    public Holder(int x, int y, int z, T data) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.data = data;
    }
}