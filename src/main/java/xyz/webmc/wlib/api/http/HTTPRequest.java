package xyz.webmc.wlib.api.http;

import java.util.Map;

public final class HTTPRequest extends AbstractHTTPObject {
  private static final String DEFAULT_METHOD = "GET";
  
  private final String method;
  private final String path;
  private final String query;

  public HTTPRequest(final String method, final String path, final String query, final Map<String, String> headers, final byte[] body) {
    super(headers, body);
    this.method = method.toUpperCase();
    this.path = path;
    this.query = query;
  }

  public HTTPRequest(final String method, final String path, final String query, final Map<String, String> headers) {
    this(method, path, query, headers, new byte[0]);
  }

  public HTTPRequest(final String method, final String path, final Map<String, String> headers) {
    this(method, path, "", headers);
  }

  public HTTPRequest(final String method, final String path, final String query) {
    this(method, path, query, Map.of());
  }

  public HTTPRequest(final String method, final String path) {
    this(method, path, Map.of());
  }

  public HTTPRequest(final String path, final String query, final Map<String, String> headers, final byte[] body) {
    this(DEFAULT_METHOD, path, query, headers, body);
  }

  public HTTPRequest(final String path, final Map<String, String> headers) {
    this(DEFAULT_METHOD, path, headers);
  }

  public HTTPRequest(final String path) {
    this(DEFAULT_METHOD, path);
  }

  public final String getMethod() {
    return this.method;
  }

  public final String getPath() {
    return this.path;
  }

  public final String getQuery() {
    return this.query;
  }
}
