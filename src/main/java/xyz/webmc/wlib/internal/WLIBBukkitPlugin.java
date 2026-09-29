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
import xyz.webmc.wlib.api.util.DatapackUtil;
import xyz.webmc.wlib.api.util.EventUtil;
import xyz.webmc.wlib.api.util.PermissionUtil;
import xyz.webmc.wlib.api.util.PlaceholderUtil;
import xyz.webmc.wlib.api.util.SchedulerUtil;
import xyz.webmc.wlib.devkit.annotation.InternalClass;
import xyz.webmc.wlib.devkit.annotation.PluginMeta;
import xyz.webmc.wlib.internal.command.WLIBBlankCommand;
import xyz.webmc.wlib.internal.command.WLIBCommand;
import xyz.webmc.wlib.internal.util.InternalAgentUtil;
import xyz.webmc.wlib.internal.util.InternalUtil;
import xyz.webmc.wlib.internal.util.TestStructureUtil;

import java.util.Set;

import net.sandrohc.schematic4j.SchematicLoader;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.bukkit.event.Listener;

@InternalClass
@WPluginMeta(shutdownOnFailure = true, bStats = 33913)
@PluginMeta(
  name = "${plugin.name}",
  version = "${plugin.vers}",
  authors = "${plugin.athr}",
  website = "${plugin.repo}",
  load = "STARTUP",
  softdepend = "${plugin.deps}",
  foliaSupported = true
)
public final class WLIBBukkitPlugin extends WPlugin implements Listener {
  private static final Set<Class<?>> DISABLE_LOGGERS = Set.of(SchematicLoader.class);

  @Override
  protected void enable() throws Exception {
    WLIB._init(this);

    InternalAgentUtil._init(this);
    InternalUtil._init(this);
    TestStructureUtil._init();

    CommandUtil._init(this);
    DatapackUtil._init(this);
    EventUtil._init(this);
    PermissionUtil._init();
    PlaceholderUtil._init();
    SchedulerUtil._init(this);

    EventUtil.registerEvents(new WLIBEventListener(this));

    CommandUtil.registerCommand(new WLIBCommand(WLIB.getWLIBKeyString()));
    CommandUtil.registerCommand(new WLIBBlankCommand(WLIB.getBlankCommandName()));
    CommandUtil.registerCommandAliases("wlib:wlib plugins", "wplugins", "wpl");
    CommandUtil.registerCommandAliases("wlib:wlib debug", "wdebug", "wdbg");
    // CommandUtil.registerCommandAliases("wlib:wlib alerts", "walerts");
    CommandUtil.registerCommandAliases("wlib:wlib debug alert", "walert");
    CommandUtil.registerCommandAliases("wlib:wlib version", "wversion", "wver");

    // PermissionUtil.setGroupPermission("default", "wlib.alerts.muted.*", false);
  }

  @Override
  protected void disable() {
    InternalAgentUtil._shutdown();
    SchedulerUtil._cancelAllTasks();
  }

  @Override
  protected void agentReady() {
  }

  static {
    for (Class<?> clazz : DISABLE_LOGGERS) {
      Configurator.setLevel(clazz.getPackageName(), Level.OFF);
    }
  }
}
