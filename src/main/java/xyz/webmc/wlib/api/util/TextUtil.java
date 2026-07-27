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

import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class TextUtil {
  public static String parsePlaceholders(final Player player, final String txt) {
    return PlaceholderUtil.parsePlaceholders(player, txt);
  }

  public static String serializeExceptionStackString(final String stack) {
    return stack
        .replaceAll("\t", "    ")
        .replaceAll("[\\p{Cntrl}&&[^\\r\\n]]", "");
  }

  public static String[] serializeExceptionStackStringMultiline(final String stack) {
    return serializeExceptionStackString(stack).split("\\R");
  }

  public static void sendStringListMessageType1(final CommandSender sender, final String name, final List<String> lst) {
    sender.sendMessage(name + " (" + lst.size() + "): " + getStringListMessage(lst));
  }

  public static void sendStringListMessageType2(final CommandSender sender, final String name, final List<String> lst) {
    sendPluginName24(sender, name);
    sender.sendMessage(ChatColor.DARK_GRAY + " - " + getStringListMessage(lst));
  }

  public static void sendStringListMessageType3(final CommandSender sender, final String name, final List<String> lst) {
    sendStringListMessageType2(sender, name + " (" + lst.size() + ")", lst);
  }

  public static void sendStringListMessageType4(final CommandSender sender, final String name, final List<String> lst) {
    sendPluginName24(sender, name);
    sender.sendMessage(
        ChatColor.DARK_GRAY + " - [" + getStringListMessage(ChatColor.DARK_GRAY, lst) + ChatColor.DARK_GRAY + "]");
  }

  public static String getStringListMessage(final ChatColor strColor, final ChatColor sepColor,
      final List<String> lst) {
    Collections.sort(lst, String.CASE_INSENSITIVE_ORDER);
    return strColor + String.join(sepColor + ", " + strColor, lst);
  }

  public static String getStringListMessage(final ChatColor sepColor, final List<String> lst) {
    return getStringListMessage(ChatColor.GREEN, sepColor, lst);
  }

  public static String getStringListMessage(final List<String> lst) {
    return getStringListMessage(ChatColor.RESET, lst);
  }

  private static void sendPluginName24(final CommandSender sender, final String name) {
    sender.sendMessage(ChatColor.BLUE + name + ":");
  }
}
