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
import xyz.webmc.wlib.api.misc.iface.EventRunnable;
import xyz.webmc.wlib.api.util.CommandUtil;
import xyz.webmc.wlib.api.util.DatapackUtil;
import xyz.webmc.wlib.api.util.EventUtil;
import xyz.webmc.wlib.api.util.PlayerUtil;
import xyz.webmc.wlib.api.util.SchedulerUtil;
import xyz.webmc.wlib.api.util.TextUtil;
import xyz.webmc.wlib.internal.util.InternalUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import dev.colbster937.reflect.MirrorSafe;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.plugin.Plugin;

public final class WLIBEventListener implements Listener {
  private static final List<Runnable> SERVER_STARTUP_CALLBACKS = Collections.synchronizedList(new ArrayList<>());
  private static boolean serverStarted = false;

  WLIBEventListener(Plugin plugin) {
    Class<?> clazz;

    if ((clazz = MirrorSafe.getClass("org.bukkit.event.player.PlayerCommandSendEvent")) != null) {
      EventUtil.registerEvent(clazz.asSubclass(Event.class), this, EventPriority.MONITOR, (l, ev) -> {
        final Collection<String> commands = MirrorSafe.invokeMethod(ev, "getCommands");
        commands.remove(WLIB.getBlankCommandName());
        commands.remove(WLIB.getBlankCommandKey());
      }, plugin);
    }

    if ((clazz = MirrorSafe.getClass("org.bukkit.event.server.ServerLoadEvent")) != null) {
      EventUtil.registerEvent(
        clazz.asSubclass(Event.class),
        this,
        EventPriority.MONITOR,
        this::onServerLoad,
        plugin
      );
    }

    final Class<? extends Event> login;
    final EventRunnable<? extends Event> handler;
    if (MirrorSafe.getMethodExists(AsyncPlayerPreLoginEvent.class, "getHostname")) {
      login = AsyncPlayerPreLoginEvent.class;
      handler = (ev) -> {
        if (ev instanceof AsyncPlayerPreLoginEvent lev) {
          if (lev.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            PlayerUtil._onPlayerLogin(
              lev.getUniqueId(),
              MirrorSafe.invokeMethod(lev, "getHostname")
            );
          }
        }
      };
    } else {
      login = PlayerLoginEvent.class;
      handler = (ev) -> {
        if (ev instanceof PlayerLoginEvent lev) {
          if (lev.getResult() == PlayerLoginEvent.Result.ALLOWED) {
            PlayerUtil._onPlayerLogin(
              lev.getPlayer().getUniqueId(),
              lev.getHostname()
            );
          }
        }
      };
    }

    EventUtil.registerEvent(login, this, EventPriority.MONITOR, handler, plugin);
  }

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
  public void onPlayerQuit(PlayerQuitEvent ev) {
    PlayerUtil._onPlayerQuit(ev.getPlayer());
  }

  public static void _onServerStartup(Runnable callback) {
    InternalUtil.checkInternalCaller();
    if (!serverStarted) {
      synchronized (SERVER_STARTUP_CALLBACKS) {
        SERVER_STARTUP_CALLBACKS.add(callback);
      }
    } else {
      callback.run();
    }
  }

  private void onServerLoad(Event ev) {
    if (MirrorSafe.invokeMethod(ev, "getType").toString().equals("STARTUP")) {
      serverStarted = true;

      synchronized (SERVER_STARTUP_CALLBACKS) {
        SERVER_STARTUP_CALLBACKS.forEach(Runnable::run);
        SERVER_STARTUP_CALLBACKS.clear();
      }

      DatapackUtil._processQueue();
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
