# arcana

A library for common code shared across my Minecraft (Spigot/Paper) plugins.

## Coordinates

```kotlin
repositories {
    maven("https://maven.paulem.net/releases")
}

dependencies {
    implementation("net.paulem:arcana:0.0.2")
    // Java 17+ extensions (optional)
    implementation("net.paulem:arcana:0.0.2:j17")
}
```

## Modules

- `config` — annotation-driven YAML configuration loading (`Config`, `ConfigEntry`, `ConfigData`).
- `hook` — conditional plugin/soft-dependency hooks (`Hook`, `HookCondition`).
- `math` — fast math helpers (`FastRandom`, `ACosTable`) with a Java 17 `FastRandomJ17` variant in the `arcana-j17` module.
- `regions` — chunk/world-keyed block tracking containers (`Tracker`, `Holder`, `*BlockContainer`).
- `registry` — simple mutable/frozen key-value registries.
- `utils` — misc helpers (blocks, entities, locations, UUIDs, strings, ticks, zlib, persistent data).
- `uuid` — offline-mode UUID conversion utilities.

## Config system

The `config` module provides annotation-driven loading of YAML config values into plain Java objects,
similar in spirit to Fabric's Autoconfig.

- `@Config` — marks a class as a loadable config class.
- `ConfigData` — marker interface that loadable config classes must implement.
- `@ConfigEntry` — marks a field to be populated from the config section. Takes an optional
  `path()`; if omitted, the field's name is used as the path.

Loading is done via `ArcanaAPI#loadConfig(Class, ConfigurationSection)`:

```java
@Config
public class MyPluginConfig implements ConfigData {
    @ConfigEntry
    private String greeting = "Hello!";

    @ConfigEntry(path = "limits.max-players")
    private int maxPlayers = 20;
}

MyPluginConfig config = arcanaAPI.loadConfig(MyPluginConfig.class, null);
```

Passing `null` as the `ConfigurationSection` loads from the plugin's default `config.yml`; otherwise
the given section is used. For every `@ConfigEntry` field, `loadConfig` reads the matching path from
the section and sets the field via reflection. Supported field types are `String`, the primitive/wrapper
numeric types and `boolean`, enums (matched case-insensitively by name), and typed `List`s of any of
these. If a path is missing from the config, the field keeps whatever default value it was initialized
with in the class. `final` fields cannot be set and are logged as an error instead.

## Building

```bash
./gradlew build
```

## Publishing

```bash
./gradlew publish
```

Requires Maven credentials for `https://maven.paulem.net/releases` (see `~/.gradle/gradle.properties`:
`paulemUsername` / `paulemPassword`, or the `ORG_GRADLE_PROJECT_paulemUsername` / `ORG_GRADLE_PROJECT_paulemPassword`
environment variables).
