package xyz.webmc.wlib;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;

import dev.colbster937.reflect.MirrorSafe;
import xyz.webmc.wlib.api.misc.CaptureSender;
import xyz.webmc.wlib.api.util.CommandUtil;
import xyz.webmc.wlib.api.util.EventUtil;
import xyz.webmc.wlib.api.util.PermissionUtil;
import xyz.webmc.wlib.api.util.PlaceholderUtil;
import xyz.webmc.wlib.api.util.SchedulerUtil;
import xyz.webmc.wlib.api.util.WLIBUtil;
import xyz.webmc.wlib.internal.command.WLIBBlankCommand;
import xyz.webmc.wlib.internal.command.WLIBCommand;

@SuppressWarnings({ "unused" })
public final class WLIBBukkitPlugin extends JavaPlugin implements Listener {
  @Override
  public final void onEnable() {
    CommandUtil.init(this);
    EventUtil.init(this);
    PermissionUtil.init();
    PlaceholderUtil.init();
    SchedulerUtil.init(this);
    WLIBUtil.registerPlugin(this);
    CommandUtil.registerCommand(new WLIBCommand(this));
    CommandUtil.registerCommand(new WLIBBlankCommand());
    EventUtil.registerEvents(this);

    CommandUtil.registerCommandAliases("wlib:wlib plugins", "wplugins", "wpl");

    PermissionUtil.setGroupPermission("default", "wlib.alerts.dev.muted.*", false);
  }

  @Override
  public final void onDisable() {
    SchedulerUtil.cancelAllTasks();
  }

  @EventHandler
  public final void onCommand(final ServerCommandEvent ev) {
    if (handleCommandEvent(ev.getSender(), ev.getCommand())) {
      ev.setCommand(WLIBBlankCommand.getBlankRandomCommandKey());
    }
  }

  @EventHandler
  public final void onCommand(final PlayerCommandPreprocessEvent ev) {
    if (handleCommandEvent(ev.getPlayer(), ev.getMessage())) {
      ev.setCancelled(true);
    }
  }

  private static boolean handleCommandEvent(final CommandSender sender, final String cmdLine) {
    String commandStr = cmdLine;

    if (commandStr.startsWith("/")) {
      commandStr = commandStr.substring(1);
    }

    if (sender.hasPermission("bukkit.command.plugins")) {
      final String[] split = commandStr.split("\\s+", 2)[0].split(":", 2);

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
        final String name = "WLIB Plugins";
        final List<String> plugins = WLIBUtil.getWLIBPluginNames();
        if (MirrorSafe.getClass("io.papermc.paper.command.PaperPluginsCommand") != null) {
          SchedulerUtil.runNextTick(() -> {
            WLIBUtil.sendStringListMessageType2(sender, name, plugins);
          });
        } else if (false) {
          final CaptureSender capture = new CaptureSender(sender);
          CommandUtil.dispatch(capture, commandStr);
          final String[] msg = capture.getMessages().get(0).split(": ");
          sender.sendMessage(ChatColor.GOLD + "Bukkit " + msg[0] + ":");
          sender.sendMessage(ChatColor.DARK_GRAY + " - " +  msg[1]);
          WLIBUtil.sendStringListMessageType3(sender, name, plugins);
          return true;
        }
      }
    }

    return false;
  }
}
