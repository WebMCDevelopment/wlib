package xyz.webmc.wlib.api.util;

import xyz.webmc.wlib.internal.util.AbstractPluginRequiredUtil;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public final class PlaceholderUtil extends AbstractPluginRequiredUtil {
  private static boolean bool = false;

  public static final void init() {
    bool = check("PlaceholderAPI");
  }

  public static final String parsePlaceholders(final Player player, final String txt) {
    if (bool) {
      return PlaceholderAPI.setPlaceholders(player, txt);
    } else {
      return txt;
    }
  }
}
