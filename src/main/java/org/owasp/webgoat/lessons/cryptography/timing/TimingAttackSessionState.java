/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.cryptography.timing;

import java.io.Serial;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.concurrent.Semaphore;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class TimingAttackSessionState implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  static final String KNOWN_MESSAGE = "WebGoat";
  static final String NOISY_MESSAGE = "WebGoat with noise";
  static final int TAG_LENGTH = 4;
  static final int MAX_CONCURRENT_REQUESTS = 4;

  private final byte[] secret;
  private final Semaphore requestSlots = new Semaphore(MAX_CONCURRENT_REQUESTS);

  public TimingAttackSessionState() {
    secret = new byte[32];
    new SecureRandom().nextBytes(secret);
  }

  byte[] tagFor(String message) {
    try {
      Mac hmac = Mac.getInstance("HmacSHA256");
      hmac.init(new SecretKeySpec(secret, "HmacSHA256"));
      return Arrays.copyOf(hmac.doFinal(message.getBytes(StandardCharsets.UTF_8)), TAG_LENGTH);
    } catch (GeneralSecurityException e) {
      throw new IllegalStateException("HmacSHA256 is not available", e);
    }
  }

  boolean tryAcquireRequestSlot() {
    return requestSlots.tryAcquire();
  }

  void releaseRequestSlot() {
    requestSlots.release();
  }
}
