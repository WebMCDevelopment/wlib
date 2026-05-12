package xyz.webmc.wlib.api.util;

import dev.colbster937.reflect.Mirror;
import dev.colbster937.reflect.MirrorSafe;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

@SuppressWarnings({ "deprecation" })
public final class MagicUtil {
  public static final void sendFakeBlock(final Location loc, final Material mat, final byte dat) {
    final World wrld = loc.getWorld();
    final Chunk chnk = wrld.getChunkAt(loc);

    for (final Player plr : wrld.getPlayers()) {
      boolean sent = true;

      if (Mirror.hasMethod(plr.getClass(), "isChunkSent", Chunk.class)) {
        sent = MirrorSafe.invokeMethod(plr, "isChunkSent", chnk);
      }

      if (sent) {
        plr.sendBlockChange(loc, mat.getId(), dat);
      }
    }
  }

  public static final void sendFakeBlock(final Location loc, final Material mat) {
    sendFakeBlock(loc, mat, (byte) 0);
  }
}
