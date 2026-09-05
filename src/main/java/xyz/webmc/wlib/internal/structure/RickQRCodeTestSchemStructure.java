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

import xyz.webmc.wlib.api.WLIB;
import xyz.webmc.wlib.api.structure.AbstractBaseStructure;

import java.io.IOException;
import java.io.InputStream;

import net.sandrohc.schematic4j.exception.ParsingException;

@Deprecated(forRemoval = true)
@SuppressWarnings({ "removal" })
public final class RickQRCodeTestSchemStructure extends AbstractBaseStructure {
  @Deprecated(forRemoval = true)
  public RickQRCodeTestSchemStructure() throws IOException, ParsingException {
    super("rick_qr");
    WLIB.warnDeprecatedUsage();

    try (InputStream is = RickQRCodeTestSchemStructure.class.getResourceAsStream("/schematics/rick.schem")) {
      super.loadSchematic(is);
    }
  }

  @Deprecated(forRemoval = true)
  public static RickQRCodeTestSchemStructure getInstance() {
    WLIB.warnDeprecatedUsage();
    return getInstance(RickQRCodeTestSchemStructure.class);
  }
}
