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

import xyz.webmc.wlib.api.structures.GenerableStructure;
import xyz.webmc.wlib.api.structures.RelativeStructure;
import xyz.webmc.wlib.api.structures.Structure;
import xyz.webmc.wlib.api.structures.blocks.RelativeBlock;
import xyz.webmc.wlib.api.structures.placeble.RelativePlacebleStructure;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Chunk;
import org.bukkit.Location;

public final class HerobrineShrineTestStructure extends RelativeStructure implements GenerableStructure {
  public static HerobrineShrineTestStructure getInstance() {
    return Structure.getInstance(HerobrineShrineTestStructure.class);
  }

  @Override
  public boolean canGenerate(final Location loc) {
    return true;
  }

  @Override
  public boolean canGenerate(final Chunk chunk) {
    return true;
  }

  @Override
  public String getName() {
    return "Herobrine Shrine Test Structure";
  }

  @Override
  public RelativePlacebleStructure build(){
    final RelativePlacebleStructure structure = new RelativePlacebleStructure();

    for (int y = 0; y < 3; y++) {
      for (int x = -1; x < 2; x++) {
        for (int z = -1; z < 2; z++) {
          if (y == 0) {
            structure.addBlock(new RelativeBlock(x, y, z, XMaterial.GOLD_BLOCK));
          } else if (y == 1) {
            if (x != z && x + z != 0) {
              structure.addBlock(new RelativeBlock(x, y, z, XMaterial.REDSTONE_TORCH));
            } else if (x == 0 && z == 0) {
              structure.addBlock(new RelativeBlock(x, y, z, XMaterial.NETHERRACK));
            }
          } else if (y == 2 && x == 0 && z == 0) {
            structure.addBlock(new RelativeBlock(x, y, z, XMaterial.FIRE));
          }
        }
      }
    }

    return structure;
  }
}
