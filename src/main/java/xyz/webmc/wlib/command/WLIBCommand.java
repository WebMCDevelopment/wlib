package xyz.webmc.wlib.command;

import xyz.webmc.wlib.api.util.CommandUtil;
import xyz.webmc.wlib.api.util.PermissionUtil;
import xyz.webmc.wlib.api.util.WLIBUtil;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public final class WLIBCommand extends Command {
  public WLIBCommand() {
    super("wlib");
  }

  @Override
  public final boolean execute(final CommandSender sender, final String label, final String[] args) {
    boolean bool = true;
    if (args.length > 0) {
      final String arg = args[0].trim();
      if (arg.equals("plugins") || arg.equals("pl")) {
        WLIBUtil.sendStringListMessageType3(sender, "WLIB Plugins", WLIBUtil.getWLIBPluginNames());
        bool = false;
      } else if (args.length > 1 && arg.equals("alerts")) {
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
      }
    }

    if (bool) {
      CommandUtil.sendUnknownCommandString(sender);
    }

    return true;
  }

  @Override
  public final List<String> tabComplete(final CommandSender sender, final String label, final String[] args) {
    if (args.length == 1) {
      final List<String> ret = new ArrayList<>();
      ret.add("plugins");

      if (sender.hasPermission("wlib.dev-alert")) {
        ret.add("alerts");
      }

      return ret;
    } else {
      return List.of();
    }
  }
}
