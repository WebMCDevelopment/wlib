package xyz.webmc.wlib.api.util;

import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class TextUtil {
  public static final String parsePlaceholders(final Player player, final String txt) {
    return PlaceholderUtil.parsePlaceholders(player, txt);
  }

  public static final String serializeExceptionStackString(final String stack) {
    return stack
      .replaceAll("\t", "    ")
      .replaceAll("[\\p{Cntrl}&&[^\\r\\n]]", "");
  }

  public static final String[] serializeExceptionStackStringMultiline(final String stack) {
    return serializeExceptionStackString(stack).split("\\R");
  }

  public static final void sendStringListMessageType1(final CommandSender sender, final String name, final List<String> lst) {
    sender.sendMessage(name + " (" + lst.size() + "): " + getStringListMessage(lst));
  }

  public static final void sendStringListMessageType2(final CommandSender sender, final String name, final List<String> lst) {
    sendPluginName24(sender, name);
    sender.sendMessage(ChatColor.DARK_GRAY + " - " +  getStringListMessage(lst));
  }

  public static final void sendStringListMessageType3(final CommandSender sender, final String name, final List<String> lst) {
    sendStringListMessageType2(sender, name + " (" + lst.size() + ")", lst);
  }

  public static final void sendStringListMessageType4(final CommandSender sender, final String name, final List<String> lst) {
    sendPluginName24(sender, name);
    sender.sendMessage(ChatColor.DARK_GRAY + " - [" + getStringListMessage(ChatColor.DARK_GRAY, lst) + ChatColor.DARK_GRAY + "]");
  }

  public static final String getStringListMessage(final ChatColor strColor, final ChatColor sepColor, final List<String> lst) {
    Collections.sort(lst, String.CASE_INSENSITIVE_ORDER);
    return strColor + String.join(sepColor + ", " + strColor, lst);
  }

  public static final String getStringListMessage(final ChatColor sepColor,final List<String> lst) {
    return getStringListMessage(ChatColor.GREEN, sepColor, lst);
  }

  public static final String getStringListMessage(final List<String> lst) {
    return getStringListMessage(ChatColor.RESET, lst);
  }

  private static void sendPluginName24(final CommandSender sender, final String name) {
    sender.sendMessage(ChatColor.BLUE + name + ":");
  }
}
