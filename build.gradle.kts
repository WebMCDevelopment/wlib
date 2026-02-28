import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import xyz.webmc.wlib.PluginBuildConstants



val PLUGIN_DEPS = listOf("Caramel")
val PLUGIN_SDEP = listOf("GriefPrevention")



val ARTIFACT = "${PluginBuildConstants.PLUGIN_NAME}-${PluginBuildConstants.PLUGIN_VERS}"

group = PluginBuildConstants.PLUGIN_PCKG
version = PluginBuildConstants.PLUGIN_VERS

plugins {
  id("java")
  id("maven-publish")
  id("com.gradleup.shadow") version "9.3.2"
}

repositories {
  mavenCentral()
  maven("https://repo.papermc.io/repository/maven-public/")
  maven("https://raw.githubusercontent.com/klashdevelopment/mvn/main/repository/")
  maven("https://repo.tcoded.com/releases/")
}


dependencies {
  compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
  compileOnly("dev.klash:Caramel:1.4.7")
  implementation("com.tcoded:FoliaLib:0.5.1")
}

sourceSets {
	named("main") {
		java.srcDirs(
      "./src/main",
      "./src/api",
      "./src/plugin/java"
    )
		resources.srcDir("./src/plugin/resources")
	}
}

publishing {
  publications {
    create<MavenPublication>("maven") {
      artifact(tasks.named("shadowJar"))
    }
  }
}

tasks.named("publishToMavenLocal") {
  dependsOn(tasks.named("shadowJar"))
}

tasks.named<JavaCompile>("compileJava") {
  options.release.set(21)
}

tasks.named<ProcessResources>("processResources") {
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
  outputs.upToDateWhen { false }
  filesMatching("plugin.yml") {
    expand(
      "plugin_iden" to PluginBuildConstants.PLUGIN_IDEN,
      "plugin_name" to PluginBuildConstants.PLUGIN_NAME,
      "plugin_vers" to PluginBuildConstants.PLUGIN_VERS,
      "plugin_pckg" to PluginBuildConstants.PLUGIN_PCKG,
      "plugin_athr" to PluginBuildConstants.PLUGIN_ATHR,
      "plugin_deps" to getArrayJ(PLUGIN_DEPS),
      "plugin_sdep" to getArrayJ(PLUGIN_SDEP),
    )
  }
}

tasks.named<Jar>("jar") {
  enabled = false
}

tasks.named<ShadowJar>("shadowJar") {
  doFirst {
    delete(layout.buildDirectory.dir("libs"))
    mkdir(layout.buildDirectory.dir("libs"))
  }
  relocate("com.tcoded.folialib", "${PluginBuildConstants.PLUGIN_PCKG}.lib.folialib")
  archiveFileName.set("$ARTIFACT.jar")
}

fun getArrayJ(lst: List<String>): String {
  return lst.joinToString(
    prefix = "[",
    postfix = "]"
  ) { "\"$it\"" }
}