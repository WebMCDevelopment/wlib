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
import java.lang.instrument.Instrumentation;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class AgentMain {
  private static final Set<ClassFileTransformer> TRANSFORMERS = Collections.synchronizedSet(new HashSet<>());
  private static final Set<Class<?>> TRANSFORMED = Collections.synchronizedSet(new HashSet<>());
  static Instrumentation inst;

  public static void premain(String args, Instrumentation _inst) throws Exception {
    main(args, _inst);
  }

  public static void agentmain(String args, Instrumentation _inst) throws Exception {
    main(args, _inst);
  }

  static void _shutdown() {
    synchronized (TRANSFORMERS) {
      for (ClassFileTransformer transformer : TRANSFORMERS) {
        inst.removeTransformer(transformer);
      }

      TRANSFORMERS.clear();
    }

    synchronized (TRANSFORMED) {
      AgentBridge.retransformClasses(false, TRANSFORMED.toArray(new Class<?>[0]));
      TRANSFORMED.clear();
    }
  }

  static void _addClassTransformer(ClassFileTransformer transformer) {
    synchronized (TRANSFORMERS) {
      TRANSFORMERS.add(transformer);
    }
  }

  static void _retransformClasses(Class<?>[] classes) {
    synchronized (TRANSFORMED) {
      TRANSFORMED.addAll(Set.of(classes));
    }
  }

  private static void main(String args, Instrumentation _inst) throws Exception {
    inst = _inst;
    AgentBridge.onReady(AgentMain::ready);
  }

  private static void ready() {
    AgentBridge.retransformClasses(AgentBridge.getLoaderClass(WLIB.class));
  }
}
