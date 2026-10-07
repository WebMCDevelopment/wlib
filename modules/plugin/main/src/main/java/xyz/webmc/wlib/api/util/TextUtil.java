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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class TextUtil {
  public static String parsePlaceholders(Player plr, String txt) {
    return PlaceholderUtil.parsePlaceholders(plr, txt);
  }

  public static String serializeExceptionStackString(String stack) {
    return stack
        .replaceAll("\t", "    ")
        .replaceAll("[\\p{Cntrl}&&[^\\r\\n]]", "");
  }

  public static String[] serializeExceptionStackStringMultiline(String stack) {
    return serializeExceptionStackString(stack).split("\\R");
  }

  public static void sendStringListMessageType1(CommandSender sender, String name, Collection<String> collection) {
    sender.sendMessage(name + " (" + collection.size() + "): " + getStringListMessage(collection));
  }

  public static void sendStringListMessageType2(CommandSender sender, String name, Collection<String> collection) {
    sendPluginName24(sender, name);
    sender.sendMessage(ChatColor.DARK_GRAY + " - " + getStringListMessage(collection));
  }

  public static void sendStringListMessageType3(CommandSender sender, String name, Collection<String> collection) {
    sendStringListMessageType2(sender, name + " (" + collection.size() + ")", collection);
  }

  public static void sendStringListMessageType4(CommandSender sender, String name, Collection<String> collection) {
    sendPluginName24(sender, name);
    sender.sendMessage(ChatColor.DARK_GRAY + " - [" + getStringListMessage(ChatColor.DARK_GRAY, collection) + ChatColor.DARK_GRAY + "]");
  }

  public static String getStringListMessage(ChatColor strColor, ChatColor sepColor, Collection<String> collection) {
    final List<String> lst = new ArrayList<>(collection);
    lst.sort(String.CASE_INSENSITIVE_ORDER);
    return strColor + String.join(sepColor + ", " + strColor, lst);
  }

  public static String getStringListMessage(ChatColor sepColor, Collection<String> collection) {
    return getStringListMessage(ChatColor.GREEN, sepColor, collection);
  }

  public static String getStringListMessage(Collection<String> collection) {
    return getStringListMessage(ChatColor.RESET, collection);
  }

  private static void sendPluginName24(CommandSender sender, String name) {
    sender.sendMessage(ChatColor.BLUE + name + ":");
  }
}
