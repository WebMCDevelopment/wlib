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

import xyz.webmc.wlib.api.structures.placeable.LocatedPlaceableStructure;

import org.bukkit.Chunk;
import org.bukkit.Location;

public abstract class LocatedStructure implements AbstractBaseStructure {
  public abstract LocatedPlaceableStructure build(final Location loc);

  @Override
  public final void place(final Location loc, final Chunk... chunks) {
    if (chunks.length > 0) {
      final LocatedPlaceableStructure builder = this.build(loc);
      for (Chunk chk: chunks){
        builder.place(chk);
      }
    } else {
      this.build(loc).place();
    }
  }
}
