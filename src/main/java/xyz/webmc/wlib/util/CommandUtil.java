package xyz.webmc.wlib.util;

import java.util.Map;

import dev.colbster937.reflect.Mirror;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public final class CommandUtil {
  private static Plugin plugin;

  public static final void init(final Plugin plugin) {
    CommandUtil.plugin = plugin;
  }

  public static final void registerCommand(final Plugin plugin, final Command command) {
    commandMap().register(plugin.getName(), command);
    syncCommands();
  }

  public static final void registerCommand(final Command command) {
    registerCommand(plugin, command);
  }

  public static final void unregisterCommand(final String commandStr) {
    try {
      final Command command = knownCommands().remove(commandStr);
      if (command != null) {
        command.unregister(commandMap());
      }
      syncCommands();
    } catch (final ReflectiveOperationException ex) {
      throw new RuntimeException(ex);
    }
  }

  private static final CommandMap commandMap() {
    final CommandMap commandMap;

    try {
      final PluginManager pm = Bukkit.getPluginManager();
      commandMap = Mirror.getFieldValue(pm, "commandMap");
    } catch (final ReflectiveOperationException ex) {
      throw new RuntimeException(ex);
    }

    return commandMap;
  }

  private static final Map<String, Command> knownCommands()
      throws ReflectiveOperationException {
    return Mirror.getFieldValue(commandMap(), "knownCommands");
  }

  private static final void syncCommands() {
    SchedulerUtil.runAsync(() -> {
      try {
        Mirror.invokeMethod(Bukkit.getServer(), "syncCommands");
      } catch (final ReflectiveOperationException ex) {
        throw new RuntimeException(ex);
      }
    });
  }
}
