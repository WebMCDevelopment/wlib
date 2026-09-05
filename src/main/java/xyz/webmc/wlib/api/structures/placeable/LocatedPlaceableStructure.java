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

package xyz.webmc.wlib.api.structures.placeable;

import xyz.webmc.wlib.api.structures.blocks.LocatedBlock;
import xyz.webmc.wlib.api.structures.blocks.RelativeBlock;
import xyz.webmc.wlib.api.util.SchemUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

import com.cryptomorin.xseries.XMaterial;
import net.sandrohc.schematic4j.exception.ParsingException;
import net.sandrohc.schematic4j.schematic.Schematic;
import net.sandrohc.schematic4j.schematic.types.SchematicBlock;
import net.sandrohc.schematic4j.schematic.types.SchematicBlockPos;
import org.bukkit.Chunk;
import org.bukkit.Location;

public class LocatedPlaceableStructure extends PlaceableStructure<LocatedBlock> {
  private final Location location;

  public LocatedPlaceableStructure(Location location) {
    super(new ArrayList<>());
    this.location = location;
  }

  public void place() {
    for (final LocatedBlock block : this.blocks) {
      block.place();
    }
  }

  public void place(Chunk chunk) {
    for (final LocatedBlock block : this.blocks) {
      final Location blockLocation = block.getLocation();
      if (blockLocation.getWorld() == chunk.getWorld() && block.getX() >> 4 == chunk.getX() && block.getZ() >> 4 == chunk.getZ()) {
        block.place();
      }
    }
  }

  @Override
  public void loadSchematic(InputStream stream, int offsetx, int offsety, int offsetz) {
    try (stream) {
      final Schematic schematic = SchemUtil.readSchematic(stream);
      final SchematicBlockPos offset = schematic.offset();

      for (int y = 0; y < schematic.height(); y++) {
        for (int z = 0; z < schematic.length(); z++) {
          for (int x = 0; x < schematic.width(); x++) {
            final SchematicBlock block = schematic.block(x, y, z);
            if (block != SchematicBlock.AIR) {
              final XMaterial material = XMaterial.matchXMaterial(block.block().replace("minecraft:", "")).orElse(XMaterial.AIR);
              final StringBuilder data = new StringBuilder();

              for (final Map.Entry<String, String> entry : block.states().entrySet()) {
                if (data.length() > 0) {
                  data.append(",");
                }

                data.append(entry.getKey()).append("=").append(entry.getValue());
              }

              final RelativeBlock relativeBlock = new RelativeBlock(
                  x - offset.x + offsetx,
                  y - offset.y + offsety,
                  z - offset.z + offsetz,
                  material,
                  "[" + data + "]");
              this.blocks.add(relativeBlock.toLocated(this.location));
            }
          }
        }
      }
    } catch (final IOException | ParsingException ex) {
      throw new RuntimeException(ex);
    }
  }
}
