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

import java.util.ArrayList;
import java.util.List;

import dev.colbster937.reflect.Mirror;
import dev.colbster937.reflect.MirrorSafe;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.UnsafeValues;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

@SuppressWarnings({ "deprecation" })
public final class WorldUtil {
  private static final Class<?> BLOCK_DATA = MirrorSafe.getClass("org.bukkit.block.data.BlockData");
  private static final Plugin ESSENTIALS = PluginUtil.getPlugin("Essentials");

  public static String getChunkKey(final Chunk chnk) {
    final String w = getWorldKey(chnk.getWorld());
    final int x = chnk.getX();
    final int z = chnk.getZ();
    return w + ":" + x + ":" + z;
  }

  public static String getChunkKey(final Location loc) {
    return getChunkKey(loc.getChunk());
  }

  public static String getChunkKeyH(final Chunk chnk) {
    return HashUtil.hash64S(getChunkKey(chnk));
  }

  public static String getChunkKeyH(final Location loc) {
    return getChunkKeyH(loc.getChunk());
  }

  public static String getLocKey(final Location loc) {
    final String w = getWorldKey(loc.getWorld());
    final int x = loc.getBlockX();
    final int y = loc.getBlockY();
    final int z = loc.getBlockZ();
    return w + ":" + x + ":" + y + ":" + z;
  }

  public static String getLocKeyH(final Location loc) {
    return HashUtil.hash64S(getLocKey(loc));
  }

  public static String getWorldKey(final World wrld) {
    return wrld.getName();
  }

  public static void dropItem(final ItemStack item, final Location loc) {
    loc.getWorld().dropItemNaturally(loc.clone().add(0.5D, 0.5D, 0.5D), item);
  }

  public static void teleportPlayer(final Player plr, final Location loc) {
    final Location prev = plr.getLocation().clone();

    SchedulerUtil.teleportAsync(plr, loc);

    if (ESSENTIALS != null) {
      final Object user = MirrorSafe.invokeMethod(ESSENTIALS, "getUser", plr);
      if (user != null) {
        MirrorSafe.invokeMethod(user, "setLastLocation", prev);
      }
    }
  }

  public static void teleportPlayer(final Player plr, final World world, final double x, final double y, final double z,
      final float yaw, final float pitch) {
    teleportPlayer(plr, new Location(world, x, y, z, yaw, pitch));
  }

  public static void teleportPlayer(final Player plr, final World world, final double x, final double y, final double z) {
    final Location prev = plr.getLocation().clone();
    teleportPlayer(plr, world, x, y, z, prev.getYaw(), prev.getPitch());
  }

  public static void teleportPlayer(final Player plr, final double x, final double y, final double z, final float yaw,
      final float pitch) {
    teleportPlayer(plr, plr.getWorld(), x, y, z, yaw, pitch);
  }

  public static void teleportPlayer(final Player plr, final double x, final double y, final double z) {
    final Location prev = plr.getLocation().clone();
    teleportPlayer(plr, plr.getWorld(), x, y, z, prev.getYaw(), prev.getPitch());
  }

  public static List<Chunk> getPlayerLoadedChunks(final Player plr) {
    final List<Chunk> ret = new ArrayList<>();

    final Location loc = plr.getLocation();
    final World wrld = loc.getWorld();
    final Chunk chnk = loc.getChunk();
    final int cx = chnk.getX();
    final int cz = chnk.getZ();

    final int dist;
    if (Mirror.hasMethod(plr, "getClientViewDistance")) {
      dist = MirrorSafe.invokeMethod(plr, "getClientViewDistance");
    } else {
      dist = Bukkit.getViewDistance();
    }

    for (int x = -dist; x <= dist; x++) {
      for (int z = -dist; z <= dist; z++) {
        final int chnkX = cx + x;
        final int chnkZ = cz + z;

        if (wrld.isChunkLoaded(chnkX, chnkZ)) {
          final Chunk _chnk = wrld.getChunkAt(chnkX, chnkZ);

          boolean add = true;
          if (Mirror.hasMethod(plr, "isChunkSent", Chunk.class)) {
            add = MirrorSafe.invokeMethod(plr, "isChunkSent", _chnk);
          } else if (Mirror.hasMethod(chnk, "getPlayersSeeingChunk")) {
            final List<Player> lst = MirrorSafe.invokeMethod(_chnk, "getPlayersSeeingChunk");
            add = lst != null && lst.contains(plr);
          }

          if (add) {
            ret.add(_chnk);
          }
        }
      }
    }

    return ret;
  }

  public static void sendFakeBlock(final Player plr, final Location loc, final Material mat, final byte dat) {
    final World wrld = loc.getWorld();
    final Chunk chnk = wrld.getChunkAt(loc);

    boolean sent = true;

    if (Mirror.hasMethod(plr, "isChunkSent", Chunk.class)) {
      sent = MirrorSafe.invokeMethod(plr, "isChunkSent", chnk);
    } else if (Mirror.hasMethod(chnk, "getPlayersSeeingChunk")) {
      final List<Player> lst = MirrorSafe.invokeMethod(chnk, "getPlayersSeeingChunk");
      sent = (lst != null && lst.contains(plr));
    }

    if (sent) {
      if (BLOCK_DATA != null) {
        final UnsafeValues unsafe = Bukkit.getUnsafe();
        final Object blockData = MirrorSafe.invokeMethod(unsafe, "fromLegacy", mat, dat);
        try {
          Mirror.getMethod(plr, "sendBlockChange", Location.class, BLOCK_DATA).invoke(plr, loc, blockData);
        } catch (final ReflectiveOperationException ex) {}
      } else {
        plr.sendBlockChange(loc, mat.getId(), dat);
      }
    }
  }

  public static void sendFakeBlock(final Player plr, final Location loc, final Material mat) {
    sendFakeBlock(plr, loc, mat, (byte) 0);
  }

  public static void sendFakeBlock(final Location loc, final Material mat, final byte dat) {
    for (final Player plr : loc.getWorld().getPlayers()) {
      sendFakeBlock(plr, loc, mat, dat);
    }
  }

  public static void sendFakeBlock(final Location loc, final Material mat) {
    sendFakeBlock(loc, mat, (byte) 0);
  }
}
