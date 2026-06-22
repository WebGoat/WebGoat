/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.openredirect;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.assignments.AttackResult;

class OpenRedirectQuizTest {

  private final OpenRedirectQuiz quiz = new OpenRedirectQuiz();

  @Test
  void correctAnswersSolveQuiz() {
    AttackResult result =
        quiz.submit(
            new String[] {"Solution 0"},
            new String[] {"Solution 2"},
            new String[] {"Solution 0"},
            new String[] {"Solution 0"});

    assertThat(result.assignmentSolved()).isTrue();
  }

  @Test
  void incorrectAnswerKeepsQuizUnsolvedAndUpdatesProgress() {
    AttackResult result =
        quiz.submit(
            new String[] {"Solution 0"},
            new String[] {"Solution 1"},
            new String[] {"Solution 0"},
            new String[] {"Solution 0"});

    assertThat(result.assignmentSolved()).isFalse();
    assertThat(quiz.results()).containsExactly(true, false, true, true);
  }
}
