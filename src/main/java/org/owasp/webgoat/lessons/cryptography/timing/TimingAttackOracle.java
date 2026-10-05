/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.cryptography.timing;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TimingAttackOracle {

  static final String VALID = "Valid signature";
  static final String INVALID = "Invalid signature";

  private final InsecureHmacValidator validator = new InsecureHmacValidator();
  private final TimingAttackSessionState state;

  public TimingAttackOracle(TimingAttackSessionState state) {
    this.state = state;
  }

  @GetMapping(path = "/crypto/timing/verify", produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> verifyDiscovery(
      @RequestParam(defaultValue = TimingAttackSessionState.KNOWN_MESSAGE) String message,
      @RequestParam(required = false) String signature) {
    return verify(message, signature, 25, 0);
  }

  @GetMapping(path = "/crypto/timing/verify-tag", produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> verifyTag(
      @RequestParam(defaultValue = TimingAttackSessionState.KNOWN_MESSAGE) String message,
      @RequestParam(required = false) String signature) {
    return verify(message, signature, 5, 0);
  }

  @GetMapping(path = "/crypto/timing/verify-noisy", produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> verifyNoisy(
      @RequestParam(defaultValue = TimingAttackSessionState.NOISY_MESSAGE) String message,
      @RequestParam(required = false) String signature) {
    return verify(message, signature, 2, 8);
  }

  private ResponseEntity<String> verify(
      String message,
      String signature,
      long matchingByteDelayMillis,
      int maximumJitterMillis) {
    if (!state.tryAcquireRequestSlot()) {
      return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
          .contentType(MediaType.TEXT_PLAIN)
          .body("Too many concurrent requests");
    }

    try {
      byte[] supplied = TimingAttackSupport.parseTag(signature, TimingAttackSessionState.TAG_LENGTH);
      boolean valid =
          supplied != null
              && validator.matches(
                  state.tagFor(message),
                  supplied,
                  matchingByteDelayMillis,
                  maximumJitterMillis);
      return ResponseEntity.ok(valid ? VALID : INVALID);
    } finally {
      state.releaseRequestSlot();
    }
  }
}
