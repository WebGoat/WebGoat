/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.cryptography.timing;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AssignmentHints({"timing-attacks.hints.mitigation", "timing-attacks.hints.primitive"})
public class TimingAttackMitigationAssignment implements AssignmentEndpoint {

  @PostMapping("/crypto/timing/mitigation")
  @ResponseBody
  public AttackResult submit(
      @RequestParam(required = false) String cause,
      @RequestParam(required = false) String mitigation) {
    if ("early-exit".equals(cause) && "message-digest".equals(mitigation)) {
      return success(this).feedback("timing-attacks.mitigation.success").build();
    }
    return failed(this).feedback("timing-attacks.mitigation.try-again").build();
  }
}
