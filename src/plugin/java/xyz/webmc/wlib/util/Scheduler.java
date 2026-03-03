package xyz.webmc.wlib.util;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.impl.PlatformScheduler;
import com.tcoded.folialib.wrapper.task.WrappedTask;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

public final class Scheduler {
  private static FoliaLib lib;
  private static PlatformScheduler sch;

  public static final void init(final Plugin plugin) {
    lib = new FoliaLib(plugin);
    sch = lib.getScheduler();
  }

  public static final boolean isFolia() {
    return lib.isFolia();
  }

  public static final boolean isPaper() {
    return lib.isPaper();
  }

  public static final void cancelAllTasks() {
    sch.cancelAllTasks();
  }

  public static final CompletableFuture<Void> runNextTick(final Runnable task) {
    return sch.runNextTick(t -> task.run());
  }

  public static final WrappedTask runLater(final Runnable task, final long delayTicks) {
    return sch.runLater(task, delayTicks);
  }

  public static final WrappedTask runTimer(final Runnable task, final long delayTicks, final long periodTicks) {
    return sch.runTimer(task, delayTicks, periodTicks);
  }

  public static final CompletableFuture<Void> runAsync(final Runnable task) {
    return sch.runAsync(t -> task.run());
  }

  public static final WrappedTask runLaterAsync(final Runnable task, final long delayTicks) {
    return sch.runLaterAsync(task, delayTicks);
  }

  public static final WrappedTask runLaterAsync(final Runnable task, final long delay, final TimeUnit unit) {
    return sch.runLaterAsync(task, delay, unit);
  }

  public static final WrappedTask runTimerAsync(final Runnable task, final long delayTicks, final long periodTicks) {
    return sch.runTimerAsync(task, delayTicks, periodTicks);
  }

  public static final WrappedTask runTimerAsync(final Runnable task, final long delay, final long period, final TimeUnit unit) {
    return sch.runTimerAsync(task, delay, period, unit);
  }

  public static final CompletableFuture<Void> runAtLocation(final Location loc, final Runnable task) {
    return sch.runAtLocation(loc, t -> task.run());
  }

  public static final WrappedTask runAtLocationLater(final Location loc, final Runnable task, final long delayTicks) {
    return sch.runAtLocationLater(loc, task, delayTicks);
  }

  public static final WrappedTask runAtLocationTimer(final Location loc, final Runnable task, final long delayTicks, final long periodTicks) {
    return sch.runAtLocationTimer(loc, task, delayTicks, periodTicks);
  }

  public static final CompletableFuture<?> runAtEntity(final Entity ent, final Runnable task) {
    return sch.runAtEntity(ent, t -> task.run());
  }

  public static final WrappedTask runAtEntityLater(final Entity ent, final Runnable task, final long delayTicks) {
    return sch.runAtEntityLater(ent, task, delayTicks);
  }

  public static final WrappedTask runAtEntityTimer(final Entity ent, final Runnable task, final long delayTicks, final long periodTicks) {
    return sch.runAtEntityTimer(ent, task, delayTicks, periodTicks);
  }
}