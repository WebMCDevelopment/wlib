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

package xyz.webmc.wlib.devkit.maven;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "devkit", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public final class DevkitMojo extends AbstractMojo {
  @Parameter(defaultValue = "${project}", readonly = true, required = true)
  private MavenProject project;

  @Parameter(defaultValue = "${project.build.directory}", readonly = true, required = true)
  private String buildDirectory;

  @Override
  public void execute() throws MojoExecutionException {
    final Path file = Path.of(buildDirectory, "devkit.properties");

    try {
      Files.createDirectories(file.getParent());

      final StringWriter writer = new StringWriter();
      project.getProperties().store(writer, null);

      Files.writeString(
        file.toAbsolutePath(),
        writer.toString().replaceFirst("(?m)^#.*\\R", "")
      );
    } catch (IOException ex) {
      throw new MojoExecutionException(ex);
    }
  }
}
