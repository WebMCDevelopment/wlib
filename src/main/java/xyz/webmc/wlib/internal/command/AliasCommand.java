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
import xyz.webmc.wlib.internal.iface.WInternal;
import xyz.webmc.wlib.internal.util.InternalUtil;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import dev.colbster937.reflect.MirrorSafe;
import org.bukkit.command.CommandSender;

@WInternal
public final class AliasCommand extends WCommand {
  private static final ThreadLocal<Deque<AliasCommand>> CURRENT = ThreadLocal.withInitial(ArrayDeque::new);
  private final String cmd;

  public AliasCommand(String cmd, String name) {
    super(name);
    InternalUtil.checkInternalCaller();
    this.cmd = cmd;
  }

  @Override
  protected boolean run(CommandSender sender, String label, String[] args) {
    return this.exec("dispatch", sender, args);
  }

  @Override
  protected List<String> tab(CommandSender sender, String label, String[] args) {
    return this.exec("tabComplete", sender, args);
  }

  private <T> T exec(String method, CommandSender sender, String[] args) {
    Deque<AliasCommand> stack = CURRENT.get();

    if (stack == null) {
      stack = new ArrayDeque<>();
      CURRENT.set(stack);
    }

    stack.push(this);

    String arg = this.cmd;
    if (args.length > 0) {
      arg += " " + String.join(" ", args);
    }

    try {
      return MirrorSafe.invokeMethod(CommandUtil.class, method, sender, arg);
    } finally {
      stack.pop();
      if (stack.isEmpty()) {
        CURRENT.remove();
      }
    }
  }

  public static AliasCommand get() {
    final Deque<AliasCommand> stack = CURRENT.get();
    if (stack != null) {
      return stack.peek();
    } else {
      return null;
    }
  }
}
