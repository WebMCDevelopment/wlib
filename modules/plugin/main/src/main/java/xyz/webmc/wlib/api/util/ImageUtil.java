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

import xyz.webmc.wlib.api.misc.PixelFormatter;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.bukkit.plugin.Plugin;

import static xyz.webmc.wlib.internal.util.InternalUtil.checkInternalCaller;

public final class ImageUtil {
  private static BufferedImage WLIB_IMG;

  public static void _init(Plugin plugin) throws IOException {
    checkInternalCaller();

    try (InputStream is = plugin.getResource("resources/img/wlib.png")) {
      WLIB_IMG = resizeImage(is, 16, 16);
    }
  }

  public static BufferedImage getWLIBImage() {
    return WLIB_IMG;
  }

  public static BufferedImage resizeImage(BufferedImage img, int width, int height) {
    final BufferedImage out = new BufferedImage(width, height, img.getType());
    final Graphics2D g2d = out.createGraphics();

    try {
      g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      g2d.drawImage(img, 0, 0, width, height, null);
    } finally {
      g2d.dispose();
    }

    return out;
  }

  public static BufferedImage resizeImage(InputStream is, int width, int height) throws IOException {
    return resizeImage(ImageIO.read(is), width, height);
  }

  public static BufferedImage resizeImage(File file, int width, int height) throws IOException {
    return resizeImage(ImageIO.read(file), width, height);
  }

  public static BufferedImage resizeImage(byte[] bytes, int width, int height) throws IOException {
    return resizeImage(new ByteArrayInputStream(bytes), width, height);
  }

  public static BufferedImage scaleImage(BufferedImage img, float scale) {
    return resizeImage(img, Math.round(img.getWidth() * scale), Math.round(img.getHeight() * scale));
  }

  public static BufferedImage scaleImage(InputStream is, float scale) throws IOException {
    return scaleImage(ImageIO.read(is), scale);
  }

  public static BufferedImage scaleImage(File file, float scale) throws IOException {
    return scaleImage(ImageIO.read(file), scale);
  }

  public static BufferedImage scaleImage(byte[] bytes, float scale) throws IOException {
    return scaleImage(new ByteArrayInputStream(bytes), scale);
  }

  public static BufferedImage removeAlpha(BufferedImage img) {
    for (int y = 0; y < img.getHeight(); y++) {
      for (int x = 0; x < img.getWidth(); x++) {
        final int rgb = img.getRGB(x, y);

        final int set;
        if (((rgb >> 24) & 0xFF) < 128) {
          set = 0x00000000;
        } else {
          set = rgb | 0xFF000000;
        }

        img.setRGB(x, y, set);
      }
    }

    return img;
  }

  public static BufferedImage removeAlpha(InputStream is) throws IOException {
    return removeAlpha(ImageIO.read(is));
  }

  public static BufferedImage removeAlpha(File file) throws IOException {
    return removeAlpha(ImageIO.read(file));
  }

  public static BufferedImage removeAlpha(byte[] bytes) throws IOException {
    return removeAlpha(new ByteArrayInputStream(bytes));
  }

  public static String[] convertToMinecraftASCII(BufferedImage img, PixelFormatter formatter) {
    final int width = img.getWidth();
    final int height = img.getHeight();

    final String[] ret = new String[height];

    for (int y = 0; y < height; y++) {
      final StringBuilder line = new StringBuilder();

      for (int x = 0; x < width; x++) {
        line.append(formatter.format(img.getRGB(x, y)));
      }

      ret[y] = line.toString();
    }

    return ret;
  }

  public static String[] convertToMinecraftASCII(InputStream is, PixelFormatter formatter) throws IOException {
    return convertToMinecraftASCII(ImageIO.read(is), formatter);
  }

  public static String[] convertToMinecraftASCII(File file, PixelFormatter formatter) throws IOException {
    return convertToMinecraftASCII(ImageIO.read(file), formatter);
  }

  public static String[] convertToMinecraftASCII(byte[] bytes, PixelFormatter formatter) throws IOException {
    return convertToMinecraftASCII(new ByteArrayInputStream(bytes), formatter);
  }
}
