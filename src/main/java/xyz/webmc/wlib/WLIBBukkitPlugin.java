package xyz.webmc.wlib;

import xyz.webmc.wlib.api.util.CommandUtil;
import xyz.webmc.wlib.api.util.EventUtil;
import xyz.webmc.wlib.api.util.PermissionUtil;
import xyz.webmc.wlib.api.util.PlaceholderUtil;
import xyz.webmc.wlib.api.util.SchedulerUtil;
import xyz.webmc.wlib.api.util.WLIBUtil;
import xyz.webmc.wlib.internal.command.WLIBBlankCommand;
import xyz.webmc.wlib.internal.command.WLIBCommand;

import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class WLIBBukkitPlugin extends JavaPlugin implements Listener {
  @Override
  public final void onEnable() {
    CommandUtil.init(this);
    EventUtil.init(this);
    PermissionUtil.init();
    PlaceholderUtil.init();
    SchedulerUtil.init(this);
    WLIBUtil.registerPlugin(this);
    CommandUtil.registerCommand(new WLIBCommand());
    CommandUtil.registerCommand(new WLIBBlankCommand());
    EventUtil.registerEvents(this);

    PermissionUtil.setGroupPermission("default", "wlib.alerts.dev.muted.*", false);
  }

  @Override
  public final void onDisable() {
    SchedulerUtil.cancelAllTasks();
  }

  @EventHandler
  public final void onCommand(final ServerCommandEvent ev) {
    this.handleCommandEvent(ev.getSender(), ev.getCommand());
  }

  @EventHandler
  public final void onCommand(final PlayerCommandPreprocessEvent ev) {
    this.handleCommandEvent(ev.getPlayer(), ev.getMessage());
  }

  private void handleCommandEvent(final CommandSender sender, final String cmdLine) {
    if (sender.hasPermission("bukkit.command.plugins")) {
      try {
        Class.forName("io.papermc.paper.command.PaperPluginsCommand");
        final String[] split = cmdLine.split("\\s+", 2)[0].split(":", 2);

        if (split[0].startsWith("/")) {
          split[0] = split[0].substring(1);
        }

        final String ctx;
        final String cmd;

        if (split.length == 1) {
          ctx = "bukkit".trim();
          cmd = split[0].trim();
        } else {
          ctx = split[0].trim();
          cmd = split[1].trim();
        }

        if (ctx.equals("bukkit") && (cmd.equals("plugins") || cmd.equals("pl"))) {
          SchedulerUtil.runNextTick(() -> {
            WLIBUtil.sendStringListMessageType2(sender, "WLIB Plugins", WLIBUtil.getWLIBPluginNames());
          });
        }
      } catch (final ClassNotFoundException ex) {}
    }
  }
}
