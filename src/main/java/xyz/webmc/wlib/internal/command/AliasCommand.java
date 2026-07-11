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
import xyz.webmc.wlib.api.util.CommandUtil;

import java.util.List;

import org.bukkit.command.CommandSender;

public final class AliasCommand extends WCommand {
  private final String cmd;

  public AliasCommand(final String cmd, final String name) {
    super(name);
    this.cmd = cmd;
  }

  @Override
  public final boolean run(final CommandSender sender, final String label, final String[] args) {
    return CommandUtil.dispatch(sender, getFullCommand(args));
  }

  @Override
  public final List<String> tab(final CommandSender sender, final String label, final String[] args) {
    return CommandUtil.tabComplete(sender, getFullCommand(args));
  }

  private String getFullCommand(final String[] args) {
    String ret = this.cmd;

    if (args.length > 0) {
      ret += ' ' + String.join(" ", args);
    }

    return ret;
  }
}
