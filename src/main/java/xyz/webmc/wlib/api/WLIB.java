package xyz.webmc.wlib.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.Plugin;

public final class WLIB {
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
}
