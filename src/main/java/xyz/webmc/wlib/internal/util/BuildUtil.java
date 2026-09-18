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

import xyz.webmc.wlib.internal.iface.WInternal;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@WInternal
public final class BuildUtil {
  private static final Properties PROPERTIES = new Properties();

  public static String getProperty(String key) {
    return PROPERTIES.getProperty(key).trim();
  }

  static {
    try (InputStream is = BuildUtil.class.getResourceAsStream("/build.properties")) {
      PROPERTIES.load(is);
    } catch (IOException ex) {}
  }
}
