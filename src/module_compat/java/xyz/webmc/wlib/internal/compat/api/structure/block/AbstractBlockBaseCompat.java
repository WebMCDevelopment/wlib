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

package xyz.webmc.wlib.internal.compat.api.structure.block;

import xyz.webmc.wlib.api.WLIB;
import xyz.webmc.wlib.api.structure.block.AbstractBlockBase;
import xyz.webmc.wlib.internal.compat.WCompat;

import com.cryptomorin.xseries.XMaterial;

@WCompat(AbstractBlockBase.class)
public abstract class AbstractBlockBaseCompat {
  protected abstract XMaterial getXMaterial();

  @Deprecated(forRemoval = true)
  public final XMaterial getMaterial() {
    WLIB.warnDeprecatedUsage();
    return this.getXMaterial();
  }
}
