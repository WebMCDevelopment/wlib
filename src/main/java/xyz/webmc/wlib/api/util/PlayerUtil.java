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

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

@Deprecated
public final class PlayerUtil {
  @Deprecated
  public static void teleport(final Player plr, final Location loc) {
    WorldUtil.teleportPlayer(plr, loc);
  }

  @Deprecated
  public static void teleport(final Player plr, final World wrld, final double x, final double y, final double z,
      final float yaw, final float pitch) {
    WorldUtil.teleportPlayer(plr, wrld, x, y, z, yaw, pitch);
  }

  @Deprecated
  public static void teleport(final Player plr, final World wrld, final double x, final double y, final double z) {
    WorldUtil.teleportPlayer(plr, wrld, x, y, z);
  }

  @Deprecated
  public static void teleport(final Player plr, final double x, final double y, final double z, final float yaw,
      final float pitch) {
    WorldUtil.teleportPlayer(plr, x, y, z, yaw, pitch);
  }

  @Deprecated
  public static void teleport(final Player plr, final double x, final double y, final double z) {
    WorldUtil.teleportPlayer(plr, x, y, z);
  }
}
