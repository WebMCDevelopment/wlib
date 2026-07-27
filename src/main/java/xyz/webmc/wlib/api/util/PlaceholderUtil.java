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

import xyz.webmc.wlib.internal.util.AbstractPluginRequiredUtil;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public final class PlaceholderUtil extends AbstractPluginRequiredUtil {
  private static boolean bool = false;

  public static void init() {
    bool = check("PlaceholderAPI");
  }

  public static String parsePlaceholders(final Player player, final String txt) {
    if (bool) {
      return PlaceholderAPI.setPlaceholders(player, txt);
    } else {
      return txt;
    }
  }
}
