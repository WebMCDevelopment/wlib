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

import xyz.webmc.wlib.api.structure.AbstractBaseSchemStructure;
import xyz.webmc.wlib.api.structure.iface.TestStructure;
import xyz.webmc.wlib.internal.iface.WInternal;

import java.io.IOException;

import net.sandrohc.schematic4j.exception.ParsingException;

@WInternal
public final class RickQRCodeTestSchemStructure extends AbstractBaseSchemStructure implements TestStructure {
  public RickQRCodeTestSchemStructure() throws IOException, ParsingException {
    super("rick_qr", "/schematics/rick.schem");
  }
}
