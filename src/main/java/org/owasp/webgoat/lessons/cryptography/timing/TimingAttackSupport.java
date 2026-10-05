/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.cryptography.timing;

import java.util.HexFormat;

final class TimingAttackSupport {

  private TimingAttackSupport() {}

  static byte[] parseTag(String value, int expectedBytes) {
    if (value == null || value.length() != expectedBytes * 2) {
      return null;
    }
    try {
      return HexFormat.of().parseHex(value);
    } catch (IllegalArgumentException ignored) {
      return null;
    }
  }
}
