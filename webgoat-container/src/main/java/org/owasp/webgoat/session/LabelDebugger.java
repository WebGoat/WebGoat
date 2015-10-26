package org.owasp.webgoat.session;

import java.io.Serializable;

/**
 */
public class LabelDebugger implements Serializable {

    private boolean isEnabled = false;

    public boolean isEnabled() {
        return isEnabled;
    }

    public void enable() {
        this.isEnabled = true;
    }

}
