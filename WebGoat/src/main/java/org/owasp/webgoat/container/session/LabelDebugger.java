/*
 * SPDX-FileCopyrightText: Copyright © 2015 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.session;

import java.io.Serializable;

public class LabelDebugger implements Serializable {

  private boolean enabled = false;

  /**
   * isEnabled.
   *
   * @return a boolean.
   */
  public boolean isEnabled() {
    return enabled;
  }

  /** Enables label debugging */
  public void enable() {
    this.enabled = true;
  }

  /** Disables label debugging */
  public void disable() {
    this.enabled = false;
  }

  /**
   * Sets the status to enabled
   *
   * @param enabled {@link org.owasp.webgoat.container.session.LabelDebugger} object
   */
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
}
