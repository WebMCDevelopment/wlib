package xyz.webmc.wlib.api.command;

import java.util.List;
import java.util.logging.Level;

import dev.colbster937.reflect.MirrorSafe;
import dev.colbster937.util.ExceptionStacker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class WCommand extends Command {
  private static final String PERMISSION_MSG = ChatColor.RED + "You don't have permission to use this command.";
  protected WCommand(final String name, final String... aliases) {
    super(name, "", "/" + name, List.of(aliases));
    super.setPermissionMessage(PERMISSION_MSG);
  }

  protected abstract boolean run(final CommandSender sender, final String label, final String[] args);

  protected List<String> tab(final CommandSender sender, final String label, final String[] args) {
    return List.of();
  }

  @Override
  public final boolean execute(final CommandSender sender, final String label, final String[] args) {
    try {
      return this.run(sender, label, args);
    } catch (final Throwable t) {
      Bukkit.getLogger().log(Level.SEVERE, t.getMessage(), t);

      if (sender instanceof Player) {
        final String stack = ExceptionStacker.getFullStackString(t);
        final String[] lines = stack
          .replaceAll("\t", "    ")
          .replaceAll("[\\p{Cntrl}&&[^\\r\\n]]", "")
          .split("\\R");

        for (final String line : lines) {
          sender.sendMessage(ChatColor.DARK_RED + line);
        }
      }

      return true;
    }
  }

  @Override
  public final List<String> tabComplete(final CommandSender sender, final String label, final String[] args) {
    try {
      return this.tab(sender, label, args);
    } catch (final Throwable t) {
      final String stack = ExceptionStacker.getFullStackString(t);
      sender.sendMessage(ChatColor.DARK_RED + stack);
      return List.of();
    }
  }

  protected final boolean checkPlayer(final CommandSender sender) {
    if (!(sender instanceof Player)) {
      sendOnlyPlayersMessage(sender);
      return false;
    } else {
      return true;
    }
  }

  public static final void sendUnknownCommandMessage(final CommandSender sender) {
    final Class<?> clazz = MirrorSafe.getClass("org.spigotmc.SpigotConfig");
    final String msg = MirrorSafe.getFieldValue(clazz, "unknownCommandMessage");
    sender.sendMessage(msg);
  }

  public static final void sendOnlyPlayersMessage(final CommandSender sender) {
    sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
  }

  public static final void sendNoPermissionMessage(final CommandSender sender) {
    sender.sendMessage(PERMISSION_MSG);
  }
}
