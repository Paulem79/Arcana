package net.paulem.arcana.registry;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import net.paulem.arcana.utils.MapUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * A registry that can be frozen, preventing any further registrations.
 * This is useful for registries that should not change after a certain point.
 *
 * @param <T> The type of objects stored in the registry, which must implement {@link RegistryKey}.
 * @param <K> The key type used to identify objects in the registry.
 * @author Paulem<br>
 * Based on code by Miles Holder and The-Epic
 */
public class NewFrozenRegistry<T extends RegistryKey<K>, K> extends WriteableRegistry<T, K> {
    private boolean frozen = false;

    /**
     * Creates a new, unfrozen registry, copying the entries from the map produced by the given supplier.
     *
     * @param registrySupplier supplies the map whose entries are copied into this registry
     */
    public NewFrozenRegistry(final Supplier<Map<K, T>> registrySupplier) {
        super(() -> MapUtils.copyOf(registrySupplier.get()));
    }

    /**
     * Creates a new, empty, unfrozen registry backed by a {@link HashMap}.
     */
    public NewFrozenRegistry() {
        super(HashMap::new);
    }

    /**
     * Freezes this registry, preventing any further registrations.
     *
     * @throws IllegalStateException if the registry is already frozen
     */
    public void freeze() {
        if (frozen) {
            throw new IllegalStateException("Registry is already frozen.");
        }

        frozen = true;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException if this registry has already been frozen via {@link #freeze()}
     */
    @Override
    public boolean register(@NotNull T object) {
        Preconditions.checkState(!frozen, "Cannot register new objects to the frozen registry " + getClass().getSimpleName());

        return super.register(object);
    }
}
