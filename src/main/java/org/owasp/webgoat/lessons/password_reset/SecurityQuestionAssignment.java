/*
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 2019 Bruce Mayhew
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * Getting Source ==============
 *
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software projects.
 */

package org.owasp.webgoat.lessons.password_reset;

import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static java.util.Optional.of;

/**
 * Assignment for picking a good security question.
 *
 * @author Tobias Melzer
 * @since 11.12.18
 */
@RestController
public class SecurityQuestionAssignment extends AssignmentEndpoint {

    @Autowired
    private TriedQuestions triedQuestions;

    private static Map<String, String> questions;

    static {
        questions = new HashMap<>();
        questions.put("What is your favorite animal?", "The answer can easily be guessed and figured out through social media.");
        questions.put("In what year was your mother born?", "Can  be easily guessed.");
        questions.put("What was the time you were born?", "This may first seem like a good question, but you most likely dont know the exact time, so it might be hard to remember.");
        questions.put("What is the name of the person you first kissed?", "Can be figured out through social media, or even guessed by trying the most common names.");
        questions.put("What was the house number and street name you lived in as a child?", "Answer can be figured out through social media, or worse it might be your current address.");
        questions.put("In what town or city was your first full time job?", "In times of LinkedIn and Facebook, the answer can be figured out quite easily.");
        questions.put("In what city were you born?", "Easy to figure out through social media.");
        questions.put("What was the last name of your favorite teacher in grade three?", "Most people would probably not know the answer to that.");
        questions.put("What is the name of a college/job you applied to but didn't attend?", "It might not be easy to remember and an hacker could just try some company's/colleges in your area.");
        questions.put("What are the last 5 digits of your drivers license?", "Is subject to change, and the last digit of your driver license might follow a specific pattern. (For example your birthday).");
        questions.put("What was your childhood nickname?", "Not all people had a nickname.");
        questions.put("Who was your childhood hero?", "Most Heroes we had as a child where quite obvious ones, like Superman for example.");
        questions.put("On which wrist do you were your watch?", "There are only to possible real answers, so really easy to guess.");
        questions.put("What is your favorite color?", "Can easily be guessed.");
    }

    @PostMapping("/PasswordReset/SecurityQuestions")
    @ResponseBody
    public AttackResult completed(@RequestParam String question) {
        var answer = of(questions.get(question));
        if (answer.isPresent()) {
            triedQuestions.incr(question);
            if (triedQuestions.isComplete()) {
                return success(this).output("<b>" + answer + "</b>").build();
            }
        }
        return informationMessage(this)
                .feedback("password-questions-one-successful")
                .output(answer.orElse("Unknown question, please try again..."))
                .build();
    }
}
