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

package xyz.webmc.wlib.internal;

import xyz.webmc.wlib.api.WLIB;
import xyz.webmc.wlib.api.plugin.WPlugin;
import xyz.webmc.wlib.api.plugin.WPluginMeta;
import xyz.webmc.wlib.api.util.CommandUtil;
import xyz.webmc.wlib.api.util.EventUtil;
import xyz.webmc.wlib.api.util.ImageUtil;
import xyz.webmc.wlib.api.util.PermissionUtil;
import xyz.webmc.wlib.api.util.PlaceholderUtil;
import xyz.webmc.wlib.api.util.SchedulerUtil;
import xyz.webmc.wlib.devkit.annotation.PluginMeta;
import xyz.webmc.wlib.internal.command.WLIBBlankCommand;
import xyz.webmc.wlib.internal.command.WLIBCommand;
import xyz.webmc.wlib.internal.misc.WInfoFetcher;
import xyz.webmc.wlib.internal.util.BuildPropUtil;
import xyz.webmc.wlib.internal.util.InternalAgentUtil;
import xyz.webmc.wlib.internal.util.InternalUtil;
import xyz.webmc.wlib.internal.util.TestStructureUtil;

import java.util.Set;

import net.sandrohc.schematic4j.SchematicLoader;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.bukkit.event.Listener;

@WPluginMeta(shutdownOnFailure = true, bStats = 33913)
@PluginMeta(
  name = "${proj.name}",
  version = "${proj.vers}",
  authors = "${proj.athr}",
  website = "${proj.repo}",
  load = "STARTUP",
  softDepend = {
    "PlaceholderAPI",
    "LuckPerms",
    "Essentials",
    "EaglercraftXServer",
    "EaglercraftXBackendRPC"
  },
  foliaSupported = true
)
public final class WLIBBukkitPlugin extends WPlugin implements Listener {
  private static final Set<Class<?>> DISABLE_LOGGERS = Set.of(SchematicLoader.class);

  @Override
  protected void enable() throws Exception {
    if (InternalAgentUtil.getIsDynamicAttachmentSupported()) {
      WLIB._init(this);

      BuildPropUtil._init(this);
      InternalAgentUtil.init(this);
      InternalUtil.init(this);
      TestStructureUtil.init();

      WInfoFetcher.init(this);

      CommandUtil._init(this);
      EventUtil._init(this);
      ImageUtil._init(this);
      PermissionUtil._init();
      PlaceholderUtil._init();
      SchedulerUtil._init(this);

      EventUtil.registerEvents(new WLIBEventListener(this));

      CommandUtil.registerCommand(new WLIBCommand(WLIB.getWLIBKeyString()));
      CommandUtil.registerCommand(new WLIBBlankCommand(WLIB.getBlankCommandName()));
      CommandUtil.registerCommandAliases("wlib:wlib plugins", "wplugins", "wpl");
      CommandUtil.registerCommandAliases("wlib:wlib ascii", "wascii");
      CommandUtil.registerCommandAliases("wlib:wlib fetch", "wfetch", "neofetch");
      CommandUtil.registerCommandAliases("wlib:wlib debug", "wdebug", "wdbg");
      // CommandUtil.registerCommandAliases("wlib:wlib alerts", "walerts");
      CommandUtil.registerCommandAliases("wlib:wlib debug alert", "walert");
      CommandUtil.registerCommandAliases("wlib:wlib version", "wversion", "wver");

      // PermissionUtil.setGroupPermission("default", "wlib.alerts.muted.*", false);
    } else {
      throw new IllegalStateException();
    }
  }

  @Override
  protected void disable() {
    InternalAgentUtil.shutdown();
    SchedulerUtil._cancelAllTasks();
  }

  @Override
  protected void serverStartup() {
    WInfoFetcher.setStartup(System.currentTimeMillis());
  }

  static {
    System.setProperty("java.awt.headless", "true");
    DISABLE_LOGGERS.forEach(clazz -> Configurator.setLevel(clazz.getPackageName(), Level.OFF));
  }
}
