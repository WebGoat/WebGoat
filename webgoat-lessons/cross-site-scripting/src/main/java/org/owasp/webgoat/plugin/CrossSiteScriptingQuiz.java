package org.owasp.webgoat.plugin;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@AssignmentPath("/cross-site-scripting/quiz")
public class CrossSiteScriptingQuiz extends AssignmentEndpoint {

    String[] solutions = {"Solution 4", "Solution 3", "Solution 1", "Solution 2", "Solution 4"};
    boolean[] guesses = new boolean[solutions.length];

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public AttackResult completed(@RequestParam String[] question_0_solution, @RequestParam String[] question_1_solution, @RequestParam String[] question_2_solution, @RequestParam String[] question_3_solution, @RequestParam String[] question_4_solution) throws IOException {
        int correctAnswers = 0;

        String[] givenAnswers = {question_0_solution[0], question_1_solution[0], question_2_solution[0], question_3_solution[0], question_4_solution[0]};

        for(int i = 0; i < solutions.length; i++) {
            if (givenAnswers[i].contains(solutions[i])) {
                // answer correct
                correctAnswers++;
                guesses[i] = true;
            } else {
                // answer incorrect
                guesses[i] = false;
            }
        }

        if(correctAnswers == solutions.length) {
            return trackProgress(success().build());
        } else {
            return trackProgress(failed().build());
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public boolean[] getResults() {
        return this.guesses;
    }

    }
