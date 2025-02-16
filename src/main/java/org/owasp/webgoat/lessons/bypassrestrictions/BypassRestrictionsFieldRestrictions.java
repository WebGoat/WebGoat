/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.bypassrestrictions;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BypassRestrictionsFieldRestrictions implements AssignmentEndpoint {

  @PostMapping("/BypassRestrictions/FieldRestrictions")
  @ResponseBody
  public AttackResult completed(
      @RequestParam String select,
      @RequestParam String radio,
      @RequestParam String checkbox,
      @RequestParam String shortInput,
      @RequestParam String readOnlyInput) {
    if (select.equals("option1") || select.equals("option2")) {
      return failed(this).build();
    }
    if (radio.equals("option1") || radio.equals("option2")) {
      return failed(this).build();
    }
    if (checkbox.equals("on") || checkbox.equals("off")) {
      return failed(this).build();
    }
    if (shortInput.length() <= 5) {
      return failed(this).build();
    }
    if ("change".equals(readOnlyInput)) {
      return failed(this).build();
    }
    return success(this).build();
  }
}
