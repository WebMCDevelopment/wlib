package xyz.webmc.wlib;

import xyz.webmc.wlib.util.EventManager;
import xyz.webmc.wlib.util.Scheduler;

import org.bukkit.plugin.java.JavaPlugin;

public final class WLIBBukkitPlugin extends JavaPlugin {
  @Override
  public final void onEnable() {
    EventManager.init(this);
    Scheduler.init(this);
  }
}
