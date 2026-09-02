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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.sandrohc.schematic4j.exception.ParsingException;
import org.bukkit.Location;

public abstract class AbstractBaseStructure {
  private static final List<Class<AbstractBaseStructure>> STRUCTURES = new ArrayList<>();
  private final String name;

  protected AbstractBaseStructure(final String name) {
    this.name = name;
  }

  public final String getName() {
    return this.name;
  }

  public abstract PlaceableStructure build();

  public static final List<Class<AbstractBaseStructure>> getStructures() {
    return STRUCTURES;
  }

  public static final void registerStructure(final Class<AbstractBaseStructure> clazz) {
    if (clazz != null && !STRUCTURES.contains(clazz)) {
      STRUCTURES.add(clazz);
    }
  }

  /*
   * DEPRECATED METHODS USE PlaceableStructure and .build() INSTEAD
   *
   * The 2 first methods are here to make the transition easier, but .build must be use in the future.
   */

  @Deprecated(forRemoval = true)
  public void place(final Location loc) {
    this.build().place(loc);
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
  }

  @Deprecated(forRemoval = true)
  protected final void loadSchematic(final InputStream is) throws IOException, ParsingException {
    WLIB.warnDeprecatedUsage();
  }

  @Deprecated(forRemoval = true)
  protected final void loadSchematic(final File file) throws IOException, ParsingException {
    WLIB.warnDeprecatedUsage();
  }

  @Deprecated(forRemoval = true)
  public static final <T extends AbstractBaseStructure> T getInstance(final Class<T> clazz, final Object... params) {
    WLIB.warnDeprecatedUsage();
    return null;
  }
}
