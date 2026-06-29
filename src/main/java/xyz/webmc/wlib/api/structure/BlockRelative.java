package xyz.webmc.wlib.api.structure;

import com.cryptomorin.xseries.XMaterial;
import dev.colbster937.reflect.MirrorSafe;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

@SuppressWarnings({ "deprecation" })
public class BlockRelative {

  private final int x;
  private final int y;
  private final int z;

  private final XMaterial mat;
  private final String dataModern;
  private final byte dataLegacy;

  private BlockRelative(final int x, final int y, final int z, final XMaterial mat, final String dataModern, final byte dataLegacy) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.mat = mat;
    this.dataModern = dataModern;
    this.dataLegacy = dataLegacy;
  }

  public BlockRelative(final int x, final int y, final int z, final XMaterial mat) {
    this(x, y, z, mat, null, (byte) 0);
  }

  public BlockRelative(final int x, final int y, final int z, final Material mat) {
    this(x, y, z, XMaterial.matchXMaterial(mat));
  }

  public BlockRelative(final int x, final int y, final int z, final XMaterial mat, final String dataModern) {
    this(x, y, z, mat, dataModern, (byte) 0);
  }

  public BlockRelative(final int x, final int y, final int z, final Material mat, final String dataModern) {
    this(x, y, z, XMaterial.matchXMaterial(mat), dataModern);
  }

  public BlockRelative(final int x, final int y, final int z, final XMaterial mat, final byte dataLegacy) {
    this(x, y, z, mat, null, dataLegacy);
  }

  public BlockRelative(final int x, final int y, final int z, final Material mat, final byte dataLegacy) {
    this(x, y, z, XMaterial.matchXMaterial(mat), dataLegacy);
  }

  public final void place(final Location loc) {
    final Location rel = loc.clone().add(this.x, this.y, this.z);
    final Block blk = rel.getBlock();
    final Material _mat = this.mat.parseMaterial();

    if (mat != null) {
      if (isModern() && this.dataModern != null) {
        final Object data = MirrorSafe.invokeMethod(Bukkit.class, "createBlockData", new Object[] { "minecraft:" + _mat.name().toLowerCase() + this.dataModern });
        MirrorSafe.invokeMethod(Block.class, blk, "setBlockData", data, false);
      } else {
        blk.setType(_mat, false);
        if (this.dataLegacy != 0) {
          blk.setData(this.dataLegacy);
        }
      }
    }
  }

  private static boolean isModern() {
    try {
      Class.forName("org.bukkit.block.data.BlockData");
      return true;
    } catch (final ClassNotFoundException ex) {
      return false;
    }
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int getZ() {
    return z;
  }

  public XMaterial getMaterial() {
    return mat;
  }

  public Material getBukkitMaterial() {
    return mat.parseMaterial();
  }

  public String getDataModern() {
    return dataModern;
  }

  public byte getDataLegacy() {
    return dataLegacy;
  }
}
