package xyz.webmc.wlib.util;

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
    final List<String> plugins = new ArrayList<>();

    for (final Plugin plugin : getWLIBPlugins()) {
      plugins.add(plugin.getName());
    }

    return plugins;
  }

  public static final void sendStringCountMessage(final CommandSender sender, final String name, final List<String> lst) {
    Collections.sort(lst, String.CASE_INSENSITIVE_ORDER);
    sender.sendMessage("WLIB Plugins (" + plugins.size() + "): " + ChatColor.GREEN + String.join(ChatColor.RESET + ", " + ChatColor.GREEN, lst));
  }
}
