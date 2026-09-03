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
import org.bukkit.World;

import xyz.webmc.wlib.api.WLIB;

public abstract class AbstractGenerableStructure extends AbstractBaseStructure {
  static final int SEARCH_RADIUS = 100;

  protected AbstractGenerableStructure(final String name) {
    super(name);
  }

  public abstract boolean canGenerateAt(final int chunkX, final int chunkZ, final long worldSeed);

  public abstract long getGenerationSeed(final int chunkX, final int chunkZ, final long worldSeed);

  public abstract PlaceableStructure build(final long seed);

  @Override
  public final PlaceableStructure build() {
    return this.build(0);
  }

  public final Location locateNearest(final Location pos, final long worldSeed) {
    Location ret = null;

    if (pos != null && pos.getWorld() != null) {
      final int centerChunkX = pos.getChunk().getX();
      final int centerChunkZ = pos.getChunk().getZ();
      final World world = pos.getWorld();

      search:
      for (int radius = 0; radius <= SEARCH_RADIUS; radius++) {
        final int minX = centerChunkX - radius;
        final int maxX = centerChunkX + radius;
        final int minZ = centerChunkZ - radius;
        final int maxZ = centerChunkZ + radius;

        for (int chunkX = minX; chunkX <= maxX; chunkX++) {
          for (int chunkZ : new int[] { minZ, maxZ }) {
            if (this.canGenerateAt(chunkX, chunkZ, worldSeed)) {
              ret = this.createLocation(world, chunkX, chunkZ);
              break search;
            }
          }
        }

        for (int chunkZ = minZ + 1; chunkZ < maxZ; chunkZ++) {
          for (int chunkX : new int[] { minX, maxX }) {
            if (this.canGenerateAt(chunkX, chunkZ, worldSeed)) {
              ret = this.createLocation(world, chunkX, chunkZ);
              break search;
            }
          }
        }
      }
    }

    return ret;
  }

  @Deprecated(forRemoval = true)
  public final void place(final Location loc) {
    WLIB.warnDeprecatedUsage();
    this.build(getGenerationSeed(loc.getChunk().getX(), loc.getChunk().getZ(), loc.getWorld().getSeed())).place(loc);
  }

  private Location createLocation(final World world, final int chunkX, final int chunkZ) {
    return new Location(world, chunkX * 16 + 8, 65, chunkZ * 16 + 8);
  }
}
