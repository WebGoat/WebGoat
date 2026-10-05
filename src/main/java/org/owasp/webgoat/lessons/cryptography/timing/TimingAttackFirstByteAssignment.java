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
@AssignmentHints({
  "timing-attacks.hints.behavior",
  "timing-attacks.hints.measure",
  "timing-attacks.hints.first-byte"
})
public class TimingAttackFirstByteAssignment implements AssignmentEndpoint {

  private final TimingAttackSessionState state;

  public TimingAttackFirstByteAssignment(TimingAttackSessionState state) {
    this.state = state;
  }

  @PostMapping("/crypto/timing/first-byte")
  @ResponseBody
  public AttackResult submit(@RequestParam(required = false) String firstByte) {
    byte[] supplied = TimingAttackSupport.parseTag(firstByte, 1);
    byte expected = state.tagFor(TimingAttackSessionState.KNOWN_MESSAGE)[0];
    if (supplied != null && supplied[0] == expected) {
      return success(this).feedback("timing-attacks.first-byte.success").build();
    }
    return failed(this).feedback("timing-attacks.try-again").build();
  }
}
