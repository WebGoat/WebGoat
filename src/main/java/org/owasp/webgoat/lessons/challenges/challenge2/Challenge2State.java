/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.challenges.challenge2;

import java.io.Serial;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.HexFormat;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class Challenge2State implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  private final String flag;
  private final String internalToken;
  private boolean reachedThroughXml;

  public Challenge2State() {
    byte[] random = new byte[16];
    new SecureRandom().nextBytes(random);
    flag = UUID.randomUUID().toString();
    internalToken = HexFormat.of().formatHex(random);
  }

  String flag() {
    return flag;
  }

  String internalToken() {
    return internalToken;
  }

  synchronized void recordXmlChain() {
    reachedThroughXml = true;
  }

  synchronized boolean completedXmlChain() {
    return reachedThroughXml;
  }
}
