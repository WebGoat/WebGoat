package org.owasp.webgoat;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


public class LogSpoofingTest extends IntegrationTest {

    @Test
    public void logSpoofing() {
        startLesson("LogSpoofing");
        Map<String, Object> params = new HashMap<>();
        params.clear();
        params.put("username", "test \n admin");
        params.put("password", "test");

        checkAssignment(url("LogSpoofing/log-spoofing"), params, true);
    }
}
