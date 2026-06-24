package xyz.webmc.wlib.api.util;

import xyz.webmc.wlib.api.WLIB;

@Deprecated(forRemoval = true)
public final class AlertUtil {
  @Deprecated(forRemoval = true)
  public static final void devAlert(final String... txt) {
    WLIB.warnDeprecatedUsage();
    WLIB.devAlert(txt);
  }
}
