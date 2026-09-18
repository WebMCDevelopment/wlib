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
import xyz.webmc.wlib.internal.iface.WInternal;

import java.lang.StackWalker.StackFrame;

@WInternal
public final class InternalUtil {
  private static final StackWalker STACK_WALKER = WLIB.getStackWalker();
  private static WPlugin plugin;

  public static void _init(WPlugin _plugin) {
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
}
