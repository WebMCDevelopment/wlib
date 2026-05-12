package xyz.webmc.wlib.command;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import xyz.webmc.wlib.api.util.RandomUtil;

public final class WLIBBlankCommand extends Command {
  private static final String name = RandomUtil.randomStringLowercaseAZ(16);

  public WLIBBlankCommand() {
    super(name);
  }

  public static final String getBlankCommandName() {
    return name;
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
