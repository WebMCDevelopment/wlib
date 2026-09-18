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
import xyz.webmc.wlib.api.util.TextUtil;
import xyz.webmc.wlib.internal.command.WLIBBlankCommand;
import xyz.webmc.wlib.internal.command.WLIBCommand;
import xyz.webmc.wlib.internal.iface.WInternal;
import xyz.webmc.wlib.internal.util.InternalAgentUtil;
import xyz.webmc.wlib.internal.util.InternalUtil;
import xyz.webmc.wlib.internal.util.MetricsUtil;
import xyz.webmc.wlib.internal.util.TestStructureUtil;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import dev.colbster937.reflect.Mirror;
import dev.colbster937.reflect.MirrorSafe;
import net.sandrohc.schematic4j.SchematicLoader;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.world.WorldInitEvent;

@WInternal
@WPluginMeta(shutdownOnFailure = true)
public final class WLIBBukkitPlugin extends WPlugin implements Listener {
  private static final Set<Class<?>> DISABLE_LOGGERS = Set.of(SchematicLoader.class);

  @Override
  protected void enable() throws Exception {
    WLIB._init(this);

    InternalAgentUtil._init(this);
    InternalUtil._init(this);
    MetricsUtil._init(this);
    TestStructureUtil._init();

    CommandUtil._init(this);
    DatapackUtil._init(this);
    EventUtil._init(this);
    PermissionUtil._init();
    PlaceholderUtil._init();
    SchedulerUtil._init(this);

    CommandUtil.registerCommand(new WLIBCommand());
    CommandUtil.registerCommand(new WLIBBlankCommand(WLIB.getBlankCommandName()));
    CommandUtil.registerCommandAliases("wlib:wlib plugins", "wplugins", "wpl");
    CommandUtil.registerCommandAliases("wlib:wlib debug", "wdebug", "wdbg");
    // CommandUtil.registerCommandAliases("wlib:wlib alerts", "walerts");
    CommandUtil.registerCommandAliases("wlib:wlib debug alert", "walert");
    CommandUtil.registerCommandAliases("wlib:wlib version", "wversion", "wver");

    final Class<?> clazz = MirrorSafe.getClass("org.bukkit.event.player.PlayerCommandSendEvent");
    if (clazz != null) {
      final Class<? extends Event> ev = clazz.asSubclass(Event.class);
      EventUtil.registerEvent(ev, new Listener() {}, EventPriority.MONITOR, (a, b) -> {
        final Collection<String> commands = MirrorSafe.invokeMethod(b, "getCommands");
        commands.remove(WLIB.getBlankCommandName());
        commands.remove(WLIB.getBlankCommandKey());
      }, this);
    }

    PermissionUtil.setGroupPermission("default", "wlib.alerts.muted.*", false);
  }

  @Override
  protected void disable() {
    InternalAgentUtil._shutdown();
    MetricsUtil._shutdown();
    SchedulerUtil._cancelAllTasks();
  }

  @EventHandler
  public void onServerCommand(ServerCommandEvent ev) {
    if (handleCommandEvent(ev.getSender(), ev.getCommand())) {
      if (Mirror.hasMethod(ev, "setCancelled", boolean.class)) {
        MirrorSafe.invokeMethod(ev, "setCancelled", true);
      } else {
        ev.setCommand(WLIB.getBlankCommandKey());
      }
    }
  }

  @EventHandler
  public void onPlayerCommand(PlayerCommandPreprocessEvent ev) {
    if (handleCommandEvent(ev.getPlayer(), ev.getMessage())) {
      ev.setCancelled(true);
    }
  }

  @EventHandler
  public void onWorldInit(WorldInitEvent ev) {
    if (WLIB.getIsModernServer()) {
      DatapackUtil._initWorld(ev.getWorld());
    }
  }

  private static boolean handleCommandEvent(CommandSender sender, String cmd) {
    cmd = cmd.trim();

    if (cmd.startsWith("/")) {
      cmd = cmd.substring(1);
    }

    if (sender.hasPermission("bukkit.command.plugins")) {
      final String[] split = cmd.split("\\s+", 2)[0].split(":", 2);
      final String ctx;

      if (split.length == 1) {
        ctx = "bukkit".trim();
        cmd = split[0].trim();
      } else {
        ctx = split[0].trim();
        cmd = split[1].trim();
      }

      if (ctx.equals("bukkit") && (cmd.equals("plugins") || cmd.equals("pl"))) {
        final String name = "WLIB Plugins";
        final List<String> plugins = WLIB.getWLIBPluginNames();
        if (MirrorSafe.getClassExists("io.papermc.paper.command.PaperPluginsCommand")) {
          final int type = !MirrorSafe.getClassExists("io.canvasmc.horizon.HorizonLoader") ? 3 : 4;
          if (type < 4 || !(sender instanceof ConsoleCommandSender)) {
            SchedulerUtil.runNextTick(() -> {
              MirrorSafe.invokeMethod(TextUtil.class, "sendStringListMessageType" + type, sender, name, plugins);
            });
          }
        } else {
          final String[] msg = CommandUtil.dispatchCapture(sender, cmd).get(0).split(": ");
          sender.sendMessage(ChatColor.GOLD + "Bukkit " + msg[0] + ":");
          sender.sendMessage(ChatColor.DARK_GRAY + " - " + msg[1]);
          TextUtil.sendStringListMessageType3(sender, name, plugins);
          return true;
        }
      }
    }

    return false;
  }

  static {
    for (Class<?> clazz : DISABLE_LOGGERS) {
      Configurator.setLevel(clazz.getPackageName(), Level.OFF);
    }
  }
}
