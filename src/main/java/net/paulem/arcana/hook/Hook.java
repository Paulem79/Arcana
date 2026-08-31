package net.paulem.arcana.hook;

import lombok.AccessLevel;
import lombok.Getter;
import net.paulem.arcana.ArcanaAPI;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * Base class for hooks resolving an optional third-party implementation of type {@code H}, chosen based on
 * a key of type {@code K} (typically derived from what's currently available on the server), through a
 * {@link HookCondition} mapping keys to lazy suppliers of the implementation.
 *
 * @param <H> the type of the object returned by the hook once resolved
 * @param <K> the type of the key used to select which supplier to resolve the hook from
 */
public abstract class Hook<H, K> {
    @Getter(value = AccessLevel.PROTECTED)
    private final ArcanaAPI<?> api;
    private final HookCondition<K, Supplier<H>> hooks;

    /**
     * Creates a new Hook.
     * @param api the ArcanaAPI instance this hook belongs to
     * @param hooks the condition mapping keys to suppliers of the hooked implementation
     */
    public Hook(ArcanaAPI<?> api, HookCondition<K, Supplier<H>> hooks) {
        this.api = api;
        this.hooks = hooks;
    }

    /**
     * Resolves the hooked implementation for the key produced by the given supplier.
     * If no supplier is registered for that key, this returns {@code null} rather than throwing.
     *
     * @param supplier supplies the key used to look up the registered hook supplier
     * @return the resolved implementation, or {@code null} if no hook is registered for the key
     */
    @Nullable
    public H get(Supplier<K> supplier) {
        Supplier<H> hookSupplier = hooks.get(supplier.get());
        return hookSupplier == null ? null : hookSupplier.get();
    }
}
