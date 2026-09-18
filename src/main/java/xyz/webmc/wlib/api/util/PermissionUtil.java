/*
 * Copyright (C) 2026 Colbster937
 *
 * SPDX-License-Identifier: AGPL-3.0-or-later
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * See the LICENSE file for details.
 */

package xyz.webmc.wlib.api.util;

import xyz.webmc.wlib.internal.util.InternalUtil;
import xyz.webmc.wlib.internal.util.LPUtil;
import xyz.webmc.wlib.internal.util.RequiredPluginUtil;

import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static xyz.webmc.wlib.internal.util.RequiredPluginUtil.checkPlugins;

public final class PermissionUtil implements RequiredPluginUtil {
  private static boolean bool = false;

  public static void _init() {
    InternalUtil.checkInternalCaller();
    bool = checkPlugins("LuckPerms");
  }

  public static boolean hasPermission(UUID uuid, String node) {
    if (bool) {
      return LPUtil.hasPermission(uuid, node);
    } else {
      return false;
    }
  }

  public static boolean hasGroupPermission(String name, String node) {
    if (bool) {
      return LPUtil.hasGroupPermission(name, node);
    } else {
      return false;
    }
  }

  public static boolean hasPermission(OfflinePlayer player, String node) {
    return hasPermission(player.getUniqueId(), node);
  }

  public static boolean hasPermissionC(CommandSender sender, String node) {
    if (sender instanceof Player player) {
      return hasPermission(player, node);
    } else {
      return true;
    }
  }

  public static boolean setUserPermission(UUID uuid, String node, boolean value) {
    if (bool) {
      return LPUtil.setUserPermission(uuid, node, value);
    } else {
      return false;
    }
  }

  public static boolean setUserPermission(OfflinePlayer player, String node, boolean value) {
    return setUserPermission(player.getUniqueId(), node, value);
  }

  public static boolean setUserPermissionC(CommandSender sender, String node, boolean value) {
    if (sender instanceof Player player) {
      return setUserPermission(player, node, value);
    } else {
      return false;
    }
  }

  public static boolean unsetUserPermission(UUID uuid, String node) {
    if (bool) {
      return LPUtil.unsetUserPermission(uuid, node);
    } else {
      return false;
    }
  }

  public static boolean unsetUserPermission(OfflinePlayer player, String node) {
    return unsetUserPermission(player.getUniqueId(), node);
  }

  public static boolean unsetUserPermissionC(CommandSender sender, String node) {
    if (sender instanceof Player player) {
      return unsetUserPermission(player, node);
    } else {
      return false;
    }
  }

  public static int toggleUserPermission(UUID uuid, String node) {
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

  public static int toggleUserPermission(OfflinePlayer player, String node) {
    return toggleUserPermission(player.getUniqueId(), node);
  }

  public static int toggleUserPermissionC(CommandSender sender, String node) {
    if (sender instanceof Player player) {
      return toggleUserPermission(player, node);
    } else {
      return -1;
    }
  }

  public static boolean setGroupPermission(String name, String node, boolean value) {
    if (bool) {
      return LPUtil.setGroupPermission(name, node, value);
    } else {
      return false;
    }
  }

  public static boolean unsetGroupPermission(String name, String node) {
    if (bool) {
      return LPUtil.unsetGroupPermission(name, node);
    } else {
      return false;
    }
  }

  public static int toggleGroupPermission(String name, String node) {
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
