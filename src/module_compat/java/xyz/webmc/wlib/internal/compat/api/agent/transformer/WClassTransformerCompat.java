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

package xyz.webmc.wlib.internal.compat.api.agent.transformer;

import xyz.webmc.wlib.api.agent.transformer.WClassTransformer;
import xyz.webmc.wlib.internal.compat.WCompat;

import java.util.HashSet;
import java.util.Set;

@WCompat(WClassTransformer.class)
public abstract class WClassTransformerCompat {
  @Deprecated
  protected final Set<Class<?>> classes = new HashSet<>();

  @Deprecated
  public final Set<Class<?>> getTransformClasses() {
    return Set.copyOf(this.classes);
  }
}
