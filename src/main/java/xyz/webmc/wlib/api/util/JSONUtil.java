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

package xyz.webmc.wlib.api.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public final class JSONUtil {
  public static final String serialize(final Object obj) {
    validateObject(obj);
    return JSONObject.valueToString(obj);
  }

  public static final Object deserialize(final String json) {
    final Object ret = new JSONTokener(json).nextValue();
    validateObject(ret);
    return ret;
  }

  public static final boolean isJSON(final String json) {
    try {
      deserialize(json);
      return true;
    } catch (final Exception ex) {
      return false;
    }
  }

  private static void validateObject(final Object obj) throws IllegalArgumentException {
    if (!(obj instanceof JSONObject || obj instanceof JSONArray)) {
      throw new IllegalArgumentException();
    }
  }
}
