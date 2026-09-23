/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.httpparameterpollution;

final class ParameterPollutionMitigation {

  private ParameterPollutionMitigation() {}

  static String requireSingleValue(String[] values) {
    if (values == null || values.length != 1) {
      throw new IllegalArgumentException("Exactly one value is required");
    }
    return values[0];
  }
}
