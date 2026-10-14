/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.cryptography.timing;

import org.owasp.webgoat.container.lessons.Category;
import org.owasp.webgoat.container.lessons.Lesson;
import org.springframework.stereotype.Component;

@Component
public class TimingAttacks extends Lesson {

  @Override
  public Category getDefaultCategory() {
    return Category.A2;
  }

  @Override
  public String getTitle() {
    return "timing-attacks.title";
  }
}
