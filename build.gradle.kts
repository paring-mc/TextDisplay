import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion

plugins {
    idea
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.paperweight)
}

val pluginName: String by project
val pluginVersion: String by project

group = project.properties["group"]!!
version = pluginVersion

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    paperweight.paperDevBundle(libs.versions.paper)
}

extra.apply {
    set("pluginName", pluginName)
    set("pluginVersion", pluginVersion)
    set("kotlinVersion", libs.versions.kotlin.get())
    set("paperVersion", libs.versions.paper.get().split('.').take(2).joinToString("."))

    // from monun/paper-sample
    val libraries = LinkedHashSet<String>()

    configurations.findByName("implementation")?.allDependencies?.forEach { dependency ->
        val group = dependency.group ?: error("group is null")
        var name = dependency.name ?: error("name is null")
        var version = dependency.version

        if (group == "org.jetbrains.kotlin" && version == null) {
            version = getKotlinPluginVersion()
        } else if (group == "io.github.monun.tap" && name.endsWith("-api")) {
            name = name.removeSuffix("api") + "core"
        }

        requireNotNull(version) { "version is null" }
        require(version != "latest.release") { "version is latest.release" }

        libraries += "$group:$name:$version"
    }

    set("pluginLibraries", libraries.joinToString("\n  ") { "- $it" })
}

tasks {
    processResources {
        filesMatching("*.yml") {
            expand(project.properties)
            expand(extra.properties)
        }
    }

    create<Copy>("serverJar") {
        from(jar)

        val plugins = file(".server/plugins")

        if (plugins.resolve("${project.name}-$version-dev.jar").exists()) {
            val updateDir = plugins.resolve("update")
            into(updateDir)

            delete(updateDir.resolve("RELOAD"))
        } else {
            into(plugins)
        }
    }
}
