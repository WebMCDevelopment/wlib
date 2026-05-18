package xyz.webmc.wlib.api.util;

import xyz.webmc.wlib.internal.command.AliasCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dev.colbster937.reflect.MirrorSafe;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public final class CommandUtil {
  private static Plugin plugin;

  public static final void init(final Plugin plugin) {
    CommandUtil.plugin = plugin;
  }

  public static final void registerCommand(final Plugin plugin, final Command command) {
    getCommandMap().register(plugin.getName(), command);
    syncCommands();
  }

  public static final void registerCommand(final Command command) {
    registerCommand(plugin, command);
  }

  public static final void registerCommands(final Plugin plugin, final List<Command> commands) {
    getCommandMap().registerAll(plugin.getName(), commands);
    syncCommands();
  }

  public static final void registerCommands(final List<Command> commands) {
    registerCommands(plugin, commands);
  }

  public static final void unregisterCommand(final String commandStr) {
    final Command command = getKnownCommands().remove(commandStr);

    if (command != null) {
      command.unregister(getCommandMap());
    }

    syncCommands();
  }

  public static final void registerCommandAliases(final Plugin plugin, final String cmd, final String ...aliases) {
    final List<Command> commands = new ArrayList<>();

    for (final String alias : aliases) {
      commands.add(new AliasCommand(cmd, alias));
    }

    registerCommands(plugin, commands);
  }

  public static final void registerCommandAliases(final String cmd, final String ...aliases) {
    registerCommandAliases(plugin, cmd, aliases);
  }

  public static final boolean dispatch(final CommandSender sender, final String cmd) throws CommandException {
    return getCommandMap().dispatch(sender, cmd);
  }

  public static final List<String> tabComplete(final CommandSender sender, final String cmd) throws IllegalArgumentException {
    return getCommandMap().tabComplete(sender, cmd);
  }

  public static final Command getCommand(final String cmd) {
    return getCommandMap().getCommand(cmd);
  }

  private static CommandMap getCommandMap() {
    return MirrorSafe.getFieldValue(Bukkit.getPluginManager(), "commandMap");
  }

  private static Map<String, Command> getKnownCommands() {
    return MirrorSafe.getFieldValue(getCommandMap(), "knownCommands");
  }

  private static void syncCommands() {
    MirrorSafe.invokeMethod(Bukkit.getServer(), "syncCommands");
  }
}
