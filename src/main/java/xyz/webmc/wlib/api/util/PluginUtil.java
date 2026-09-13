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
import xyz.webmc.wlib.api.plugin.WPlugin;

import java.io.File;
import java.lang.StackWalker.StackFrame;

import dev.colbster937.reflect.MirrorSafe;
import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.UnknownDependencyException;
import org.bukkit.plugin.java.JavaPlugin;
import org.semver4j.Semver;

public final class PluginUtil {
  private static final PluginManager PLUGIN_MANAGER = Bukkit.getPluginManager();

  public static Plugin getPlugin(String name) {
    return PLUGIN_MANAGER.getPlugin(name);
  }

  public static Plugin[] getPlugins() {
    return PLUGIN_MANAGER.getPlugins();
  }

  public static boolean isPluginEnabled(String name) {
    return PLUGIN_MANAGER.isPluginEnabled(name);
  }

  public static boolean isPluginEnabled(Plugin plugin) {
    return PLUGIN_MANAGER.isPluginEnabled(plugin);
  }

  public static Plugin loadPlugin(File file) throws InvalidPluginException, InvalidDescriptionException, UnknownDependencyException {
    return PLUGIN_MANAGER.loadPlugin(file);
  }

  public static Plugin[] loadPlugins(File dir) {
    return PLUGIN_MANAGER.loadPlugins(dir);
  }

  public static void disablePlugins() {
    PLUGIN_MANAGER.disablePlugins();
  }

  public static void clearPlugins() {
    PLUGIN_MANAGER.clearPlugins();
  }

  public static void enablePlugin(Plugin plugin) {
    PLUGIN_MANAGER.enablePlugin(plugin);
  }

  public static void disablePlugin(Plugin plugin) {
    PLUGIN_MANAGER.disablePlugin(plugin);
  }

  public static Plugin getProvidingPlugin(Class<?> clazz) {
    try {
      return JavaPlugin.getProvidingPlugin(clazz);
    } catch (Throwable t) {
      return null;
    }
  }

  public static String getPluginVersion(Plugin plugin) {
    return plugin.getDescription().getVersion();
  }

  public static String getPluginVersion(String plugin) {
    return getPluginMethod(plugin);
  }

  public static String getPluginKey(Plugin plugin) {
    return plugin.getName().toLowerCase();
  }

  public static String getPluginKey(String plugin) {
    return getPluginMethod(plugin);
  }

  public static boolean requirePluginVersion(Plugin plugin, String version) {
    final Semver semver = new Semver(getPluginVersion(plugin));
    return semver.isGreaterThanOrEqualTo(version);
  }

  public static boolean requirePluginVersion(String plugin, String version) {
    return getPluginMethod(plugin);
  }

  public static WPlugin getWPlugin(Plugin plugin) {
    if (plugin instanceof WPlugin wplugin) {
      return wplugin;
    } else {
      return null;
    }
  }

  public static WPlugin getWPlugin(String plugin) {
    return getWPlugin((Plugin) getPlugin(plugin));
  }

  private static <T> T getPluginMethod(String plugin, Object... args) {
    final Plugin _plugin = getPlugin(plugin);
    if (_plugin != null) {
      final Object[] _args = new Object[args.length + 1];
      _args[0] = _plugin;
      System.arraycopy(args, 0, _args, 1, args.length);
      return MirrorSafe.invokeMethod(
        PluginUtil.class,
        WLIB.getStackWalker().walk(s -> s.skip(1).toArray(StackFrame[]::new))[0].getMethodName(),
        _args
      );
    } else {
      return null;
    }
  }
}
