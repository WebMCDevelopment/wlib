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

package xyz.webmc.wlib.internal.util;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.bukkit.plugin.Plugin;

public final class BuildPropUtil {
  private static final Properties PROPERTIES = new Properties();

  public static void _init(Plugin plugin) throws IOException {
    try (InputStream is = plugin.getResource("resources/generated/build.properties")) {
      PROPERTIES.load(is);
    }
  }

  public static String getProperty(String key) {
    return PROPERTIES.getProperty(key).trim();
  }
}
