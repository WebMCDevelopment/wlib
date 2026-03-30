package xyz.webmc.wlib.util;

import java.lang.StackWalker.Option;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class AlertUtil {
  private static final StackWalker sw = StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE);

  public static final void devAlert(final String... msg) {
    final Class<?> clazz = sw.walk(s -> s.skip(2).findFirst().get().getDeclaringClass());
    final String str = ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "DEV" + ChatColor.DARK_GREEN + "] ["
        + ChatColor.GREEN + clazz.getSimpleName() + ChatColor.DARK_GREEN + "] " + ChatColor.RESET
        + String.join(" ", msg);
    for (final Player p : Bukkit.getOnlinePlayers()) {
      if (p.hasPermission("wlib.dev-alert")) {
        p.sendMessage(str);
      }
    }
  }
}
