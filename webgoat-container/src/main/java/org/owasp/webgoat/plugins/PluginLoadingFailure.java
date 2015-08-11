package org.owasp.webgoat.plugins;

public class PluginLoadingFailure extends RuntimeException {

    public PluginLoadingFailure(String message, Exception e) {
        super(message, e);
    }
}
