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

package xyz.webmc.wlib.internal.agent;

import xyz.webmc.wlib.api.WLIB;

import java.lang.instrument.ClassFileTransformer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

import dev.colbster937.reflect.MirrorSafe;

import static xyz.webmc.wlib.internal.agent.AgentMain.inst;

public final class AgentBridge {
  private static final List<Runnable> CALLBACKS = Collections.synchronizedList(new ArrayList<>());
  private static Function<String, Class<?>> classGetter;
  private static Class<?> pluginBridge;
  private static Logger logger;

  private static Class<?> wlib;
  private static String pckg;

  public static void _shutdown() {
    CALLBACKS.clear();

    classGetter = null;
    pluginBridge = null;
    logger = null;

    wlib = null;
    pckg = null;

    AgentMain._shutdown();
  }

  public static void _setClassGetter(Function<String, Class<?>> _classGetter) {
    classGetter = _classGetter;
    checkReady();
  }

  public static void _setPluginBridgeClass(Class<?> _pluginBridge) {
    pluginBridge = _pluginBridge;
    wlib = getLoaderClass(WLIB.class);
    pckg = wlib.getPackageName();
    checkReady();
  }

  public static void _setLogger(Logger _logger) {
    logger = _logger;
    checkReady();
  }

  public static Class<?>[] getLoadedClasses() {
    return inst.getAllLoadedClasses();
  }

  public static void addClassTransformer(ClassFileTransformer transformer) {
    inst.addTransformer(transformer, true);
    AgentMain._addClassTransformer(transformer);
  }

  public static void retransformClasses(Class<?>... classes) {
    retransformClasses(true, classes);
  }

  public static void retransformAllClasses(Class<?>... classes) {
    final Set<String> classNames = new HashSet<>();
    final Set<Class<?>> transform = new HashSet<>();

    for (Class<?> clazz : classes) {
      classNames.add(clazz.getName());
    }

    for (Class<?> clazz : inst.getAllLoadedClasses()) {
      if (clazz != null && inst.isModifiableClass(clazz) && classNames.contains(clazz.getName())) {
        transform.add(clazz);
      }
    }

    retransformClasses(transform.toArray(new Class<?>[0]));
  }

  static void retransformClasses(boolean add, Class<?>... classes) {
    if (classes.length > 0) {
      try {
        inst.retransformClasses(classes);
        if (add) {
          AgentMain._retransformClasses(classes);
        }
      } catch (Exception ex) {}
    }
  }

  static void onReady(Runnable callback) {
    synchronized (CALLBACKS) {
      CALLBACKS.add(callback);
    }

    checkReady();
  }

  static Class<?> getLoaderClass(String name) {
    if (classGetter != null) {
      return classGetter.apply(name);
    } else {
      return null;
    }
  }

  static Class<?> getLoaderClass(Class<?> clazz) {
    return getLoaderClass(clazz.getName());
  }

  static <T> T invokeWLIB(String method, Object... params) {
    return invoke(wlib, method, params);
  }

  static <T> T invokeUtil(String util, String method, Object... params) {
    return invoke(getLoaderClass(pckg + ".util." + util + "Util"), method, params);
  }

  static Class<?> getPluginBridgeClass() {
    return pluginBridge;
  }

  static void log(Level lvl, String str, Object... params) {
    if (logger != null) {
      logger.log(lvl, str, params);
    }
  }

  private static <T> T invoke(Class<?> clazz, String method, Object... params) {
    if (clazz != null) {
      return MirrorSafe.invokeMethod(clazz, method, params);
    } else {
      throw new IllegalStateException();
    }
  }

  private static void checkReady() {
    final boolean ready = !checkNull(classGetter, pluginBridge, logger, wlib, pckg);

    if (ready) {
      MirrorSafe.invokeMethod(pluginBridge, "_ready");
    }

    synchronized (CALLBACKS) {
      if (!CALLBACKS.isEmpty() && ready) {
        for (Runnable callback : CALLBACKS) {
          callback.run();
        }
      }
    }
  }

  private static boolean checkNull(Object... objs) {
    for (Object obj : objs) {
      if (obj == null) {
        return true;
      }
    }

    return false;
  }
}
