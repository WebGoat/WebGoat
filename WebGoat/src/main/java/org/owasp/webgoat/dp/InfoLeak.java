package org.owasp.webgoat.dp;
public class InfoLeak {
    public static String errorMessage(String userInput) {
        // CWE-209: verbose error discloses internal info
        try { Integer.parseInt(userInput); }
        catch (Exception e) { return "ERR:"+e.toString(); } // intentionally verbose
        return "ok";
    }
}

