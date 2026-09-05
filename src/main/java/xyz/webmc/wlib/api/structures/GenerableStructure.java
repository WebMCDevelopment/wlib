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

package xyz.webmc.wlib.api.structures;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

public interface GenerableStructure {
    int SEARCH_RADIUS = 64;

    boolean canGenerate(Location loc);
    boolean canGenerate(Chunk chunk);

    default Location createLocation(final World world, final int chunkX, final int chunkZ) {
        return new Location(world, chunkX << 4, 65, chunkZ << 4);
    }

    default Location locateNearest(final Location pos) {
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
                    for (final int chunkZ : new int[] { minZ, maxZ }) {
                        final Location candidate = createLocation(world, chunkX, chunkZ);
                        if (canGenerate(candidate)) {
                            ret = candidate;
                            break search;
                        }
                    }
                }

                for (int chunkZ = minZ + 1; chunkZ < maxZ; chunkZ++) {
                    for (final int chunkX : new int[] { minX, maxX }) {
                        final Location candidate = createLocation(world, chunkX, chunkZ);
                        if (canGenerate(candidate)) {
                            ret = candidate;
                            break search;
                        }
                    }
                }
            }
        }

        return ret;
    }
}
