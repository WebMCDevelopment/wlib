package xyz.webmc.wlib;

import xyz.webmc.wlib.util.CommandUtil;
import xyz.webmc.wlib.util.EventUtil;
import xyz.webmc.wlib.util.SchedulerUtil;

import org.bukkit.plugin.java.JavaPlugin;

public final class WLIBBukkitPlugin extends JavaPlugin {
  @Override
  public final void onEnable() {
    CommandUtil.init(this);
    EventUtil.init(this);
    SchedulerUtil.init(this);
  }

  @Override
  public final void onDisable() {
    SchedulerUtil.cancelAllTasks();
  }
}
