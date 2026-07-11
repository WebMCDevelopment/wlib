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

import xyz.webmc.wlib.api.misc.ScheduledTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.impl.PlatformScheduler;
import com.tcoded.folialib.wrapper.task.WrappedTask;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

public final class SchedulerUtil {
  private static final Map<Plugin, List<ScheduledTask>> tasks = new HashMap<>();
  private static Plugin plugin;
  private static FoliaLib lib;
  private static PlatformScheduler sch;

  public static final void init(final Plugin plugin) {
    SchedulerUtil.plugin = plugin;
    lib = new FoliaLib(plugin);
    sch = lib.getScheduler();
  }

  public static final boolean isFolia() {
    return lib.isFolia();
  }

  public static final void cancelPluginTasks(final Plugin plugin) {
    if (tasks.containsKey(plugin)) {
      for (final ScheduledTask task : tasks.get(plugin)) {
        task.cancel();
      }

      tasks.remove(plugin);
    }
  }

  public static final void cancelAllTasks() {
    sch.cancelAllTasks();
  }

  public static final CompletableFuture<Void> runNextTick(final Runnable task) {
    return sch.runNextTick(t -> task.run());
  }

  public static final ScheduledTask runLater(final Plugin plugin, final Runnable task, final long delayTicks) {
    return task(plugin, sch.runLater(task, delayTicks));
  }

  public static final ScheduledTask runLater(final Runnable task, final long delayTicks) {
    return runLater(plugin, task, delayTicks);
  }

  public static final ScheduledTask runTimer(final Plugin plugin, final Runnable task, final long delayTicks, final long periodTicks) {
    return task(plugin, sch.runTimer(task, delayTicks, periodTicks));
  }

  public static final ScheduledTask runTimer(final Runnable task, final long delayTicks, final long periodTicks) {
    return runTimer(plugin, task, delayTicks, periodTicks);
  }

  public static final CompletableFuture<Void> runAsync(final Runnable task) {
    return sch.runAsync(t -> task.run());
  }

  public static final ScheduledTask runLaterAsync(final Plugin plugin, final Runnable task, final long delayTicks) {
    return task(plugin, sch.runLaterAsync(task, delayTicks));
  }

  public static final ScheduledTask runLaterAsync(final Runnable task, final long delayTicks) {
    return runLaterAsync(plugin, task, delayTicks);
  }

  public static final ScheduledTask runLaterAsync(final Plugin plugin, final Runnable task, final long delay, final TimeUnit unit) {
    return task(plugin, sch.runLaterAsync(task, delay, unit));
  }

  public static final ScheduledTask runLaterAsync(final Runnable task, final long delay, final TimeUnit unit) {
    return runLaterAsync(plugin, task, delay, unit);
  }

  public static final ScheduledTask runTimerAsync(final Plugin plugin, final Runnable task, final long delayTicks, final long periodTicks) {
    return task(plugin, sch.runTimerAsync(task, delayTicks, periodTicks));
  }

  public static final ScheduledTask runTimerAsync(final Runnable task, final long delayTicks, final long periodTicks) {
    return runTimerAsync(plugin, task, delayTicks, periodTicks);
  }

  public static final ScheduledTask runTimerAsync(final Plugin plugin, final Runnable task, final long delay, final long period, final TimeUnit unit) {
    return task(plugin, sch.runTimerAsync(task, delay, period, unit));
  }

  public static final ScheduledTask runTimerAsync(final Runnable task, final long delay, final long period, final TimeUnit unit) {
    return runTimerAsync(plugin, task, delay, period, unit);
  }

  public static final CompletableFuture<Void> runAtLocation(final Location loc, final Runnable task) {
    return sch.runAtLocation(loc, t -> task.run());
  }

  public static final ScheduledTask runAtLocationLater(final Plugin plugin, final Location loc, final Runnable task, final long delayTicks) {
    return task(plugin, sch.runAtLocationLater(loc, task, delayTicks));
  }

  public static final ScheduledTask runAtLocationLater(final Location loc, final Runnable task, final long delayTicks) {
    return runAtLocationLater(plugin, loc, task, delayTicks);
  }

  public static final ScheduledTask runAtLocationTimer(final Plugin plugin, final Location loc, final Runnable task, final long delayTicks, final long periodTicks) {
    return task(plugin, sch.runAtLocationTimer(loc, task, delayTicks, periodTicks));
  }

  public static final ScheduledTask runAtLocationTimer(final Location loc, final Runnable task, final long delayTicks, final long periodTicks) {
    return runAtLocationTimer(plugin, loc, task, delayTicks, periodTicks);
  }

  public static final CompletableFuture<?> runAtEntity(final Entity ent, final Runnable task) {
    return sch.runAtEntity(ent, t -> task.run());
  }

  public static final ScheduledTask runAtEntityLater(final Plugin plugin, final Entity ent, final Runnable task, final long delayTicks) {
    return task(plugin, sch.runAtEntityLater(ent, task, delayTicks));
  }

  public static final ScheduledTask runAtEntityLater(final Entity ent, final Runnable task, final long delayTicks) {
    return runAtEntityLater(plugin, ent, task, delayTicks);
  }

  public static final ScheduledTask runAtEntityTimer(final Plugin plugin, final Entity ent, final Runnable task, final long delayTicks, final long periodTicks) {
    return task(plugin, sch.runAtEntityTimer(ent, task, delayTicks, periodTicks));
  }

  public static final ScheduledTask runAtEntityTimer(final Entity ent, final Runnable task, final long delayTicks, final long periodTicks) {
    return runAtEntityTimer(plugin, ent, task, delayTicks, periodTicks);
  }

  public static final void teleportAsync(final Entity ent, final Location loc) {
    sch.teleportAsync(ent, loc);
  }

  private static ScheduledTask task(final Plugin plugin, final WrappedTask task) {
    final ScheduledTask scheduled = ScheduledTask.from(task);

    if (!tasks.containsKey(plugin)) {
      tasks.put(plugin, new ArrayList<>());
    }

    tasks.get(plugin).add(scheduled);

    return scheduled;
  }
}
