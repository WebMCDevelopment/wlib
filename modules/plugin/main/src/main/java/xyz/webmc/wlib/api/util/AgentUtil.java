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

package xyz.webmc.wlib.api.util;

import xyz.webmc.wlib.internal.util.InternalAgentUtil;

import java.lang.instrument.ClassFileTransformer;
import java.util.Collection;

public final class AgentUtil {
  public static void onReady(Runnable callback) {
    InternalAgentUtil.onReady(callback);
  }

  public static boolean getIsReady() {
    return InternalAgentUtil.getIsReady();
  }

  public static Class<?>[] getLoadedClasses() {
    return InternalAgentUtil.getLoadedClasses();
  }

  public static void addClassTransformer(ClassFileTransformer transformer) {
    InternalAgentUtil.addClassTransformer(transformer);
  }

  public static void retransformClasses(Class<?>... classes) {
    InternalAgentUtil.retransformClasses(classes);
  }

  public static void retransformClasses(Collection<Class<?>> classes) {
    retransformClasses(classes.toArray(new Class<?>[0]));
  }

  public static void retransformAllClasses(Class<?>... classes) {
    InternalAgentUtil.retransformAllClasses(classes);
  }

  public static void retransformAllClasses(Collection<Class<?>> classes) {
    retransformAllClasses(classes.toArray(new Class<?>[0]));
  }

  public static void retransformAllClasses(String... classes) {
    InternalAgentUtil.retransformAllClasses(classes);
  }

  public static void retransformAllClassesStr(Collection<String> classes) {
    retransformAllClasses(classes.toArray(new String[0]));
  }

  public static void retransformAllClasses() {
    InternalAgentUtil.retransformAllClasses();
  }
}
