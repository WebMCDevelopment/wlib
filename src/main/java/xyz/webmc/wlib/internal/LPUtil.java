package xyz.webmc.wlib.internal;

import java.util.UUID;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.group.GroupManager;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;

public final class LPUtil {
  public static final boolean hasPermission(final UUID uuid, final String node) {
    return getUserManager().loadUser(uuid).thenApply(user -> {
      if (user != null) {
        return user.getCachedData().getPermissionData().checkPermission(node).asBoolean();
      } else {
        return false;
      }
    }).join();
  }

  public static final boolean setUserPermission(final UUID uuid, final String node, final boolean value) {
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

  public static final boolean unsetUserPermission(final UUID uuid, final String node) {
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

  public static final boolean hasGroupPermission(final String name, final String node) {
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

  public static final boolean setGroupPermission(final String name, final String node, final boolean value) {
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

  public static final boolean unsetGroupPermission(final String name, final String node) {
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
