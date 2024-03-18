package org.owasp.webgoat.lessons.challenges;

public record Flag(int number, String answer) {

  public boolean isCorrect(String flag) {
    return answer.equals(flag);
  }

  @Override
  public String toString() {
    return answer;
  }
}
