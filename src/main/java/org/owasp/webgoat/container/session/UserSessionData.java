package org.owasp.webgoat.container.session;

import java.util.HashMap;

/**
 * Created by jason on 1/4/17.
 */
public class UserSessionData {

    private HashMap<String,Object> userSessionData = new HashMap<>();

    public UserSessionData() {
    }

    public UserSessionData(String key, String value) {
        setValue(key,value);
    }

    //GETTERS & SETTERS
    public Object getValue(String key) {
        if (!userSessionData.containsKey(key)) {
            return null;
        }
        // else
        return userSessionData.get(key);
    }

    public void setValue(String key, Object value) {
        if (userSessionData.containsKey(key)) {
            userSessionData.replace(key,value);
        } else {
            userSessionData.put(key,value);
        }
    }

}
