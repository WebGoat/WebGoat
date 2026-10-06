/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.passwordstorage;

import org.owasp.webgoat.container.lessons.Category;
import org.owasp.webgoat.container.lessons.Lesson;
import org.springframework.stereotype.Component;

@Component
public class PasswordStorage extends Lesson {

  @Override
  protected Category getDefaultCategory() {
    return Category.A2;
  }

  @Override
  public String getTitle() {
    return "password-storage.title";
  }
}
