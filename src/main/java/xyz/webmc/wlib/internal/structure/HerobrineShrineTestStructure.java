package xyz.webmc.wlib.internal.structure;

import xyz.webmc.wlib.api.structure.AbstractBaseStructure;
import xyz.webmc.wlib.api.structure.BlockRelative;

import com.cryptomorin.xseries.XMaterial;

public final class HerobrineShrineTestStructure extends AbstractBaseStructure {
  public HerobrineShrineTestStructure() {
    super("herobrine_shrine");

    for (int y = 0; y < 3; y++) {
      for (int x = -1; x < 2; x++) {
        for (int z = -1; z < 2; z++) {
          if (y == 0) {
            super.addBlock(new BlockRelative(x, y, z, XMaterial.GOLD_BLOCK));
          } else if (y == 1) {
            if (x != z && x + z != 0) {
              super.addBlock(new BlockRelative(x, y, z, XMaterial.REDSTONE_TORCH));
            } else if (x == 0 && z == 0) {
              super.addBlock(new BlockRelative(x, y, z, XMaterial.NETHERRACK));
            }
          } else if (y == 2 && x == 0 && z == 0) {
            super.addBlock(new BlockRelative(x, y, z, XMaterial.FIRE));
          }
        }
      }
    }
  }

  public static final HerobrineShrineTestStructure getInstance() {
    return getInstance(HerobrineShrineTestStructure.class);
  }
}
