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

import xyz.webmc.wlib.internal.misc.WPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@SuppressWarnings({ "deprecation" })
public final class PlayerUtil {
  private static final Map<UUID, WPlayer> PLAYERS = new HashMap<>();

  public static void _onPlayerLogin(UUID uuid, String host) {
    PLAYERS.putIfAbsent(uuid, new WPlayer(uuid, host, host));
  }

  public static void _onPlayerQuit(Player player) {
    PLAYERS.remove(player.getUniqueId());
  }

  public static String getPlayerVirtualHost(UUID uuid) {
    final WPlayer player = PLAYERS.get(uuid);
    if (player != null) {
      final String host = player.host();
      if (host != null && !host.isBlank()) {
        return host;
      }
    }

    return null;
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
