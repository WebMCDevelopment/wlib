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

import xyz.webmc.wlib.internal.compat.api.structure.block.AbstractBlockBaseCompat;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Location;
import org.bukkit.Material;

public abstract sealed class AbstractBlockBase extends AbstractBlockBaseCompat permits BlockRelative {
  protected final XMaterial mat;
  protected final String dataModern;
  protected final byte dataLegacy;

  protected AbstractBlockBase(XMaterial mat, String dataModern, byte dataLegacy) {
    this.mat = mat;
    this.dataModern = dataModern;
    this.dataLegacy = dataLegacy;
  }

  protected AbstractBlockBase(XMaterial mat, String data) {
    this(mat, data, (byte) 0);
  }

  protected AbstractBlockBase(XMaterial mat, byte data) {
    this(mat, null, data);
  }

  protected abstract void place(Location loc);

  public final XMaterial getXMaterial() {
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
}
