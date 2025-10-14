/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.commandinjection;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.container.users.WebGoatUser;

class CommandInjectionTask3Test {

  private CommandInjectionTask3 task;
  private WebGoatUser user;

  @BeforeEach
  void setUp() {
    task = new CommandInjectionTask3("./target/webgoat-test");
    user = new WebGoatUser("alice", "password");
    task.initialize(user);
  }

  @Test
  void shouldFailWhenTitleMissing() {
    AttackResult result = task.search(user, "");
    assertThat(result.assignmentSolved()).isFalse();
    assertThat(result.getFeedback()).isEqualTo("commandinjection.task3.failure.payload");
  }

  @Test
  void shouldFailWhenFlagMissing() {
    AttackResult result = task.submitFlag(user, "");
    assertThat(result.assignmentSolved()).isFalse();
    assertThat(result.getFeedback()).isEqualTo("commandinjection.task3.failure.blank");
  }

  @Test
  void shouldSucceedWhenFlagMatches() {
    AttackResult firstRun =
        task.search(user, "luna; cat command-injection/alice/flag.txt");
    Matcher matcher = Pattern.compile("CI_FLAG\\{[A-Za-z0-9]+\\}").matcher(firstRun.getOutput());
    assertThat(matcher.find()).isTrue();
    String flag = matcher.group();

    AttackResult result = task.submitFlag(user, flag);

    assertThat(result.assignmentSolved()).isTrue();
    assertThat(result.getFeedback()).isEqualTo("commandinjection.task3.success");
  }
}
