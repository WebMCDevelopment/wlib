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

package xyz.webmc.wlib.internal.command;

import xyz.webmc.wlib.api.command.WCommand;
import xyz.webmc.wlib.api.util.RNGUtil;

import java.util.List;

import org.bukkit.command.CommandSender;

public final class WLIBBlankCommand extends WCommand {
  private static final String NAME = RNGUtil.getRandomStringLowercaseAZ(16);

  public WLIBBlankCommand() {
    super(NAME);
  }

  public static final String getBlankRandomCommandName() {
    return NAME;
  }

  public static final String getBlankRandomCommandKey() {
    return "wlib:" + getBlankRandomCommandName();
  }

  @Override
  public final boolean run(final CommandSender sender, final String label, final String[] args) {
    return true;
  }

  @Override
  public final List<String> tab(final CommandSender sender, final String label, final String[] args) {
    return List.of();
  }
}
