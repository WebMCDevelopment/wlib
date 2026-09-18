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

  public static void retransformAllClasses(Class<?>... classes) {
    InternalAgentUtil.retransformAllClasses(classes);
  }
}
