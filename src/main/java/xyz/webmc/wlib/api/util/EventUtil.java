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

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public final class EventUtil {
  private static final PluginManager pm = Bukkit.getPluginManager();
  private static Plugin plugin;

  public static final void init(final Plugin plugin) {
    EventUtil.plugin = plugin;
  }

  public static final void registerEvents(final Listener listener, final Plugin plugin) {
    pm.registerEvents(listener, plugin);
  }

  public static final void registerEvent(final Class<? extends Event> event, final Listener listener, final EventPriority priority, final EventExecutor executor, final Plugin plugin) {
    pm.registerEvent(event, listener, priority, executor, plugin);
  }

  public static final void registerEvents(final Listener listener) {
    registerEvents(listener, plugin);
  }

  public static final void registerEvent(final Class<? extends Event> event, final Listener listener, final EventPriority priority, final EventExecutor executor) {
    pm.registerEvent(event, listener, priority, executor, plugin);
  }

  public static final void callEvent(final Event ev) {
    pm.callEvent(ev);
  }
}
