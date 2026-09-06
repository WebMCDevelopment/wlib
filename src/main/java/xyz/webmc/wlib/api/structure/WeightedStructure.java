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

package xyz.webmc.wlib.api.structure;

import xyz.webmc.wlib.api.WLIB;

import dev.colbster937.util.WeightedObject;

@SuppressWarnings({ "removal" })
@Deprecated(forRemoval = true)
public final class WeightedStructure extends WeightedObject<AbstractBaseStructure> {
  @Deprecated(forRemoval = true)
  public WeightedStructure(final AbstractBaseStructure structure, final int weight) {
    super(structure, weight);
    WLIB.warnDeprecatedUsage();
  }
}
