/*
 * Copyright (C) 2026 ${plugin.athr}
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

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.UnknownDependencyException;

public final class PluginUtil {
  private static final PluginManager pm = Bukkit.getPluginManager();

  public static final Plugin getPlugin(final String name) {
    return pm.getPlugin(name);
  }

  public static final Plugin[] getPlugins() {
    return pm.getPlugins();
  }

  public static final boolean isPluginEnabled(final String name) {
    return pm.isPluginEnabled(name);
  }

  public static final boolean isPluginEnabled(final Plugin plugin) {
    return pm.isPluginEnabled(plugin);
  }

  public static final Plugin loadPlugin(final File file)
      throws InvalidPluginException, InvalidDescriptionException, UnknownDependencyException {
    return pm.loadPlugin(file);
  }

  public static final Plugin[] loadPlugins(final File dir) {
    return pm.loadPlugins(dir);
  }

  public static final void disablePlugins() {
    pm.disablePlugins();
  }

  public static final void clearPlugins() {
    pm.clearPlugins();
  }

  public static final void enablePlugin(final Plugin plugin) {
    pm.enablePlugin(plugin);
  }

  public static final void disablePlugin(final Plugin plugin) {
    pm.disablePlugin(plugin);
  }
}
