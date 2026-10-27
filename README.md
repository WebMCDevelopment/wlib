<div align="center">
  <h1>
    <img width="96" height="96" src="img/wlib.png" />
  </h1>

  <!-- <p><b><i>Simple library for complex bukkit plugins</i></b></p> -->

  <a href="LICENSE.md">
    <img src=".github/readme/badges/license.svg">
  </a>

  <br>

  <a href="https://www.java.com/">
    <img src=".github/readme/badges/built-with/java17.svg">
  </a>
  <a href="https://maven.apache.org/">
    <img src=".github/readme/badges/built-with/maven.svg">
  </a>

  <br>

  <a href="https://github.com/WebMCDevelopment/wlib">
    <img src=".github/readme/badges/available/github.svg">
  </a>
  <a href="https://modrinth.com/plugin/wlib/">
    <img src=".github/readme/badges/available/modrinth.svg">
  </a>
  <a href="https://hangar.papermc.io/WebMC/wlib/">
    <img src=".github/readme/badges/available/hangar.svg">
  </a>

  <br>

  <a href="https://dev.bukkit.org/">
    <img src=".github/readme/badges/supported/bukkit.svg">
  </a>
  <a href="https://www.spigotmc.org/">
    <img src=".github/readme/badges/supported/spigot.svg">
  </a>
  <a href="https://papermc.io/software/paper/">
    <img src=".github/readme/badges/supported/paper.svg">
  </a>
  <!-- <a href="https://purpurmc.org/">
    <img src=".github/readme/badges/supported/purpur.svg">
  </a> -->
  <a href="https://papermc.io/software/folia/">
    <img src=".github/readme/badges/supported/folia.svg">
  </a>

  <br>

  <!-- <a href="https://bstats.org/plugin/bukkit/WLIB/33913/">
    <img src="https://bstats.org/signatures/bukkit/WLIB.svg" style="border-radius: 12px;">
  </a> -->
</div>

<br>

## Developers

### Project Config

#### Maven

```xml
<properties>
  <!-- Replace with the latest WLIB version -->
  <wlib.version>{version}</wlib.version>
</properties>

<repositories>
  <repository>
    <id>webmc</id>
    <url>https://codeberg.org/api/packages/WebMCDevelopment/maven/</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>xyz.webmc.wlib</groupId>
    <artifactId>wlib-plugin</artifactId>
    <version>${wlib.version}</version>
    <scope>provided</scope>
  </dependency>

  <dependency>
    <groupId>xyz.webmc.wlib</groupId>
    <artifactId>wlib-devkit</artifactId>
    <version>${wlib.version}</version>
    <scope>provided</scope>
  </dependency>
</dependencies>

<build>
  <plugins>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-compiler-plugin</artifactId>
      <configuration>
        <annotationProcessorPaths>
          <path>
            <groupId>xyz.webmc.wlib</groupId>
            <artifactId>wlib-devkit</artifactId>
            <version>${wlib.version}</version>
          </path>
        </annotationProcessorPaths>
      </configuration>
    </plugin>
  </plugins>
</build>
```

#### Gradle
```kotlin
// Replace with the latest WLIB version
val WLIB_VERSION = "{version}"

repositories {
  maven("https://codeberg.org/api/packages/WebMCDevelopment/maven/")
}

dependencies {
  compileOnly("xyz.webmc.wlib:wlib-plugin:$WLIB_VERSION")
  compileOnly("xyz.webmc.wlib:wlib-devkit:$WLIB_VERSION")
  annotationProcessor("xyz.webmc.wlib:wlib-devkit:$WLIB_VERSION")
}
```

### Example Plugin

```java
package com.example;

import xyz.webmc.wlib.api.plugin.WPlugin;
import xyz.webmc.wlib.devkit.annotation.PluginMeta;

@PluginMeta(
  name = "ExamplePlugin",
  version = "1.0-SNAPSHOT",
  foliaSupported = true
)
public final class ExamplePlugin extends WPlugin {
  @Override
  protected void load() {}

  @Override
  protected void enable() {}

  @Override
  protected void disable() {}
}
```
