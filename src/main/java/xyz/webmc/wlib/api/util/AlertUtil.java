package xyz.webmc.wlib.api.util;

import java.lang.StackWalker.Option;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class AlertUtil {
  private static final StackWalker sw = StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE);

  public static final void devAlert(final String... txt) {
    final Class<?> clazz = sw.getCallerClass();
    final String ctx = clazz.getSimpleName();
    final String str = ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "DEV" + ChatColor.RESET + " - " + ChatColor.GREEN + ChatColor.DARK_GREEN + ctx + "] " + ChatColor.RESET + String.join(" ", txt);

    for (final Player p : Bukkit.getOnlinePlayers()) {
      if (p.hasPermission("wlib.alerts.dev") && !p.hasPermission("wlib.alerts.dev.muted." + ctx)) {
        p.sendMessage(str);
      }
    }
  }
}
