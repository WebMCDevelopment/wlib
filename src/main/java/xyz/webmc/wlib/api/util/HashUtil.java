/*
 * Copyright (C) 2026 ${plugin.athr}
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

public final class HashUtil {
  public static int mix32(final int z) {
    int ret = z;
    ret = (ret ^ (ret >>> 16)) * 0x85EBCA6B;
    ret = (ret ^ (ret >>> 13)) * 0xC2B2AE35;
    return ret ^ (ret >>> 16);
  }

  public static final long mix64(final long z) {
    long ret = z;
    ret = (ret ^ (ret >>> 33)) * 0xFF51AFD7ED558CCDL;
    ret = (ret ^ (ret >>> 33)) * 0xC4CEB9FE1A85EC53L;
    return ret ^ (ret >>> 33);
  }

  public static int hash32(final byte[] data) {
    int hash = 0x811C9DC5;

    for (final byte b : data) {
      hash ^= b & 0xFF;
      hash *= 0x01000193;
    }

    return mix32(hash);
  }

  public static String hash32H(final byte[] data) {
    return String.format("%08x", hash32(data));
  }

  public static String hash32S(final String str) {
    return hash32H(str.getBytes());
  }

  public static long hash64(final byte[] data) {
    long hash = 0xCBF29CE484222325L;

    for (final byte b : data) {
      hash ^= b & 0xFFL;
      hash *= 0x100000001B3L;
    }

    return mix64(hash);
  }

  public static String hash64H(final byte[] data) {
    return String.format("%016x", hash64(data));
  }

  public static String hash64S(final String str) {
    return hash64H(str.getBytes());
  }
}
