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

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.Plugin;

public final class MetricsUtil {
  private static final int PLUGIN_ID = 33913;
  private static Metrics metrics;

  public static void _init(Plugin plugin) {
    _shutdown();
    metrics = new Metrics(plugin, PLUGIN_ID);
  }

  public static void _shutdown() {
    if (metrics != null) {
      metrics.shutdown();
    }
  }
}
