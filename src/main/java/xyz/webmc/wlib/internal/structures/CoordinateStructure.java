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

package xyz.webmc.wlib.internal.structures;

import xyz.webmc.wlib.api.structures.LocatedStructure;
import xyz.webmc.wlib.api.structures.StructuresInstances;
import xyz.webmc.wlib.api.structures.blocks.LocatedBlock;
import xyz.webmc.wlib.api.structures.placeable.LocatedPlaceableStructure;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Location;

public final class CoordinateStructure extends LocatedStructure {
  @Override
  public LocatedPlaceableStructure build(final Location loc) {
    final LocatedPlaceableStructure structure = new LocatedPlaceableStructure(loc);
    final int x = loc.getBlockX();
    final int z = loc.getBlockZ();
    final int y = loc.getBlockY();

    final int maxBits = Math.max(1, Integer.SIZE - Integer.numberOfLeadingZeros(Math.max(Math.abs(x), Math.abs(z))));
    for (int bit = 0; bit < maxBits; bit++) {
      final int xBit = (x >> bit) & 1;
      final int zBit = (z >> bit) & 1;

      structure.addBlock(new LocatedBlock(x + 1 + bit, y, z, bitToMaterial(xBit)));
      structure.addBlock(new LocatedBlock(x, y, z + 1 + bit, bitToMaterial(zBit)));
    }

    return structure;
  }

  private static XMaterial bitToMaterial(final int bit) {
    return bit == 0 ? XMaterial.WHITE_CONCRETE : XMaterial.BLACK_CONCRETE;
  }

  public static CoordinateStructure getInstance() {
    return StructuresInstances.getInstance(CoordinateStructure.class);
  }

  static {
    StructuresInstances.registerInstance(CoordinateStructure.class);
  }
}
