package org.owasp.webgoat.plugin;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Assignment for picking a good security question.
 * @author Tobias Melzer
 * @since 11.12.18
 */
@AssignmentPath("/PasswordReset/SecurityQuestions")
public class SecurityQuestionAssignment extends AssignmentEndpoint {

  private static int triedQuestions = 0;

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
  @RequestMapping(method = RequestMethod.POST)
  public
  @ResponseBody
  AttackResult completed(@RequestParam String question) {
    triedQuestions+=1;
    String answer = questions.get(question);
    answer = "<b>" + answer + "</b>";
    if(triedQuestions > 1)
      return trackProgress(success().output(answer).build());
    return failed().output(answer).build();
  }
}
