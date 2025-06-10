/*
 * SPDX-FileCopyrightText: Copyright © 2017 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.challenges.challenge7;

import org.owasp.webgoat.container.lessons.Category;
import org.owasp.webgoat.container.lessons.Lesson;
import org.springframework.stereotype.Component;

@Component
public class Challenge7 extends Lesson {

  @Override
  public Category getDefaultCategory() {
    return Category.CHALLENGE;
  }

  @Override
  public String getTitle() {
    return "challenge7.title";
  }
}
