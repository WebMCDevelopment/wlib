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

package xyz.webmc.wlib.api.agent.transformer;

import java.lang.instrument.ClassFileTransformer;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.Set;

public abstract class WClassTransformer implements ClassFileTransformer {
  protected abstract Set<String> getTransformList();
  protected abstract byte[] transform(ClassLoader loader, String name, Class<?> clazz, byte[] bytes) throws Exception;

  private final Set<Class<?>> classes = new HashSet<>();
  private final Set<PathMatcher> matchers = this.getTransformMatchers();

  @Override
  public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
    try {
      if (className != null) {
        final Path path = Path.of(className);
        for (PathMatcher matcher : this.matchers) {
          if (matcher.matches(path)) {
            return this.transform(loader, className, classBeingRedefined, classfileBuffer);
          }
        }
      }
    } catch (Exception ex) {
      throw new IllegalStateException(ex);
    }

    return null;
  }

  public final Set<Class<?>> getTransformClasses() {
    return Set.copyOf(this.classes);
  }

  protected Set<String> transformList(Object... args) {
    final Set<String> ret = new HashSet<>();

    for (Object obj : args) {
      final String add;

      if (obj instanceof String str) {
        add = str;
      } else if (obj instanceof Class clazz) {
        add = getClassPath(clazz);
        this.classes.add(clazz);
      } else {
        add = String.valueOf(obj);
      }

      ret.add(add);
    }

    return Set.copyOf(ret);
  }

  private static String getClassPath(Class<?> clazz) {
    return clazz.getName().replaceAll("\\.", "/").trim();
  }

  private Set<PathMatcher> getTransformMatchers() {
    final Set<PathMatcher> ret = new HashSet<>();
    final FileSystem fs = FileSystems.getDefault();

    for (String transform : this.getTransformList()) {
      ret.add(fs.getPathMatcher("glob:" + transform));
    }

    return Set.copyOf(ret);
  }
}
