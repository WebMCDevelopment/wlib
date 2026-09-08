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

package xyz.webmc.wlib.api.structures;


import java.util.HashMap;
import java.util.Map;

import dev.colbster937.reflect.MirrorSafe;


@SuppressWarnings({"unchecked"})
public class StructuresInstances {
  private static final Map<Class<? extends AbstractBaseStructure>, AbstractBaseStructure> INSTANCES = new HashMap<>();

  public static final <T extends AbstractBaseStructure> T getInstance(final Class<T> clazz, final Object... params) {
    registerInstance(clazz, params);

    return (T) INSTANCES.get(clazz);
  }

  public static final <T extends AbstractBaseStructure> void registerInstance(final Class<T> clazz, final Object... params) {
    if (INSTANCES.containsKey(clazz)) {
      INSTANCES.put(clazz, MirrorSafe.invokeConstructor(clazz, params));
    }
  }
}
