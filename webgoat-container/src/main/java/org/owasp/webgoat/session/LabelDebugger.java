package org.owasp.webgoat.session;

import java.io.Serializable;

/**
 * <p>LabelDebugger class.</p>
 *
 * @author dm
 * @version $Id: $Id
 */
public class LabelDebugger implements Serializable {

    private boolean enabled = false;

    /**
     * <p>isEnabled.</p>
     *
     * @return a boolean.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * <p>Enables label debugging</p>
     */
    public void enable() {
        this.enabled = true;
    }

    /**
     * <p>Disables label debugging</p>
     */
    public void disable() {
        this.enabled = false;
    }

    /**
     * <p>Sets the status to enabled</p>
     * @param enabled
     * @throws Exception if enabled is null
     */
    public void setEnabled(Boolean enabled) throws Exception {
        if(enabled == null) throw new Exception("Cannot set enabled to null");
        this.enabled = enabled;
    }

}
