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

package xyz.webmc.wlib.internal.util;

import xyz.webmc.wlib.api.WLIB;
import xyz.webmc.wlib.api.plugin.WPlugin;

import java.lang.StackWalker.StackFrame;
import java.lang.reflect.Method;

import dev.colbster937.reflect.Mirror;
import dev.colbster937.reflect.MirrorSafe;

public final class InternalUtil {
  private static final StackWalker STACK_WALKER = WLIB.getStackWalker();
  private static WPlugin plugin;

  public static void init(WPlugin _plugin) {
    if (plugin == null) {
      plugin = _plugin;
    } else {
      checkInternalCaller();
    }
  }

  public static void checkInternalCaller() {
    if (plugin != null) {
      final StackFrame[] frames = STACK_WALKER.walk(s -> s.skip(1).toArray(StackFrame[]::new));
      if (frames.length > 0) {
        final StackFrame frame = frames[1];
        if (!plugin.getOwnsClass(frame.getDeclaringClass())) {
          throw new IllegalCallerException();
        }
      }
    }
  }

  public static boolean getClassOwnsMethod(Class<?> clazz, String name, Object... params) {
    final Method method = MirrorSafe.getMethod(clazz, name, Mirror.getTypes(params));
    if (method != null) {
      return method.getDeclaringClass().equals(clazz);
    } else {
      return false;
    }
  }
}
