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

package xyz.webmc.wlib.internal.util;

import xyz.webmc.wlib.api.structure.BaseStructure;
import xyz.webmc.wlib.internal.structure.CoordinateStructure;
import xyz.webmc.wlib.internal.structure.HerobrineShrineTestStructure;
import xyz.webmc.wlib.internal.structure.RickQRCodeTestStructure;

import java.util.HashSet;
import java.util.Set;

import dev.colbster937.reflect.MirrorSafe;

public final class TestStructureUtil {
  private static final Set<Class<? extends BaseStructure>> WLIB_TEST_STRUCTURES = Set.of(
      HerobrineShrineTestStructure.class,
      RickQRCodeTestStructure.class,
      CoordinateStructure.class);

  private static final Set<BaseStructure> STRUCTURES = new HashSet<>();

  public static void _init() {
    for (Class<? extends BaseStructure> clazz : WLIB_TEST_STRUCTURES) {
      getInstance(clazz);
    }
  }

  public static <T extends BaseStructure> T getInstance(Class<T> clazz, Object... params) {
    for (BaseStructure structure : STRUCTURES) {
      if (structure.getClass().equals(clazz)) {
        return clazz.cast(structure);
      }
    }

    T structure = MirrorSafe.invokeConstructor(clazz, params);
    registerInstance(structure);
    return structure;
  }

  public static <T extends BaseStructure> void registerInstance(Class<T> clazz, Object... params) {
    getInstance(clazz, params);
  }

  private static void registerInstance(BaseStructure structure) {
    STRUCTURES.add(structure);
  }

  public static BaseStructure getTestStructure(String name) {
    for (BaseStructure structure : getStructures()) {
      if (structure.getName().equals(name)) {
        return structure;
      }
    }

    return null;
  }

  public static BaseStructure getTestStructure(Class<?> clazz) {
    for (BaseStructure structure : getStructures()) {
      if (structure.getClass().equals(clazz)) {
        return structure;
      }
    }

    return null;
  }

  public static Set<BaseStructure> getStructures() {
    return STRUCTURES;
  }

  public static Set<String> getStructureNames() {
    Set<String> names = new HashSet<>();

    for (BaseStructure structure : getStructures()) {
      names.add(structure.getName());
    }

    return names;
  }

}
