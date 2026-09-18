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

import xyz.webmc.wlib.internal.util.InternalUtil;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public final class EventUtil {
  private static final PluginManager PLUGIN_MANAGER = Bukkit.getPluginManager();
  private static Plugin plugin;

  public static void _init(Plugin _plugin) {
    InternalUtil.checkInternalCaller();
    plugin = _plugin;
  }

  public static void registerEvents(Listener listener, Plugin plugin) {
    PLUGIN_MANAGER.registerEvents(listener, plugin);
  }

  public static void registerEvents(Listener listener) {
    registerEvents(listener, plugin);
  }

  public static void registerEvent(Class<? extends Event> event, Listener listener, EventPriority priority, EventExecutor executor, Plugin plugin) {
    PLUGIN_MANAGER.registerEvent(event, listener, priority, executor, plugin);
  }

  public static void registerEvent(Class<? extends Event> event, Listener listener, EventPriority priority, EventExecutor executor) {
    registerEvent(event, listener, priority, executor, plugin);
  }

  public static void callEvent(Event ev) {
    PLUGIN_MANAGER.callEvent(ev);
  }
}
