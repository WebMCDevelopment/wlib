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

package xyz.webmc.wlib.modern.internal;

import xyz.webmc.wlib.modern.api.util.DatapackUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.event.server.ServerLoadEvent.LoadType;

public final class WLIBModernEventListener implements Listener {
  private static final List<Runnable> STARTUP_CALLBACKS = Collections.synchronizedList(new ArrayList<>());
  private static boolean serverStarted = false;
  private final Set<String> removeCommands;

  public WLIBModernEventListener(Set<String> removeCommands) {
    this.removeCommands = Set.copyOf(removeCommands);
  }

  @EventHandler
  public void onPlayerCommandSend(PlayerCommandSendEvent ev) {
    ev.getCommands().removeAll(this.removeCommands);
  }

  @EventHandler
  public void onServerLoad(ServerLoadEvent ev) {
    if (ev.getType().equals(LoadType.STARTUP)) {
      serverStarted = true;

      synchronized (STARTUP_CALLBACKS) {
        STARTUP_CALLBACKS.forEach(Runnable::run);
        STARTUP_CALLBACKS.clear();
      }

      DatapackUtil._processQueue();
    }
  }

  public static void onServerStartup(Runnable callback) {
    if (!serverStarted) {
      synchronized (STARTUP_CALLBACKS) {
        STARTUP_CALLBACKS.add(callback);
      }
    } else {
      callback.run();
    }
  }
}
