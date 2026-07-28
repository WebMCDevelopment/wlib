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

import dev.colbster937.reflect.MirrorSafe;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public final class PlayerUtil {
  private static final Plugin essentials = PluginUtil.getPlugin("Essentials");
  public static void teleport(final Player plr, final Location loc) {
    final Location prev = plr.getLocation().clone();

    SchedulerUtil.teleportAsync(plr, loc);

    if (essentials != null) {
      final Object user = MirrorSafe.invokeMethod(essentials, "getUser", plr);
      if (user != null) {
        MirrorSafe.invokeMethod(user, "setLastLocation", prev);
      }
    }
  }

  public static void teleport(final Player plr, final World world, final double x, final double y, final double z,
      final float yaw, final float pitch) {
    teleport(plr, new Location(world, x, y, z, yaw, pitch));
  }

  public static void teleport(final Player plr, final World world, final double x, final double y, final double z) {
    final Location prev = plr.getLocation().clone();
    teleport(plr, world, x, y, z, prev.getYaw(), prev.getPitch());
  }

  public static void teleport(final Player plr, final double x, final double y, final double z, final float yaw,
      final float pitch) {
    teleport(plr, plr.getWorld(), x, y, z, yaw, pitch);
  }

  public static void teleport(final Player plr, final double x, final double y, final double z) {
    final Location prev = plr.getLocation().clone();
    teleport(plr, plr.getWorld(), x, y, z, prev.getYaw(), prev.getPitch());
  }
}
