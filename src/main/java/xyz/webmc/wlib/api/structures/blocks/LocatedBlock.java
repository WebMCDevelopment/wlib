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

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Location;
import org.bukkit.Material;

public class LocatedBlock extends Block {
  private final Location loc;

  LocatedBlock(final Location loc, final XMaterial mat, final String dataModern, final byte dataLegacy) {
    super(mat, dataModern, dataLegacy);
    this.loc = loc;
  }

  public LocatedBlock(final int x, final int y, final int z, final XMaterial mat) {
    this(new Location(null, x, y, z), mat, null, (byte) 0);
  }

  public LocatedBlock(final int x, final int y, final int z, final Material mat) {
    this(x, y, z, XMaterial.matchXMaterial(mat));
  }

  public LocatedBlock(final int x, final int y, final int z, final XMaterial mat, final String dataModern) {
    this(new Location(null, x, y, z), mat, dataModern, (byte) 0);
  }

  public LocatedBlock(final int x, final int y, final int z, final Material mat, final String dataModern) {
    this(x, y, z, XMaterial.matchXMaterial(mat), dataModern);
  }

  public LocatedBlock(final int x, final int y, final int z, final XMaterial mat, final byte dataLegacy) {
    this(new Location(null, x, y, z), mat, null, dataLegacy);
  }

  public LocatedBlock(final int x, final int y, final int z, final Material mat, final byte dataLegacy) {
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

  public final RelativeBlock toRelative(final Location newRelativeOrigin) {
    return new RelativeBlock(
        this.loc.getBlockX() - newRelativeOrigin.getBlockX(),
        this.loc.getBlockY() - newRelativeOrigin.getBlockY(),
        this.loc.getBlockZ() - newRelativeOrigin.getBlockZ(),
        this.mat,
        this.dataModern,
        this.dataLegacy);
  }
}
