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

import xyz.webmc.wlib.internal.util.RequiredPluginUtil;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import static xyz.webmc.wlib.internal.util.RequiredPluginUtil.checkPlugins;

public final class PlaceholderUtil implements RequiredPluginUtil {
  private static boolean bool = false;

  public static void _init() {
    bool = checkPlugins("PlaceholderAPI");
  }

  public static String parsePlaceholders(Player plr, String txt) {
    if (bool) {
      return PlaceholderAPI.setPlaceholders(plr, txt);
    } else {
      return txt;
    }
  }
}
