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

import dev.colbster937.reflect.Mirror;
import dev.colbster937.reflect.MirrorSafe;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings({ "deprecation" })
public final class WorldUtil {
  public static final String getChunkKey(final Chunk chnk) {
    final String w = getWorldKey(chnk.getWorld());
    final int x = chnk.getX();
    final int z = chnk.getZ();
    return w + ":" + x + ":" + z;
  }

  public static final String getChunkKey(final Location loc) {
    return getChunkKey(loc.getChunk());
  }

  public static final String getChunkKeyH(final Chunk chnk) {
    return HashUtil.hash64S(getChunkKey(chnk));
  }

  public static final String getChunkKeyH(final Location loc) {
    return getChunkKeyH(loc.getChunk());
  }

  public static final String getLocKey(final Location loc) {
    final String w = getWorldKey(loc.getWorld());
    final int x = loc.getBlockX();
    final int y = loc.getBlockY();
    final int z = loc.getBlockZ();
    return w + ":" + x + ":" + y + ":" + z;
  }

  public static final String getLocKeyH(final Location loc) {
    return HashUtil.hash64S(getLocKey(loc));
  }

  public static final String getWorldKey(final World wrld) {
    return wrld.getName();
  }

  public static final void dropItem(final ItemStack item, final Location loc) {
    loc.getWorld().dropItemNaturally(loc.clone().add(0.5D, 0.5D, 0.5D), item);
  }

  public static final void sendFakeBlock(final Location loc, final Material mat, final byte dat) {
    final World wrld = loc.getWorld();
    final Chunk chnk = wrld.getChunkAt(loc);

    for (final Player plr : wrld.getPlayers()) {
      boolean sent = true;

      if (Mirror.hasMethod(plr.getClass(), "isChunkSent", Chunk.class)) {
        sent = MirrorSafe.invokeMethod(plr, "isChunkSent", chnk);
      }

      if (sent) {
        plr.sendBlockChange(loc, mat.getId(), dat);
      }
    }
  }

  public static final void sendFakeBlock(final Location loc, final Material mat) {
    sendFakeBlock(loc, mat, (byte) 0);
  }
}
