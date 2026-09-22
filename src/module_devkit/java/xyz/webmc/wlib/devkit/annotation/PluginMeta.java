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

package xyz.webmc.wlib.devkit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface PluginMeta {
  String name();

  String version();

  String description() default "";

  String[] authors() default {};

  String[] contributors() default {};

  String website() default "";

  String apiVersion() default "1.13";

  String load() default "";

  String prefix() default "";

  String[] libraries() default {};

  String defaultPermission() default "";

  String[] depend() default {};

  String[] softdepend() default {};

  String[] loadbefore() default {};

  String[] provides() default {};

  boolean foliaSupported() default false;
}
