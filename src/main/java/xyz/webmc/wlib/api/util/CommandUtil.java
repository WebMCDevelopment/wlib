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

package xyz.webmc.wlib.api.util;

import xyz.webmc.wlib.internal.command.AliasCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dev.colbster937.reflect.MirrorSafe;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public final class CommandUtil {
  private static Plugin plugin;

  public static void init(final Plugin plugin) {
    CommandUtil.plugin = plugin;
  }

  public static void registerCommand(final Plugin plugin, final Command command) {
    getCommandMap().register(plugin.getName(), command);
    syncCommands();
  }

  public static void registerCommand(final Command command) {
    registerCommand(plugin, command);
  }

  public static void registerCommands(final Plugin plugin, final List<Command> commands) {
    getCommandMap().registerAll(plugin.getName(), commands);
    syncCommands();
  }

  public static void registerCommands(final List<Command> commands) {
    registerCommands(plugin, commands);
  }

  public static void unregisterCommand(final String commandStr) {
    final Command command = getKnownCommands().remove(commandStr);

    if (command != null) {
      command.unregister(getCommandMap());
    }

    syncCommands();
  }

  public static void registerCommandAliases(final Plugin plugin, final String cmd, final String... aliases) {
    final List<Command> commands = new ArrayList<>();

    for (final String alias : aliases) {
      commands.add(new AliasCommand(cmd, alias));
    }

    registerCommands(plugin, commands);
  }

  public static void registerCommandAliases(final String cmd, final String... aliases) {
    registerCommandAliases(plugin, cmd, aliases);
  }

  public static boolean dispatch(final CommandSender sender, final String cmd) throws CommandException {
    return getCommandMap().dispatch(sender, cmd);
  }

  public static List<String> tabComplete(final CommandSender sender, final String cmd)
      throws IllegalArgumentException {
    return getCommandMap().tabComplete(sender, cmd);
  }

  public static Command getCommand(final String cmd) {
    return getCommandMap().getCommand(cmd);
  }

  private static CommandMap getCommandMap() {
    return MirrorSafe.getFieldValue(Bukkit.getPluginManager(), "commandMap");
  }

  private static Map<String, Command> getKnownCommands() {
    return MirrorSafe.getFieldValue(getCommandMap(), "knownCommands");
  }

  private static void syncCommands() {
    MirrorSafe.invokeMethod(Bukkit.getServer(), "syncCommands");
  }
}
