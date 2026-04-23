package xyz.webmc.wlib.command;

import xyz.webmc.wlib.util.CommandUtil;
import xyz.webmc.wlib.util.WLIBUtil;

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
        WLIBUtil.sendStringCountMessage(sender, "WLIB Plugins", WLIBUtil.getWLIBPluginNames());
        bool = false;
      }
    }

    if (bool) {
      CommandUtil.sendUnknownCommandString(sender);
    }

    return true;
  }
}
