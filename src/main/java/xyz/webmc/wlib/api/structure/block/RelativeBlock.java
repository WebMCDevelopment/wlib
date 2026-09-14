/*
 * Copyright (C) 2026 Colbster937
 *
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package xyz.webmc.wlib.api.structure.block;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Location;
import org.bukkit.Material;

public non-sealed class RelativeBlock extends AbstractBlockBase {
  private final int x;
  private final int y;
  private final int z;

  public RelativeBlock(int x, int y, int z, XMaterial mat, String dataModern, byte dataLegacy) {
    super(mat, dataModern, dataLegacy);
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public RelativeBlock(int x, int y, int z, XMaterial mat) {
    this(x, y, z, mat, null, (byte) 0);
  }

  public RelativeBlock(int x, int y, int z, Material mat) {
    this(x, y, z, XMaterial.matchXMaterial(mat));
  }

  public RelativeBlock(int x, int y, int z, XMaterial mat, String dataModern) {
    this(x, y, z, mat, dataModern, (byte) 0);
  }

  public RelativeBlock(int x, int y, int z, Material mat, String dataModern) {
    this(x, y, z, XMaterial.matchXMaterial(mat), dataModern);
  }

  public RelativeBlock(int x, int y, int z, XMaterial mat, byte dataLegacy) {
    this(x, y, z, mat, null, dataLegacy);
  }

  public RelativeBlock(int x, int y, int z, Material mat, byte dataLegacy) {
    this(x, y, z, XMaterial.matchXMaterial(mat), dataLegacy);
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

  @Override
  public void place(Location loc) {
    super.place(loc.clone().add(this.x, this.y, this.z));
  }

  public void place(Location loc, Object chunk) {
    super.place(loc.clone().add(this.x, this.y, this.z), chunk);
  }

  public final AbsoluteBlock toAbsolute(Location relativeOrigin) {
    return new AbsoluteBlock(
        relativeOrigin.clone().add(this.x, this.y, this.z),
        this.mat,
        this.dataModern,
        this.dataLegacy);
  }
}
