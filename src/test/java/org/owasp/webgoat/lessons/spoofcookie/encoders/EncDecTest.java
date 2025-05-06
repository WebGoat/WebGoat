/*
 * SPDX-FileCopyrightText: Copyright © 2021 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.spoofcookie.encoders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class EncDecTest {

  @ParameterizedTest
  @DisplayName("Encode test")
  @MethodSource("providedForEncValues")
  void testEncode(String decoded, String encoded) {
    String result = EncDec.encode(decoded);

    assertThat(result.endsWith(encoded)).isTrue();
  }

  @ParameterizedTest
  @DisplayName("Decode test")
  @MethodSource("providedForDecValues")
  void testDecode(String decoded, String encoded) {
    String result = EncDec.decode(encoded);

    assertThat(decoded, is(result));
  }

  @Test
  @DisplayName("null encode test")
  void testNullEncode() {
    assertThat(EncDec.encode(null)).isNull();
  }

  @Test
  @DisplayName("null decode test")
  void testNullDecode() {
    assertThat(EncDec.decode(null)).isNull();
  }

  private static Stream<Arguments> providedForEncValues() {
    return Stream.of(
        Arguments.of("webgoat", "YxNmY2NzYyNjU3Nw=="),
        Arguments.of("admin", "2ZTY5NmQ2NDYx"),
        Arguments.of("tom", "2ZDZmNzQ="));
  }

  private static Stream<Arguments> providedForDecValues() {
    return Stream.of(
        Arguments.of("webgoat", "NjI2MTcwNGI3YTQxNGE1OTU2NzQ3NDYxNmY2NzYyNjU3Nw=="),
        Arguments.of("admin", "NjI2MTcwNGI3YTQxNGE1OTU2NzQ2ZTY5NmQ2NDYx"),
        Arguments.of("tom", "NjI2MTcwNGI3YTQxNGE1OTU2NzQ2ZDZmNzQ="));
  }
}
