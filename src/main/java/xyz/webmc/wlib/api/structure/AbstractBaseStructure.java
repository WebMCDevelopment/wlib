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

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

public abstract class AbstractBaseStructure {
  private static final List<Class<AbstractBaseStructure>> STRUCTURES = new ArrayList<>();
  private final String name;

  protected AbstractBaseStructure(final String name) {
    this.name = name;
  }

  public final String getName() {
    return this.name;
  }

  public abstract PlaceableStructure build(long seed);

  public final void place(final Location loc, final long seed) {
    this.build(seed).place(loc);
  }

  public final void place(final Location loc) {
    this.place(loc, 0L);
  }

  public final PlaceableStructure getChunk(final int chunkX, final int chunkZ, final long seed) {
    return this.build(seed).getChunk(chunkX, chunkZ);
  }

  public final PlaceableStructure getChunk(final int chunkX, final int chunkZ) {
    return this.getChunk(chunkX, chunkZ, 0L);
  }

  public static List<Class<AbstractBaseStructure>> getStructures() {
    return STRUCTURES;
  }

  public static void registerStructure(final Class<AbstractBaseStructure> clazz) {
    if (clazz != null && !STRUCTURES.contains(clazz)) {
      STRUCTURES.add(clazz);
    }
  }
}
