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

package xyz.webmc.wlib.api.structures.blocks;

import xyz.webmc.wlib.api.WLIB;

import com.cryptomorin.xseries.XMaterial;
import dev.colbster937.reflect.MirrorSafe;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

public abstract class Block {
  protected final XMaterial mat;
  protected final String dataModern;
  protected final byte dataLegacy;

  protected Block(final XMaterial mat, final String dataModern, final byte dataLegacy) {
    this.mat = mat;
    this.dataModern = dataModern;
    this.dataLegacy = dataLegacy;
  }

  protected void place(final Location loc) {
    final org.bukkit.block.Block blk = loc.getBlock();
    final Material _mat = this.mat.get();

    if (mat != null) {
      if (WLIB.getIsModernServer() && this.dataModern != null && _mat != null) {
        final Object data = MirrorSafe.invokeMethod(Bukkit.class, "createBlockData", new Object[] { "minecraft:" + _mat.name().toLowerCase() + this.dataModern });
        MirrorSafe.invokeMethod(org.bukkit.block.Block.class, blk, "setBlockData", data, false);
      } else {
        blk.setType(_mat, false);
      }
    }
  }

  public final XMaterial getMaterial() {
    return mat;
  }

  public final Material getBukkitMaterial() {
    return mat.get();
  }

  public final String getDataModern() {
    return dataModern;
  }

  public final byte getDataLegacy() {
    return dataLegacy;
  }
}
