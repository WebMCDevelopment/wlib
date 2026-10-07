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

import xyz.webmc.wlib.internal.compat.WCompat;

import dev.colbster937.reflect.MirrorSafe;

import static xyz.webmc.wlib.internal.util.InternalUtil.checkInternalCaller;

public final class CompatUtil {
  public static WCompat getWCompat(Class<?> clazz) {
    checkInternalCaller();
    return clazz.getAnnotation(WCompat.class);
  }

  public static Class<?> getCompatClass(Class<?> clazz) {
    checkInternalCaller();
    return getCompatValue(clazz, "value");
  }

  public static boolean getIsCompatClass(Class<?> clazz) {
    checkInternalCaller();
    return getWCompat(clazz) != null;
  }

  private static <T> T getCompatValue(WCompat compat, String name) {
    if (compat != null) {
      return MirrorSafe.invokeMethod(compat, name);
    } else {
      return null;
    }
  }

  private static <T> T getCompatValue(Class<?> clazz, String name) {
    return getCompatValue(getWCompat(clazz), name);
  }
}
