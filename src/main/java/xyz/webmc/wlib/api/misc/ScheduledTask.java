package xyz.webmc.wlib.api.misc;

import com.tcoded.folialib.wrapper.task.WrappedTask;

public final class ScheduledTask {
  private final WrappedTask task;

  private ScheduledTask(final WrappedTask task) {
    this.task = task;
  }

  public final void cancel() {
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

  public static final ScheduledTask from(final WrappedTask task) {
    return new ScheduledTask(task);
  }
}
