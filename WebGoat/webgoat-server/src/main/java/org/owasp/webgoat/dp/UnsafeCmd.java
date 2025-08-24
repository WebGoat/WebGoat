package org.owasp.webgoat.dp;

import javax.servlet.http.HttpServletRequest;

public class UnsafeCmd {
    // Intentional OS Command Injection (CWE-78)
    public void run(HttpServletRequest request) throws Exception {
        String cmd = request.getParameter("cmd"); // tainted input
        Runtime.getRuntime().exec(cmd); //  dangr link
    }
}

