/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.cryptography;

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
@AssignmentHints({"crypto-encoding-xor.hints.1"})
public class XOREncodingAssignment implements AssignmentEndpoint {

  @PostMapping("/crypto/encoding/xor")
  @ResponseBody
  public AttackResult completed(@RequestParam String answer_pwd1) {
    if (answer_pwd1 != null && answer_pwd1.equals("databasepassword")) {
      return success(this).feedback("crypto-encoding-xor.success").build();
    }
    return failed(this).feedback("crypto-encoding-xor.empty").build();
  }
}
