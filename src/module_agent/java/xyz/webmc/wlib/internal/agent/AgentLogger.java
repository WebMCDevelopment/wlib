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

import java.util.logging.Level;

public final class AgentLogger {
  static void info(String str, Object... params) {
    AgentBridge.log(Level.INFO, str, params);
  }

  static void warn(String str, Object... params) {
    AgentBridge.log(Level.WARNING, str, params);
  }

  static void severe(String str, Object... params) {
    AgentBridge.log(Level.SEVERE, str, params);
  }
}
