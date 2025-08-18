package ovh.paulem.arcana.config;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;
import ovh.paulem.arcana.ArcanaAPI;

public interface ConfigData {
    /**
     * Loads the config data from the given section.
     * @param api The ArcanaAPI instance.
     * @param section The configuration section to load from, or null to use the default spigot configuration.
     */
    default void load(ArcanaAPI<?> api, @Nullable ConfigurationSection section) {
        api.loadConfig(this.getClass(), section);
    }
}
