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

import xyz.webmc.wlib.api.plugin.WPlugin;
import xyz.webmc.wlib.internal.util.ModernServerUtil;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dev.colbster937.reflect.MirrorSafe;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import static xyz.webmc.wlib.internal.util.ModernServerUtil.requireModernServer;

public final class DatapackUtil implements ModernServerUtil {
  private static final List<Plugin> INIT_QUEUE = new ArrayList<>();
  private static Path DATAPACK_FOLDER;

  public static void _init(Plugin plugin) {
    final Class<?> clazz = MirrorSafe.getClass("org.bukkit.event.server.ServerLoadEvent");
    if (clazz != null) {
      EventUtil.registerEvent(
        clazz.asSubclass(Event.class),
        new Listener() {},
        EventPriority.NORMAL,
        (a, b) -> processQueue(),
        plugin
      );
    }
  }

  public static void _initWorld(World world) {
    requireModernServer();

    if (DATAPACK_FOLDER == null) {
      DATAPACK_FOLDER = world.getWorldFolder().toPath()
        .resolve("datapacks")
        .toAbsolutePath();
    }
  }

  public static void _initPlugin(Plugin plugin) {
    requireModernServer();

    if (DATAPACK_FOLDER != null) {
      __initPlugin(plugin);
    } else {
      INIT_QUEUE.add(plugin);
    }
  }

  public static void _shutdownPlugin(Plugin plugin) {
    requireModernServer();

    CommandUtil.dispatchConsole("minecraft:datapack disable " + getPluginDatapackString(plugin));
  }

  public static void enable(String datapack) {
    requireModernServer();

    CommandUtil.dispatchConsole("minecraft:datapack list available");
    CommandUtil.dispatchConsole("minecraft:datapack enable " + datapackString(datapack));
  }

  public static void disable(String datapack) {
    requireModernServer();

    CommandUtil.dispatchConsole("minecraft:datapack disable " + datapackString(datapack));
  }

  private static String datapackString(String datapack) {
    return "\"file/" + datapack + "\"";
  }

  private static String getPluginDatapackString(Plugin plugin) {
    return datapackString(plugin.getName() + ".zip");
  }

  private static void processQueue() {
    final Iterator<Plugin> it = INIT_QUEUE.iterator();
    while (it.hasNext()) {
      final Plugin plugin = it.next();
      _initPlugin(plugin);
      it.remove();
    }
  }

  private static void __initPlugin(Plugin plugin) {
    final WPlugin wPlugin = PluginUtil.getWPlugin(plugin);

    String resource = "datapack.zip";
    if (wPlugin != null) {
      final String path = wPlugin.getWPluginMeta().datapackPath();
      if (path != null && !path.isBlank()) {
        resource = path;
      }
    }

    try (InputStream is = plugin.getResource(resource)) {
      if (is != null) {
        final byte[] pack = is.readAllBytes();
        final String name = getPluginDatapackString(plugin);
        final Path out = DATAPACK_FOLDER.resolve(name).toAbsolutePath();
        final boolean exists = Files.exists(out);

        if (!exists || HashUtil.hash64(pack) != HashUtil.hash64(Files.readAllBytes(out))) {
          if (exists) {
            disable(name);
          }

          Files.write(out, pack);
        }

        enable(name);
      }
    } catch (Throwable t) {
    }
  }
}
