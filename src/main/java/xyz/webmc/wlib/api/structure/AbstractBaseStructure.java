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

import xyz.webmc.wlib.api.WLIB;
import xyz.webmc.wlib.api.util.SchemUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cryptomorin.xseries.XMaterial;
import dev.colbster937.reflect.MirrorSafe;
import net.sandrohc.schematic4j.exception.ParsingException;
import net.sandrohc.schematic4j.schematic.Schematic;
import net.sandrohc.schematic4j.schematic.types.SchematicBlock;
import net.sandrohc.schematic4j.schematic.types.SchematicBlockPos;
import org.bukkit.Location;

@SuppressWarnings({ "unchecked", "removal" })
@Deprecated(forRemoval = true)
public abstract class AbstractBaseStructure {
  private static final Map<Class<? extends AbstractBaseStructure>, AbstractBaseStructure> INSTANCES = new HashMap<>();

  private final List<BlockRelative> blocks = new ArrayList<>();
  private final String name;

  @Deprecated(forRemoval = true)
  protected AbstractBaseStructure(final String name) {
    this.name = name;
  }

  @Deprecated(forRemoval = true)
  public final void place(final Location loc) {
    WLIB.warnDeprecatedUsage();
    final Location offset = loc.clone().add(this.getOffsetX(), this.getOffsetY(), this.getOffsetZ());
    for (final BlockRelative blk : blocks) {
      blk.place(offset);
    }
  }

  @Deprecated(forRemoval = true)
  public final String getName() {
    WLIB.warnDeprecatedUsage();
    return this.name;
  }

  @Deprecated(forRemoval = true)
  public int getOffsetX() {
    WLIB.warnDeprecatedUsage();
    return 0;
  }

  @Deprecated(forRemoval = true)
  public int getOffsetY() {
    WLIB.warnDeprecatedUsage();
    return 0;
  }

  @Deprecated(forRemoval = true)
  public int getOffsetZ() {
    WLIB.warnDeprecatedUsage();
    return 0;
  }

  @Deprecated(forRemoval = true)
  protected final void addBlock(final BlockRelative blk) {
    WLIB.warnDeprecatedUsage();
    this.blocks.add(blk);
  }

  @Deprecated(forRemoval = true)
  protected final void loadSchematic(final InputStream is) throws IOException, ParsingException {
    WLIB.warnDeprecatedUsage();
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

                this.addBlock(new BlockRelative(x - offset.x, y - offset.y, z - offset.z, mat, "[" + sb.toString() + "]"));
              }
            }
          }
        }
      }
    }
  }

  @Deprecated(forRemoval = true)
  protected final void loadSchematic(final File file) throws IOException, ParsingException {
    loadSchematic(new FileInputStream(file));
  }

  @Deprecated(forRemoval = true)
  public static final <T extends AbstractBaseStructure> T getInstance(final Class<T> clazz, final Object... params) {
    WLIB.warnDeprecatedUsage();
    AbstractBaseStructure structure = INSTANCES.get(clazz);

    if (structure == null) {
      structure = MirrorSafe.invokeConstructor(clazz, params);
      INSTANCES.put(clazz, structure);
    }

    return (T) structure;
  }
}
