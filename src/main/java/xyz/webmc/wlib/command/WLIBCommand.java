package xyz.webmc.wlib.command;

import xyz.webmc.wlib.api.command.WCommand;
import xyz.webmc.wlib.api.util.PermissionUtil;
import xyz.webmc.wlib.api.util.WLIBUtil;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

public final class WLIBCommand extends WCommand {
  public WLIBCommand() {
    super("wlib");
  }

  @Override
  public final boolean run(final CommandSender sender, final String label, final String[] args) {
    boolean bool = true;
    if (args.length > 0) {
      final String arg = args[0].trim();
      if (arg.equals("plugins") || arg.equals("pl")) {
        WLIBUtil.sendStringListMessageType3(sender, "WLIB Plugins", WLIBUtil.getWLIBPluginNames());
        bool = false;
      } else if (args.length > 1) {
        if (arg.equals("alerts") && sender.hasPermission("wlib.alerts.dev")) {
          final String ctx = args[1].trim();
          final int ret = PermissionUtil.toggleUserPermissionC(sender, ctx);
          bool = ret < 0;
          if (!bool) {
            final String state;

            if (ret > 0) {
              state = ChatColor.GREEN + "ENABLED";
            } else {
              state = ChatColor.RED + "DISABLED";
            }

            sender.sendMessage(state + ChatColor.RESET + " alerts for " + ChatColor.AQUA + ctx + ChatColor.AQUA);
          }
        } else if (arg.equals("debug") && sender.hasPermission("wlib.debug")) {
          final String act = args[1].trim();
          if (act.equals("throw")) {
            throw new CommandException();
          }
        }
      }
    }

    if (bool) {
      sendUnknownCommandMessage(sender);
    }

    return true;
  }

  @Override
  public final List<String> tab(final CommandSender sender, final String label, final String[] args) {
    if (args.length > 0) {
      final List<String> ret = new ArrayList<>();

      if (args.length == 1) {
        ret.add("plugins");

        if (sender.hasPermission("wlib.alerts.dev")) {
          ret.add("alerts");
        }

        if (sender.hasPermission("wlib.debug")) {
          ret.add("debug");
        }
      } else if (args.length == 2) {
        if (args[0].equals("debug") && sender.hasPermission("wlib.debug")) {
          ret.add("throw");
        }
      }

      return ret;
    } else {
      return List.of();
    }
  }
}
