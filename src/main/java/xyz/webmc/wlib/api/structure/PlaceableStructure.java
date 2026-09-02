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

package xyz.webmc.wlib.api.structure;

import xyz.webmc.wlib.api.util.SchemUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.cryptomorin.xseries.XMaterial;
import net.sandrohc.schematic4j.exception.ParsingException;
import net.sandrohc.schematic4j.schematic.Schematic;
import net.sandrohc.schematic4j.schematic.types.SchematicBlock;
import net.sandrohc.schematic4j.schematic.types.SchematicBlockPos;
import org.bukkit.Chunk;
import org.bukkit.Location;

public final class PlaceableStructure {
  private final String name;
  private final List<BlockRelative> blocks = new ArrayList<>();

  public PlaceableStructure(final String name) {
    this.name = name;
  }

  public final String getName() {
    return this.name;
  }

  public final List<BlockRelative> getBlocks() {
    return Collections.unmodifiableList(this.blocks);
  }

  public final void addBlock(final BlockRelative block) {
    if (block != null) {
      this.blocks.add(block);
    }
  }

  public final void addBlocks(final Collection<BlockRelative> blocks) {
    if (blocks != null) {
      this.blocks.addAll(blocks);
    }
  }

  public final void addBlocks(final BlockRelative... blocks) {
    if (blocks != null) {
      Collections.addAll(this.blocks, blocks);
    }
  }

  public final void place(final Location loc) {
    final Location origin = loc.clone();
    for (final BlockRelative block : this.blocks) {
      block.place(origin);
    }
  }

  public final void place(final Chunk chunk) {
    final PlaceableStructure chunkData = this.getChunk(0, 0);
    final Location origin = new Location(chunk.getWorld(), chunk.getX() * 16, 65, chunk.getZ() * 16);
    chunkData.place(origin);
  }

  public final PlaceableStructure getChunk(final int chunkX, final int chunkZ) {
    final int minX = chunkX * 16;
    final int maxX = minX + 15;
    final int minZ = chunkZ * 16;
    final int maxZ = minZ + 15;

    final PlaceableStructure chunk = new PlaceableStructure(this.name + "_chunk_" + chunkX + "_" + chunkZ);
    for (final BlockRelative block : this.blocks) {
      if (block.getX() >= minX && block.getX() <= maxX && block.getZ() >= minZ && block.getZ() <= maxZ) {
        chunk.addBlock(block.offset(-minX, 0, -minZ));
      }
    }

    return chunk;
  }

  public final void loadSchematic(final InputStream is) throws IOException, ParsingException {
    this.loadSchematic(is, 0, 0, 0);
  }

  public final void loadSchematic(final InputStream is, final int offsetX, final int offsetY, final int offsetZ) throws IOException, ParsingException {
    try (is) {
      final Schematic schematic = SchemUtil.readSchematic(is);

      final int width = schematic.width();
      final int height = schematic.height();
      final int length = schematic.length();
      final SchematicBlockPos offset = schematic.offset();

      for (int y = 0; y < height; y++) {
        for (int z = 0; z < length; z++) {
          for (int x = 0; x < width; x++) {
            final SchematicBlock block = schematic.block(x, y, z);
            if (block != SchematicBlock.AIR) {
              final XMaterial mat = XMaterial.matchXMaterial(block.block().replace("minecraft:", "")).orElse(XMaterial.AIR);
              if (mat != null) {
                final StringBuilder sb = new StringBuilder();

                for (final Map.Entry<String, String> entry : block.states().entrySet()) {
                  if (sb.length() > 0) {
                    sb.append(",");
                  }

                  sb.append(entry.getKey());
                  sb.append("=");
                  sb.append(entry.getValue());
                }

                this.addBlock(new BlockRelative(x - offset.x + offsetX, y - offset.y + offsetY, z - offset.z + offsetZ, mat, "[" + sb.toString() + "]"));
              }
            }
          }
        }
      }
    }
  }

  public final void loadSchematic(final File file) throws IOException, ParsingException {
    this.loadSchematic(file, 0, 0, 0);
  }

  public final void loadSchematic(final File file, final int offsetX, final int offsetY, final int offsetZ) throws IOException, ParsingException {
    this.loadSchematic(new FileInputStream(file), offsetX, offsetY, offsetZ);
  }
}
