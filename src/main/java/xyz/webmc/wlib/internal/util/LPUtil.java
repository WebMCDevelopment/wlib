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

package xyz.webmc.wlib.internal.util;

import xyz.webmc.wlib.internal.iface.WInternal;

import java.util.UUID;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.group.GroupManager;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;

@WInternal
public final class LPUtil {
  public static boolean hasPermission(UUID uuid, String node) {
    return getUserManager().loadUser(uuid).thenApply(user -> {
      if (user != null) {
        return user.getCachedData().getPermissionData().checkPermission(node).asBoolean();
      } else {
        return false;
      }
    }).join();
  }

  public static boolean setUserPermission(UUID uuid, String node, boolean value) {
    final UserManager um = getUserManager();
    return um.loadUser(uuid).thenApply(user -> {
      if (user != null) {
        user.data().remove(Node.builder(node).build());
        user.data().add(Node.builder(node).value(value).build());
        um.saveUser(user);
        return true;
      } else {
        return false;
      }
    }).join();
  }

  public static boolean unsetUserPermission(UUID uuid, String node) {
    final UserManager um = getUserManager();
    return um.loadUser(uuid).thenApply(user -> {
      if (user != null) {
        user.data().remove(Node.builder(node).build());
        um.saveUser(user);
        return true;
      } else {
        return false;
      }
    }).join();
  }

  public static boolean hasGroupPermission(String name, String node) {
    final GroupManager gm = getGroupManager();

    return gm.loadGroup(name).thenApply(optional -> {
      if (optional.isPresent()) {
        final Group group = optional.get();
        return group.getCachedData().getPermissionData().checkPermission(node).asBoolean();
      } else {
        return false;
      }
    }).join();
  }

  public static boolean setGroupPermission(String name, String node, boolean value) {
    final GroupManager gm = getGroupManager();
    return gm.loadGroup(name).thenApply(optional -> {
      if (optional.isPresent()) {
        final Group group = optional.get();
        group.data().remove(Node.builder(node).build());
        group.data().add(Node.builder(node).value(value).build());
        gm.saveGroup(group);
        return true;
      } else {
        return false;
      }
    }).join();
  }

  public static boolean unsetGroupPermission(String name, String node) {
    final GroupManager gm = getGroupManager();
    return gm.loadGroup(name).thenApply(optional -> {
      if (optional.isPresent()) {
        final Group group = optional.get();
        group.data().remove(Node.builder(node).build());
        gm.saveGroup(group);
        return true;
      } else {
        return false;
      }
    }).join();
  }

  private static GroupManager getGroupManager() {
    return LuckPermsProvider.get().getGroupManager();
  }

  private static UserManager getUserManager() {
    return LuckPermsProvider.get().getUserManager();
  }
}
