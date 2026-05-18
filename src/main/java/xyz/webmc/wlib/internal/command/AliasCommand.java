package xyz.webmc.wlib.internal.command;

import xyz.webmc.wlib.api.command.WCommand;
import xyz.webmc.wlib.api.util.CommandUtil;

import java.util.List;

import org.bukkit.command.CommandSender;

public final class AliasCommand extends WCommand {
  private final String cmd;

  public AliasCommand(final String cmd, final String name) {
    super(name);
    this.cmd = cmd;
  }

  @Override
  public final boolean run(final CommandSender sender, final String label, final String[] args) {
    return CommandUtil.dispatch(sender, getFullCommand(args));
  }

  @Override
  public final List<String> tab(final CommandSender sender, final String label, final String[] args) {
    return CommandUtil.tabComplete(sender, getFullCommand(args));
  }

  private String getFullCommand(final String[] args) {
    String ret = this.cmd;

    if (args.length > 0) {
      ret += ' ' + String.join(" ", args);
    }

    return ret;
  }
}
