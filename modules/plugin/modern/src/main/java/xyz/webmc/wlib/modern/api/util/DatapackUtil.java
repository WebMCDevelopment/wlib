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

package xyz.webmc.wlib.modern.api.util;

import xyz.webmc.wlib.api.util.HashUtil;
import xyz.webmc.wlib.internal.iface.IWPlugin;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import static xyz.webmc.wlib.internal.util.InternalUtil.checkInternalCaller;

public class DatapackUtil {
  private static final List<Plugin> INIT_QUEUE = new ArrayList<>();
  private static Consumer<String> dispatchConsole;
  private static Path datapackFolder;

  public static void _init(Consumer<String> _dispatchConsole) {
    checkInternalCaller();

    dispatchConsole = _dispatchConsole;
  }

  public static void _initWorld(World world) {
    checkInternalCaller();

    if (datapackFolder == null) {
      datapackFolder = world.getWorldFolder().toPath()
        .resolve("datapacks")
        .toAbsolutePath();
    }
  }

  public static void _initPlugin(Plugin plugin) {
    checkInternalCaller();

    if (datapackFolder != null) {
      __initPlugin(plugin);
    } else {
      synchronized (INIT_QUEUE) {
        INIT_QUEUE.add(plugin);
      }
    }
  }

  public static void _shutdownPlugin(Plugin plugin) {
    checkInternalCaller();

    dispatchConsole.accept("minecraft:datapack disable " + getPluginDatapackString(plugin));
  }

  public static void enableDatapack(String datapack) {
    dispatchConsole.accept("minecraft:datapack list available");
    dispatchConsole.accept("minecraft:datapack enable " + datapackString(datapack));
  }

  public static void disableDatapack(String datapack) {
    dispatchConsole.accept("minecraft:datapack disable " + datapackString(datapack));
  }

  public static void _processQueue() {
    checkInternalCaller();

    synchronized (INIT_QUEUE) {
      INIT_QUEUE.forEach(DatapackUtil::__initPlugin);
      INIT_QUEUE.clear();
    }
  }

  private static String datapackString(String datapack) {
    return "\"file/" + datapack + "\"";
  }

  private static String getPluginDatapackString(Plugin plugin) {
    return datapackString(plugin.getName() + ".zip");
  }

  private static void __initPlugin(Plugin plugin) {
    String resource = "datapack.zip";
    if (plugin instanceof IWPlugin wPlugin) {
      final String path = wPlugin.getWPluginMeta().datapackPath();
      if (path != null && !path.isBlank()) {
        resource = path;
      }
    }

    final String name = getPluginDatapackString(plugin);
    final Path out = datapackFolder.resolve(name).toAbsolutePath();
    final boolean exists = Files.exists(out);

    try (InputStream is = plugin.getResource(resource)) {
      if (is != null) {
        final byte[] pack = is.readAllBytes();

        if (!exists || HashUtil.hash64(pack) != HashUtil.hash64(Files.readAllBytes(out))) {
          if (exists) {
            enableDatapack(name);
          }

          Files.write(out, pack);
        }

        enableDatapack(name);
      } else if (exists) {
        enableDatapack(name);
        Files.delete(out);
      }
    } catch (Exception ex) {}
  }
}
