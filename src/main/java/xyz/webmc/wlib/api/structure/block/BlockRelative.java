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

import com.cryptomorin.xseries.XMaterial;
import dev.colbster937.reflect.MirrorSafe;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

@SuppressWarnings({ "deprecation" })
public non-sealed class BlockRelative extends AbstractBlockBase {
  private final int x;
  private final int y;
  private final int z;

  protected BlockRelative(int x, int y, int z, XMaterial mat, String dataModern, byte dataLegacy) {
    super(mat, dataModern, dataLegacy);
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public BlockRelative(int x, int y, int z, XMaterial mat) {
    this(x, y, z, mat, null, (byte) 0);
  }

  public BlockRelative(int x, int y, int z, Material mat) {
    this(x, y, z, XMaterial.matchXMaterial(mat));
  }

  public BlockRelative(int x, int y, int z, XMaterial mat, String dataModern) {
    this(x, y, z, mat, dataModern, (byte) 0);
  }

  public BlockRelative(int x, int y, int z, Material mat, String dataModern) {
    this(x, y, z, XMaterial.matchXMaterial(mat), dataModern);
  }

  public BlockRelative(int x, int y, int z, XMaterial mat, byte dataLegacy) {
    this(x, y, z, mat, null, dataLegacy);
  }

  public BlockRelative(int x, int y, int z, Material mat, byte dataLegacy) {
    this(x, y, z, XMaterial.matchXMaterial(mat), dataLegacy);
  }

  @Override
  public void place(Location loc) {
    if (this.mat != null) {
      final Location rel = loc.clone().add(this.x, this.y, this.z);
      final Block blk = rel.getBlock();
      final Material _mat = this.getBukkitMaterial();
      if (_mat != null) {
        if (WLIB.getIsModernServer() && this.dataModern != null) {
          final Object data = MirrorSafe.invokeMethod(Bukkit.class, "createBlockData", new Object[] { "minecraft:" + _mat.name().toLowerCase() + this.dataModern });
          MirrorSafe.invokeMethod(Block.class, blk, "setBlockData", data, false);
        } else {
          blk.setType(_mat, false);
          if (this.dataLegacy != 0) {
            blk.setData(this.dataLegacy);
          }
        }
      }
    }
  }

  public final int getX() {
    return this.x;
  }

  public final int getY() {
    return this.y;
  }

  public final int getZ() {
    return this.z;
  }
}
