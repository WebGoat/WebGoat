/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.openredirect;

import org.owasp.webgoat.container.lessons.Category;
import org.owasp.webgoat.container.lessons.Lesson;
import org.springframework.stereotype.Component;

/** Entry point for the Open Redirect lesson */
@Component
public class OpenRedirect extends Lesson {
  @Override
  public Category getDefaultCategory() {
    return Category.GENERAL;
  }

  @Override
  public String getTitle() {
    return "openredirect.title";
  }
}
