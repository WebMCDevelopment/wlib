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

import java.util.List;

import org.bukkit.command.CommandSender;

public final class WLIBBlankCommand extends WCommand {
  public WLIBBlankCommand(String name) {
    super(name);
  }

  @Override
  public boolean run(CommandSender sender, String label, String[] args) {
    return true;
  }

  @Override
  public List<String> tab(CommandSender sender, String label, String[] args) {
    return List.of();
  }
}
