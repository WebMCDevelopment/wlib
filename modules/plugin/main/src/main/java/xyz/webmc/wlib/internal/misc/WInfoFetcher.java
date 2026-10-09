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

package xyz.webmc.wlib.internal.misc;

import xyz.webmc.wlib.api.plugin.WPlugin;
import xyz.webmc.wlib.api.util.PluginUtil;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;

public final class WInfoFetcher {
  private static final String[] BYTE_UNITS = { "KiB", "MiB", "GiB", "TiB", "PiB" };

  private static WPlugin plugin;
  private static long startup;

  public static void init(WPlugin _plugin) {
    plugin = _plugin;
    setStartup(System.currentTimeMillis());
  }

  public static void setStartup(long _startup) {
    startup = _startup;
  }

  public static Map<String, String> fetchInfo() {
    final Map<String, String> ret = new LinkedHashMap<>();

    ret.put(Bukkit.getVersion(), null);

    final List<String> uptimeStr = new ArrayList<>();
    final Duration uptime = Duration.ofMillis(System.currentTimeMillis() - startup);

    final long uptimeDays = uptime.toDays();
    final long uptimeHours = uptime.toHoursPart();
    final long uptimeMinutes = uptime.toMinutesPart();
    final long uptimeSeconds = uptime.toSecondsPart();

    if (uptimeDays > 0) {
      uptimeStr.add(uptimeDays + "d");
    }

    if (uptimeHours > 0) {
      uptimeStr.add(uptimeHours + "h");
    }

    if (uptimeMinutes > 0) {
      uptimeStr.add(uptimeMinutes + "m");
    }

    if (uptimeSeconds > 0) {
      uptimeStr.add(uptimeSeconds + "s");
    }

    ret.put("Uptime", String.join(" ", uptimeStr));
    ret.put("Players", Bukkit.getOnlinePlayers().size() + " / " + Bukkit.getMaxPlayers());

    ret.put("Plugins", Integer.toString(PluginUtil.getPlugins().length));
    ret.put("OS", System.getProperty("os.name") + " " + System.getProperty("os.version") + " " + System.getProperty("os.arch"));
    ret.put("Java", System.getProperty("java.version"));

    final Runtime runtime = Runtime.getRuntime();
    final long totalMemory = runtime.totalMemory();
    final long usedMemory = totalMemory - runtime.freeMemory();
    ret.put("Memory", formatBytes(usedMemory) + " / " + formatBytes(totalMemory));

    ret.put("WLIB", plugin.getVersion());

    return ret;
  }

  public static String formatBytes(long bytes) {
    if (bytes >= 1024) {
      double value = bytes;
      int unit = -1;

      do {
        value /= 1024;
        unit++;
      } while (value >= 1024 && unit < BYTE_UNITS.length - 1);

      return ((long) value) + BYTE_UNITS[unit];
    } else {
      return bytes + "B";
    }
  }
}
