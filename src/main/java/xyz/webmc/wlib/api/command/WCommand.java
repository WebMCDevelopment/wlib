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

package xyz.webmc.wlib.api.command;

import xyz.webmc.wlib.api.WLIB;
import xyz.webmc.wlib.api.util.CommandUtil;
import xyz.webmc.wlib.api.util.TextUtil;

import java.util.List;

import dev.colbster937.reflect.MirrorSafe;
import dev.colbster937.util.ExceptionStacker;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class WCommand extends Command {
  protected WCommand(String name, String... aliases) {
    super(name, "", ChatColor.RED + "Incorrect usage for /" + name + ".", List.of(aliases));
    super.setPermissionMessage(ChatColor.RED + "You don't have permission to use this command.");
  }

  protected abstract boolean run(CommandSender sender, String label, String[] args);

  protected List<String> tab(CommandSender sender, String label, String[] args) {
    return List.of();
  }

  @Override
  public final boolean execute(CommandSender sender, String label, String[] args) {
    try {
      return this.run(sender, label, args);
    } catch (Throwable t) {
      this.showStack(sender, t);
      return true;
    }
  }

  @Override
  public final List<String> tabComplete(CommandSender sender, String label, String[] args) {
    try {
      return this.tab(sender, label, args);
    } catch (Throwable t) {
      this.showStack(sender, t);
      return List.of();
    }
  }

  public final void sendUsageMessage(CommandSender sender, String alias) {
    sender.sendMessage(this.replaceUsedAlias(super.getUsage(), alias));
  }

  public final void sendPermissionMessage(CommandSender sender, String alias) {
    sender.sendMessage(this.replaceUsedAlias(super.getPermission(), alias));
  }

  public final void sendUsageMessage(CommandSender sender) {
    this.sendUsageMessage(sender, "");
  }

  public final void sendPermissionMessage(CommandSender sender) {
    this.sendPermissionMessage(sender, "");
  }

  protected final boolean checkIsPlayer(CommandSender sender) {
    if (!CommandUtil.isPlayer(sender)) {
      sendOnlyPlayersMessage(sender);
      return false;
    } else {
      return true;
    }
  }

  protected final boolean checkHasPermission(CommandSender sender, String perm) {
    if (!sender.hasPermission(perm)) {
      this.sendPermissionMessage(sender);
      return false;
    } else {
      return true;
    }
  }

  public static void sendUnknownCommandMessage(CommandSender sender) {
    boolean bool = true;

    final Class<?> clazz = MirrorSafe.getClass("org.spigotmc.SpigotConfig");
    if (clazz != null) {
      final String msg = MirrorSafe.getFieldValue(clazz, "unknownCommandMessage");
      if (msg != null) {
        sender.sendMessage(msg);
        bool = false;
      }
    }

    if (bool) {
      sender.sendMessage(ChatColor.RED + "Unknown command.");
    }
  }

  public static void sendOnlyPlayersMessage(CommandSender sender) {
    sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
  }

  private void showStack(CommandSender sender, Throwable t) {
    final String stack = ExceptionStacker.getFullStackString(t);

    WLIB.getLogger().severe(stack);

    if (sender instanceof Player) {
      final String[] lines = TextUtil.serializeExceptionStackStringMultiline(stack);
      for (String line : lines) {
        sender.sendMessage(ChatColor.DARK_RED + line);
      }
    }
  }

  private String replaceUsedAlias(String str, String alias) {
    final Command aliasCommand = WLIB.getCurrentAlias();
    String replace = null;

    if (aliasCommand != null) {
      replace = aliasCommand.getName();
    } else {
      replace = alias;
    }

    if (str != null && !str.isBlank() && replace != null) {
      return str.replace("/" + super.getName(), "/" + replace);
    } else {
      return str;
    }
  }
}
