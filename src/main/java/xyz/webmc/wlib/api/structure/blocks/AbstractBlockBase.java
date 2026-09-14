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

package xyz.webmc.wlib.api.structure.blocks;

import xyz.webmc.wlib.api.WLIB;
import xyz.webmc.wlib.api.structure.BuilderChunk;

import com.cryptomorin.xseries.XMaterial;
import dev.colbster937.reflect.MirrorSafe;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public abstract class AbstractBlockBase {
  protected final XMaterial mat;
  protected final String dataModern;
  protected final byte dataLegacy;

  protected AbstractBlockBase(XMaterial mat, String dataModern, byte dataLegacy) {
    this.mat = mat;
    this.dataModern = dataModern;
    this.dataLegacy = dataLegacy;
  }

  protected void place(Location loc) {
    final Block blk = loc.getBlock();
    final Material _mat = this.mat.get();

    if (this.mat != null) {
      if (WLIB.getIsModernServer() && this.dataModern != null && _mat != null) {
        final Object data = MirrorSafe.invokeMethod(Bukkit.class, "createBlockData", new Object[] { "minecraft:" + _mat.name().toLowerCase() + this.dataModern });
        MirrorSafe.invokeMethod(Block.class, blk, "setBlockData", data, false);
      } else {
        blk.setType(_mat, false);
      }
    }
  }

  protected void place(Location loc, Object chunk) {
    if (chunk instanceof org.bukkit.Chunk liveChunk) {
      if (!isInChunk(loc, liveChunk)) {
        return;
      }

      this.place(loc);
      return;
    }

    if (chunk instanceof BuilderChunk builderChunk) {
      if (!isInChunk(loc, builderChunk)) {
        return;
      }

      final int x = loc.getBlockX() - (builderChunk.x() << 4);
      final int y = loc.getBlockY();
      final int z = loc.getBlockZ() - (builderChunk.z() << 4);
      final Material material = this.mat.get();

      if (material == null) {
        return;
      }

      if (this.dataModern != null) {
        final Object data = MirrorSafe.invokeMethod(Bukkit.class, "createBlockData",
            new Object[] { "minecraft:" + material.name().toLowerCase() + this.dataModern });
        MirrorSafe.invokeMethod(builderChunk.chunkData().getClass(), builderChunk.chunkData(), "setBlockData", x, y, z, data);
      } else {
        MirrorSafe.invokeMethod(builderChunk.chunkData().getClass(), builderChunk.chunkData(), "setBlock", x, y, z, material);
      }
      return;
    }

    throw new IllegalArgumentException("Unsupported chunk type: " + chunk);
  }

  public final XMaterial getMaterial() {
    return this.mat;
  }

  public final Material getBukkitMaterial() {
    return this.mat.get();
  }

  public final String getDataModern() {
    return this.dataModern;
  }

  public final byte getDataLegacy() {
    return this.dataLegacy;
  }

  private static boolean isInChunk(Location location, org.bukkit.Chunk chunk) {
    return location.getWorld() == chunk.getWorld()
        && location.getBlockX() >> 4 == chunk.getX()
        && location.getBlockZ() >> 4 == chunk.getZ();
  }

  private static boolean isInChunk(Location location, BuilderChunk chunk) {
    return location.getBlockX() >> 4 == chunk.x()
        && location.getBlockZ() >> 4 == chunk.z();
  }
}
