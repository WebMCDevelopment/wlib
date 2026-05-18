package xyz.webmc.wlib.api.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public final class WLIBUtil {
  private static final List<Plugin> plugins = new ArrayList<>();

  public static final void registerPlugin(final Plugin plugin) {
    plugins.add(plugin);
  }

  public static final List<Plugin> getWLIBPlugins() {
    return plugins;
  }

  public static final List<String> getWLIBPluginNames() {
    final List<String> ret = new ArrayList<>();

    for (final Plugin plugin : getWLIBPlugins()) {
      ret.add(plugin.getName());
    }

    return ret;
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
    sender.sendMessage(ChatColor.BLUE + name + ":");
    sender.sendMessage(ChatColor.DARK_GRAY + " - " +  getStringListMessage(lst));
  }

  public static final void sendStringListMessageType3(final CommandSender sender, final String name, final List<String> lst) {
    sendStringListMessageType2(sender, name + " (" + lst.size() + ")", lst);
  }

  public static final String getStringListMessage(final ChatColor color, final List<String> lst) {
    Collections.sort(lst, String.CASE_INSENSITIVE_ORDER);
    return color + String.join(ChatColor.RESET + ", " + color, lst);
  }

  public static final String getStringListMessage(final List<String> lst) {
    return getStringListMessage(ChatColor.GREEN, lst);
  }
}
