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

import xyz.webmc.wlib.internal.util.ModernServerRequiredUtil;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import dev.colbster937.reflect.MirrorSafe;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public final class DatapackUtil extends ModernServerRequiredUtil {
  private static final List<Plugin> INIT_QUEUE = new ArrayList<>();
  private static Path DATAPACK_FOLDER;

  public static void _init(final Plugin plugin) {
    if (MirrorSafe.getClassExists("org.bukkit.event.server.ServerLoadEvent")) {
      final Class<?> clazz = MirrorSafe.getClass("org.bukkit.event.server.ServerLoadEvent");
      final Class<? extends Event> ev = clazz.asSubclass(Event.class);
      final Listener listener = new Listener() {};
      EventUtil.registerEvent(ev, listener, EventPriority.NORMAL, (x, y) -> processQueue(), plugin);
    }
  }

  public static void initWorld(final World world) {
    checkIsModernServer();

    if (DATAPACK_FOLDER == null) {
      DATAPACK_FOLDER = world.getWorldFolder().toPath()
        .resolve("datapacks")
        .toAbsolutePath();
    }
  }

  public static void initPlugin(final Plugin plugin) {
    checkIsModernServer();

    if (DATAPACK_FOLDER != null) {
      _initPlugin(plugin);
    } else {
      INIT_QUEUE.add(plugin);
    }
  }

  public static void enable(final String datapack) {
    checkIsModernServer();

    CommandUtil.dispatchConsole("minecraft:datapack list available");
    CommandUtil.dispatchConsole("minecraft:datapack enable " + datapackString(datapack));
  }

  public static void disable(final String datapack) {
    checkIsModernServer();

    CommandUtil.dispatchConsole("minecraft:datapack disable " + datapackString(datapack));
  }

  private static String datapackString(final String datapack) {
    return "\"file/" + datapack + "\"";
  }

  private static void processQueue() {
    final Iterator<Plugin> it = INIT_QUEUE.iterator();
    while (it.hasNext()) {
      final Plugin plugin = it.next();
      _initPlugin(plugin);
      it.remove();
    }
  }

  private static void _initPlugin(final Plugin plugin) {
    try (final JarFile jar = new JarFile(Path.of(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).toAbsolutePath().toString())) {
      final JarEntry packEntry = jar.getJarEntry("datapack.zip");
      if (packEntry != null) {
        final byte[] pack = jar.getInputStream(packEntry).readAllBytes();
        final long hash = HashUtil.hash64(pack);

        final String outName = plugin.getName() + ".zip";
        final Path out = DATAPACK_FOLDER.resolve(outName).toAbsolutePath();
        final boolean outExists = Files.exists(out);

        if (!outExists || hash != HashUtil.hash64(Files.readAllBytes(out))) {
          if (outExists) {
            disable(outName);
          }

          Files.copy(new ByteArrayInputStream(pack), out, StandardCopyOption.REPLACE_EXISTING);
        }

        enable(outName);
      }
    } catch (final Throwable t) {
    }
  }
}
