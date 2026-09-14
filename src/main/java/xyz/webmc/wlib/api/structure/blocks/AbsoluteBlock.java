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

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Location;
import org.bukkit.Material;

public class AbsoluteBlock extends AbstractBlockBase {
  private final Location loc;

  public AbsoluteBlock(Location loc, XMaterial mat, String dataModern, byte dataLegacy) {
    super(mat, dataModern, dataLegacy);
    this.loc = loc;
  }

  public AbsoluteBlock(int x, int y, int z, XMaterial mat) {
    this(new Location(null, x, y, z), mat, null, (byte) 0);
  }

  public AbsoluteBlock(int x, int y, int z, Material mat) {
    this(x, y, z, XMaterial.matchXMaterial(mat));
  }

  public AbsoluteBlock(int x, int y, int z, XMaterial mat, String dataModern) {
    this(new Location(null, x, y, z), mat, dataModern, (byte) 0);
  }

  public AbsoluteBlock(int x, int y, int z, Material mat, String dataModern) {
    this(x, y, z, XMaterial.matchXMaterial(mat), dataModern);
  }

  public AbsoluteBlock(int x, int y, int z, XMaterial mat, byte dataLegacy) {
    this(new Location(null, x, y, z), mat, null, dataLegacy);
  }

  public AbsoluteBlock(int x, int y, int z, Material mat, byte dataLegacy) {
    this(x, y, z, XMaterial.matchXMaterial(mat), dataLegacy);
  }

  public final int getX() {
    return this.loc.getBlockX();
  }

  public final int getY() {
    return this.loc.getBlockY();
  }

  public final int getZ() {
    return this.loc.getBlockZ();
  }

  public final Location getLocation() {
    return this.loc;
  }

  public void place() {
    super.place(this.loc);
  }

  public void place(Object chunk) {
    super.place(this.loc, chunk);
  }

  public final RelativeBlock toRelative(Location newRelativeOrigin) {
    return new RelativeBlock(
        this.loc.getBlockX() - newRelativeOrigin.getBlockX(),
        this.loc.getBlockY() - newRelativeOrigin.getBlockY(),
        this.loc.getBlockZ() - newRelativeOrigin.getBlockZ(),
        this.mat,
        this.dataModern,
        this.dataLegacy);
  }
}
