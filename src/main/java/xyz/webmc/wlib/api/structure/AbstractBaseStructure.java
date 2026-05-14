package xyz.webmc.wlib.api.structure;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

public abstract class AbstractBaseStructure {
  private final List<BlockRelative> blocks = new ArrayList<>();
  private final String name;

  protected AbstractBaseStructure(final String name) {
    this.name = name;
  }

  protected final void addBlock(final BlockRelative blk) {
    this.blocks.add(blk);
  }

  public final void place(final Location loc) {
    for (final BlockRelative blk : blocks) {
      blk.place(loc);
    }
  }

  public final String getName() {
    return this.name;
  }
}
