package net.paulem.arcana.config;

/**
 * Marker interface for classes that hold config values loaded through
 * {@link net.paulem.arcana.ArcanaAPI#loadConfig(Class, org.bukkit.configuration.ConfigurationSection)}.
 * Implementing classes must be annotated with {@link Config}, and their fields to populate with
 * {@link ConfigEntry}.
 */
public interface ConfigData {
}
