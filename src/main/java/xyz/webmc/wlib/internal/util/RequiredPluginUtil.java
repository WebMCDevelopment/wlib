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

import xyz.webmc.wlib.api.util.PluginUtil;
import xyz.webmc.wlib.internal.iface.WInternal;

import java.util.ArrayList;
import java.util.List;

@WInternal
public interface RequiredPluginUtil {
  static boolean checkPlugins(String... plugins) {
    final List<String> missing = new ArrayList<>();

    for (String plugin : plugins) {
      if (!PluginUtil.isPluginEnabled(plugin)) {
        missing.add(plugin);
      }
    }

    return missing.isEmpty();
  }
}
