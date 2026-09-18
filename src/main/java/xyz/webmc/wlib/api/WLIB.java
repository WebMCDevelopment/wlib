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
import xyz.webmc.wlib.api.util.PluginUtil;
import xyz.webmc.wlib.api.util.RNGUtil;
import xyz.webmc.wlib.api.util.TextUtil;
import xyz.webmc.wlib.internal.command.AliasCommand;
import xyz.webmc.wlib.internal.compat.WCompat;
import xyz.webmc.wlib.internal.compat.api.WLIBCompat;
import xyz.webmc.wlib.internal.util.CompatUtil;

import java.lang.StackWalker.Option;
import java.lang.StackWalker.StackFrame;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import dev.colbster937.reflect.MirrorSafe;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.semver4j.Semver;

@SuppressWarnings({ "unused", "NonConstantLogger" })
public final class WLIB extends WLIBCompat {
  private static final StackWalker STACK_WALKER = StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE);
  private static final String BLANK_COMMAND = RNGUtil.getRandomStringLowercaseAZ(16);
  private static final Set<Plugin> PLUGINS = new HashSet<>();
  private static Plugin plugin;
  private static Logger logger;

  public static void _init(Plugin _plugin) {
    plugin = _plugin;
    logger = plugin.getLogger();
  }

  public static boolean requireWLIBVersion(String ver) {
    return PluginUtil.requirePluginVersion(plugin, ver);
  }

  public static Semver getWLIBVersion() {
    return new Semver(PluginUtil.getPluginVersion(plugin));
  }

  public static String getWLIBVersionString() {
    return getWLIBVersion().getVersion();
  }

  public static String getBlankCommandName() {
    return BLANK_COMMAND;
  }

  public static String getBlankCommandKey() {
    return plugin.getName().toLowerCase() + ":" + getBlankCommandName();
  }

  public static StackWalker getStackWalker() {
    return STACK_WALKER;
  }

  public static void initPlugin(Plugin plugin) {
    if (!PLUGINS.contains(plugin)) {
      if (getIsModernServer()) {
        DatapackUtil._initPlugin(plugin);
      }

      PLUGINS.add(plugin);
    }
  }

  public static void shutdownPlugin(Plugin plugin) {
    if (PLUGINS.contains(plugin)) {
      if (getIsModernServer()) {
        DatapackUtil._shutdownPlugin(plugin);
      }

      PLUGINS.remove(plugin);
    }
  }

  public static Set<Plugin> getWLIBPluginSet() {
    return Set.copyOf(PLUGINS);
  }

  public static List<Plugin> getWLIBPluginList() {
    return List.copyOf(getWLIBPluginSet());
  }

  public static Set<String> getWLIBPluginNameSet() {
    final Set<String> ret = new HashSet<>();

    for (Plugin plugin : getWLIBPluginSet()) {
      ret.add(plugin.getName());
    }

    return Set.copyOf(ret);
  }

  public static List<String> getWLIBPluginNameList() {
    return List.copyOf(getWLIBPluginNameSet());
  }

  public static void alert(int index, String... msg) {
    final Optional<StackFrame> optional = STACK_WALKER.walk(stream -> stream.skip(index).findFirst());
    optional.ifPresent(caller -> {
      final String ctx = getClassNameFromFileName(caller.getFileName());
      final String str = ChatColor.DARK_GREEN +
        "[" + ChatColor.GREEN + ctx + ":" + caller.getLineNumber() + ChatColor.DARK_GREEN + "] " +
        ChatColor.RESET + ChatColor.GRAY +
        String.join(" ", msg);

      for (Player p : Bukkit.getOnlinePlayers()) {
        if (p.hasPermission("wlib.alerts") && !p.hasPermission("wlib.alerts.muted." + ctx)) {
          p.sendMessage(str);
        }
      }

      Bukkit.getConsoleSender().sendMessage(str);
    });
  }

  public static void alert(String... msg) {
    alert(2, msg);
  }

  public static void warnDeprecatedUsage() {
    if (!getWLIBPropertyExists("ignoreDeprecationWarnings")) {
      final StackFrame[] frames = STACK_WALKER.walk(s -> s.skip(1).toArray(StackFrame[]::new));

      if (frames.length > 1) {
        final StackFrame called = frames[0];
        final StackFrame caller = frames[1];

        final String name = called.getMethodName().trim();
        final boolean ctor = name.equals("<init>");
        final Class<?> clazz = called.getDeclaringClass();
        final Class<?>[] params = called.getMethodType().parameterArray();

        Deprecated deprecated = null;

        if (!ctor) {
          final Method method = MirrorSafe.getMethod(clazz, name, params);
          if (method != null) {
            deprecated = method.getAnnotation(Deprecated.class);
          }
        } else {
          final Constructor<?> constructor = MirrorSafe.getConstructor(clazz, params);
          if (constructor != null) {
            deprecated = constructor.getAnnotation(Deprecated.class);
          }
        }

        final StringBuilder sb = new StringBuilder();

        sb.append(caller.getClassName())
          .append(".")
          .append(caller.getMethodName())
          .append(":")
          .append(caller.getLineNumber())
          .append(" invoked deprecated ");

        if (!ctor) {
          sb.append("method");
        } else {
          sb.append("constructor");
        }

        sb.append(" ");

        final WCompat compat = CompatUtil.getWCompat(clazz);
        Class<?> _clazz = clazz;

        if (compat != null) {
          _clazz = compat.value();
        }

        final String clazzName = _clazz.getCanonicalName();

        if (!ctor) {
          sb.append(clazzName).append(".").append(name);
        } else {
          sb.append(clazzName);
        }

        sb.append("!");

        final Plugin plugin = PluginUtil.getProvidingPlugin(caller.getDeclaringClass());
        Logger log = logger;

        if (plugin != null) {
          log = plugin.getLogger();

          sb.append(" Nag developers of ")
            .append(plugin.getName());

          final List<String> authors = plugin.getDescription().getAuthors();
          if (authors != null && authors.size() > 0) {
            final String author = String.join(", ", authors).trim();
            if (author != null && !author.isBlank()) {
              sb.append(" (")
                .append(author)
                .append(")");
            }
          }

          sb.append(" to update their plugin!");
        }

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

        final String str = sb.toString().trim();

        log.warning(str);

        final String[] multiStr = TextUtil.serializeExceptionStackStringMultiline(str);

        for (Player p : Bukkit.getOnlinePlayers()) {
          if (p.hasPermission("wlib.alerts")) {
            for (String _str : multiStr) {
              p.sendMessage(ChatColor.RED + _str);
            }
          }
        }
      }
    }
  }

  public static boolean getIsModernServer() {
    return MirrorSafe.getClassExists("org.bukkit.block.data.BlockData");
  }

  public static Command getCurrentAlias() {
    return AliasCommand.get();
  }

  public static String getWLIBProperty(String name) {
    return System.getProperty(plugin.getName().toLowerCase() + "." + name);
  }

  public static boolean getWLIBPropertyExists(String name) {
    return getWLIBProperty(name) != null;
  }

  public static Logger getLogger() {
    return logger;
  }

  private static String getClassNameFromFileName(String file) {
    return file.substring(0, file.lastIndexOf('.'));
  }
}
