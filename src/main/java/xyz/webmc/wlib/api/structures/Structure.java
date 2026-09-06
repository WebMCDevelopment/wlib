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

import java.util.HashMap;
import java.util.Map;

import dev.colbster937.reflect.MirrorSafe;
import org.bukkit.Chunk;
import org.bukkit.Location;

@SuppressWarnings({ "unchecked" })
public abstract class Structure {
  private static final Map<Class<? extends Structure>, Structure> INSTANCES = new HashMap<>();

  public abstract void place(Location loc);

  public abstract void place(Location loc, Chunk chunk);

  public String getName() {
    return this.getClass().getSimpleName();
  }

  public static final <T extends Structure> T getInstance(final Class<T> clazz, final Object... params) {
    Structure structure = INSTANCES.get(clazz);

    if (structure == null) {
      structure = MirrorSafe.invokeConstructor(clazz, params);
      INSTANCES.put(clazz, structure);
    }

    return (T) structure;
  }
}
