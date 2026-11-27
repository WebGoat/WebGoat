/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.promptinjection;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.owasp.webgoat.container.CurrentUsername;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/** Quiz on which mitigations against indirect prompt injection actually hold. */
@RestController
public class PromptInjectionQuiz implements AssignmentEndpoint {

  private static final String[] SOLUTIONS = {
    "Solution 1", "Solution 2", "Solution 0", "Solution 3", "Solution 1"
  };
  private final Map<String, boolean[]> guesses = new ConcurrentHashMap<>();

  @PostMapping("/PromptInjection/quiz")
  @ResponseBody
  public AttackResult completed(
      @CurrentUsername String username,
      @RequestParam String[] question_0_solution,
      @RequestParam String[] question_1_solution,
      @RequestParam String[] question_2_solution,
      @RequestParam String[] question_3_solution,
      @RequestParam String[] question_4_solution) {
    String[] given = {
      question_0_solution[0],
      question_1_solution[0],
      question_2_solution[0],
      question_3_solution[0],
      question_4_solution[0]
    };
    boolean[] results = new boolean[SOLUTIONS.length];
    int correct = 0;
    for (int i = 0; i < SOLUTIONS.length; i++) {
      results[i] = given[i].contains(SOLUTIONS[i]);
      if (results[i]) {
        correct++;
      }
    }
    guesses.put(username, results);
    if (correct == SOLUTIONS.length) {
      return success(this).feedback("promptinjection.quiz.success").build();
    }
    return failed(this).feedback("promptinjection.quiz.failure").build();
  }

  @GetMapping("/PromptInjection/quiz")
  @ResponseBody
  public boolean[] results(@CurrentUsername String username) {
    return guesses.getOrDefault(username, new boolean[SOLUTIONS.length]);
  }
}
