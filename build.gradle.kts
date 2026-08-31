plugins {
    java
    alias(libs.plugins.run.paper)
    `maven-publish`
}

val catalogLibs = the<org.gradle.api.artifacts.VersionCatalogsExtension>().named("libs")

allprojects {
    apply(plugin = "java")

    group = "net.paulem"
    version = "0.0.2"

    repositories {
        mavenCentral()
        maven {
            name = "spigotmc-repo"
            url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        }
    }

    dependencies {
        compileOnly(catalogLibs.findLibrary("spigot-api").get())

        compileOnly(catalogLibs.findLibrary("jetbrains-annotations").get())

        compileOnly(catalogLibs.findLibrary("fastutil").get())
        compileOnly(catalogLibs.findLibrary("commons-lang3").get())
        compileOnly(catalogLibs.findLibrary("lombok").get())
        annotationProcessor(catalogLibs.findLibrary("lombok").get())
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
            url = uri("https://maven.paulem.net/releases")
            credentials {
                username = (findProperty("MAVEN_USERNAME") as String?) ?: System.getenv("MAVEN_USERNAME")
                password = (findProperty("MAVEN_PASSWORD") as String?) ?: System.getenv("MAVEN_PASSWORD")
            }
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