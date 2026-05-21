package xyz.webmc.wlib.api.util;

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

  public static final long mix64(final long z) {
    long ret = z;
    ret = (ret ^ (ret >>> 33)) * 0xFF51AFD7ED558CCDL;
    ret = (ret ^ (ret >>> 33)) * 0xC4CEB9FE1A85EC53L;
    return ret ^ (ret >>> 33);
  }
}
