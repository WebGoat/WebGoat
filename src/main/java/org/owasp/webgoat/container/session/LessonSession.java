package org.owasp.webgoat.container.session;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for managing user session data within a lesson. It uses a HashMap to
 * store key-value pairs representing session data.
 */
public class LessonSession {

  private Map<String, Object> userSessionData = new HashMap<>();

  /** Default constructor initializing an empty session. */
  public LessonSession() {}

  /**
   * Retrieves the value associated with the given key.
   *
   * @param key the key for the session data
   * @return the value associated with the key, or null if the key does not exist
   */
  public Object getValue(String key) {
    if (!userSessionData.containsKey(key)) {
      return null;
    }
    // else
    return userSessionData.get(key);
  }

  /**
   * Sets the value for the given key. If the key already exists, its value is updated.
   *
   * @param key the key for the session data
   * @param value the value to be associated with the key
   */
  public void setValue(String key, Object value) {
    if (userSessionData.containsKey(key)) {
      userSessionData.replace(key, value);
    } else {
      userSessionData.put(key, value);
    }
  }
}
