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

package xyz.webmc.wlib.api.plugin;

import xyz.webmc.wlib.api.WLIB;
import xyz.webmc.wlib.api.util.EventUtil;
import xyz.webmc.wlib.api.util.PluginUtil;
import xyz.webmc.wlib.api.util.SchedulerUtil;
import xyz.webmc.wlib.internal.WLIBBukkitPlugin;

import dev.colbster937.util.ExceptionStacker;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

@WPluginMeta
public abstract class WPlugin extends JavaPlugin {
  private WPluginMeta meta;

  @Override
  public final void onLoad() {
    try {
      meta = this.getWPluginMeta();
      this.load();
    } catch (Throwable t) {
      this.handleThrowable("load", t);
    }
  }

  @Override
  public final void onEnable() {
    try {
      String error = null;

      if (!this.getClass().equals(WLIBBukkitPlugin.class)) {
        final String req = this.meta.requiredWLIBVersion();
        if (req != null && !req.isBlank() && !WLIB.requireWLIBVersion(req)) {
          error = "WLIB version " + WLIB.getWLIBVersionString() + " is not supported, please use " + req + " or newer";
        }

        if (this.meta.requireModernServer() && !WLIB.getIsModernServer()) {
          error = "Legacy server " + Bukkit.getBukkitVersion() + " is not supported, please use a modern version";
        }
      }

      if (error == null || error.isBlank()) {
        this.enable();
        WLIB.initPlugin(this);
        if (this instanceof Listener listener) {
          EventUtil.registerEvents(listener, this);
        }
      } else {
        throw new IllegalStateException(error);
      }
    } catch (Throwable t) {
      this.handleThrowable("enable", t);
    }
  }

  @Override
  public final void onDisable() {
    try {
      this.disable();
      WLIB.shutdownPlugin(this);
      SchedulerUtil.cancelPluginTasks(this);
    } catch (Throwable t) {
      this.handleThrowable("disable", t);
    }
  }

  public final String getVersion() {
    return this.getDescription().getVersion();
  }

  public final boolean getOwnsClass(Class<?> clazz) {
    return PluginUtil.getProvidingPlugin(clazz).equals(this);
  }

  public final WPluginMeta getWPluginMeta() {
    return this.getClass().getAnnotation(WPluginMeta.class);
  }

  protected void load() throws Throwable {}
  protected void enable() throws Throwable {}
  protected void disable() throws Throwable {}

  private void handleThrowable(String stage, Throwable t) {
    final boolean shutdown = this.meta.shutdownOnFailure() && !stage.equals("disable");
    String message = "";

    if (shutdown) {
      message = ", shutting down server..";
    }

    this.getLogger().severe(
      "Failed to " + stage + " plugin" + message + "." +
      System.lineSeparator() + ExceptionStacker.getFullStackString(t)
    );

    if (shutdown) {
      PluginUtil.disablePlugin(this);
      this.getServer().shutdown();
    }
  }
}
