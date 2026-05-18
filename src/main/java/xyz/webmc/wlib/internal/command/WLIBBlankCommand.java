package xyz.webmc.wlib.internal.command;

import xyz.webmc.wlib.api.command.WCommand;
import xyz.webmc.wlib.api.util.RNGUtil;

import java.util.List;

import org.bukkit.command.CommandSender;

public final class WLIBBlankCommand extends WCommand {
  private static final String NAME = RNGUtil.randomStringLowercaseAZ(16);

  public WLIBBlankCommand() {
    super(NAME);
  }

  public static final String getBlankRandomCommandName() {
    return NAME;
  }

  public static final String getBlankRandomCommandKey() {
    return "wlib:" + getBlankRandomCommandName();
  }

  @Override
  public final boolean run(final CommandSender sender, final String label, final String[] args) {
    return true;
  }

  @Override
  public final List<String> tab(final CommandSender sender, final String label, final String[] args) {
    return List.of();
  }
}
