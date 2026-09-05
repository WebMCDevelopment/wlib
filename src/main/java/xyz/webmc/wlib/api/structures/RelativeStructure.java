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

import xyz.webmc.wlib.api.structures.placeble.RelativePlacebleStructure;

import org.bukkit.Chunk;
import org.bukkit.Location;

public abstract class RelativeStructure extends Structure {
    public abstract RelativePlacebleStructure build();

    @Override
    public final void place(Location loc) {
        build().place(loc);
    }

    @Override
    public final void place(Location loc, Chunk chunk) {
        build().place(loc, chunk);
    }
}
