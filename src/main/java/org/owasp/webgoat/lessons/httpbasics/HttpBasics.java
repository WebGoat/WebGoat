/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.httpbasics;

import org.owasp.webgoat.container.lessons.Category;
import org.owasp.webgoat.container.lessons.Lesson;
import org.springframework.stereotype.Component;

@Component
public class HttpBasics extends Lesson {
  @Override
  public Category getDefaultCategory() {
    return Category.GENERAL;
  }

  @Override
  public String getTitle() {
    return "1.http-basics.title"; // first lesson in general
  }
}
