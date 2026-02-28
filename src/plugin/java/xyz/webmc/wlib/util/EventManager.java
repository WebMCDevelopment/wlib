package xyz.webmc.wlib.util;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public final class EventManager {
  private static PluginManager pm;

  public static final void init(final Plugin plugin) {
    pm = plugin.getServer().getPluginManager();
  }

  public static final void registerEvents(final Listener listener, final Plugin plugin) {
    pm.registerEvents(listener, plugin);
  }

  public static final void callEvent(final Event ev) {
    pm.callEvent(ev);
  }
}
