package xyz.webmc.wlib.api.http;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public final class HTTPResponse extends AbstractHTTPObject {
  private static final int DEFAULT_CODE = 200;

  private final int code;

  public HTTPResponse(final int code, final Map<String, String> headers, final byte[] body) {
    super(headers, body);
    this.code = code;
  }

  public HTTPResponse(final int code, final Map<String, String> headers, final String body) {
    this(
      code,
      headers.containsKey("Content-Type") ?
        headers :
        addTypeHeader(headers, "text/plain; charset=utf-8"),
      body.getBytes(StandardCharsets.UTF_8)
    );
  }

  public HTTPResponse(final int code, final byte[] body) {
    this(code, Map.of(), body);
  }

  public HTTPResponse(final int code, final String body) {
    this(code, Map.of(), body);
  }

  public HTTPResponse(final Map<String, String> headers, final byte[] body) {
    this(DEFAULT_CODE, headers, body);
  }

  public HTTPResponse(final Map<String, String> headers, final String body) {
    this(DEFAULT_CODE, headers, body);
  }

  public HTTPResponse(final byte[] body) {
    this(DEFAULT_CODE, body);
  }

  public HTTPResponse(final String body) {
    this(DEFAULT_CODE, body);
  }

  public final int getCode() {
    return this.code;
  }
  
  private static Map<String, String> addTypeHeader(final Map<String, String> map, final String type) {
    final Map<String, String> ret = new HashMap<>(map);
    ret.put("Content-Type", type);
    return ret;
  }
}
