/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.commandinjection;

import static java.util.regex.Pattern.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.assignments.AttackResult;

class CommandInjectionTask2Test {

  private CommandInjectionTask2 task;

  @BeforeEach
  void setUp() {
    task = new CommandInjectionTask2();
  }

  @Test
  void shouldBuildCommandWithPayload() {
    String command = task.buildCommand("uname -a", "; whoami");

    assertThat(command).contains("whoami");
  }

  @Test
  void shouldFailWhenTokenMissing() {
    AttackResult result = task.run("uname -a", "; whoami", "");

    assertThat(result.assignmentSolved()).isFalse();
    assertThat(result.getFeedback()).isEqualTo("commandinjection.task2.failure.blank");
  }

  @Test
  void shouldSucceedWhenTokenMatches() {
    boolean isWindows = System.getProperty("os.name", "").toLowerCase().contains("win");
    String payload = isWindows ? "&& echo %WEBGOAT_BUILD_TOKEN%" : "; echo $WEBGOAT_BUILD_TOKEN";

    AttackResult failedAttempt = task.run("", payload, "");
    String output = failedAttempt.getOutput();
    var matcher =
        compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}")
            .matcher(output);
    if (!matcher.find()) {
      throw new IllegalStateException("Token not present in output: " + output);
    }
    String token = matcher.group();

    AttackResult result = task.run("", payload, token);

    assertThat(result.assignmentSolved()).isTrue();
    assertThat(result.getFeedback()).isEqualTo("commandinjection.task2.success");
  }
}
