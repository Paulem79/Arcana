plugins {
    java
    id("xyz.jpenilla.run-paper") version "3.0.0"
    `maven-publish`
}

allprojects {
    apply(plugin = "java")

    group = "ovh.paulem"
    version = "0.0.1"

    repositories {
        mavenCentral()
        maven {
            name = "spigotmc-repo"
            url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        }
    }

    dependencies {
        compileOnly("org.spigotmc:spigot-api:1.21.8-R0.1-SNAPSHOT")

        compileOnly("org.jetbrains:annotations:26.0.2-1")

        compileOnly("it.unimi.dsi:fastutil:8.5.16")
        compileOnly("org.apache.commons:commons-lang3:3.18.0")
        compileOnly("org.projectlombok:lombok:1.18.42")
        annotationProcessor("org.projectlombok:lombok:1.18.42")
    }
}

val targetJavaVersion = 8
val devJavaVersion = 21

java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
    toolchain.languageVersion.set(JavaLanguageVersion.of(devJavaVersion))

    withSourcesJar()
    withJavadocJar()
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(targetJavaVersion)
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}

evaluationDependsOn(":arcana-j17")

publishing {
    repositories {
        maven {
            name = "paulem"
            url = uri("https://maven.paulem.ovh/releases")
            credentials(PasswordCredentials::class)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
            from(components["java"])
            // Artefacts Java 17 du sous-projet
            val j17 = project(":arcana-j17")
            val j17Jar = j17.tasks.named("jar")
            val j17Sources = j17.tasks.named("sourcesJar")
            val j17Javadoc = j17.tasks.named("javadocJar")
            artifact(j17Jar)
            artifact(j17Sources)
            artifact(j17Javadoc)
        }
    }
}