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

import xyz.webmc.wlib.api.util.AgentUtil;
import xyz.webmc.wlib.internal.compat.api.agent.transformer.WClassTransformerCompat;
import xyz.webmc.wlib.internal.util.InternalAgentUtil;
import xyz.webmc.wlib.internal.util.InternalUtil;

import java.lang.instrument.ClassFileTransformer;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({ "removal" })
public abstract class WClassTransformer extends WClassTransformerCompat implements ClassFileTransformer {
  protected abstract Set<String> getTransformList();
  protected abstract byte[] transform(ClassLoader loader, String name, Class<?> clazz, byte[] bytes) throws Exception;

  private final Set<String> transformList = this.getTransformList();
  private final Set<PathMatcher> matchers = this.getTransformMatchers();

  public final void _ready() {
    if (InternalUtil.getClassOwnsMethod(this.getClass(), "ready")) {
      this.ready();
    }

    AgentUtil.retransformAllClassesStr(this.transformList);
  }

  @Override
  public final byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
    try {
      if (className != null) {
        final Path path = InternalAgentUtil.getPackageFSPath(className);
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

  protected void ready() {
  }

  protected Set<String> transformList(Object... args) {
    final Set<String> ret = new HashSet<>();

    for (Object obj : args) {
      final String add;

      if (obj instanceof String str) {
        add = str;
      } else if (obj instanceof Class clazz) {
        add = clazz.getName();
        this.classes.add(clazz);
      } else {
        add = String.valueOf(obj);
      }

      ret.add(getPackagePath(add));
    }

    return Set.copyOf(ret);
  }

  protected static String getPackagePath(String pckg) {
    return InternalAgentUtil.getPackageFS(pckg);
  }

  private Set<PathMatcher> getTransformMatchers() {
    final Set<PathMatcher> ret = new HashSet<>();

    for (String transform : this.transformList) {
      ret.add(InternalAgentUtil.getClassMatcher(transform));
    }

    return Set.copyOf(ret);
  }
}
