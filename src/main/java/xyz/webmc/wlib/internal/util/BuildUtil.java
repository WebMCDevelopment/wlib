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

public final class BuildUtil {
  private static final Properties properties = new Properties();

  public static String getProperty(final String key) {
    return properties.getProperty(key).trim();
  }

  static {
    try (InputStream is = BuildUtil.class.getClassLoader().getResourceAsStream("build.properties")) {
      properties.load(is);
    } catch (IOException ex) {}
  }
}
