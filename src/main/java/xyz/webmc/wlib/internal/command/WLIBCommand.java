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

package xyz.webmc.wlib.internal.command;

import xyz.webmc.wlib.api.WLIB;
import xyz.webmc.wlib.api.command.WCommand;
import xyz.webmc.wlib.api.structure.AbstractBaseStructure;
import xyz.webmc.wlib.api.structure.AbstractGenerableStructure;
import xyz.webmc.wlib.api.util.PermissionUtil;
import xyz.webmc.wlib.api.util.SchedulerUtil;
import xyz.webmc.wlib.api.util.TextUtil;
import xyz.webmc.wlib.internal.structure.HerobrineShrineTestStructure;
import xyz.webmc.wlib.internal.structure.RickQRCodeTestSchemStructure;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public final class WLIBCommand extends WCommand {
  private final Plugin plugin;

  public WLIBCommand(final Plugin plugin) {
    super("wlib");
    this.plugin = plugin;
  }

  @Override
  public final boolean run(final CommandSender sender, final String label, final String[] args) {
    boolean bool1 = true;
    boolean bool2 = false;
    if (args.length > 0) {
      final String arg = args[0].trim();
      if ((arg.equals("plugins") || arg.equals("pl")) && (bool2 = sender.hasPermission("wlib.plugins"))) {
        TextUtil.sendStringListMessageType3(sender, "WLIB Plugins", WLIB.getWLIBPluginNames());
        bool1 = false;
      } else if ((arg.equals("version") || arg.equals("ver")) && (bool2 = sender.hasPermission("wlib.version"))) {
        sender.sendMessage(ChatColor.GREEN + "Running WLIB Version " + this.plugin.getDescription().getVersion());
        bool1 = false;
      } else if (args.length > 1) {
        if (arg.equals("alerts") && (bool2 = sender.hasPermission("wlib.alerts.dev"))) {
          final String ctx = args[1].trim();
          final int ret = PermissionUtil.toggleUserPermissionC(sender, ctx);
          bool1 = ret < 0;
          if (!bool1) {
            final String state;

            if (ret > 0) {
              state = ChatColor.GREEN + "ENABLED";
            } else {
              state = ChatColor.RED + "DISABLED";
            }

            sender.sendMessage(state + ChatColor.RESET + " alerts for " + ChatColor.AQUA + ctx);
          }
        } else if (arg.equals("debug") && (bool2 = sender.hasPermission("wlib.debug"))) {
          final String act = args[1].trim();
          if (act.equals("throw")) {
            throw new CommandException();
          } else if (act.equals("place")) {
            if (args.length >= 3) {
              bool1 = false;
              if (sender instanceof Player plr) {
                final String struct = args[2].trim();
                final AbstractBaseStructure structure;

                switch (struct) {
                  case "shrine" -> structure = new HerobrineShrineTestStructure();
                  case "rick" -> structure = new RickQRCodeTestSchemStructure();
                  default -> {
                    bool1 = true;
                    bool2 = false;
                    structure = null;
                  }
                }

                if (structure != null) {
                  final Location loc = plr.getLocation();
                  if (structure instanceof AbstractGenerableStructure generable) {
                    final long seed = generable.getGenerationSeed(loc.getChunk().getX(), loc.getChunk().getZ(), plr.getWorld().getSeed());
                    generable.build(seed).place(loc.clone());
                  } else {
                    structure.build().place(loc.clone());
                  }
                  SchedulerUtil.teleportAsync(plr, loc.clone().add(0, 1, 0).getBlock().getLocation().clone().add(0.5D, 0, 0.5D));
                  sender.sendMessage(ChatColor.GREEN + "Placed structure " + structure.getName());
                }
              } else {
                sendOnlyPlayersMessage(sender);
                return true;
              }
            }
          }
        }
      }
    }

    if (bool1) {
      if (!bool2) {
        super.sendUsageMessage(sender, label);
      } else {
        super.sendPermissionMessage(sender, label);
      }
    }

    return true;
  }

  @Override
  public final List<String> tab(final CommandSender sender, final String label, final String[] args) {
    final List<String> ret = new ArrayList<>();

    switch (args.length) {
      case 1 -> {
        if (sender.hasPermission("wlib.plugins")) {
          ret.add("plugins");
        }

        if (sender.hasPermission("wlib.version")) {
          ret.add("version");
        }

        if (sender.hasPermission("wlib.alerts.dev")) {
          ret.add("alerts");
        }

        if (sender.hasPermission("wlib.debug")) {
          ret.add("debug");
        }
      }

      case 2 -> {
        if (args[0].equals("debug") && sender.hasPermission("wlib.debug")) {
          ret.add("throw");
          ret.add("place");
        }
      }

      case 3 -> {
        if (args[0].equals("debug") && args[1].equals("place") && sender.hasPermission("wlib.debug")) {
          ret.add("shrine");
          ret.add("rick");
        }
      }
    }

    return ret;
  }
}
