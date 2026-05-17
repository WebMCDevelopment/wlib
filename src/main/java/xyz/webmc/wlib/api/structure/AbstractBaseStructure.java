package xyz.webmc.wlib.api.structure;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionException;

import com.cryptomorin.xseries.XMaterial;
import dev.zerite.craftlib.commons.world.Block;
import dev.zerite.craftlib.schematic.Schematic;
import dev.zerite.craftlib.schematic.SchematicIO;
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

  public final void loadFromSchematic(final File file) {
    try {
      final Schematic schematic = readSchematic(file);

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
            if (block == null || block == Block.AIR) continue;

            final XMaterial mat = XMaterial.matchXMaterial(block.toString()).get();
            if (mat == null) continue;

            this.blocks.add(new BlockRelative(
              x - offsetX,
              y - offsetY,
              z - offsetZ,
              mat,
              (byte) block.getMetadata()
            ));
          }
        }
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed to load schematic: " + file.getName(), e);
    }
  }

  private static Schematic readSchematic(final File file) throws IOException {
    try (FileInputStream input = new FileInputStream(file)) {
      try {
        return SchematicIO.readFuture(input, false).join();
      } catch (CompletionException ex) {
        final Throwable cause = ex.getCause();
        if (cause instanceof IOException) {
          throw (IOException) cause;
        }
        throw ex;
      }
    }
  }

  public final void saveToSchematic(final File file) {
    try {
      final Schematic schematic = buildSchematic();
      writeSchematic(schematic, file);
    } catch (Exception e) {
      throw new RuntimeException("Failed to save schematic: " + file.getName(), e);
    }
  }

  private Schematic buildSchematic() {
    if (this.blocks.isEmpty()) {
      return new Schematic((short) 0, (short) 0, (short) 0, SchematicMaterials.ALPHA);
    }

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
  }

  private static void writeSchematic(final Schematic schematic, final File file) throws IOException {
    try (FileOutputStream output = new FileOutputStream(file)) {
      try {
        SchematicIO.writeFuture(schematic, output, false).join();
      } catch (CompletionException ex) {
        final Throwable cause = ex.getCause();
        if (cause instanceof IOException) {
          throw (IOException) cause;
        }
        throw ex;
      }
    }
  }
}
