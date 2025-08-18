package ovh.paulem.arcana.hook;

import lombok.AccessLevel;
import lombok.Getter;
import ovh.paulem.arcana.ArcanaAPI;

import java.util.function.Supplier;

public abstract class Hook<H, K> {
    @Getter(value = AccessLevel.PROTECTED)
    private final ArcanaAPI<?> api;
    private final HookCondition<K, Supplier<H>> hooks;

    public Hook(ArcanaAPI<?> api, HookCondition<K, Supplier<H>> hooks) {
        this.api = api;
        this.hooks = hooks;
    }

    public H get(Supplier<K> supplier) {
        return hooks.get(supplier.get()).get();
    }
}
