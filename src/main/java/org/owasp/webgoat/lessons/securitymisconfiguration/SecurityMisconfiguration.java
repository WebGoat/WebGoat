/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.securitymisconfiguration;

import org.owasp.webgoat.container.lessons.Category;
import org.owasp.webgoat.container.lessons.Lesson;
import org.springframework.stereotype.Component;

/** Lesson entry point for Security Misconfiguration. */
@Component
public class SecurityMisconfiguration extends Lesson {

  @Override
  public Category getDefaultCategory() {
    return Category.A5;
  }

  @Override
  public String getTitle() {
    return "securitymisconfiguration.title";
  }
}
