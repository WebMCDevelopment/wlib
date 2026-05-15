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
