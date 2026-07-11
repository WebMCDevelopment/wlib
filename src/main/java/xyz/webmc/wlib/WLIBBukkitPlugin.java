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

package xyz.webmc.wlib;

import xyz.webmc.wlib.api.WLIB;
import xyz.webmc.wlib.api.misc.CaptureSender;
import xyz.webmc.wlib.api.util.CommandUtil;
import xyz.webmc.wlib.api.util.EventUtil;
import xyz.webmc.wlib.api.util.PermissionUtil;
import xyz.webmc.wlib.api.util.PlaceholderUtil;
import xyz.webmc.wlib.api.util.SchedulerUtil;
import xyz.webmc.wlib.api.util.TextUtil;
import xyz.webmc.wlib.internal.command.WLIBBlankCommand;
import xyz.webmc.wlib.internal.command.WLIBCommand;

import java.util.List;

import dev.colbster937.reflect.Mirror;
import dev.colbster937.reflect.MirrorSafe;
import net.sandrohc.schematic4j.SchematicLoader;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class WLIBBukkitPlugin extends JavaPlugin implements Listener {
  private static final List<String> DISABLE_LOGGERS = List.of(
    SchematicLoader.class.getPackageName()
  );

  @Override
  public final void onEnable() {
    WLIB.init(this.getLogger());
    CommandUtil.init(this);
    EventUtil.init(this);
    PermissionUtil.init();
    PlaceholderUtil.init();
    SchedulerUtil.init(this);
    WLIB.registerPlugin(this);
    CommandUtil.registerCommand(new WLIBCommand(this));
    CommandUtil.registerCommand(new WLIBBlankCommand());
    EventUtil.registerEvents(this);

    CommandUtil.registerCommandAliases("wlib:wlib plugins", "wplugins", "wpl");

    PermissionUtil.setGroupPermission("default", "wlib.alerts.dev.muted.*", false);
  }

  @Override
  public final void onDisable() {
    SchedulerUtil.cancelAllTasks();
  }

  @EventHandler
  public final void onServerCommand(final ServerCommandEvent ev) {
    if (handleCommandEvent(ev.getSender(), ev.getCommand())) {
      if (Mirror.hasMethod(ev.getClass(), "setCancelled", boolean.class)) {
        MirrorSafe.invokeMethod(ev, "setCancelled", true);
      } else {
        ev.setCommand(WLIBBlankCommand.getBlankRandomCommandKey());
      }
    }
  }

  @EventHandler
  public final void onPlayerCommand(final PlayerCommandPreprocessEvent ev) {
    if (handleCommandEvent(ev.getPlayer(), ev.getMessage())) {
      ev.setCancelled(true);
    }
  }

  private static boolean handleCommandEvent(final CommandSender sender, final String cmdLine) {
    String commandStr = cmdLine;

    if (commandStr.startsWith("/")) {
      commandStr = commandStr.substring(1);
    }

    if (sender.hasPermission("bukkit.command.plugins")) {
      final String[] split = commandStr.split("\\s+", 2)[0].split(":", 2);

      final String ctx;
      final String cmd;

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
          final int type = !MirrorSafe.getClassExists("io.canvasmc.horizon.HorizonLoader") ? 2 : 4;
          if (type < 4 || !(sender instanceof ConsoleCommandSender)) {
            SchedulerUtil.runNextTick(() -> {
              MirrorSafe.invokeMethod(TextUtil.class, "sendStringListMessageType" + type, sender, name, plugins);
            });
          }
        } else {
          final CaptureSender capture = new CaptureSender(sender);
          CommandUtil.dispatch(capture, commandStr);
          final String[] msg = capture.getMessages().get(0).split(": ");
          sender.sendMessage(ChatColor.GOLD + "Bukkit " + msg[0] + ":");
          sender.sendMessage(ChatColor.DARK_GRAY + " - " +  msg[1]);
          TextUtil.sendStringListMessageType3(sender, name, plugins);
          return true;
        }
      }
    }

    return false;
  }

  static {
    for (final String logger : DISABLE_LOGGERS) {
      Configurator.setLevel(
        logger,
        Level.OFF
      );
    }
  }
}
