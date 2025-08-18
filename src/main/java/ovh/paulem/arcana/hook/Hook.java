package ovh.paulem.arcana.hook;

import lombok.AccessLevel;
import lombok.Getter;
import ovh.paulem.arcana.ArcanaAPI;

import java.util.Map;
import java.util.function.Supplier;

public abstract class Hook<H extends Hook<H, C, T>, C, T> {
    @Getter(value = AccessLevel.PROTECTED)
    private final ArcanaAPI<?> api;
    private final Map<T, Supplier<C>> hooks;

    public Hook(ArcanaAPI<?> api, Map<T, Supplier<C>> hooks) {
        this.api = api;
        this.hooks = hooks;
    }

    public H get(Supplier<T> supplier) {
        return (H) hooks.get(supplier.get());
    }
}
