/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.webgoatintroduction;

import org.owasp.webgoat.container.lessons.Category;
import org.owasp.webgoat.container.lessons.Lesson;
import org.springframework.stereotype.Component;


@Component
public class WebGoatIntroduction extends Lesson {
  @Override
  public Category getDefaultCategory() {
    return Category.INTRODUCTION;
  }

  @Override
  public String getTitle() {
    return "webgoat.title";
  }
}
