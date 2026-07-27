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

package xyz.webmc.wlib.api.util;

import xyz.webmc.wlib.api.WLIB;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import net.sandrohc.schematic4j.SchematicLoader;
import net.sandrohc.schematic4j.exception.ParsingException;
import net.sandrohc.schematic4j.schematic.Schematic;
import org.bukkit.Bukkit;

public final class SchemUtil {
  public static Schematic readSchematic(final InputStream is) throws IOException, ParsingException {
    warnSchemUnsupportedServerVersion();
    return SchematicLoader.load(is);
  }

  public static Schematic readSchematic(final File file) throws IOException, ParsingException {
    return readSchematic(new FileInputStream(file));
  }

  public static void warnSchemUnsupportedServerVersion() {
    if (!WLIB.getIsModernServer()) {
      WLIB.getLogger().log(Level.WARNING, "Schematic loading is unsupported on server version {0}.",
          Bukkit.getVersion());
    }
  }
}
