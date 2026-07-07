package xyz.webmc.wlib.api.interfaces;

public interface Deserializable<T> {
  T deserialize(String data);
}
