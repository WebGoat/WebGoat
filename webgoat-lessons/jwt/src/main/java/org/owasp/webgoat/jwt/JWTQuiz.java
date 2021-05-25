package org.owasp.webgoat.jwt;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AttackResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class JWTQuiz extends AssignmentEndpoint {

    private final String[] solutions = {"Solution 1", "Solution 3"};
    private final boolean[] guesses = new boolean[solutions.length];

    @PostMapping("/JWT/quiz")
    @ResponseBody
    public AttackResult completed(@RequestParam String[] question_0_solution, @RequestParam String[] question_1_solution) {
        int correctAnswers = 0;

        String[] givenAnswers = {question_0_solution[0], question_1_solution[0]};

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

    @GetMapping("/JWT/quiz")
    @ResponseBody
    public boolean[] getResults() {
        return this.guesses;
    }

}
