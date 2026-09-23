/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.cryptography.timing;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

class InsecureHmacValidatorTest {

  @Test
  void delayDependsOnTheNumberOfMatchingPrefixBytes() {
    List<Long> delays = new ArrayList<>();
    var validator = new InsecureHmacValidator(delays::add, ignored -> 0);
    byte[] expected = {0x01, 0x02, 0x03, 0x04};

    assertThat(validator.matches(expected, new byte[] {0x01, 0x02, 0x7f, 0x00}, 5, 0))
        .isFalse();

    assertThat(delays)
        .containsExactly(TimeUnit.MILLISECONDS.toNanos(5), TimeUnit.MILLISECONDS.toNanos(5));
  }

  @Test
  void firstByteMismatchHasNoMatchingByteDelay() {
    List<Long> delays = new ArrayList<>();
    var validator = new InsecureHmacValidator(delays::add, ignored -> 0);

    assertThat(
            validator.matches(
                new byte[] {0x01, 0x02, 0x03, 0x04},
                new byte[] {0x7f, 0x02, 0x03, 0x04},
                25,
                0))
        .isFalse();
    assertThat(delays).isEmpty();
  }

  @Test
  void jitterIsIndependentOfTheMatchingPrefix() {
    List<Long> delays = new ArrayList<>();
    var validator = new InsecureHmacValidator(delays::add, ignored -> 7);

    assertThat(
            validator.matches(
                new byte[] {0x01, 0x02, 0x03, 0x04},
                new byte[] {0x7f, 0x02, 0x03, 0x04},
                2,
                8))
        .isFalse();
    assertThat(delays).containsExactly(TimeUnit.MILLISECONDS.toNanos(7));
  }

  @Test
  void equalTagsMatchAfterEveryByteIsChecked() {
    List<Long> delays = new ArrayList<>();
    var validator = new InsecureHmacValidator(delays::add, ignored -> 0);
    byte[] tag = {0x01, 0x02, 0x03, 0x04};

    assertThat(validator.matches(tag, tag.clone(), 2, 0)).isTrue();
    assertThat(delays).hasSize(tag.length);
  }
}
