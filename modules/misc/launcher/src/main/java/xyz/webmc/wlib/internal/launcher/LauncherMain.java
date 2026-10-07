/*
 * Copyright (C) 2026 Colbster937
 *
 * SPDX-License-Identifier: AGPL-3.0-or-later
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * See the LICENSE file for details.
 */

package xyz.webmc.wlib.internal.launcher;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public final class LauncherMain {
  private static final List<String> JVM_ARGS = List.of(
    "-XX:+IgnoreUnrecognizedVMOptions",
    "-XX:+EnableDynamicAgentLoading",
    "-DPaper.IgnoreJavaVersion=true",
    "-DPaper.skipServerPropertiesComments=true",
    "-Dgale.log.warning.offline.mode=false"
  );

  private static final List<String> PROGRAM_ARGS = List.of(
    "--nogui"
  );

  public static void main(String[] args) throws Exception {
    final Path propertiesPath = Path.of(System.getProperty("launcher.propertiesPath", "launcher.properties")).toAbsolutePath();
    final Properties properties = new Properties();

    if (Files.exists(propertiesPath)) {
      try (InputStream is = Files.newInputStream(propertiesPath)) {
        properties.load(is);
      }
    }

    final String serverJar = properties.getProperty("serverJar", "server.jar");
    final String jvmArgs = properties.getProperty("jvmArgs", "\"\"");
    final String programArgs = properties.getProperty("programArgs", "\"\"");
    final boolean addPlugin = Boolean.parseBoolean(properties.getProperty("addPlugin", "false"));
    final boolean acceptEula = Boolean.parseBoolean(properties.getProperty("acceptEula", "false"));

    properties.setProperty("serverJar", serverJar);
    properties.setProperty("jvmArgs", jvmArgs);
    properties.setProperty("programArgs", programArgs);
    properties.setProperty("addPlugin", Boolean.toString(addPlugin));
    properties.setProperty("acceptEula", Boolean.toString(acceptEula));

    final String qJvmArgs = unquote(jvmArgs);
    final String qProgramArgs = unquote(programArgs);

    try (BufferedWriter writer = Files.newBufferedWriter(propertiesPath)) {
      for (Map.Entry<Object, Object> entry : properties.entrySet()) {
        writer.write(entry.getKey() + " = " + entry.getValue());
        writer.newLine();
      }
    }

    final List<String> cmd = new ArrayList<>();
    final String jar = Paths.get(
      LauncherMain.class.getProtectionDomain().getCodeSource().getLocation().toURI()
    ).toAbsolutePath().normalize().toString();

    cmd.add(ProcessHandle.current().info().command().orElseThrow());
    cmd.addAll(ManagementFactory.getRuntimeMXBean().getInputArguments());
    cmd.addAll(JVM_ARGS);

    if (!qJvmArgs.isBlank()) {
      cmd.addAll(List.of(qJvmArgs.split(" ")));
    }

    if (acceptEula) {
      cmd.add("-Dcom.mojang.eula.agree=true");
    }

    cmd.add("-jar");
    cmd.add(serverJar);

    cmd.addAll(Arrays.asList(args));
    cmd.addAll(PROGRAM_ARGS);

    if (!qProgramArgs.isBlank()) {
      cmd.addAll(List.of(qProgramArgs.split(" ")));
    }

    if (addPlugin) {
      cmd.add("--add-extra-plugin-jar");
      cmd.add(jar);
    }

    final ProcessBuilder pb = new ProcessBuilder(cmd).inheritIO();
    final Process proc = pb.start();
    final int exit = proc.waitFor();

    System.out.flush();
    System.err.flush();

    System.exit(exit);
  }

  private static String unquote(String str) {
    if (str.length() >= 2 && str.startsWith("\"") && str.endsWith("\"")) {
      return str.substring(1, str.length() - 1);
    } else {
      return str;
    }
  }
}
