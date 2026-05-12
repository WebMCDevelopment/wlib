package xyz.webmc.wlib.api.util;

import xyz.webmc.wlib.internal.AbstractPluginRequiredUtil;
import xyz.webmc.wlib.internal.LPUtil;

import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class PermissionUtil extends AbstractPluginRequiredUtil {
  private static boolean bool = false;

  public static final void init() {
    bool = check("LuckPerms");
    if (bool) {
      LPUtil.setGroupPermission("default", "wlib.dev-alert.muted.*", false);
    }
  }

  public static final boolean hasPermission(final UUID uuid, final String node) {
    if (bool) {
      return LPUtil.hasPermission(uuid, node);
    } else {
      return false;
    }
  }

  public static final boolean hasGroupPermission(final String name, final String node) {
    if (bool) {
      return LPUtil.hasGroupPermission(name, node);
    } else {
      return false;
    }
  }

  public static final boolean hasPermission(final OfflinePlayer player, final String node) {
    return hasPermission(player.getUniqueId(), node);
  }

  public static final boolean hasPermissionC(final CommandSender sender, final String node) {
    if (sender instanceof Player player) {
      return hasPermission(player, node);
    } else {
      return true;
    }
  }

  public static final boolean setUserPermission(final UUID uuid, final String node, final boolean value) {
    if (bool) {
      return LPUtil.setUserPermission(uuid, node, value);
    } else {
      return false;
    }
  }

  public static final boolean setUserPermission(final OfflinePlayer player, final String node, final boolean value) {
    return setUserPermission(player.getUniqueId(), node, value);
  }

  public static final boolean setUserPermissionC(final CommandSender sender, final String node, final boolean value) {
    if (sender instanceof Player player) {
      return setUserPermission(player, node, value);
    } else {
      return false;
    }
  }

  public static final boolean unsetUserPermission(final UUID uuid, final String node) {
    if (bool) {
      return LPUtil.unsetUserPermission(uuid, node);
    } else {
      return false;
    }
  }

  public static final boolean unsetUserPermission(final OfflinePlayer player, final String node) {
    return unsetUserPermission(player.getUniqueId(), node);
  }

  public static final boolean unsetUserPermissionC(final CommandSender sender, final String node) {
    if (sender instanceof Player player) {
      return unsetUserPermission(player, node);
    } else {
      return false;
    }
  }

  public static final int toggleUserPermission(final UUID uuid, final String node) {
    if (!hasPermission(uuid, node)) {
      if (setUserPermission(uuid, node, true)) {
        return 1;
      }
    } else {
      if (unsetUserPermission(uuid, node)) {
        return 0;
      }
    }

    return -1;
  }

  public static final int toggleUserPermission(final OfflinePlayer player, final String node) {
    return toggleUserPermission(player.getUniqueId(), node);
  }

  public static final int toggleUserPermissionC(final CommandSender sender, final String node) {
    if (sender instanceof Player player) {
      return toggleUserPermission(player, node);
    } else {
      return -1;
    }
  }

  public static final boolean setGroupPermission(final String name, final String node, final boolean value) {
    if (bool) {
      return LPUtil.setGroupPermission(name, node, value);
    } else {
      return false;
    }
  }

  public static final boolean unsetGroupPermission(final String name, final String node) {
    if (bool) {
      return LPUtil.unsetGroupPermission(name, node);
    } else {
      return false;
    }
  }

  public static final int toggleGroupPermission(final String name, final String node) {
    if (!hasGroupPermission(name, node)) {
      if (setGroupPermission(name, node, true)) {
        return 1;
      }
    } else {
      if (unsetGroupPermission(name, node)) {
        return 0;
      }
    }

    return -1;
  }
}
