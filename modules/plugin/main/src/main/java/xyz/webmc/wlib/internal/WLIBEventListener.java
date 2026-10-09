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
import xyz.webmc.wlib.api.util.CommandUtil;
import xyz.webmc.wlib.api.util.PlayerUtil;
import xyz.webmc.wlib.api.util.SchedulerUtil;
import xyz.webmc.wlib.api.util.TextUtil;
import xyz.webmc.wlib.modern.api.util.DatapackUtil;

import java.util.Set;

import dev.colbster937.reflect.MirrorSafe;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.world.WorldInitEvent;

final class WLIBEventListener implements Listener {
  @EventHandler
  public void onServerCommand(ServerCommandEvent ev) {
    if (handleCommandEvent(ev.getSender(), ev.getCommand())) {
      if (ev instanceof Cancellable cancellable) {
        cancellable.setCancelled(true);
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

  @EventHandler
  public void onPlayerLogin(PlayerLoginEvent ev) {
    if (ev.getResult() == PlayerLoginEvent.Result.ALLOWED) {
      final Player player = ev.getPlayer();
      PlayerUtil._onPlayerLogin(
        player.getUniqueId(),
        player.getName(),
        ev.getHostname()
      );
    }
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent ev) {
    PlayerUtil._onPlayerQuit(ev.getPlayer());
  }

  private static boolean handleCommandEvent(CommandSender sender, String cmd) {
    cmd = cmd.trim();

    if (cmd.startsWith("/")) {
      cmd = cmd.substring(1);
    }

    if (sender.hasPermission("bukkit.command.plugins")) {
      final String[] split = cmd.split("\\s+", 2)[0].split(":", 2);
      final String[] ctx = new String[2];

      if (split.length == 1) {
        ctx[0] = "bukkit".trim();
        ctx[1] = split[0].trim();
      } else {
        ctx[0] = split[0].trim();
        ctx[1] = split[1].trim();
      }

      if (ctx[0].equals("bukkit") && (ctx[1].equals("plugins") || ctx[1].equals("pl"))) {
        final String name = "WLIB Plugins";
        final Set<String> plugins = WLIB.getWLIBPluginNameSet();
        if (MirrorSafe.getClassExists("io.papermc.paper.command.PaperPluginsCommand")) {
          final int type = !MirrorSafe.getClassExists("io.canvasmc.horizon.HorizonLoader") ? 3 : 4;
          if (type < 4 || !(sender instanceof ConsoleCommandSender)) {
            SchedulerUtil.runNextTick(() -> {
              MirrorSafe.invokeMethod(TextUtil.class, "sendStringListMessageType" + type, sender, name, plugins);
            });
          }
        } else {
          final String[] msg = CommandUtil.dispatchCapture(sender, cmd).get(0).split(": ");
          sender.sendMessage(ChatColor.GOLD + "Server " + msg[0] + ":");
          sender.sendMessage(ChatColor.DARK_GRAY + " - " + msg[1]);
          TextUtil.sendStringListMessageType3(sender, name, plugins);
          return true;
        }
      }
    }

    return false;
  }
}
