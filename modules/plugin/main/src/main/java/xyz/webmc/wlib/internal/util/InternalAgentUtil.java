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

import xyz.webmc.wlib.api.WLIB;
import xyz.webmc.wlib.api.agent.transformer.WClassTransformer;
import xyz.webmc.wlib.api.util.LoggerUtil;

import java.lang.instrument.ClassFileTransformer;
import java.lang.management.ManagementFactory;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.function.Function;

import com.sun.management.HotSpotDiagnosticMXBean;
import dev.colbster937.reflect.MirrorSafe;
import dev.colbster937.util.ExceptionStacker;
import org.bukkit.plugin.Plugin;

import static xyz.webmc.wlib.internal.util.InternalUtil.checkInternalCaller;

public final class InternalAgentUtil {
  private static final List<Runnable> CALLBACKS = Collections.synchronizedList(new ArrayList<>());
  private static Plugin plugin;
  private static Class<?> bridge;
  private static boolean ready;

  public static void init(Plugin _plugin) {
    checkInternalCaller();
    plugin = _plugin;
    try {
      bridge = getBridgeClass();
      ready = false;
      if (bridge == null) {
        final Path path = Files.createTempFile(plugin.getName() + "-agent-", ".jar");
        path.toFile().deleteOnExit();
        Files.copy(plugin.getResource("resources/agent.jar"), path, StandardCopyOption.REPLACE_EXISTING);

        final String jar = path.toAbsolutePath().toString();
        final ProcessHandle handle = ProcessHandle.current();
        final String java = handle.info().command().orElseThrow();
        final Process proc = new ProcessBuilder(
          java, "-cp", jar, BuildPropUtil.getProperty("agent.boot"), Long.toString(handle.pid()), jar
        ).inheritIO().start();

        proc.onExit().thenAccept(exit -> {
          try {
            final int code = exit.exitValue();
            if (code == 0) {
              bridge = getBridgeClass();
              initBridge();
            } else {
              throw new IllegalStateException(Integer.toString(code));
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

  public static void shutdown() {
    checkInternalCaller();
    invokeBridge("shutdown");
  }

  public static boolean getIsDynamicAttachmentSupported() {
    if (Runtime.version().feature() >= 21) {
      return Boolean.parseBoolean(
        ManagementFactory.getPlatformMXBean(HotSpotDiagnosticMXBean.class)
        .getVMOption("EnableDynamicAgentLoading").getValue()
      );
    } else {
      return true;
    }
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

  public static Path _getPackageFSPath(String pckg) {
    checkInternalCaller();
    return Path.of(getPackageFS(pckg));
  }

  public static PathMatcher _getClassMatcher(String glob) {
    checkInternalCaller();
    return FileSystems.getDefault().getPathMatcher("glob:" + getPackageFS(glob));
  }

  private static void initBridge() throws Exception {
    setBridge("ClassGetter", (Function<String, Class<?>>) InternalAgentUtil::getLoaderClass);
    setBridge("ReadyCallback", (Runnable) InternalAgentUtil::ready);
    setBridge("PluginBridgeClass", InternalAgentUtil.class);
    setBridge("WLIBClass", WLIB.class);
    setBridge("Logger", plugin.getLogger());
  }

  private static void setBridge(String name, Object obj) {
    invokeBridge("set" + name, obj);
  }

  private static <T> T invokeBridge(String method, Object... params) {
    if (bridge != null) {
      return MirrorSafe.invokeMethod(bridge, method, params);
    } else {
      return null;
    }
  }

  private static void ready() {
    ready = true;
    synchronized (CALLBACKS) {
      CALLBACKS.forEach(Runnable::run);
      CALLBACKS.clear();
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
        ret = Class.forName(BuildPropUtil.getProperty("agent.bdge"), true, ClassLoader.getSystemClassLoader());
      } catch (Exception ex) {}
    }

    return ret;
  }
}
