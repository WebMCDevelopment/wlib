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

import com.sun.tools.attach.VirtualMachine;

public final class AgentBootstrap {
  public static void main(String[] args) {
    int code = 0;
    try {
      final VirtualMachine vm = VirtualMachine.attach(args[0]);
      try {
        vm.loadAgent(args[1]);
      } finally {
        vm.detach();
      }
    } catch (Exception ex) {
      code = 1;
    } finally {
      System.exit(code);
    }
  }
}
