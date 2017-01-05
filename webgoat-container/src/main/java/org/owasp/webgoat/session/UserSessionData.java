package org.owasp.webgoat.session;

import java.util.HashMap;

/**
 * Created by jason on 1/4/17.
 */
public class UserSessionData {

    private HashMap<String,String> userSessionData = new HashMap<>();

    public UserSessionData() {
    }

    public UserSessionData(String key, String value) {
        setValue(key,value);
    }

    //GETTERS & SETTERS
    public String getValue(String key) {
        return userSessionData.get(key);
    }

    public void setValue(String key, String value) {
        if (userSessionData.containsKey(key)) {
            userSessionData.replace(key,value);
        } else {
            userSessionData.put(key,value);
        }
    }

}
