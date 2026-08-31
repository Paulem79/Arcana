package net.paulem.arcana.config;

/**
 * Marker interface for classes that hold config values loaded through
 * {@link net.paulem.arcana.ArcanaAPI#loadConfig(Class, org.bukkit.configuration.ConfigurationSection)}.
 * Implementing classes must be annotated with {@link Config}, and their fields to populate with
 * {@link ConfigEntry}.
 * <p>
 * Implementations must have a no-args constructor (it is instantiated reflectively) that sets each
 * field's default value, since {@code loadConfig} leaves fields with no matching config entry untouched.
 * The loader does not keep a reference to the returned instance: the caller (typically the plugin) is
 * expected to store it (e.g. a field with a getter) and expose the values to the rest of its code from
 * there, re-calling {@code loadConfig} to refresh it when the config changes.
 */
public interface ConfigData {
}
