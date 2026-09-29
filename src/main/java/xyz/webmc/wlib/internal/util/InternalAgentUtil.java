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

package xyz.webmc.wlib.internal.util;

import xyz.webmc.wlib.api.agent.transformer.WClassTransformer;
import xyz.webmc.wlib.api.plugin.WPlugin;
import xyz.webmc.wlib.api.util.LoggerUtil;
import xyz.webmc.wlib.devkit.annotation.InternalClass;
import xyz.webmc.wlib.internal.agent.AgentBootstrap;
import xyz.webmc.wlib.internal.agent.AgentBridge;

import java.lang.instrument.ClassFileTransformer;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.function.Function;

import dev.colbster937.reflect.MirrorSafe;
import dev.colbster937.util.ExceptionStacker;

import static xyz.webmc.wlib.internal.util.InternalUtil.checkInternalCaller;

@InternalClass
public final class InternalAgentUtil {
  private static final List<Runnable> CALLBACKS = Collections.synchronizedList(new ArrayList<>());
  private static WPlugin plugin;
  private static Class<?> bridge;
  private static boolean ready;

  public static void _init(WPlugin _plugin) {
    checkInternalCaller();
    plugin = _plugin;
    try {
      bridge = getBridgeClass();
      ready = false;
      if (bridge == null) {
        final ProcessHandle handle = ProcessHandle.current();
        final String java = handle.info().command().orElseThrow();
        final String jar = plugin.getFile().getAbsolutePath().toString();
        final Process proc = new ProcessBuilder(
          java, "-cp", jar, AgentBootstrap.class.getName(), Long.toString(handle.pid()), jar
        ).inheritIO().start();
        proc.onExit().thenAccept(exit -> {
          try {
            final int code = exit.exitValue();
            if (code == 0) {
              bridge = getBridgeClass();
              initBridge();
            } else {
              new IllegalStateException(Integer.toString(code));
            }
          } catch (Exception ex) {
            throw new CompletionException(ex);
          }
        });
      } else {
        initBridge();
      }
    } catch (Exception ex) {
      LoggerUtil.error(ExceptionStacker.getFullStackString(ex));
    }
  }

  public static void _shutdown() {
    checkInternalCaller();
    invokeBridge("_shutdown");
  }

  public static void onReady(Runnable callback) {
    checkInternalCaller();
    if (!ready) {
      synchronized (CALLBACKS) {
        CALLBACKS.add(callback);
      }
    } else {
      callback.run();
    }
  }

  public static boolean getIsReady() {
    checkInternalCaller();
    return ready;
  }

  public static Class<?>[] getLoadedClasses() {
    checkInternalCaller();
    return invokeBridge("getLoadedClasses");
  }

  public static void addClassTransformer(ClassFileTransformer transformer) {
    checkInternalCaller();
    invokeBridge("addClassTransformer", transformer);
    if (transformer instanceof WClassTransformer wtransformer) {
      onReady(wtransformer::_ready);
    }
  }

  public static void retransformClasses(Class<?>... classes) {
    checkInternalCaller();
    invokeBridge("retransformClasses", (Object) classes);
  }

  public static void retransformAllClasses(Class<?>... classes) {
    checkInternalCaller();
    invokeBridge("retransformAllClasses", (Object) classes);
  }

  public static void retransformAllClasses(String... classes) {
    checkInternalCaller();
    invokeBridge("retransformAllClasses", (Object) classes);
  }

  public static void retransformAllClasses() {
    checkInternalCaller();
    invokeBridge("retransformAllClasses");
  }

  public static String getPackageFS(String pckg) {
    checkInternalCaller();
    return pckg.replaceAll("\\.", "/").trim();
  }

  public static Path getPackageFSPath(String pckg) {
    checkInternalCaller();
    return Path.of(getPackageFS(pckg));
  }

  public static PathMatcher getClassMatcher(String glob) {
    checkInternalCaller();
    return FileSystems.getDefault().getPathMatcher("glob:" + getPackageFS(glob));
  }

  private static void initBridge() throws Exception {
    setBridge("ClassGetter", (Function<String, Class<?>>) InternalAgentUtil::getLoaderClass);
    setBridge("ReadyCallback", (Runnable) InternalAgentUtil::ready);
    setBridge("PluginBridgeClass", InternalAgentUtil.class);
    setBridge("Logger", plugin.getLogger());
  }

  private static void setBridge(String name, Object obj) {
    invokeBridge("_set" + name, obj);
  }

  private static <T> T invokeBridge(String method, Object... params) {
    return MirrorSafe.invokeMethod(bridge, method, params);
  }

  private static void ready() {
    ready = true;
    synchronized (CALLBACKS) {
      for (Runnable callback : CALLBACKS) {
        callback.run();
      }
    }
  }

  private static Class<?> getLoaderClass(String name) {
    try {
      return Class.forName(name, false, plugin.getClass().getClassLoader());
    } catch (ClassNotFoundException ex) {
      return null;
    }
  }

  private static Class<?> getBridgeClass() {
    Class<?> ret = bridge;

    if (ret == null) {
      try {
        ret = Class.forName(AgentBridge.class.getName(), true, ClassLoader.getSystemClassLoader());
      } catch (Exception ex) {}
    }

    return ret;
  }
}
