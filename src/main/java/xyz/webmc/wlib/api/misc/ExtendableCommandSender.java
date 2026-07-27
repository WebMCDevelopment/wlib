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

package xyz.webmc.wlib.api.misc;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

public abstract class ExtendableCommandSender implements CommandSender {
  private final PermissibleBase perm;

  protected ExtendableCommandSender(final CommandSender parent) {
    if (parent != null) {
      this.perm = new PermissibleBase(parent);
    } else {
      this.perm = new PermissibleBase(this);
    }
  }

  protected ExtendableCommandSender() {
    this(null);
  }

  @Override
  public final boolean isPermissionSet(final String name) {
    return this.perm.isPermissionSet(name);
  }

  @Override
  public final boolean isPermissionSet(final Permission perm) {
    return this.perm.isPermissionSet(perm);
  }

  @Override
  public final boolean hasPermission(final String name) {
    return this.perm.hasPermission(name);
  }

  @Override
  public final boolean hasPermission(final Permission perm) {
    return this.perm.hasPermission(perm);
  }

  @Override
  public final PermissionAttachment addAttachment(final Plugin plugin, final String name, final boolean value) {
    return this.perm.addAttachment(plugin, name, value);
  }

  @Override
  public final PermissionAttachment addAttachment(final Plugin plugin) {
    return this.perm.addAttachment(plugin);
  }

  @Override
  public final PermissionAttachment addAttachment(final Plugin plugin, final String name, final boolean value,
      final int ticks) {
    return this.perm.addAttachment(plugin, name, value, ticks);
  }

  @Override
  public final PermissionAttachment addAttachment(final Plugin plugin, final int ticks) {
    return this.perm.addAttachment(plugin, ticks);
  }

  @Override
  public void removeAttachment(final PermissionAttachment attachment) {
    this.perm.removeAttachment(attachment);
  }

  @Override
  public void recalculatePermissions() {
    this.perm.recalculatePermissions();
  }

  @Override
  public final Set<PermissionAttachmentInfo> getEffectivePermissions() {
    return this.perm.getEffectivePermissions();
  }

  @Override
  public final boolean isOp() {
    return this.perm.isOp();
  }

  @Override
  public void setOp(final boolean value) {
    this.perm.setOp(value);
  }

  @Override
  public final Server getServer() {
    return Bukkit.getServer();
  }

  @Override
  public final String getName() {
    return this.getClass().getSimpleName();
  }
}
