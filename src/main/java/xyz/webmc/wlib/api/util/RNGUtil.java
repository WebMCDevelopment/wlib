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

package xyz.webmc.wlib.api.util;


import java.util.SplittableRandom;

public final class RNGUtil {
  private static final SplittableRandom RNG = new SplittableRandom();

  public static SplittableRandom getRandom(long seed) {
    return new SplittableRandom(seed);
  }

  public static SplittableRandom getRandom() {
    return RNG.split();
  }

  public static long getRandomSeed() {
    return RNG.nextLong();
  }

  public static String getRandomString(String chars, int len, long seed) {
    final StringBuilder sb = new StringBuilder(len);
    final SplittableRandom rng = getRandom(seed);

    for (int i = 0; i < len; i++) {
      sb.append(chars.charAt(rng.nextInt(chars.length())));
    }

    return sb.toString();
  }

  public static String getRandomStringLowercaseAZ(int len, long seed) {
    return getRandomString("abcdefghijklmnopqrstuvwxyz", len, seed);
  }

  public static String getRandomString(String chars, int len) {
    return getRandomString(chars, len, getRandomSeed());
  }

  public static String getRandomStringLowercaseAZ(int len) {
    return getRandomStringLowercaseAZ(len, getRandomSeed());
  }
}
