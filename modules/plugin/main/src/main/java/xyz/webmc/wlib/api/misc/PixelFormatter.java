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

package xyz.webmc.wlib.api.misc;

import java.util.Map;
import java.util.function.IntFunction;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public final class PixelFormatter {
  private static final Map<ChatColor, Integer> MC_COLORS = Map.ofEntries(
    Map.entry(ChatColor.BLACK, 0x000000),
    Map.entry(ChatColor.DARK_BLUE, 0x0000AA),
    Map.entry(ChatColor.DARK_GREEN, 0x00AA00),
    Map.entry(ChatColor.DARK_AQUA, 0x00AAAA),
    Map.entry(ChatColor.DARK_RED, 0xAA0000),
    Map.entry(ChatColor.DARK_PURPLE, 0xAA00AA),
    Map.entry(ChatColor.GOLD, 0xFFAA00),
    Map.entry(ChatColor.GRAY, 0xAAAAAA),
    Map.entry(ChatColor.DARK_GRAY, 0x555555),
    Map.entry(ChatColor.BLUE, 0x5555FF),
    Map.entry(ChatColor.GREEN, 0x55FF55),
    Map.entry(ChatColor.AQUA, 0x55FFFF),
    Map.entry(ChatColor.RED, 0xFF5555),
    Map.entry(ChatColor.LIGHT_PURPLE, 0xFF55FF),
    Map.entry(ChatColor.YELLOW, 0xFFFF55),
    Map.entry(ChatColor.WHITE, 0xFFFFFF)
  );

  public static final char PIXEL_BASE = '\u2588';
  public static final char PIXEL_NONE = '\u3000';

  public static final PixelFormatter MINECRAFT = new PixelFormatter((int rgb) -> {
    final String hex = String.format("%06x", rgb & 0xFFFFFF);

    final StringBuilder rgbString = new StringBuilder(ChatColor.COLOR_CHAR + "x");
    hex.codePoints().forEach(c -> rgbString.append(ChatColor.COLOR_CHAR).appendCodePoint(c));

    return rgbString.toString();
  });

  public static final PixelFormatter MINECRAFT_LEGACY = new PixelFormatter((int rgb) -> {
    final int[] argb = getARGB(rgb);

    final ChatColor[] color = { ChatColor.WHITE };
    final int[] d = { Integer.MAX_VALUE };

    MC_COLORS.forEach((k, v) -> {
      final int cr = (v >> 16) & 0xFF;
      final int cg = (v) & 0xFF;
      final int cb = v & 0xFF;
      final int dr = argb[1] - cr;
      final int dg = argb[2] - cg;
      final int db = argb[3] - cb;
      final int cd = dr * dr + dg * dg + db * db;
      if (cd < d[0]) {
        color[0] = k;
        d[0] = cd;
      }
    });

    return color[0].toString();
  });

  public static final PixelFormatter MINECRAFT_TERMINAL = new PixelFormatter(MINECRAFT.func, 2);

  public static final PixelFormatter ANSI = new PixelFormatter(rgb -> {
    final int[] color = getARGB(rgb);
    return "\u001B[38;2;" +
      color[1] + ";" +
      color[2] + ";" +
      color[3] + "m";
  }, 2);

  private final IntFunction<String> func;
  private final int count;

  private PixelFormatter(IntFunction<String> func, int count) {
    this.func = func;
    this.count = count;
  }

  private PixelFormatter(IntFunction<String> func) {
    this(func, 1);
  }

  public String format(int rgb) {
    String ret;

    final int alpha = getARGB(rgb)[0];
    if (alpha > 0) {
      ret = this.func.apply(rgb);

      final char pixel;
      if (alpha < 64) {
        pixel = '\u2591';
      } else if (alpha < 128) {
        pixel = '\u2592';
      } else if (alpha < 192) {
        pixel = '\u2593';
      } else {
        pixel = PIXEL_BASE;
      }

      for (int i = 0; i < this.count; i++) {
        ret += pixel;
      }
    } else {
      ret = Character.toString(PIXEL_NONE);
    }

    return ret;
  }

  public static PixelFormatter getFromCommandSender(CommandSender sender) {
    if (!getIsCommandSenderTerminal(sender)) {
      return MINECRAFT;
    } else {
      return MINECRAFT_TERMINAL;
    }
  }

  public static boolean getIsCommandSenderTerminal(CommandSender sender) {
    return sender instanceof ConsoleCommandSender;
  }

  private static int[] getARGB(int rgb) {
    return new int[] {
      (rgb >> 24) & 0xFF,
      (rgb >> 16) & 0xFF,
      (rgb >> 8) & 0xFF,
      rgb & 0xFF
    };
  }
}
