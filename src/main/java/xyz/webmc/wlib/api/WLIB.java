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

package xyz.webmc.wlib.api;

import xyz.webmc.wlib.api.util.DatapackUtil;

import java.lang.StackWalker.Option;
import java.lang.StackWalker.StackFrame;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import dev.colbster937.reflect.MirrorSafe;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.semver4j.Semver;

@SuppressWarnings({ "NonConstantLogger" })
public final class WLIB {
  private static final StackWalker stackWalker = StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE);
  private static final List<Plugin> plugins = new ArrayList<>();
  private static Semver version;
  private static Logger logger;

  public static void _init(final Plugin plugin) {
    version = new Semver(plugin.getDescription().getVersion());
    logger = plugin.getLogger();
  }

  public static boolean requireWLIB(final String ver) {
    return version.isGreaterThanOrEqualTo(ver);
  }

  public static void initPlugin(final Plugin plugin) {
    if (getIsModernServer()) {
      DatapackUtil.initPlugin(plugin);
    }

    plugins.add(plugin);
  }

  @Deprecated(forRemoval = true)
  public static void registerPlugin(final Plugin plugin) {
    initPlugin(plugin);
  }

  public static List<Plugin> getWLIBPlugins() {
    return plugins;
  }

  public static List<String> getWLIBPluginNames() {
    final List<String> ret = new ArrayList<>();

    for (final Plugin plugin : getWLIBPlugins()) {
      ret.add(plugin.getName());
    }

    return ret;
  }

  public static void devAlert(final String... txt) {
    final Class<?> clazz = stackWalker.getCallerClass();
    final String ctx = clazz.getSimpleName();
    final String str = ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "DEV" + ChatColor.RESET + " - " + ChatColor.GREEN + ChatColor.AQUA + ctx + ChatColor.DARK_GREEN + "] " + ChatColor.RESET + String.join(" ", txt);

    for (final Player p : Bukkit.getOnlinePlayers()) {
      if (p.hasPermission("wlib.alerts.dev") && !p.hasPermission("wlib.alerts.dev.muted." + ctx)) {
        p.sendMessage(str);
      }
    }
  }

  public static void warnDeprecatedUsage() {
    final StackFrame[] frames = stackWalker.walk(s -> s.skip(1).toArray(StackFrame[]::new));
    if (frames.length > 1) {
      final StackFrame called = frames[0];
      final StackFrame caller = frames[1];

      final StringBuilder sb = new StringBuilder();

      sb.append(caller.getClassName())
          .append('.')
          .append(caller.getMethodName())
          .append(':')
          .append(caller.getLineNumber())
          .append(" called deprecated method ")
          .append(called.getClassName())
          .append('.')
          .append(called.getMethodName());

      for (int i = 2; i < frames.length; i++) {
        final StackFrame frame = frames[i];

        sb.append("\n  at ")
            .append(frame.getClassName())
            .append('.')
            .append(frame.getMethodName())
            .append('(')
            .append(frame.getFileName())
            .append(':')
            .append(frame.getLineNumber())
            .append(")");
      }

      logger.warning(sb.toString());
    }
  }

  public static boolean getIsModernServer() {
    return MirrorSafe.getClassExists("org.bukkit.block.data.BlockData");
  }

  public static Logger getLogger() {
    return logger;
  }
}
