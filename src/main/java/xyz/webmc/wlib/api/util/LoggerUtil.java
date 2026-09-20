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

import xyz.webmc.wlib.api.WLIB;

import java.lang.StackWalker.StackFrame;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;

public final class LoggerUtil {
  public static void info(String str, Object... params) {
    log(Level.INFO, str, params);
  }

  public static void warn(String str, Object... params) {
    log(Level.WARNING, str, params);
  }

  public static void error(String str, Object... params) {
    log(Level.SEVERE, str, params);
  }

  public static void debug(String str, Object... params) {
    if (WLIB.getWLIBPropertyExists("debugLogEnabled")) {
      log(Level.INFO, str, params);
    }
  }

  private static void log(Level lvl, String str, Object... params) {
    final StackFrame frame = WLIB.getStackWalker().walk(s -> s.skip(2).toArray(StackFrame[]::new))[0];
    final Plugin plugin = PluginUtil.getProvidingPlugin(frame.getDeclaringClass());

    final Logger logger;
    if (plugin != null) {
      logger = plugin.getLogger();
    } else {
      logger = WLIB.getLogger();
    }

    logger.log(lvl, str, params);
  }
}
