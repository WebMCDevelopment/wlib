package xyz.webmc.wlib.api.structure;

import xyz.webmc.wlib.api.util.SchemUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.cryptomorin.xseries.XMaterial;
import dev.zerite.craftlib.commons.world.Block;
import dev.zerite.craftlib.schematic.Schematic;
import dev.zerite.craftlib.schematic.SchematicMaterials;
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

  public final void saveSchematic(final File file) throws IOException {
    final Schematic schematic = createSchematic();
    SchemUtil.writeSchematicFile(schematic, file);
  }

  public final void loadSchematic(final File file) throws IOException {
    final Schematic schematic = SchemUtil.readSchematicFile(file);

    final int width = schematic.getWidth();
    final int height = schematic.getHeight();
    final int length = schematic.getLength();

    final int offsetX = schematic.getOffsetX();
    final int offsetY = schematic.getOffsetY();
    final int offsetZ = schematic.getOffsetZ();

    for (int y = 0; y < height; y++) {
      for (int z = 0; z < length; z++) {
        for (int x = 0; x < width; x++) {
          final Block block = schematic.get(x, y, z);
          if (block != Block.AIR) {
            final XMaterial mat = XMaterial.matchXMaterial(block.toString()).get();
            if (mat != null) {
              this.addBlock(new BlockRelative(x - offsetX, y - offsetY, z - offsetZ, mat, (byte) block.getMetadata()));
            }
          }
        }
      }
    }
  }

  private Schematic createSchematic() {
    if (!this.blocks.isEmpty()) {
      int minX = Integer.MAX_VALUE;
      int minY = Integer.MAX_VALUE;
      int minZ = Integer.MAX_VALUE;
      int maxX = Integer.MIN_VALUE;
      int maxY = Integer.MIN_VALUE;
      int maxZ = Integer.MIN_VALUE;

      for (final BlockRelative blk : this.blocks) {
        minX = Math.min(minX, blk.getX());
        minY = Math.min(minY, blk.getY());
        minZ = Math.min(minZ, blk.getZ());
        maxX = Math.max(maxX, blk.getX());
        maxY = Math.max(maxY, blk.getY());
        maxZ = Math.max(maxZ, blk.getZ());
      }

      final short width = (short) (maxX - minX + 1);
      final short height = (short) (maxY - minY + 1);
      final short length = (short) (maxZ - minZ + 1);

      final Block[] blockArray = new Block[width * height * length];
      for (int i = 0; i < blockArray.length; i++) {
        blockArray[i] = Block.AIR;
      }

      for (final BlockRelative blk : this.blocks) {
        final int x = blk.getX() - minX;
        final int y = blk.getY() - minY;
        final int z = blk.getZ() - minZ;
        final int index = x + z * width + y * width * length;

        final XMaterial xmat = blk.getMaterial();
        if (xmat != null && xmat.getId() >= 0) {
          final int blockId = (xmat.getId() << 4) | (blk.getDataLegacy() & 0xF);
          blockArray[index] = new Block(blockId, x, y, z);
        }
      }

      return new Schematic(width, height, length, SchematicMaterials.ALPHA, blockArray, new ArrayList<>(), new ArrayList<>(), -minX, -minY, -minZ);
    } else {
      return new Schematic((short) 0, (short) 0, (short) 0, SchematicMaterials.ALPHA);
    }
  }
}
