package xyz.webmc.wlib.api.util;

public final class RandomUtil {
  public static final String randomString(final String chars, final int len) {
    final StringBuilder sb = new StringBuilder();

    for (int i = 0; i < len; i++) {
      sb.append(chars.charAt((int) (Math.random() * chars.length())));
    }

    return sb.toString();
  }

  public static final String randomStringLowercaseAZ(final int len) {
    return randomString("abcdefghijklmnopqrstuvwxyz", len);
  }
}
