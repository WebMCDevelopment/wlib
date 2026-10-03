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
import xyz.webmc.wlib.api.misc.PixelFormatter;
import xyz.webmc.wlib.api.structure.AbstractBaseStructure;
import xyz.webmc.wlib.api.util.CommandUtil;
import xyz.webmc.wlib.api.util.ImageUtil;
import xyz.webmc.wlib.api.util.PlayerUtil;
import xyz.webmc.wlib.api.util.SchedulerUtil;
import xyz.webmc.wlib.api.util.TextUtil;
import xyz.webmc.wlib.internal.misc.WInfoFetcher;
import xyz.webmc.wlib.internal.util.InternalUtil;
import xyz.webmc.wlib.internal.util.TestStructureUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class WLIBCommand extends WCommand {
  public WLIBCommand(String name) {
    super(name);
    InternalUtil.checkInternalCaller();
  }

  @Override
  protected boolean run(CommandSender sender, String label, String[] args) {
    boolean bool1 = true;
    boolean bool2 = false;
    boolean bool3 = false;

    if (args.length > 0) {
      final String arg = args[0].trim();
      if ((arg.equals("plugins") || arg.equals("pl")) && (bool2 = sender.hasPermission("wlib.command.plugins"))) {
        TextUtil.sendStringListMessageType3(sender, "WLIB Plugins", WLIB.getWLIBPluginNameSet());
        bool1 = false;
      } else if ((arg.equals("version") || arg.equals("ver")) && (bool2 = sender.hasPermission("wlib.command.version"))) {
        sender.sendMessage("Running WLIB Version " + ChatColor.BLUE + WLIB.getWLIBVersionString());
        bool1 = false;
      } else if ((arg.equals("ascii")) && (bool2 = sender.hasPermission("wlib.command.ascii"))) {
        sender.sendMessage(getASCII(sender));
        bool1 = false;
      } else if ((arg.equals("fetch")) && (bool2 = sender.hasPermission("wlib.command.fetch"))) {
        final Map<String, String> info = WInfoFetcher.fetchInfo();
        final List<String> lines = new ArrayList<>();

        sender.sendMessage("");

        String host = sender.getName().toLowerCase() + '@';
        if (sender instanceof Player player) {
          host += PlayerUtil.getPlayerVirtualHost(player).split(":")[0].toLowerCase();
        } else {
          host += Bukkit.getName().toLowerCase();
        }

        lines.add(ChatColor.RED + host);
        lines.add(ChatColor.GRAY + "-".repeat(host.length()));

        for (Map.Entry<String, String> line : info.entrySet()) {
          final String key = line.getKey();
          final String value = line.getValue();

          String msg = ChatColor.GOLD + key;
          if (value != null && !value.isBlank()) {
            msg += ChatColor.RESET + ": " + value;
          }

          lines.add(msg);
        }

        final boolean terminal = PixelFormatter.getIsCommandSenderTerminal(sender);
        final int repeat = !terminal ? 1 : 2;

        lines.add(
          (ChatColor.BLACK.toString() + PixelFormatter.PIXEL_BASE).repeat(repeat) +
          (ChatColor.DARK_RED.toString() + PixelFormatter.PIXEL_BASE).repeat(repeat) +
          (ChatColor.DARK_GREEN.toString() + PixelFormatter.PIXEL_BASE).repeat(repeat) +
          (ChatColor.GOLD.toString() + PixelFormatter.PIXEL_BASE).repeat(repeat) +
          (ChatColor.DARK_BLUE.toString() + PixelFormatter.PIXEL_BASE).repeat(repeat) +
          (ChatColor.DARK_PURPLE.toString() + PixelFormatter.PIXEL_BASE).repeat(repeat) +
          (ChatColor.DARK_AQUA.toString() + PixelFormatter.PIXEL_BASE).repeat(repeat) +
          (ChatColor.GRAY.toString() + PixelFormatter.PIXEL_BASE).repeat(repeat)
        );

        lines.add(
          (ChatColor.DARK_GRAY.toString() + PixelFormatter.PIXEL_BASE).repeat(repeat) +
          (ChatColor.RED.toString() + PixelFormatter.PIXEL_BASE).repeat(repeat) +
          (ChatColor.GREEN.toString() + PixelFormatter.PIXEL_BASE).repeat(repeat) +
          (ChatColor.YELLOW.toString() + PixelFormatter.PIXEL_BASE).repeat(repeat) +
          (ChatColor.BLUE.toString() + PixelFormatter.PIXEL_BASE).repeat(repeat) +
          (ChatColor.LIGHT_PURPLE.toString() + PixelFormatter.PIXEL_BASE).repeat(repeat) +
          (ChatColor.AQUA.toString() + PixelFormatter.PIXEL_BASE).repeat(repeat) +
          (ChatColor.WHITE.toString() + PixelFormatter.PIXEL_BASE).repeat(repeat)
        );

        if (terminal) {
          final String[] ascii = getASCII(sender);
          for (int i = 0; i < Math.max(ascii.length, lines.size()); i++) {
            String msg;

            if (i < ascii.length) {
              msg = ascii[i];
            } else {
              msg = Character.toString(PixelFormatter.PIXEL_NONE).repeat(ascii[0].length());
            }

            msg += PixelFormatter.PIXEL_NONE;

            if (i < lines.size()) {
              msg += lines.get(i);
            }

            sender.sendMessage(msg);
          }
        } else {
          for (String line : lines) {
            sender.sendMessage(ChatColor.DARK_GRAY + "$ " + ChatColor.RESET + line);
          }
        }

        sender.sendMessage("");

        bool1 = false;
      } else if (args.length > 1) {
        /* if (arg.equals("alerts") && (bool2 = sender.hasPermission("wlib.alerts"))) {
          final String ctx = args[1].trim();
          final int ret = PermissionUtil.toggleUserPermissionC(sender, "wlib.alerts.muted." + ctx);
          bool1 = ret < 0;

          if (!bool1) {
            final String state;

            if (ret > 0) {
              state = ChatColor.RED + "DISABLED";
            } else {
              state = ChatColor.GREEN + "ENABLED";
            }

            sender.sendMessage(state + ChatColor.RESET + " alerts for " + ChatColor.AQUA + ctx);
          }
        } else */
        if (arg.equals("debug") && (bool2 = sender.hasPermission("wlib.command.debug"))) {
          final String act = args[1].trim();
          if (act.equals("throw")) {
            bool1 = false;
            throw new CommandException();
          } else if (act.equals("place") && args.length > 2) {
            bool1 = false;
            if (sender instanceof Player player) {
              final String struct = args[2].trim();
              final AbstractBaseStructure structure = TestStructureUtil.getTestStructure(struct);

              if (structure != null) {
                final Location loc = player.getLocation();
                structure.place(loc.clone());
                SchedulerUtil.teleportAsync(player, loc.clone().add(1, 1, 0).getBlock().getLocation().clone().add(0.5D, 0, 0.5D));
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
  protected List<String> tab(CommandSender sender, String label, String[] args) {
    if (args.length > 0) {
      final List<String> ret = new ArrayList<>();

      if (args.length == 1) {
        if (sender.hasPermission("wlib.command.plugins")) {
          ret.add("plugins");
        }

        if (sender.hasPermission("wlib.command.version")) {
          ret.add("version");
        }

        if (sender.hasPermission("wlib.command.ascii")) {
          ret.add("ascii");
        }

        if (sender.hasPermission("wlib.command.fetch")) {
          ret.add("fetch");
        }

        /* if (sender.hasPermission("wlib.alerts")) {
          ret.add("alerts");
        } */

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
        if (args[0].equals("debug") && args[1].equals("place") && CommandUtil.isPlayer(sender) && sender.hasPermission("wlib.command.debug")) {
          ret.addAll(TestStructureUtil.getStructureNames());
        }
      }

      return ret;
    } else {
      return List.of();
    }
  }

  private static String[] getASCII(CommandSender sender) {
    return ImageUtil.convertToMinecraftASCII(
      ImageUtil.removeAlpha(ImageUtil.scaleImage(ImageUtil.getWLIBImage(), .75f)),
      PixelFormatter.getFromCommandSender(sender)
    );
  }
}
