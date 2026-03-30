package xyz.webmc.wlib.util;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public final class EventUtil {
  private static PluginManager pm;
  private static Plugin plugin;

  public static final void init(final Plugin plugin) {
    pm = Bukkit.getPluginManager();
    EventUtil.plugin = plugin;
  }

  public static final void registerEvents(final Listener listener, final Plugin plugin) {
    pm.registerEvents(listener, plugin);
  }

  public static final void registerEvent(final Class<? extends Event> event, final Listener listener, final EventPriority priority, final EventExecutor executor, final Plugin plugin) {
    pm.registerEvent(event, listener, priority, executor, plugin);
  }

  public static final void registerEvents(final Listener listener) {
    registerEvents(listener, plugin);
  }

  public static final void registerEvent(final Class<? extends Event> event, final Listener listener, final EventPriority priority, final EventExecutor executor) {
    pm.registerEvent(event, listener, priority, executor, plugin);
  }

  public static final void callEvent(final Event ev) {
    pm.callEvent(ev);
  }
}
