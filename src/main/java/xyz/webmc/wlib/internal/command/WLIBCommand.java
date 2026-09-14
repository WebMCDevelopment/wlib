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
import xyz.webmc.wlib.api.structure.BaseStructure;
import xyz.webmc.wlib.api.util.CommandUtil;
import xyz.webmc.wlib.api.util.SchedulerUtil;
import xyz.webmc.wlib.api.util.TextUtil;
import xyz.webmc.wlib.internal.util.TestStructureUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class WLIBCommand extends WCommand {
  public WLIBCommand() {
    super("wlib");
  }

  @Override
  public boolean run(CommandSender sender, String label, String[] args) {
    boolean bool1 = true;
    boolean bool2 = false;
    boolean bool3 = false;

    if (args.length > 0) {
      final String arg = args[0].trim();
      if ((arg.equals("plugins") || arg.equals("pl")) && (bool2 = sender.hasPermission("wlib.command.plugins"))) {
        TextUtil.sendStringListMessageType3(sender, "WLIB Plugins", WLIB.getWLIBPluginNames());
        bool1 = false;
      } else if ((arg.equals("version") || arg.equals("ver"))
          && (bool2 = sender.hasPermission("wlib.command.version"))) {
        sender.sendMessage("Running WLIB Version " + ChatColor.BLUE + WLIB.getWLIBVersionString());
        bool1 = false;
      } else if (args.length > 1) {
        /*
         * if (arg.equals("alerts") && (bool2 = sender.hasPermission("wlib.alerts"))) {
         * final String ctx = args[1].trim();
         * final int ret = PermissionUtil.toggleUserPermissionC(sender,
         * "wlib.alerts.muted." + ctx);
         * bool1 = ret < 0;
         *
         * if (!bool1) {
         * final String state;
         *
         * if (ret > 0) {
         * state = ChatColor.RED + "DISABLED";
         * } else {
         * state = ChatColor.GREEN + "ENABLED";
         * }
         *
         * sender.sendMessage(state + ChatColor.RESET + " alerts for " + ChatColor.AQUA
         * + ctx);
         * }
         * } else
         */
        if (arg.equals("debug") && (bool2 = sender.hasPermission("wlib.command.debug"))) {
          final String act = args[1].trim();
          if (act.equals("throw")) {
            bool1 = false;
            throw new CommandException();
          } else if (act.equals("place") && args.length > 2) {
            bool1 = false;
            if (sender instanceof Player plr) {
              final String struct = args[2].trim();
              final BaseStructure structure = TestStructureUtil.getTestStructure(struct);

              if (structure != null) {
                final Location loc = plr.getLocation();
                structure.place(loc.clone());
                SchedulerUtil.teleportAsync(plr,
                    loc.clone().add(1, 1, 0).getBlock().getLocation().clone().add(0.5D, 0, 0.5D));
                sender.sendMessage(ChatColor.GREEN + "Placed structure " + structure.getName());
              } else {
                bool3 = true;
              }
            } else {
              sendOnlyPlayersMessage(sender);
            }
          } else if (act.equals("alert")) {
            if (args.length > 2) {
              bool1 = false;

              final String[] msg = Arrays.copyOfRange(args, 2, args.length);

              WLIB.alert(msg);
            } else {
              bool3 = true;
            }
          } else if (act.equals("deprecation")) {
            bool1 = false;
            WLIB.warnDeprecatedUsage();
          } else {
            bool3 = true;
          }
        }
      }
    }

    if (bool3) {
      bool1 = true;
      bool2 = false;
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
  public List<String> tab(CommandSender sender, String label, String[] args) {
    final List<String> ret = new ArrayList<>();

    if (args.length == 1) {
      if (sender.hasPermission("wlib.command.plugins")) {
        ret.add("plugins");
      }

      if (sender.hasPermission("wlib.command.version")) {
        ret.add("version");
      }

      /*
       * if (sender.hasPermission("wlib.alerts")) {
       * ret.add("alerts");
       * }
       */

      if (sender.hasPermission("wlib.command.debug")) {
        ret.add("debug");
      }
    } else if (args.length == 2) {
      if (args[0].equals("debug") && sender.hasPermission("wlib.command.debug")) {
        ret.add("throw");

        if (CommandUtil.isPlayer(sender)) {
          ret.add("place");
        }

        ret.add("alert");
        ret.add("deprecation");
      }
    } else if (args.length == 3) {
      if (args[0].equals("debug") && args[1].equals("place") && CommandUtil.isPlayer(sender)
          && sender.hasPermission("wlib.command.debug")) {
        ret.addAll(TestStructureUtil.getStructureNames());
        if (args[0].equals("debug") && args[1].equals("place") && sender.hasPermission("wlib.debug")) {
          ret.add("shrine");
          ret.add("rick");
          ret.add("coord");
        }
      }

      return ret;
    } else {
      return ret;
    }

    return List.of();
  }
}
