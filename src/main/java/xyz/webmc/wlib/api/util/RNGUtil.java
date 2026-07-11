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

import xyz.webmc.wlib.api.WLIB;

import java.util.SplittableRandom;

public final class RNGUtil {
  private static final SplittableRandom RNG = new SplittableRandom();

  public static final SplittableRandom getRandom(final long seed) {
    return new SplittableRandom(seed);
  }

  public static final SplittableRandom getRandom() {
    return RNG.split();
  }

  public static final long getRandomSeed() {
    return RNG.nextLong();
  }

  public static final String getRandomString(final String chars, final int len, final long seed) {
    final StringBuilder sb = new StringBuilder(len);
    final SplittableRandom rng = getRandom(seed);

    for (int i = 0; i < len; i++) {
      sb.append(chars.charAt(rng.nextInt(chars.length())));
    }

    return sb.toString();
  }

  public static final String getRandomStringLowercaseAZ(final int len, final long seed) {
    return getRandomString("abcdefghijklmnopqrstuvwxyz", len, seed);
  }

  public static final String getRandomString(final String chars, final int len) {
    return getRandomString(chars, len, getRandomSeed());
  }

  public static final String getRandomStringLowercaseAZ(final int len) {
    return getRandomStringLowercaseAZ(len, getRandomSeed());
  }

  @Deprecated(forRemoval = true)
  public static int mix32(final int z) {
    WLIB.warnDeprecatedUsage();
    return HashUtil.mix32(z);
  }

  @Deprecated(forRemoval = true)
  public static final long mix64(final long z) {
    WLIB.warnDeprecatedUsage();
    return HashUtil.mix64(z);
  }
}
