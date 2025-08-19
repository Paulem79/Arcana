plugins {
    `java-library`
}

dependencies {
    api(project(":"))
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
    withJavadocJar()
}

// Configure classifiers and base names so that this module publishes as ovh.paulem:Arcana:<version>:j17
// while avoiding clashes with the root module's standard artifacts.
val j17Classifier = "j17"

tasks.named<Jar>("jar") {
    archiveBaseName.set("Arcana")
    archiveClassifier.set(j17Classifier)
}

// Adjust sources and javadoc jars to carry j17-specific classifiers and base name
// to prevent overwriting the root project's sources/javadoc artifacts.
tasks.withType<Jar>().configureEach {
    // Ensure base name alignment
    if (archiveBaseName.get() != "Arcana") {
        archiveBaseName.set("Arcana")
    }
}

tasks.named<Jar>("sourcesJar") {
    archiveBaseName.set("Arcana")
    archiveClassifier.set("${j17Classifier}-sources")
}

tasks.named<Jar>("javadocJar") {
    archiveBaseName.set("Arcana")
    archiveClassifier.set("${j17Classifier}-javadoc")
}
