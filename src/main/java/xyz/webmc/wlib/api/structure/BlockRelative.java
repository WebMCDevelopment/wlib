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

package xyz.webmc.wlib.api.structure;

import xyz.webmc.wlib.api.WLIB;

import com.cryptomorin.xseries.XMaterial;
import dev.colbster937.reflect.MirrorSafe;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

@Deprecated(forRemoval = true)
public class BlockRelative {
  private final int x;
  private final int y;
  private final int z;

  private final XMaterial mat;
  private final String dataModern;
  private final byte dataLegacy;

  @Deprecated(forRemoval = true)
  private BlockRelative(final int x, final int y, final int z, final XMaterial mat, final String dataModern, final byte dataLegacy) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.mat = mat;
    this.dataModern = dataModern;
    this.dataLegacy = dataLegacy;
  }

  @Deprecated(forRemoval = true)
  public BlockRelative(final int x, final int y, final int z, final XMaterial mat) {
    this(x, y, z, mat, null, (byte) 0);
    WLIB.warnDeprecatedUsage();
  }

  @Deprecated(forRemoval = true)
  public BlockRelative(final int x, final int y, final int z, final Material mat) {
    this(x, y, z, XMaterial.matchXMaterial(mat));
    WLIB.warnDeprecatedUsage();
  }

  @Deprecated(forRemoval = true)
  public BlockRelative(final int x, final int y, final int z, final XMaterial mat, final String dataModern) {
    this(x, y, z, mat, dataModern, (byte) 0);
    WLIB.warnDeprecatedUsage();
  }

  @Deprecated(forRemoval = true)
  public BlockRelative(final int x, final int y, final int z, final Material mat, final String dataModern) {
    this(x, y, z, XMaterial.matchXMaterial(mat), dataModern);
    WLIB.warnDeprecatedUsage();
  }

  @Deprecated(forRemoval = true)
  public BlockRelative(final int x, final int y, final int z, final XMaterial mat, final byte dataLegacy) {
    this(x, y, z, mat, null, dataLegacy);
    WLIB.warnDeprecatedUsage();
  }

  @Deprecated(forRemoval = true)
  public BlockRelative(final int x, final int y, final int z, final Material mat, final byte dataLegacy) {
    this(x, y, z, XMaterial.matchXMaterial(mat), dataLegacy);
    WLIB.warnDeprecatedUsage();
  }

  @Deprecated(forRemoval = true)
  public void place(final Location loc) {
    WLIB.warnDeprecatedUsage();
    final Location rel = loc.clone().add(this.x, this.y, this.z);
    final Block blk = rel.getBlock();
    final Material _mat = this.mat.parseMaterial();

    if (mat != null && blk != null) {
      if (WLIB.getIsModernServer() && this.dataModern != null && _mat != null) {
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

  @Deprecated(forRemoval = true)
  public final int getX() {
    WLIB.warnDeprecatedUsage();
    return x;
  }

  @Deprecated(forRemoval = true)
  public final int getY() {
    WLIB.warnDeprecatedUsage();
    return y;
  }

  @Deprecated(forRemoval = true)
  public final int getZ() {
    WLIB.warnDeprecatedUsage();
    return z;
  }

  @Deprecated(forRemoval = true)
  public final XMaterial getMaterial() {
    WLIB.warnDeprecatedUsage();
    return mat;
  }

  @Deprecated(forRemoval = true)
  public final Material getBukkitMaterial() {
    WLIB.warnDeprecatedUsage();
    return mat.parseMaterial();
  }

  @Deprecated(forRemoval = true)
  public final String getDataModern() {
    WLIB.warnDeprecatedUsage();
    return dataModern;
  }

  @Deprecated(forRemoval = true)
  public final byte getDataLegacy() {
    WLIB.warnDeprecatedUsage();
    return dataLegacy;
  }
}
