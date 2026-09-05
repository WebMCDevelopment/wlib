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

public final class RelativeBlock extends Block {
  private final int x;
  private final int y;
  private final int z;

  public RelativeBlock(final int x, final int y, final int z, final XMaterial mat, final String dataModern, final byte dataLegacy) {
    super(mat, dataModern, dataLegacy);
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public RelativeBlock(final int x, final int y, final int z, final XMaterial mat) {
    this(x, y, z, mat, null, (byte) 0);
  }

  public RelativeBlock(final int x, final int y, final int z, final Material mat) {
    this(x, y, z, XMaterial.matchXMaterial(mat));
  }

  public RelativeBlock(final int x, final int y, final int z, final XMaterial mat, final String dataModern) {
    this(x, y, z, mat, dataModern, (byte) 0);
  }

  public RelativeBlock(final int x, final int y, final int z, final Material mat, final String dataModern) {
    this(x, y, z, XMaterial.matchXMaterial(mat), dataModern);
  }

  public RelativeBlock(final int x, final int y, final int z, final XMaterial mat, final byte dataLegacy) {
    this(x, y, z, mat, null, dataLegacy);
  }

  public RelativeBlock(final int x, final int y, final int z, final Material mat, final byte dataLegacy) {
    this(x, y, z, XMaterial.matchXMaterial(mat), dataLegacy);
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int getZ() {
    return z;
  }

  @Override
  public void place(final Location loc) {
    super.place(loc.clone().add(x, y, z));
  }

  public LocatedBlock toLocated(final Location relativeOrigin) {
    return new LocatedBlock(
        relativeOrigin.clone().add(x, y, z),
        this.mat,
        this.dataModern,
        this.dataLegacy);
  }
}
