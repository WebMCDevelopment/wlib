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

import xyz.webmc.wlib.api.structure.placeable.RelativePlaceableStructure;

import org.bukkit.Location;

public abstract class RelativeStructure implements BaseStructure {
  public abstract RelativePlaceableStructure build();

  @Override
  public final void place(Location loc, Object... chunks) {
    if (chunks.length > 0) {
      final RelativePlaceableStructure builder = this.build();
      for (Object chk: chunks){
        builder.place(loc, chk);
      }
    } else {
      this.build().place(loc);
    }
  }
}
