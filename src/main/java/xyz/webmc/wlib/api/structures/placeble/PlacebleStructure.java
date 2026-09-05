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

package xyz.webmc.wlib.api.structures.placeble;

import xyz.webmc.wlib.api.misc.SchemLoadingError;
import xyz.webmc.wlib.api.structures.blocks.Block;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings({ "unchecked" })
public abstract class PlacebleStructure<B extends Block> {
  protected List<B> blocks = new ArrayList<>();

  protected PlacebleStructure(List<B> blocks) {
    this.blocks = blocks;
  }

  public void addBlock(B blk) {
    this.blocks.add(blk);
  }

  public void addBlocks(Collection<B> blks) {
    this.blocks.addAll(blks);
  }

  public void addBlocks(B... blks) {
    this.blocks.addAll(List.of(blks));
  }

  public abstract void loadSchematic(InputStream stream, int offsetx, int offsety, int offsetz)
      throws SchemLoadingError;

  public void loadSchematic(File file, int offsetx, int offsety, int offsetz) throws SchemLoadingError {
    try (final FileInputStream fis = new FileInputStream(file)) {
      this.loadSchematic(fis, offsetx, offsety, offsetz);
    } catch (Exception e) {
      throw new SchemLoadingError("Failed to load schematic from file: " + file.getAbsolutePath());
    }
  }

  public void loadSchematic(InputStream stream) throws SchemLoadingError {
    this.loadSchematic(stream, 0, 0, 0);
  }

  public void loadSchematic(File file) throws SchemLoadingError {
    this.loadSchematic(file, 0, 0, 0);
  }
}
