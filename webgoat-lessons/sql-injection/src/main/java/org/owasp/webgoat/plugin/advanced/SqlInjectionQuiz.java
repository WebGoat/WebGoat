package org.owasp.webgoat.plugin.advanced;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.owasp.webgoat.session.DatabaseUtilities;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * add a question: 1. Append new question to JSON string
 * 2. add right solution to solutions array
 * 3. add Request param with name of question to method head
 * For a more detailed description how to implement the quiz go to the quiz.js file in webgoat-container -> js
 */
@AssignmentPath("/SqlInjection/quiz")
public class SqlInjectionQuiz extends AssignmentEndpoint {

    String[] solutions = {"Solution 4", "Solution 3", "Solution 2", "Solution 3", "Solution 4"};
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
