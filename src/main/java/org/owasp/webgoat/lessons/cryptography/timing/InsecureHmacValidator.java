/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.cryptography.timing;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.function.IntUnaryOperator;
import java.util.function.LongConsumer;

/** Intentionally insecure code used only to demonstrate a timing side channel. */
final class InsecureHmacValidator {

  private final LongConsumer delay;
  private final IntUnaryOperator randomDelay;

  InsecureHmacValidator() {
    this(LockSupport::parkNanos, bound -> ThreadLocalRandom.current().nextInt(bound));
  }

  InsecureHmacValidator(LongConsumer delay, IntUnaryOperator randomDelay) {
    this.delay = delay;
    this.randomDelay = randomDelay;
  }

  boolean matches(
      byte[] expected,
      byte[] supplied,
      long matchingByteDelayMillis,
      int maximumJitterMillis) {
    if (maximumJitterMillis > 0) {
      pause(randomDelay.applyAsInt(maximumJitterMillis + 1));
    }

    if (expected.length != supplied.length) {
      return false;
    }

    for (int i = 0; i < expected.length; i++) {
      if (expected[i] != supplied[i]) {
        return false;
      }
      pause(matchingByteDelayMillis);
    }
    return true;
  }

  private void pause(long milliseconds) {
    if (milliseconds > 0) {
      delay.accept(TimeUnit.MILLISECONDS.toNanos(milliseconds));
    }
  }
}
