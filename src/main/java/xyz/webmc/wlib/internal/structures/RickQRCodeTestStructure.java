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

package xyz.webmc.wlib.internal.structures;

import xyz.webmc.wlib.api.structures.RelativeStructure;
import xyz.webmc.wlib.api.structures.Structure;
import xyz.webmc.wlib.api.structures.placeble.RelativePlacebleStructure;

import java.io.InputStream;

public final class RickQRCodeTestStructure extends RelativeStructure {
  public static RickQRCodeTestStructure getInstance() {
    return Structure.getInstance(RickQRCodeTestStructure.class);
  }

  @Override
  public String getName() {
    return "Rick QR Code Test Structure";
  }

  @Override
  public RelativePlacebleStructure build() {
    final RelativePlacebleStructure structure = new RelativePlacebleStructure();
    final InputStream stream = RickQRCodeTestStructure.class.getResourceAsStream("/schematics/rick.schem");
    structure.loadSchematic(stream);
    return structure;
  }
}
