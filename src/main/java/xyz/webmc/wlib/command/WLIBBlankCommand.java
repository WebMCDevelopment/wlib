package xyz.webmc.wlib.command;

import xyz.webmc.wlib.api.util.RandomUtil;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public final class WLIBBlankCommand extends Command {
  public static final String NAME = RandomUtil.randomStringLowercaseAZ(16);

  public WLIBBlankCommand () {
    super(NAME);
  }

  @Override
  public final boolean execute(final CommandSender sender, final String label, final String[] args) {
    return true;
  }

  @Override
  public final List<String> tabComplete(final CommandSender sender, final String label, final String[] args) {
    return List.of();
  }
}
