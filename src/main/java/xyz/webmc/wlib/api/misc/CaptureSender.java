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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;

public final class CaptureSender extends ExtendableCommandSender {
  private final List<String> messages = new ArrayList<>();
  private final boolean forward;

  public CaptureSender(CommandSender parent, boolean forward) {
    super(parent);
    this.forward = forward;
  }

  public CaptureSender(CommandSender parent) {
    this(parent, false);
  }

  public CaptureSender() {
    this(null);
  }

  public List<String> getMessages() {
    return this.messages;
  }

  @Override
  public void sendMessage(String message) {
    this.messages.add(message);
    if (this.forward) {
      super.parent.sendMessage(message);
    }
  }

  @Override
  public void sendMessage(String[] messages) {
    this.messages.addAll(Arrays.asList(messages));
    if (this.forward) {
      super.parent.sendMessage(messages);
    }
  }
}
