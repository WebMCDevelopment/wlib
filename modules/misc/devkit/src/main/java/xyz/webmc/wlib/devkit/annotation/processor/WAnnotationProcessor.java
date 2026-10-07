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

import java.io.FileNotFoundException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

public final class WAnnotationProcessor extends AbstractProcessor {
  private static final List<Class<? extends AbstractGeneratorProcessor<?>>> PROCESSORS = List.of(
    GeneratePluginMetaProcessor.class
  );

  private boolean processed = false;
  private Properties properties;

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    if (!this.processed && !roundEnv.processingOver()) {
      try {
        final Map<String, String> options = new HashMap<>(processingEnv.getOptions());
        properties = new Properties();

        final String propertiesFile = options.remove("devkit.propertiesFile");
        if (propertiesFile != null && !propertiesFile.isBlank()) {
          try (Reader reader = Files.newBufferedReader(Path.of(propertiesFile))) {
            properties.load(reader);
          } catch (FileNotFoundException ex) {}
        }

        properties.putAll(options);

        for (Class<? extends AbstractGeneratorProcessor<?>> clazz : PROCESSORS) {
          clazz.getDeclaredConstructor(
            ProcessingEnvironment.class, Properties.class
          ).newInstance(this.processingEnv, properties).process(roundEnv);
        }
      } catch (Throwable t) {
        t.printStackTrace();
      }

      processed = true;
    }

    return false;
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return Set.of("*");
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  @Override
  public Set<String> getSupportedOptions() {
    return Set.copyOf(this.processingEnv.getOptions().keySet());
  }
}
