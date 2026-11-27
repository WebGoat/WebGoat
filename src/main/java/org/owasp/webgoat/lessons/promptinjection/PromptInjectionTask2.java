/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.promptinjection;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import lombok.RequiredArgsConstructor;
import org.owasp.webgoat.container.CurrentUsername;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Assignment 2: the learner enables the server-side argument guard and repeats the task. The
 * assignment is solved when the server refuses a call that carries the session token, which shows
 * that a deterministic check holds even when the model obeys the injected instruction.
 */
@RestController
@RequiredArgsConstructor
@AssignmentHints({
  "promptinjection.task2.hint1",
  "promptinjection.task2.hint2",
  "promptinjection.task2.hint3"
})
public class PromptInjectionTask2 implements AssignmentEndpoint {

  private final InvoiceServerState state;

  @PostMapping("/PromptInjection/task2")
  public AttackResult completed(@CurrentUsername String username) {
    var learner = state.forUser(username);
    if (learner.refusedLeak().isPresent()) {
      return success(this).feedback("promptinjection.task2.success").build();
    }
    if (!learner.settings().argumentGuard()) {
      return failed(this).feedback("promptinjection.task2.guard-off").build();
    }
    return failed(this).feedback("promptinjection.task2.no-refusal").build();
  }
}
