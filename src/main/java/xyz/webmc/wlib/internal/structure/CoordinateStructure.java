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

package xyz.webmc.wlib.internal.structure;

import xyz.webmc.wlib.api.structure.AbsoluteStructure;
import xyz.webmc.wlib.api.structure.block.AbsoluteBlock;
import xyz.webmc.wlib.api.structure.placeable.AbsolutePlaceableStructure;
import xyz.webmc.wlib.internal.util.TestStructureUtil;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Location;

public final class CoordinateStructure extends AbsoluteStructure {
  @Override
  public AbsolutePlaceableStructure build(Location loc) {
    final AbsolutePlaceableStructure structure = new AbsolutePlaceableStructure(loc);
    final int x = loc.getBlockX();
    final int z = loc.getBlockZ();
    final int y = loc.getBlockY();

    final int maxBits = Math.max(1, Integer.SIZE - Integer.numberOfLeadingZeros(Math.max(Math.abs(x), Math.abs(z))));
    for (int bit = 0; bit < maxBits; bit++) {
      final int xBit = (x >> bit) & 1;
      final int zBit = (z >> bit) & 1;

      structure.addBlock(new AbsoluteBlock(x + 1 + bit, y, z, bitToMaterial(xBit)));
      structure.addBlock(new AbsoluteBlock(x, y, z + 1 + bit, bitToMaterial(zBit)));
    }

    return structure;
  }

  private static XMaterial bitToMaterial(int bit) {
    return bit == 0 ? XMaterial.WHITE_CONCRETE : XMaterial.BLACK_CONCRETE;
  }

  public static CoordinateStructure getInstance() {
    return TestStructureUtil.getInstance(CoordinateStructure.class);
  }

  static {
    TestStructureUtil.registerInstance(CoordinateStructure.class);
  }
}
