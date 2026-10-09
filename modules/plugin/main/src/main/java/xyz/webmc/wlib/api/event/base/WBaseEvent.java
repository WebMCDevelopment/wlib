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

package xyz.webmc.wlib.api.event.base;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class WBaseEvent extends Event {
  private static final Map<Class<? extends WBaseEvent>, HandlerList> HANDLERS = new HashMap<>();

  @Override
  public final HandlerList getHandlers() {
    return getHandlerList(this.getClass());
  }

  public static HandlerList getHandlerList() {
    return getHandlerList(WBaseEvent.class);
  }

  protected static HandlerList getHandlerList(Class<? extends WBaseEvent> clazz) {
    return HANDLERS.computeIfAbsent(clazz, k -> new HandlerList());
  }
}
