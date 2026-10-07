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

package xyz.webmc.wlib.devkit.annotation.processor;

import xyz.webmc.wlib.devkit.annotation.PaperPluginDependency;
import xyz.webmc.wlib.devkit.annotation.PaperPluginMeta;
import xyz.webmc.wlib.devkit.annotation.PluginMeta;

import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.AnnotationTypeMismatchException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

final class GeneratePluginMetaProcessor extends AbstractGeneratorProcessor<PluginMeta> {
  GeneratePluginMetaProcessor(ProcessingEnvironment processingEnv, Properties properties) {
    super(processingEnv, properties);
  }

  @Override
  protected Class<PluginMeta> getAnnotationClass() {
    return PluginMeta.class;
  }

  @Override
  protected void process(TypeElement type) {
    final String main = type.getQualifiedName().toString();
    final PluginMeta meta = type.getAnnotation(PluginMeta.class);
    PaperPluginMeta paper;

    try {
      paper = meta.paper();
    } catch (AnnotationTypeMismatchException ex) {
      paper = defaultAnnotation(PaperPluginMeta.class);
    }

    try {
      final Filer filter = processingEnv.getFiler();

      if (meta.writePluginYmlFile()) {
        final FileObject file = filter
          .createResource(
            StandardLocation.CLASS_OUTPUT,
            "",
            "plugin.yml",
            type
          );

        try (Writer writer = file.openWriter()) {
          writeCommon(writer, meta, main);

          writeYml(writer, "contributors", meta.contributors());
          writeYml(writer, "load", meta.load());
          writeYml(writer, "libraries", meta.libraries());
          writeYml(writer, "default-permission", meta.defaultPermission());

          writeYmlList(writer, "depend", meta.depend());
          writeYmlList(writer, "softdepend", meta.softDepend());

          writeYml(writer, "loadbefore", meta.loadBefore());
          writeYml(writer, "provides", meta.provides());

          writeYml(writer, "paper-plugin-loader", paper.loader());

          if (!isElementDefault(paper, "skipLibraries")) {
            writeYml(writer, "paper-skip-libraries", paper.skipLibraries());
          }
        }
      }

      if (paper.writePaperPluginYmlFile()) {
        final FileObject file = filter
          .createResource(
            StandardLocation.CLASS_OUTPUT,
            "",
            "paper-plugin.yml",
            type
          );

        try (Writer writer = file.openWriter()) {
          writeCommon(writer, meta, main);

          writeYml(writer, "bootstrapper", paper.bootstrapper());
          writeYml(writer, "loader", paper.loader());
          writeYml(writer, "dependencies");

          final Set<String> categories = new HashSet<>();

          for (PaperPluginDependency dep : paper.dependencies()) {
            final String cat = dep.category();
            if (categories.add(cat)) {
              writeYml(writer, 1, cat);
            }

            writeYml(writer, 2, dep.name());
            writeYml(writer, 3, "load", dep.load());
            writeYml(writer, 3, "required", dep.required());
            writeYml(writer, 3, "join-classpath", dep.joinClasspath());
          }
        }
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  private void writeCommon(Writer writer, PluginMeta meta, String main) throws IOException {
    writeYml(writer, "name", meta.name());
    writeYml(writer, "version", meta.version());
    writeYml(writer, "main", main);

    final String[] authors = meta.authors();
    if (authors.length > 0) {
      if (authors.length > 1) {
        writeYml(writer, "authors", authors);
      } else {
        writeYml(writer, "author", authors[0]);
      }
    }

    writeYml(writer, "website", meta.website());
    writeYml(writer, "api-version", meta.apiVersion());
    writeYml(writer, "prefix", meta.prefix());

    if (!isElementDefault(meta, "foliaSupported")) {
      writeYml(writer, "folia-supported", meta.foliaSupported());
    }
  }
}
