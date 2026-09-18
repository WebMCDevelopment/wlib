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

package xyz.webmc.wlib.internal.compat.api;

import xyz.webmc.wlib.api.WLIB;
import xyz.webmc.wlib.internal.compat.WCompat;

import java.util.List;

import org.bukkit.plugin.Plugin;

import static xyz.webmc.wlib.api.WLIB.*;

@WCompat(WLIB.class)
public abstract class WLIBCompat {
  @Deprecated(forRemoval = true)
  public static boolean requireWLIB(String ver) {
    warnDeprecatedUsage();
    return requireWLIBVersion(ver);
  }

  @Deprecated(forRemoval = true)
  public static void registerPlugin(Plugin plugin) {
    warnDeprecatedUsage();
    initPlugin(plugin);
  }

  @Deprecated(forRemoval = true)
  public static List<Plugin> getWLIBPlugins() {
    warnDeprecatedUsage();
    return getWLIBPluginList();
  }

  @Deprecated(forRemoval = true)
  public static List<String> getWLIBPluginNames() {
    return getWLIBPluginNameList();
  }

  @Deprecated(forRemoval = true)
  public static void devAlert(String... msg) {
    warnDeprecatedUsage();
    alert(msg);
  }
}
