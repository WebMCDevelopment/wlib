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

import xyz.webmc.wlib.internal.compat.api.misc.ScheduledTaskCompat;

import com.tcoded.folialib.wrapper.task.WrappedTask;
import org.bukkit.plugin.Plugin;

public final class ScheduledTask extends ScheduledTaskCompat {
  private final WrappedTask task;
  private final Plugin plugin;

  public ScheduledTask(WrappedTask task, Plugin plugin) {
    this.task = task;
    this.plugin = plugin;
  }

  public ScheduledTask(WrappedTask task) {
    this(task, task.getOwningPlugin());
  }

  public void cancel() {
    this.task.cancel();
  }

  public boolean isCancelled() {
    return this.task.isCancelled();
  }

  public boolean isAsync() {
    return this.task.isAsync();
  }

  public Plugin getOwningPlugin() {
    return this.plugin;
  }

  @Deprecated
  public WrappedTask getWrappedTask() {
    return this.task;
  }
}
