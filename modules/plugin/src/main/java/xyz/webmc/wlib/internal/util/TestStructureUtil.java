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

import xyz.webmc.wlib.api.structure.AbstractBaseStructure;
import xyz.webmc.wlib.internal.structure.HerobrineShrineTestStructure;
import xyz.webmc.wlib.internal.structure.RickQRCodeTestSchemStructure;

import java.util.HashSet;
import java.util.Set;

import dev.colbster937.reflect.MirrorSafe;

import static xyz.webmc.wlib.internal.util.InternalUtil.checkInternalCaller;

public final class TestStructureUtil {
  private static final Set<Class<? extends AbstractBaseStructure>> WLIB_TEST_STRUCTURES = Set.of(
    HerobrineShrineTestStructure.class,
    RickQRCodeTestSchemStructure.class
  );

  private static final Set<AbstractBaseStructure> STRUCTURES = new HashSet<>();

  public static void init() {
    checkInternalCaller();
    for (Class<? extends AbstractBaseStructure> clazz : WLIB_TEST_STRUCTURES) {
      AbstractBaseStructure.getInstance(clazz);
    }
  }

  public static void register(AbstractBaseStructure structure) {
    checkInternalCaller();
    STRUCTURES.add(structure);
  }

  public static AbstractBaseStructure getTestStructure(String name) {
    checkInternalCaller();
    return getTestStructure(name, "getName");
  }

  public static AbstractBaseStructure getTestStructure(Class<?> clazz) {
    checkInternalCaller();
    return getTestStructure(clazz, "getClass");
  }

  public static Set<AbstractBaseStructure> getStructures() {
    checkInternalCaller();
    return STRUCTURES;
  }

  public static Set<String> getStructureNames() {
    checkInternalCaller();

    final Set<String> names = new HashSet<>();

    for (AbstractBaseStructure structure : getStructures()) {
      names.add(structure.getName());
    }

    return names;
  }

  private static <T> AbstractBaseStructure getTestStructure(T obj, String method) {
    for (AbstractBaseStructure structure : getStructures()) {
      if (MirrorSafe.invokeMethod(structure, method).equals(obj)) {
        return structure;
      }
    }

    return null;
  }
}
