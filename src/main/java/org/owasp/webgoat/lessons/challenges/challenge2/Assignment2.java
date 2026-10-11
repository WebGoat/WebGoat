/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.challenges.challenge2;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AssignmentHints({
  "challenge2.chain.hint1",
  "challenge2.chain.hint2",
  "challenge2.chain.hint3"
})
public class Assignment2 implements AssignmentEndpoint {

  private final Challenge2State state;

  public Assignment2(Challenge2State state) {
    this.state = state;
  }

  @PostMapping("/northstar/recovery")
  public AttackResult solve(@RequestParam String flag) {
    if (state.completedXmlChain() && state.flag().equalsIgnoreCase(flag.trim())) {
      return success(this).feedback("challenge2.chain.success").build();
    }
    return failed(this).feedback("challenge2.chain.failure").build();
  }
}
