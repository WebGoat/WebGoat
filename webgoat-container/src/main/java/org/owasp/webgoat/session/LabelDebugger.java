package org.owasp.webgoat.session;

/**
 */
public class LabelDebugger {

    private boolean isEnabled = false;

    public boolean isEnabled() {
        return isEnabled;
    }

    public void enable() {
        this.isEnabled = true;
    }

}
