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

package xyz.webmc.wlib.internal.structure;

import xyz.webmc.wlib.api.structure.RelativeStructure;
import xyz.webmc.wlib.api.structure.placeable.RelativePlaceableStructure;
import xyz.webmc.wlib.internal.util.TestStructureUtil;

import java.io.InputStream;

public final class RickQRCodeTestStructure extends RelativeStructure {
  @Override
  public RelativePlaceableStructure build() {
    final RelativePlaceableStructure structure = new RelativePlaceableStructure();
    final InputStream stream = RickQRCodeTestStructure.class.getResourceAsStream("/schematics/rick.schem");
    structure.loadSchematic(stream);
    return structure;
  }

  public static RickQRCodeTestStructure getInstance() {
    return TestStructureUtil.getInstance(RickQRCodeTestStructure.class);
  }

  static {
    TestStructureUtil.registerInstance(RickQRCodeTestStructure.class);
  }
}
