package xyz.webmc.wlib.util;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.impl.PlatformScheduler;
import com.tcoded.folialib.wrapper.task.WrappedTask;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

public final class Scheduler {

  private static FoliaLib lib;
  private static PlatformScheduler sch;

  private Scheduler() {
  }

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

  public static final void runNextTick(final Consumer<WrappedTask> task) {
    sch.runNextTick(task);
  }

  public static final void runLater(final Consumer<WrappedTask> task, final long delayTicks) {
    sch.runLater(task, delayTicks);
  }

  public static final void runTimer(final Consumer<WrappedTask> task, final long delayTicks, final long periodTicks) {
    sch.runTimer(task, delayTicks, periodTicks);
  }

  public static final void runAsync(final Consumer<WrappedTask> task) {
    sch.runAsync(task);
  }

  public static final void runLaterAsync(final Consumer<WrappedTask> task, final long delayTicks) {
    sch.runLaterAsync(task, delayTicks);
  }

  public static final void runLaterAsync(final Consumer<WrappedTask> task, final long delay, final TimeUnit unit) {
    sch.runLaterAsync(task, delay, unit);
  }

  public static final void runTimerAsync(final Consumer<WrappedTask> task, final long delayTicks, final long periodTicks) {
    sch.runTimerAsync(task, delayTicks, periodTicks);
  }

  public static final void runTimerAsync(final Consumer<WrappedTask> task, final long delay, final long period, final TimeUnit unit) {
    sch.runTimerAsync(task, delay, period, unit);
  }

  public static final void runAtLocation(final Location loc, final Consumer<WrappedTask> task) {
    sch.runAtLocation(loc, task);
  }

  public static final void runAtLocationLater(final Location loc, final Consumer<WrappedTask> task, final long delayTicks) {
    sch.runAtLocationLater(loc, task, delayTicks);
  }

  public static final void runAtLocationTimer(final Location loc, final Consumer<WrappedTask> task, final long delayTicks, final long periodTicks) {
    sch.runAtLocationTimer(loc, task, delayTicks, periodTicks);
  }

  public static final void runAtEntity(final Entity ent, final Consumer<WrappedTask> task) {
    sch.runAtEntity(ent, task);
  }

  public static final void runAtEntityLater(final Entity ent, final Consumer<WrappedTask> task, final long delayTicks) {
    sch.runAtEntityLater(ent, task, delayTicks);
  }

  public static final void runAtEntityTimer(final Entity ent, final Consumer<WrappedTask> task, final long delayTicks, final long periodTicks) {
    sch.runAtEntityTimer(ent, task, delayTicks, periodTicks);
  }
}