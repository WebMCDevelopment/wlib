package xyz.webmc.wlib.api;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import dev.colbster937.reflect.MirrorSafe;
import org.bukkit.plugin.Plugin;

@SuppressWarnings({ "NonConstantLogger" })
public final class WLIB {
  private static final List<Plugin> plugins = new ArrayList<>();
  private static Logger logger;

  public static final void init(final Logger logger) {
    WLIB.logger = logger;
  }

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

  public static final Logger getLogger() {
    return logger;
  }

  public static final boolean getIsModernServer() {
    return MirrorSafe.getClassExists("org.bukkit.block.data.BlockData");
  }
}
