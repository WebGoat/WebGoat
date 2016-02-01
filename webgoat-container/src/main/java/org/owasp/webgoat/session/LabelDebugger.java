package org.owasp.webgoat.session;

import java.io.Serializable;

/**
 * <p>LabelDebugger class.</p>
 *
 * @author dm
 * @version $Id: $Id
 */
public class LabelDebugger implements Serializable {

    private boolean isEnabled = false;

    /**
     * <p>isEnabled.</p>
     *
     * @return a boolean.
     */
    public boolean isEnabled() {
        return isEnabled;
    }

    /**
     * <p>enable.</p>
     */
    public void enable() {
        this.isEnabled = true;
    }

}
