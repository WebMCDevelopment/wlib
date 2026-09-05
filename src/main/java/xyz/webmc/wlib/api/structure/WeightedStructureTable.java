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
import xyz.webmc.wlib.api.util.RNGUtil;

import java.util.ArrayList;
import java.util.List;

import dev.colbster937.reflect.MirrorSafe;
import dev.colbster937.util.WeightedObjectTable;
import org.bukkit.Location;

@SuppressWarnings({ "removal" })
@Deprecated(forRemoval = true)
public final class WeightedStructureTable extends WeightedObjectTable<AbstractBaseStructure> {
  @Deprecated(forRemoval = true)
  public WeightedStructureTable(final long seed, final WeightedStructure... structures) {
    super(seed, structures);
    WLIB.warnDeprecatedUsage();
  }

  @Deprecated(forRemoval = true)
  public WeightedStructureTable(final WeightedStructure... structures) {
    this(RNGUtil.getRandomSeed(), structures);
    WLIB.warnDeprecatedUsage();
  }

  @SafeVarargs
  @Deprecated(forRemoval = true)
  public WeightedStructureTable(final long seed, final Class<? extends AbstractBaseStructure>... structures) {
    this(seed, weigh(structures));
    WLIB.warnDeprecatedUsage();
  }

  @SafeVarargs
  @Deprecated(forRemoval = true)
  public WeightedStructureTable(final Class<? extends AbstractBaseStructure>... structures) {
    this(weigh(structures));
    WLIB.warnDeprecatedUsage();
  }

  @Deprecated(forRemoval = true)
  public void place(final Location loc) {
    WLIB.warnDeprecatedUsage();
    this.computeRandomObject().place(loc);
  }

  @SafeVarargs
  @Deprecated(forRemoval = true)
  private static WeightedStructure[] weigh(final Class<? extends AbstractBaseStructure>... structures) {
    final List<WeightedStructure> lst = new ArrayList<>();
    final int chance = 100 / structures.length;

    for (final Class<? extends AbstractBaseStructure> clazz : structures) {
      lst.add(new WeightedStructure(MirrorSafe.invokeConstructor(clazz), chance));
    }

    return lst.toArray(WeightedStructure[]::new);
  }
}
