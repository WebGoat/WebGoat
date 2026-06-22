/*
 * SPDX-FileCopyrightText: Copyright © 2017 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.cia;

import org.owasp.webgoat.container.lessons.Category;
import org.owasp.webgoat.container.lessons.Lesson;
import org.springframework.stereotype.Component;

@Component
public class CIA extends Lesson {

  @Override
  public Category getDefaultCategory() {
    return Category.GENERAL;
  }

  @Override
  public String getTitle() {
    return "4.cia.title"; // 4th lesson in general
  }
}
