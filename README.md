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
