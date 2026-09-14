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

import xyz.webmc.wlib.api.structure.placeable.LocatedPlaceableStructure;

import org.bukkit.Location;

public abstract class LocatedStructure implements BaseStructure {
  public abstract LocatedPlaceableStructure build(Location loc);

  @Override
  public final void place(Location loc, Object... chunks) {
    if (chunks.length > 0) {
      final LocatedPlaceableStructure builder = this.build(loc);
      for (Object chk: chunks){
        builder.place(chk);
      }
    } else {
      this.build(loc).place();
    }
  }
}
