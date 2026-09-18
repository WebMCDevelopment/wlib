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

import xyz.webmc.wlib.api.misc.CaptureSender;
import xyz.webmc.wlib.internal.command.AliasCommand;
import xyz.webmc.wlib.internal.util.InternalUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import dev.colbster937.reflect.MirrorSafe;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public final class CommandUtil {
  private static final Map<String, String[]> ALIASES = new ConcurrentHashMap<>();
  private static Plugin plugin;

  public static void _init(Plugin plugin) {
    InternalUtil.checkInternalCaller();
    CommandUtil.plugin = plugin;
  }

  public static void registerCommand(Plugin plugin, Command command) {
    getCommandMap().register(plugin.getName(), command);
    syncCommands();
  }

  public static void registerCommand(Command command) {
    registerCommand(plugin, command);
  }

  public static void registerCommands(Plugin plugin, List<Command> commands) {
    getCommandMap().registerAll(plugin.getName(), commands);
    syncCommands();
  }

  public static void registerCommands(List<Command> commands) {
    registerCommands(plugin, commands);
  }

  public static void unregisterCommand(String cmd) {
    final Command command = getKnownCommands().remove(cmd);

    if (command != null) {
      command.unregister(getCommandMap());
    }

    syncCommands();
  }

  public static void registerCommandAliases(Plugin plugin, String cmd, String... aliases) {
    final List<Command> commands = new ArrayList<>();

    cmd = cmd.trim();

    for (String alias : aliases) {
      commands.add(new AliasCommand(cmd, alias));
    }

    registerCommands(plugin, commands);
  }

  public static void registerCommandAliases(String cmd, String... aliases) {
    registerCommandAliases(plugin, cmd, aliases);
  }

  public static void unregisterCommandAliases(String cmd) {
    final String[] aliases = ALIASES.get(cmd.trim());
    if (aliases != null) {
      for (String alias : aliases) {
        unregisterCommand(alias);
      }
    }
  }

  public static boolean dispatch(CommandSender sender, String cmd) throws CommandException {
    return getCommandMap().dispatch(sender, cmd);
  }

  public static List<String> tabComplete(CommandSender sender, String cmd) throws IllegalArgumentException {
    return getCommandMap().tabComplete(sender, cmd);
  }

  public static boolean isPlayer(CommandSender sender) {
    return sender instanceof Player;
  }

  public static List<String> dispatchCapture(CommandSender sender, String cmd) throws CommandException {
    final CaptureSender capture = new CaptureSender(Bukkit.getConsoleSender());
    dispatch(capture, cmd);
    return capture.getMessages();
  }

  public static boolean dispatchConsole(String cmd) throws CommandException {
    return dispatch(Bukkit.getConsoleSender(), cmd);
  }

  public static List<String> dispatchConsoleCapture(String cmd) throws CommandException {
    return dispatchCapture(Bukkit.getConsoleSender(), cmd);
  }

  public static List<String> tabCompleteConsole(String cmd) throws IllegalArgumentException {
    return tabComplete(Bukkit.getConsoleSender(), cmd);
  }

  public static Command getCommand(String cmd) {
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
