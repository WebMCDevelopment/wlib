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

import xyz.webmc.wlib.api.structure.AbstractBaseStructure;
import xyz.webmc.wlib.api.structure.PlaceableStructure;

import java.io.IOException;
import java.io.InputStream;

import net.sandrohc.schematic4j.exception.ParsingException;

public final class RickQRCodeTestSchemStructure extends AbstractBaseStructure {
  public RickQRCodeTestSchemStructure(){
    super("rick_qr");
  }

  @Override
  public PlaceableStructure build(final long seed) {
    final PlaceableStructure structure = new PlaceableStructure(this.getName());

    try (InputStream is = RickQRCodeTestSchemStructure.class.getResourceAsStream("/schematics/rick.schem")) {
      structure.loadSchematic(is);
    } catch (final IOException | ParsingException ex) {
      throw new RuntimeException("Failed to load schematic for structure " + this.getName(), ex);
    }

    return structure;
  }
}
