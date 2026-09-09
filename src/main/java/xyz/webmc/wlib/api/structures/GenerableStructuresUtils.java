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

public class GenerableStructuresUtils {
	private static Location createLocation(final World world, final int chunkX, final int chunkZ) {
		return new Location(world, chunkX << 4, 65, chunkZ << 4);
	}

	public static Location locateNearest(final GenerableStructure structure, final Location pos) {
		Location ret = null;

		if (structure != null && pos != null && pos.getWorld() != null) {
			final int centerChunkX = pos.getChunk().getX();
			final int centerChunkZ = pos.getChunk().getZ();
			final int searchRadius = GenerableStructure.SEARCH_RADIUS;

			search:
			for (int radius = 0; radius <= searchRadius; radius++) {
				final int minX = centerChunkX - radius;
				final int maxX = centerChunkX + radius;
				final int minZ = centerChunkZ - radius;
				final int maxZ = centerChunkZ + radius;

				for (int chunkX = minX; chunkX <= maxX; chunkX++) {
					for (final int chunkZ : new int[] { minZ, maxZ }) {
						final Location loc = createLocation(pos.getWorld(), chunkX, chunkZ);
						if (structure.canGenerate(loc)) {
							ret = loc;
							break search;
						}
					}
				}

				for (int chunkZ = minZ + 1; chunkZ < maxZ; chunkZ++) {
					for (final int chunkX : new int[] { minX, maxX }) {
						final Location loc = createLocation(pos.getWorld(), chunkX, chunkZ);
						if (structure.canGenerate(loc)) {
							ret = loc;
							break search;
						}
					}
				}
			}
		}

		return ret;
	}

	public static <T extends GenerableStructure> void generateChunk(final T structure, final Chunk chunk) {
		if (structure == null || chunk == null) {
			return;
		}

		final Location loc = locateNearest(structure, chunk.getBlock(0, 65, 0).getLocation());
		if (loc != null) {
			structure.place(loc, chunk);
		}
	}

	public static <T extends GenerableStructure> void generateChunk(final Class<T> structureClass,
			final Chunk chunk) {
		if (structureClass != null) {
			generateChunk(StructuresInstances.getInstance(structureClass), chunk);
		}
	}

}
