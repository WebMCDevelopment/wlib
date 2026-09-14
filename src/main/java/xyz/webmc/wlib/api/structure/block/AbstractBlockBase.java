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

package xyz.webmc.wlib.api.structure.block;

import xyz.webmc.wlib.api.WLIB;
import xyz.webmc.wlib.api.structure.BuilderChunk;

import com.cryptomorin.xseries.XMaterial;
import dev.colbster937.reflect.MirrorSafe;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;


public abstract sealed class AbstractBlockBase permits AbsoluteBlock, BlockRelative, RelativeBlock {
  protected final XMaterial mat;
  protected final String dataModern;
  protected final byte dataLegacy;

  protected AbstractBlockBase(XMaterial mat, String dataModern, byte dataLegacy) {
    WLIB.warnDeprecatedUsage();
    this.mat = mat;
    this.dataModern = dataModern;
    this.dataLegacy = dataLegacy;
  }

  protected AbstractBlockBase(XMaterial mat, String data) {
    this(mat, data, (byte) 0);
    WLIB.warnDeprecatedUsage();
  }

  protected AbstractBlockBase(XMaterial mat, byte data) {
    this(mat, null, data);
    WLIB.warnDeprecatedUsage();
  }

  protected void place(Location loc) {
    final Material material = this.getBukkitMaterial();
    if (material == null) {
      return;
    }

    final Block block = loc.getBlock();
    if (WLIB.getIsModernServer() && this.dataModern != null) {
      final Object data = MirrorSafe.invokeMethod(Bukkit.class, "createBlockData",
          new Object[] { "minecraft:" + material.name().toLowerCase() + this.dataModern });
      MirrorSafe.invokeMethod(Block.class, block, "setBlockData", data, false);
    } else {
      block.setType(material, false);
      if (this.dataLegacy != 0) {
        block.setData(this.dataLegacy);
      }
    }
  }

  protected void place(Location loc, Object chunk) {
    if (chunk instanceof org.bukkit.Chunk liveChunk) {
      if (loc.getWorld() == liveChunk.getWorld()
          && loc.getBlockX() >> 4 == liveChunk.getX()
          && loc.getBlockZ() >> 4 == liveChunk.getZ()) {
        this.place(loc);
      }
      return;
    }

    if (chunk instanceof BuilderChunk builderChunk) {
      if (loc.getBlockX() >> 4 != builderChunk.x()
          || loc.getBlockZ() >> 4 != builderChunk.z()) {
        return;
      }

      final Material material = this.getBukkitMaterial();
      if (material == null) {
        return;
      }

      final int x = loc.getBlockX() - (builderChunk.x() << 4);
      final int y = loc.getBlockY();
      final int z = loc.getBlockZ() - (builderChunk.z() << 4);
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

  public final XMaterial getXMaterial() {
    WLIB.warnDeprecatedUsage();
    return this.mat;
  }

  public final XMaterial getMaterial() {
    WLIB.warnDeprecatedUsage();
    return this.getXMaterial();
  }

  public final Material getBukkitMaterial() {
    WLIB.warnDeprecatedUsage();
    return this.mat == null ? null : this.mat.get();
  }

  public final String getDataModern() {
    WLIB.warnDeprecatedUsage();
    return this.dataModern;
  }

  public final byte getDataLegacy() {
    WLIB.warnDeprecatedUsage();
    return this.dataLegacy;
  }
}
