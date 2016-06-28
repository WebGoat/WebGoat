package org.owasp.webgoat.plugins;

/**
 * <p>PluginLoadingFailure class.</p>
 *
 * @version $Id: $Id
 * @author dm
 */
public class PluginLoadingFailure extends RuntimeException {

    /**
     * <p>Constructor for PluginLoadingFailure.</p>
     *
     * @param message a {@link java.lang.String} object.
     */
    public PluginLoadingFailure(String message) {
        super(message);
    }

    /**
     * <p>Constructor for PluginLoadingFailure.</p>
     *
     * @param message a {@link java.lang.String} object.
     * @param e a {@link java.lang.Exception} object.
     */
    public PluginLoadingFailure(String message, Exception e) {
        super(message, e);
    }
}
