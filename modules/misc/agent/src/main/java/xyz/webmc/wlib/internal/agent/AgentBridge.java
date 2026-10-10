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

import java.lang.instrument.ClassFileTransformer;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

import static xyz.webmc.wlib.internal.agent.AgentMain.inst;

@SuppressWarnings({ "unchecked" })
public final class AgentBridge {
  private static final List<Runnable> CALLBACKS = Collections.synchronizedList(new ArrayList<>());
  private static Function<String, Class<?>> classGetter;
  private static Runnable readyCallback;
  private static Class<?> pluginBridge;
  private static Logger logger;

  private static Class<?> wlib;
  private static String pckg;

  public static void shutdown() {
    CALLBACKS.clear();

    classGetter = null;
    readyCallback = null;
    pluginBridge = null;
    logger = null;

    wlib = null;
    pckg = null;

    AgentMain.shutdown();
  }

  public static void setClassGetter(Function<String, Class<?>> _classGetter) {
    classGetter = _classGetter;
    checkReady();
  }

  public static void setReadyCallback(Runnable _readyCallback) {
    readyCallback = _readyCallback;
    checkReady();
  }

  public static void setPluginBridgeClass(Class<?> _pluginBridge) {
    pluginBridge = _pluginBridge;
    checkReady();
  }

  public static void setWLIBClass(Class<?> _wlib) {
    wlib = _wlib;
    pckg = wlib.getPackageName();
    checkReady();
  }

  public static void setLogger(Logger _logger) {
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

    retransformClasses(transform.toArray(Class<?>[]::new));
  }

  public static void retransformAllClasses(String... classes) {
    final Set<PathMatcher> matchers = new HashSet<>();
    final Set<Class<?>> transform = new HashSet<>();

    for (String clazz : classes) {
      matchers.add(getPluginBridge("ClassMatcher", clazz));
    }

    for (Class<?> clazz : inst.getAllLoadedClasses()) {
      if (clazz != null && inst.isModifiableClass(clazz)) {
        final Path path = getPluginBridge("PackageFSPath", clazz.getName());
        for (PathMatcher matcher : matchers) {
          if (matcher.matches(path)) {
            transform.add(clazz);
            break;
          }
        }
      }
    }

    retransformClasses(transform.toArray(Class<?>[]::new));
  }

  public static void retransformAllClasses() {
    retransformAllClasses("**");
  }

  static void retransformClasses(boolean add, Class<?>... classes) {
    if (classes.length > 0) {
      try {
        inst.retransformClasses(classes);
        if (add) {
          AgentMain._addRetransformedClasses(classes);
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

  static void log(Level lvl, String str, Object... params) {
    if (logger != null) {
      logger.log(lvl, str, params);
    }
  }

  private static <T> T getPluginBridge(String name, Object param) {
    if (pluginBridge != null) {
      try {
        final Method method = pluginBridge.getMethod("_get" + name, param.getClass());
        method.setAccessible(true);
        return (T) method.invoke(null, param);
      } catch (ReflectiveOperationException ex) {
        AgentLogger.severe(ex.getLocalizedMessage());
        return null;
      }
    } else {
      throw new IllegalStateException();
    }
  }

  private static void checkReady() {
    final boolean ready = !checkNull(classGetter, pluginBridge, logger, wlib, pckg);

    if (ready) {
      readyCallback.run();
    }

    synchronized (CALLBACKS) {
      if (!CALLBACKS.isEmpty() && ready) {
        CALLBACKS.forEach(Runnable::run);
        CALLBACKS.clear();
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
