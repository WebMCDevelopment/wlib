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
import com.tcoded.folialib.enums.EntityTaskResult;
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

  public static void init(final Plugin plugin) {
    SchedulerUtil.plugin = plugin;
    lib = new FoliaLib(plugin);
    sch = lib.getScheduler();
  }

  public static boolean isFolia() {
    return lib.isFolia();
  }

  public static void cancelPluginTasks(final Plugin plugin) {
    if (tasks.containsKey(plugin)) {
      for (final ScheduledTask task : tasks.get(plugin)) {
        task.cancel();
      }

      tasks.remove(plugin);
    }
  }

  public static void cancelAllTasks() {
    sch.cancelAllTasks();
  }

  public static CompletableFuture<Void> runNextTick(final Runnable task) {
    return sch.runNextTick(t -> task.run());
  }

  public static ScheduledTask runLater(final Plugin plugin, final Runnable task, final long delayTicks) {
    return task(plugin, sch.runLater(task, delayTicks));
  }

  public static ScheduledTask runLater(final Runnable task, final long delayTicks) {
    return runLater(plugin, task, delayTicks);
  }

  public static ScheduledTask runTimer(final Plugin plugin, final Runnable task, final long delayTicks,
      final long periodTicks) {
    return task(plugin, sch.runTimer(task, delayTicks, periodTicks));
  }

  public static ScheduledTask runTimer(final Runnable task, final long delayTicks, final long periodTicks) {
    return runTimer(plugin, task, delayTicks, periodTicks);
  }

  public static CompletableFuture<Void> runAsync(final Runnable task) {
    return sch.runAsync(t -> task.run());
  }

  public static ScheduledTask runLaterAsync(final Plugin plugin, final Runnable task, final long delayTicks) {
    return task(plugin, sch.runLaterAsync(task, delayTicks));
  }

  public static ScheduledTask runLaterAsync(final Runnable task, final long delayTicks) {
    return runLaterAsync(plugin, task, delayTicks);
  }

  public static ScheduledTask runLaterAsync(final Plugin plugin, final Runnable task, final long delay,
      final TimeUnit unit) {
    return task(plugin, sch.runLaterAsync(task, delay, unit));
  }

  public static ScheduledTask runLaterAsync(final Runnable task, final long delay, final TimeUnit unit) {
    return runLaterAsync(plugin, task, delay, unit);
  }

  public static ScheduledTask runTimerAsync(final Plugin plugin, final Runnable task, final long delayTicks,
      final long periodTicks) {
    return task(plugin, sch.runTimerAsync(task, delayTicks, periodTicks));
  }

  public static ScheduledTask runTimerAsync(final Runnable task, final long delayTicks, final long periodTicks) {
    return runTimerAsync(plugin, task, delayTicks, periodTicks);
  }

  public static ScheduledTask runTimerAsync(final Plugin plugin, final Runnable task, final long delay,
      final long period, final TimeUnit unit) {
    return task(plugin, sch.runTimerAsync(task, delay, period, unit));
  }

  public static ScheduledTask runTimerAsync(final Runnable task, final long delay, final long period,
      final TimeUnit unit) {
    return runTimerAsync(plugin, task, delay, period, unit);
  }

  public static CompletableFuture<Void> runAtLocation(final Location loc, final Runnable task) {
    return sch.runAtLocation(loc, t -> task.run());
  }

  public static ScheduledTask runAtLocationLater(final Plugin plugin, final Location loc, final Runnable task,
      final long delayTicks) {
    return task(plugin, sch.runAtLocationLater(loc, task, delayTicks));
  }

  public static ScheduledTask runAtLocationLater(final Location loc, final Runnable task, final long delayTicks) {
    return runAtLocationLater(plugin, loc, task, delayTicks);
  }

  public static CompletableFuture<Void> runAtLocationNextTick(final Location loc, final Runnable task) {
    final CompletableFuture<Void> future = new CompletableFuture<>();

    runAtLocationLater(loc, () -> {
      try {
        task.run();
        future.complete(null);
      } catch (final Throwable t) {
        future.completeExceptionally(t);
      }
    }, 1L);

    return future;
  }

  public static ScheduledTask runAtLocationTimer(final Plugin plugin, final Location loc, final Runnable task,
      final long delayTicks, final long periodTicks) {
    return task(plugin, sch.runAtLocationTimer(loc, task, delayTicks, periodTicks));
  }

  public static ScheduledTask runAtLocationTimer(final Location loc, final Runnable task, final long delayTicks,
      final long periodTicks) {
    return runAtLocationTimer(plugin, loc, task, delayTicks, periodTicks);
  }

  public static CompletableFuture<EntityTaskResult> runAtEntity(final Entity ent, final Runnable task) {
    return sch.runAtEntity(ent, t -> task.run());
  }

  public static ScheduledTask runAtEntityLater(final Plugin plugin, final Entity ent, final Runnable task,
      final long delayTicks) {
    return task(plugin, sch.runAtEntityLater(ent, task, delayTicks));
  }

  public static ScheduledTask runAtEntityLater(final Entity ent, final Runnable task, final long delayTicks) {
    return runAtEntityLater(plugin, ent, task, delayTicks);
  }

  public static CompletableFuture<Void> runAtEntityNextTick(final Entity ent, final Runnable task) {
    final CompletableFuture<Void> future = new CompletableFuture<>();

    runAtEntityLater(ent, () -> {
      try {
        task.run();
        future.complete(null);
      } catch (final Throwable t) {
        future.completeExceptionally(t);
      }
    }, 1L);

    return future;
  }

  public static ScheduledTask runAtEntityTimer(final Plugin plugin, final Entity ent, final Runnable task,
      final long delayTicks, final long periodTicks) {
    return task(plugin, sch.runAtEntityTimer(ent, task, delayTicks, periodTicks));
  }

  public static ScheduledTask runAtEntityTimer(final Entity ent, final Runnable task, final long delayTicks,
      final long periodTicks) {
    return runAtEntityTimer(plugin, ent, task, delayTicks, periodTicks);
  }

  public static void teleportAsync(final Entity ent, final Location loc) {
    sch.teleportAsync(ent, loc);
  }

  public static void teleportAsync(final Entity ent, final Entity tent) {
    teleportAsync(ent, tent.getLocation());
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
