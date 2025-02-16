/*
 * SPDX-FileCopyrightText: Copyright © 2017 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.challenges;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FlagController implements AssignmentEndpoint {

  private final Flags flags;

  public FlagController(Flags flags) {
    this.flags = flags;
  }

  @PostMapping(path = "/challenge/flag/{flagNumber}")
  @ResponseBody
  public AttackResult postFlag(@PathVariable int flagNumber, @RequestParam String flag) {
    var expectedFlag = flags.getFlag(flagNumber);
    if (expectedFlag.isCorrect(flag)) {
      return success(this).feedback("challenge.flag.correct").build();
    } else {
      return failed(this).feedback("challenge.flag.incorrect").build();
    }
  }
}
