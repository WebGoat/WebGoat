/*
 * SPDX-FileCopyrightText: Copyright © 2017 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.challenges;

public record Flag(int number, String answer) {

  public boolean isCorrect(String flag) {
    return answer.equals(flag);
  }

  @Override
  public String toString() {
    return answer;
  }
}
