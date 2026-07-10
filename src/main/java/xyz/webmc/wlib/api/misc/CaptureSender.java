/*
 * Copyright (C) 2026 ${plugin.athr}
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

  public CaptureSender(final CommandSender parent) {
    super(parent);
  }

  public CaptureSender() {
    super(null);
  }

  public final List<String> getMessages() {
    return this.messages;
  }

  @Override
  public final void sendMessage(final String message) {
    this.messages.add(message);
  }

  @Override
  public final void sendMessage(final String[] messages) {
    this.messages.addAll(Arrays.asList(messages));
  }
}
