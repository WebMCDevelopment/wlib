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
import org.bukkit.Location;
import org.bukkit.Material;

@SuppressWarnings({ "removal" })
@Deprecated(forRemoval = true)
public abstract sealed class AbstractBlockBase permits BlockRelative {
  protected final XMaterial mat;
  protected final String dataModern;
  protected final byte dataLegacy;

  @Deprecated(forRemoval = true)
  protected AbstractBlockBase(XMaterial mat, String dataModern, byte dataLegacy) {
    WLIB.warnDeprecatedUsage();
    this.mat = mat;
    this.dataModern = dataModern;
    this.dataLegacy = dataLegacy;
  }

  @Deprecated(forRemoval = true)
  protected AbstractBlockBase(XMaterial mat, String data) {
    this(mat, data, (byte) 0);
    WLIB.warnDeprecatedUsage();
  }

  @Deprecated(forRemoval = true)
  protected AbstractBlockBase(XMaterial mat, byte data) {
    this(mat, null, data);
    WLIB.warnDeprecatedUsage();
  }

  @Deprecated(forRemoval = true)
  protected abstract void place(Location loc);

  @Deprecated(forRemoval = true)
  public final XMaterial getXMaterial() {
    WLIB.warnDeprecatedUsage();
    return this.mat;
  }

  @Deprecated(forRemoval = true)
  public final XMaterial getMaterial() {
    WLIB.warnDeprecatedUsage();
    return this.getXMaterial();
  }

  @Deprecated(forRemoval = true)
  public final Material getBukkitMaterial() {
    WLIB.warnDeprecatedUsage();
    return this.mat.get();
  }

  @Deprecated(forRemoval = true)
  public final String getDataModern() {
    WLIB.warnDeprecatedUsage();
    return this.dataModern;
  }

  @Deprecated(forRemoval = true)
  public final byte getDataLegacy() {
    WLIB.warnDeprecatedUsage();
    return this.dataLegacy;
  }
}
