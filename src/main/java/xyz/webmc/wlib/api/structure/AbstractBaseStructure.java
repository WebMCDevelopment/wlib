package xyz.webmc.wlib.api.structure;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.block.BlockStateHolder;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;

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

  public final void loadFromSchematic(final File file) {
    try {
      final ClipboardFormat format = ClipboardFormats.findByFile(file);
      if (format == null) {
        throw new IllegalArgumentException("Invalid schematic: " + file.getName());
      }

      try (
        ClipboardReader reader = format.getReader(new FileInputStream(file))
      ) {
        final Clipboard clipboard = reader.read();

        final BlockVector3 origin = clipboard.getOrigin();
        final Region region = clipboard.getRegion();

        for (BlockVector3 pos : region) {
          final BlockStateHolder<?> block = clipboard.getBlock(pos);

          if (block.getBlockType().getMaterial().isAir()) continue;

          final int dx = pos.getX() - origin.getX();
          final int dy = pos.getY() - origin.getY();
          final int dz = pos.getZ() - origin.getZ();

          final String full = block.getAsString();

          final String[] split = full.split("\\[", 2);
          final String materialName = split[0].replace(
            "minecraft:",
            ""
          ).toUpperCase();

          final Material mat = Material.matchMaterial(materialName);

          String dataModern = null;
          if (split.length > 1) {
            dataModern = "[" + split[1];
          }

          if (mat != null) {
            this.blocks.add(new BlockRelative(dx, dy, dz, mat, dataModern));
          }
        }
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed to load schematic: " + file.getName(), e);
    }
  }
}
