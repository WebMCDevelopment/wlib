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
