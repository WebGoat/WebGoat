package org.owasp.webgoat.container.session;

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
     * @param enabled {@link org.owasp.webgoat.container.session.LabelDebugger} object
     */
    public void setEnabled(boolean enabled)  {
        this.enabled = enabled;
    }

}
