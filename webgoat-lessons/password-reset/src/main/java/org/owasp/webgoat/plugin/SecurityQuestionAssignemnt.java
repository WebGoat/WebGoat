package org.owasp.webgoat.plugin;

import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@AssignmentPath("/PasswordReset/SecurityQuestions")
@AssignmentHints("security-questions-hint1")
public class SecurityQuestionAssignemnt extends AssignmentEndpoint {

  private static Map<String, String> questions;

  static {
    questions = new HashMap<>();
    questions.put("What is your favorite animal?", "Bad: Can easily be guessed and can most likely be figured out through social media.");
    questions.put("In what year was your mother born?", "Bad: Can  be easily guessed.");
    questions.put("What was the time you were born?", "Good: If you know the time you were born it is really good, because " +
            "it is hard to figure out through social media and the answer is not subject to change.");
    questions.put("What is the name of the person you first kissed?", "Fair: it is not a bad question, but friends and family may know and someone might figure it out through social media.");
    questions.put("What was the house number and street name you lived in as a child?", "Good: hard to guess and even close friends might not know the answer.");
    questions.put("In what town or city was your first full time job?", "Fair / Good: Might be easy to figure out if someone is on LinkedIn or posts a lot on social media");
    questions.put("In what city were you born?", "Fair: Might be hard to figure out for a person who does not know you, but not for a person that knows, did know you.");
    questions.put("What was the last name of your favorite teacher in grade three?", "Good/Fair: Most people would probably not know the answer to that, but if someone does its quite a good question.");
    questions.put("What is the name of a college/job you applied to but didn't attend?", "Good: Most people will probably no an answer to that and it is really hard to figure out, even for people close to you.");
    questions.put("What are the last 5 digits of your drivers license?", "Bad: Is subject to change, and the last digit of your driver license might follow a specific pattern. (For example your birthday.)");
    questions.put("What was your childhood nickname?", "Fair: if someone had a nickname they probably remember it, but not all people had one.");
    questions.put("Who was your childhood hero?", "Fair: If your childhood hero, was someone not obvious it can be quite good, but not everyone really had one and can remember it easily.");
    questions.put("On which wrist do you were your watch?", "Awful: Easy to guess.");
    questions.put("What is your favorite color?", "Bad: Can easily be guessed.");
  }
  @RequestMapping(method = RequestMethod.POST)
  public
  @ResponseBody
  AttackResult completed(@RequestParam String question) {
    System.out.println("moin");
    String answer = questions.get(question);
    if(answer.startsWith("Good"))
      return success().output(answer).build();
    return failed().output(answer).build();
  }
}
