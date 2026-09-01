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

import org.bukkit.Location;

public abstract class AbstractGenerableStructure extends AbstractBaseStructure {
  static final int SEARCH_RADIUS = 100;

  protected AbstractGenerableStructure(final String name) {
    super(name);
  }

  public abstract boolean canGenerateAt(final int chunkX, final int chunkZ, final long worldSeed);

  public final Location locateNearest(final Location pos, final long worldSeed) {
    if (pos == null || pos.getWorld() == null) {
      return null;
    }

    final int centerChunkX = pos.getChunk().getX();
    final int centerChunkZ = pos.getChunk().getZ();

    if (this.canGenerateAt(centerChunkX, centerChunkZ, worldSeed)) {
      return new Location(pos.getWorld(), centerChunkX * 16 + 8, pos.getWorld().getHighestBlockYAt(centerChunkX * 16 + 8, centerChunkZ * 16 + 8), centerChunkZ * 16 + 8);
    }

    for (int radius = 1; radius <= SEARCH_RADIUS; radius++) {
      for (int dz = -radius; dz <= radius; dz++) {
        for (int dx = -radius; dx <= radius; dx++) {
          if (Math.max(Math.abs(dx), Math.abs(dz)) != radius) {
            continue;
          }

          final int chunkX = centerChunkX + dx;
          final int chunkZ = centerChunkZ + dz;
          if (this.canGenerateAt(chunkX, chunkZ, worldSeed)) {
            return new Location(pos.getWorld(), chunkX * 16 + 8, pos.getWorld().getHighestBlockYAt(chunkX * 16 + 8, chunkZ * 16 + 8), chunkZ * 16 + 8);
          }
        }
      }
    }

    return null;
  }
}
