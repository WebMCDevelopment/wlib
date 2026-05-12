package xyz.webmc.wlib.internal;

import xyz.webmc.wlib.api.util.PluginUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPluginRequiredUtil {
  protected static final boolean check(final String ...plugins) {
    final List<String> missing = new ArrayList<>();

    for (final String plugin : plugins) {
      if (!PluginUtil.isPluginEnabled(plugin)) {
        missing.add(plugin);
      }
    }

    return missing.isEmpty();
  }
}
