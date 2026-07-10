/*
 * Copyright (C) 2026 ${plugin.athr}
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

package xyz.webmc.wlib.api.http;

import java.util.Map;

public abstract class AbstractHTTPObject {
  private final Map<String, String> headers;
  private final byte[] body;

  protected AbstractHTTPObject(final Map<String, String> headers, final byte[] body) {
    this.headers = headers;
    this.body = body;
  }

  public final Map<String, String> getHeaders() {
    return this.headers;
  }

  public final String getHeader(final String key) {
    return this.getHeaders().get(key);
  }

  public final byte[] getBody() {
    return this.body;
  }

  public final int getBodySize() {
    return this.getBody().length;
  }
}
