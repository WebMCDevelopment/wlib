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

import xyz.webmc.wlib.api.structures.blocks.PlaceableBlock;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings({ "unchecked" })
public abstract class PlaceableStructure<B extends PlaceableBlock> {
  protected List<B> blocks = new ArrayList<>();

  protected PlaceableStructure(final List<B> blocks) {
    this.blocks = blocks;
  }

  public abstract void loadSchematic(final InputStream stream, final int offsetx, final int offsety, final int offsetz);

  public final void loadSchematic(final File file, final int offsetx, final int offsety, final int offsetz) {
    try (final FileInputStream fis = new FileInputStream(file)) {
      this.loadSchematic(fis, offsetx, offsety, offsetz);
    } catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  public final void loadSchematic(final InputStream stream) {
    this.loadSchematic(stream, 0, 0, 0);
  }

  public final void loadSchematic(final File file) {
    this.loadSchematic(file, 0, 0, 0);
  }

  public final void addBlock(final B blk) {
    this.blocks.add(blk);
  }

  public final void addBlocks(final Collection<B> blks) {
    this.blocks.addAll(blks);
  }

  public final void addBlocks(final B... blks) {
    this.blocks.addAll(List.of(blks));
  }
}
