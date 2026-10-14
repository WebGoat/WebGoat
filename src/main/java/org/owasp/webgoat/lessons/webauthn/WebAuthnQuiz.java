/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.webauthn;

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

@RestController
public class WebAuthnQuiz implements AssignmentEndpoint {

  private static final String[] SOLUTIONS = {
    "Solution 2", "Solution 2", "Solution 2", "Solution 2", "Solution 1"
  };
  private final Map<String, boolean[]> guesses = new ConcurrentHashMap<>();

  @PostMapping("/WebAuthn/quiz")
  @ResponseBody
  public AttackResult completed(
      @CurrentUsername String username,
      @RequestParam String[] question_0_solution,
      @RequestParam String[] question_1_solution,
      @RequestParam String[] question_2_solution,
      @RequestParam String[] question_3_solution,
      @RequestParam String[] question_4_solution) {
    String[] givenAnswers = {
      question_0_solution[0],
      question_1_solution[0],
      question_2_solution[0],
      question_3_solution[0],
      question_4_solution[0]
    };
    boolean[] results = new boolean[SOLUTIONS.length];
    int correctAnswers = 0;
    for (int i = 0; i < SOLUTIONS.length; i++) {
      results[i] = givenAnswers[i].contains(SOLUTIONS[i]);
      if (results[i]) {
        correctAnswers++;
      }
    }
    guesses.put(username, results);

    if (correctAnswers == SOLUTIONS.length) {
      return success(this).build();
    }
    return failed(this).build();
  }

  @GetMapping("/WebAuthn/quiz")
  @ResponseBody
  public boolean[] getResults(@CurrentUsername String username) {
    return guesses.getOrDefault(username, new boolean[SOLUTIONS.length]);
  }
}
