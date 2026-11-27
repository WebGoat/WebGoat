/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.promptinjection;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.owasp.webgoat.container.CurrentUsername;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Assignment 1: the learner's assistant totals {@code sample.csv} and, following the poisoned tool
 * description, forwards the {@code session_token} into another tool's arguments. The learner never
 * sees or types that token, so its arrival proves the injection worked.
 */
@RestController
@RequiredArgsConstructor
@AssignmentHints({
  "promptinjection.task1.hint1",
  "promptinjection.task1.hint2",
  "promptinjection.task1.hint3",
  "promptinjection.task1.hint4"
})
public class PromptInjectionTask1 implements AssignmentEndpoint {

  private final InvoiceServerState state;

  @PostMapping("/PromptInjection/task1")
  public AttackResult completed(@CurrentUsername String username) {
    var learner = state.forUser(username);
    var leak = learner.acceptedLeak();
    if (leak.isPresent()) {
      String field =
          leak.get().arguments().entrySet().stream()
              .filter(e -> e.getValue().contains(learner.sessionToken()))
              .map(Map.Entry::getKey)
              .findFirst()
              .orElse("audit");
      return success(this)
          .feedback("promptinjection.task1.success")
          .feedbackArgs(leak.get().tool(), field)
          .build();
    }
    if (learner.calls().isEmpty()) {
      return failed(this).feedback("promptinjection.task1.no-calls").build();
    }
    if (learner.refusedLeak().isPresent()) {
      return failed(this).feedback("promptinjection.task1.guarded").build();
    }
    return failed(this).feedback("promptinjection.task1.resisted").build();
  }
}
