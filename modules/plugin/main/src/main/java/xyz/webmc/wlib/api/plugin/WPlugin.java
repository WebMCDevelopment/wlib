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
import xyz.webmc.wlib.api.util.AgentUtil;
import xyz.webmc.wlib.api.util.EventUtil;
import xyz.webmc.wlib.api.util.PluginUtil;
import xyz.webmc.wlib.api.util.SchedulerUtil;
import xyz.webmc.wlib.internal.WLIBBukkitPlugin;
import xyz.webmc.wlib.internal.iface.IWPlugin;
import xyz.webmc.wlib.internal.util.InternalUtil;

import java.io.File;

import dev.colbster937.reflect.Mirror;
import dev.colbster937.util.ExceptionStacker;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

@WPluginMeta
public abstract class WPlugin extends JavaPlugin implements IWPlugin {
  private final boolean modern = WLIB.getIsModernServer();
  private WPluginMeta meta;
  private Metrics metrics;

  @Override
  public final void onLoad() {
    try {
      this.meta = this.getWPluginMeta();

      if (getMethodOverwritten("agentReady")) {
        AgentUtil.onReady(() -> {
          try {
            this.agentReady();
          } catch (Throwable t) {
            this.handleThrowable("agentReady", t);
          }
        });
      }

      this.execStage("load");

      if (modern) {
        this.execStage("loadModern");
      }
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
        this.execStage("enable");

        if (modern) {
          this.execStage("enableModern");
        }

        WLIB.initPlugin(this);
        if (this instanceof Listener listener) {
          EventUtil.registerEvents(listener, this);
        }

        if (getMethodOverwritten("serverStartup")) {
          WLIB.onServerStartup(this::onServerStartup);
        }

        final int bStats = this.meta.bStats();
        if (bStats > 0) {
          this.metrics = new Metrics(this, bStats);
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
      this.execStage("disable");

      if (modern) {
        this.execStage("disableModern");
      }

      if (this.metrics != null) {
        this.metrics.shutdown();
      }

      WLIB.shutdownPlugin(this);
      SchedulerUtil.cancelPluginTasks(this);
    } catch (Throwable t) {
      this.handleThrowable("disable", t, false);
    }
  }

  @Override
  public final File getFile() {
    return super.getFile();
  }

  @Override
  public final WPluginMeta getWPluginMeta() {
    return this.getClass().getAnnotation(WPluginMeta.class);
  }

  @Override
  public final boolean getOwnsClass(Class<?> clazz) {
    return PluginUtil.getOwnsClass(this, clazz);
  }

  public final String getVersion() {
    return this.getDescription().getVersion();
  }

  public final Metrics getMetrics() {
    return this.metrics;
  }

  protected void load() throws Throwable {}
  protected void loadModern() throws Throwable {}
  protected void enable() throws Throwable {}
  protected void enableModern() throws Throwable {}
  protected void disable() throws Throwable {}
  protected void disableModern() throws Throwable {}
  protected void agentReady() throws Throwable {}
  protected void serverStartup() throws Throwable {}

  private void onServerStartup() {
    try {
      this.serverStartup();
    } catch (Throwable t) {
      this.handleThrowable("serverStartup", t);
    }
  }

  private void handleThrowable(String stage, Throwable t, boolean shutdown) {
    shutdown = shutdown && this.meta.shutdownOnFailure();
    String message = "";

    if (shutdown) {
      message = ", shutting down server..";
    }

    this.getLogger().severe(
      "Failed to execute '" + stage + "' stage of plugin " + this.getName() + message + "." +
      System.lineSeparator() + ExceptionStacker.getFullStackString(t)
    );

    if (shutdown) {
      PluginUtil.disablePlugin(this);
      this.getServer().shutdown();
    }
  }

  private void handleThrowable(String stage, Throwable t) {
    this.handleThrowable(stage, t, true);
  }

  private void execStage(String name) throws Exception {
    if (getMethodOverwritten(name)) {
      Mirror.invokeMethod(this, name);
    }
  }

  private static boolean getMethodOverwritten(String name) {
    return InternalUtil.getClassOwnsMethod(WPlugin.class, name);
  }
}
