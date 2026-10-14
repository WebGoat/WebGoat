/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.httpbasics;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HttpBasicsQuiz implements AssignmentEndpoint {

  private final String[] solutions = {"Solution 2", "Solution 3", "Solution 1",
      "Solution 2", "Solution 3", "Solution 4", "Solution 4"};
  boolean[] guesses = new boolean[solutions.length];

  @PostMapping("/HttpBasics/quiz")
  @ResponseBody
  public AttackResult completed(
      @RequestParam String[] question_0_solution,
      @RequestParam String[] question_1_solution,
      @RequestParam String[] question_2_solution,
      @RequestParam String[] question_3_solution,
      @RequestParam String[] question_4_solution,
      @RequestParam String[] question_5_solution,
      @RequestParam String[] question_6_solution) {
    int correctAnswers = 0;

    String[] givenAnswers = {
      question_0_solution[0], question_1_solution[0], question_2_solution[0], question_3_solution[0],
      question_4_solution[0], question_5_solution[0], question_6_solution[0],
    };

    for (int i = 0; i < solutions.length; i++) {
      if (givenAnswers[i].contains(solutions[i])) {
        // answer correct
        correctAnswers++;
        guesses[i] = true;
      } else {
        // answer incorrect
        guesses[i] = false;
      }
    }

    if (correctAnswers == solutions.length) {
      return success(this).build();
    } else {
      return failed(this).build();
    }
  }

  @GetMapping("/HttpBasics/quiz")
  @ResponseBody
  public boolean[] getResults() {
    return this.guesses;
  }
}
