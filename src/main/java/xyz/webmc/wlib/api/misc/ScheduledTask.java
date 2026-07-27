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

import com.tcoded.folialib.wrapper.task.WrappedTask;

public final class ScheduledTask {
  private final WrappedTask task;

  private ScheduledTask(final WrappedTask task) {
    this.task = task;
  }

  public void cancel() {
    this.task.cancel();
  }

  public final boolean isCancelled() {
    return this.task.isCancelled();
  }

  public final boolean isAsync() {
    return this.task.isAsync();
  }

  public final WrappedTask getWrappedTask() {
    return this.task;
  }

  public static ScheduledTask from(final WrappedTask task) {
    return new ScheduledTask(task);
  }
}
