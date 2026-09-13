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

import java.io.IOException;
import java.io.InputStream;

import net.sandrohc.schematic4j.exception.ParsingException;

public abstract class AbstractBaseSchemStructure extends AbstractBaseStructure {
  public AbstractBaseSchemStructure(String name, String resource) throws IOException, ParsingException {
    super(name);

    try (InputStream is = this.getClass().getResourceAsStream(resource)) {
      super.loadSchematic(is);
    }
  }
}
