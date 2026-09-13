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

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Material;

import static xyz.webmc.wlib.api.WLIB.warnDeprecatedUsage;

@Deprecated(forRemoval = true)
public class BlockRelative extends xyz.webmc.wlib.api.structure.block.BlockRelative {
  public BlockRelative(int x, int y, int z, XMaterial mat) {
    super(x, y, z, mat, null, (byte) 0);
    warnDeprecatedUsage();
  }

  public BlockRelative(int x, int y, int z, Material mat) {
    super(x, y, z, XMaterial.matchXMaterial(mat));
    warnDeprecatedUsage();
  }

  public BlockRelative(int x, int y, int z, XMaterial mat, String dataModern) {
    super(x, y, z, mat, dataModern, (byte) 0);
    warnDeprecatedUsage();
  }

  public BlockRelative(int x, int y, int z, Material mat, String dataModern) {
    super(x, y, z, XMaterial.matchXMaterial(mat), dataModern);
    warnDeprecatedUsage();
  }

  public BlockRelative(int x, int y, int z, XMaterial mat, byte dataLegacy) {
    super(x, y, z, mat, null, dataLegacy);
    warnDeprecatedUsage();
  }

  public BlockRelative(int x, int y, int z, Material mat, byte dataLegacy) {
    super(x, y, z, XMaterial.matchXMaterial(mat), dataLegacy);
    warnDeprecatedUsage();
  }
}
