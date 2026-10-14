/*
 * SPDX-FileCopyrightText: Copyright © 2017 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.passwordreset;

import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class TriedQuestions {

  private Set<String> answeredQuestions = new HashSet<>();

  public void incr(String question) {
    answeredQuestions.add(question);
  }

  public boolean isComplete() {
    return answeredQuestions.size() > 1;
  }
}
