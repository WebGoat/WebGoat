/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.cryptography.timing;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import java.security.MessageDigest;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AssignmentHints({
  "timing-attacks.hints.noise",
  "timing-attacks.hints.statistics",
  "timing-attacks.hints.elimination"
})
public class TimingAttackNoiseAssignment implements AssignmentEndpoint {

  private final TimingAttackSessionState state;

  public TimingAttackNoiseAssignment(TimingAttackSessionState state) {
    this.state = state;
  }

  @PostMapping("/crypto/timing/noisy-tag")
  @ResponseBody
  public AttackResult submit(@RequestParam(required = false) String signature) {
    byte[] supplied = TimingAttackSupport.parseTag(signature, TimingAttackSessionState.TAG_LENGTH);
    byte[] expected = state.tagFor(TimingAttackSessionState.NOISY_MESSAGE);
    if (supplied != null && MessageDigest.isEqual(expected, supplied)) {
      return success(this).feedback("timing-attacks.noise.success").build();
    }
    return failed(this).feedback("timing-attacks.try-again").build();
  }
}
