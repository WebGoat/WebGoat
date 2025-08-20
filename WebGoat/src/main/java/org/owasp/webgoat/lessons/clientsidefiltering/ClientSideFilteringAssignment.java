/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.clientsidefiltering;

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
  "ClientSideFilteringHint1",
  "ClientSideFilteringHint2",
  "ClientSideFilteringHint3",
  "ClientSideFilteringHint4"
})
public class ClientSideFilteringAssignment implements AssignmentEndpoint {

  @PostMapping("/clientSideFiltering/attack1")
  @ResponseBody
  public AttackResult completed(@RequestParam String answer) {
    return "450000".equals(answer)
        ? success(this).feedback("assignment.solved").build()
        : failed(this).feedback("ClientSideFiltering.incorrect").build();
  }
}
