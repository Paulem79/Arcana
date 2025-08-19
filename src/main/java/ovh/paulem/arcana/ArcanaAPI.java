package ovh.paulem.arcana;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import ovh.paulem.arcana.config.Config;
import ovh.paulem.arcana.config.ConfigData;
import ovh.paulem.arcana.config.ConfigEntry;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

public class ArcanaAPI<P extends JavaPlugin> {
    @Getter
    private final P plugin;

    public ArcanaAPI(P plugin) {
        this.plugin = plugin;
    }

    public void init() {
        getPlugin().getLogger().info("ArcanaAPI initialized.");
    }

    /**
     * Loads the config data from the given class and config.
     * @param configClass The config class to load.
     * @param config The configuration section to load from, or null to use the default spigot configuration.
     */
    public<C extends ConfigData> C loadConfig(Class<C> configClass, @Nullable ConfigurationSection config) {
        if (!configClass.isAnnotationPresent(Config.class)) {
            throw new IllegalArgumentException("This class isn't annotated with @Config!");
        }

        ConfigurationSection section = config != null ? config : getPlugin().getConfig();

        C instance;
        try {
            instance = configClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Cannot instanciate config class " + configClass.getName(), e);
        }

        for (Field field : configClass.getDeclaredFields()) {
            if (!field.isAnnotationPresent(ConfigEntry.class)) continue;
            ConfigEntry entry = field.getAnnotation(ConfigEntry.class);
            String path = entry.path().isEmpty() ? field.getName() : entry.path();

            if (!section.isSet(path)) {
                // Valeur absente -> on garde la valeur par défaut dans l'instance.
                continue;
            }

            Object value = extractValue(section, path, field);
            if (value == null) continue; // on garde le défaut si null

            try {
                field.setAccessible(true);

                if (Modifier.isFinal(field.getModifiers())) {
                    getPlugin().getLogger().severe("The config field '" + field.getName() + "' is final and so cannot be modified! Nag " + getPluginAuthors() + " about this.");
                    continue;
                }

                field.set(instance, value);
            } catch (IllegalAccessException e) {
                getPlugin().getLogger().log(Level.WARNING, "Cannot set config's field '" + field.getName() + "'", e);
            }
        }

        return instance;
    }

    private Object extractValue(ConfigurationSection section, String path, Field field) {
        Class<?> type = field.getType();

        // Primitifs & wrappers & String
        if (type == String.class) return section.getString(path);
        if (type == int.class || type == Integer.class) return section.getInt(path);
        if (type == boolean.class || type == Boolean.class) return section.getBoolean(path);
        if (type == long.class || type == Long.class) return section.getLong(path);
        if (type == double.class || type == Double.class) return section.getDouble(path);
        if (type == float.class || type == Float.class) return (float) section.getDouble(path);
        if (type == short.class || type == Short.class) return (short) section.getInt(path);
        if (type == byte.class || type == Byte.class) return (byte) section.getInt(path);

        // Enums (valeur string -> enum)
        if (type.isEnum()) {
            String raw = section.getString(path);
            if (raw == null) return null;
            try {
                @SuppressWarnings({"rawtypes", "unchecked"})
                Object enumValue = Enum.valueOf((Class) type, raw.toUpperCase(Locale.ROOT));
                return enumValue;
            } catch (IllegalArgumentException e) {
                getPlugin().getLogger().warning("Invalid enum value for '" + path + "': " + raw + " in " + section.getCurrentPath());
                return null;
            }
        }

        // Typed list
        if (List.class.isAssignableFrom(type)) {
            Type genericType = field.getGenericType();
            if (genericType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) genericType;
                Type arg = parameterizedType.getActualTypeArguments()[0];
                if (arg instanceof Class<?>) {
                    Class<?> listArgCls = (Class<?>) arg;
                    if (listArgCls == String.class) return section.getStringList(path);
                    if (listArgCls == Integer.class) return section.getIntegerList(path);
                    if (listArgCls == Long.class) return section.getLongList(path);
                    if (listArgCls == Double.class) return section.getDoubleList(path);
                    if (listArgCls == Boolean.class) return section.getBooleanList(path);
                }
            }

            // Generic list
            return section.getList(path);
        }

        // If none of those works, we do a object get and return it if it's of the corresponding type.
        Object raw = section.get(path);
        if (type.isInstance(raw)) return raw;

        return null;
    }

    private String getPluginAuthors() {
        List<String> authors = getPlugin().getDescription().getAuthors();
        return authors.isEmpty() ? "N/A" : String.join(", ", authors);
    }
}
