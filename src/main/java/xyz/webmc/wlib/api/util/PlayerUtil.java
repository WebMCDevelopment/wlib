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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@SuppressWarnings({ "deprecation" })
public final class PlayerUtil {
  private static final Map<UUID, String> PLAYER_VHOSTS = new HashMap<>();

  public static void _onPlayerLogin(PlayerLoginEvent ev) {
    if (ev.getResult() == PlayerLoginEvent.Result.ALLOWED) {
      PLAYER_VHOSTS.put(ev.getPlayer().getUniqueId(), ev.getHostname());
    }
  }

  public static void _onPlayerQuit(PlayerQuitEvent ev) {
    PLAYER_VHOSTS.remove(ev.getPlayer().getUniqueId());
  }

  public static String getPlayerVirtualHost(UUID uuid) {
    return PLAYER_VHOSTS.get(uuid);
  }

  public static String getPlayerVirtualHost(Player player) {
    return getPlayerVirtualHost(player.getUniqueId());
  }

  public static String getPlayerVirtualHost(String name) {
    final Player player = Bukkit.getPlayer(name);
    if (player != null) {
      return getPlayerVirtualHost(player.getUniqueId());
    } else {
      return null;
    }
  }
}
