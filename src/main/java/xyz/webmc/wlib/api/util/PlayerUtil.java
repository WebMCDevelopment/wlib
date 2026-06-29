package xyz.webmc.wlib.api.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import dev.colbster937.reflect.MirrorSafe;

public final class PlayerUtil {
  public static final void teleport(final Player plr, final Location loc) {
    final Location prev = plr.getLocation().clone();

    SchedulerUtil.teleportAsync(plr, loc);

    final Plugin essentials = PluginUtil.getPlugin("Essentials");
    if (essentials != null) {
      final Object user = MirrorSafe.invokeMethod(essentials, "getUser", plr);
      if (user != null) {
        MirrorSafe.invokeMethod(user, "setLastLocation", prev);
      }
    }
  }

  public static final void teleport(final Player plr, final World world, final double x, final double y, final double z, final float yaw, final float pitch) {
    teleport(plr, new Location(world, x, y, z, yaw, pitch));
  }

  public static final void teleport(final Player plr, final World world, final double x, final double y, final double z) {
    final Location prev = plr.getLocation().clone();
    teleport(plr, world, x, y, z, prev.getYaw(), prev.getPitch());
  }

  public static final void teleport(final Player plr, final double x, final double y, final double z, final float yaw, final float pitch) {
    teleport(plr, plr.getWorld(), x, y, z, yaw, pitch);
  }

  public static final void teleport(final Player plr, final double x, final double y, final double z) {
    final Location prev = plr.getLocation().clone();
    teleport(plr, plr.getWorld(), x, y, z, prev.getYaw(), prev.getPitch());
  }
}
