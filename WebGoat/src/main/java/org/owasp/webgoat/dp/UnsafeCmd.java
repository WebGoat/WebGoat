package org.owasp.webgoat.dp;

import jakarta.servlet.http.HttpServletRequest;

public class UnsafeCmd {
    // Intentional OS Command Injection (CWE-78) for testing
    public void run(HttpServletRequest request) throws Exception {
        String cmd = request.getParameter("cmd"); // tainted input
        //Dangerous: passes unvalidated input to the OS
        Runtime.getRuntime().exec(cmd);
    }
}
