package org.owasp.webgoat.lessons.cia;

import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CIAQuiz extends AssignmentEndpoint {

  String[] solutions = {"Solution 3", "Solution 1", "Solution 4", "Solution 2"};
  boolean[] guesses = new boolean[solutions.length];

  @PostMapping("/cia/quiz")
  @ResponseBody
  public AttackResult completed(
      @RequestParam String[] question_0_solution,
      @RequestParam String[] question_1_solution,
      @RequestParam String[] question_2_solution,
      @RequestParam String[] question_3_solution) {
    int correctAnswers = 0;

    String[] givenAnswers = {
      question_0_solution[0], question_1_solution[0], question_2_solution[0], question_3_solution[0]
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

  @GetMapping("/cia/quiz")
  @ResponseBody
  public boolean[] getResults() {
    return this.guesses;
  }
}
