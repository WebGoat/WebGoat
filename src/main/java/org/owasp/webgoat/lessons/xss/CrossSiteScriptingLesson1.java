/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.xss;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CrossSiteScriptingLesson1 implements AssignmentEndpoint {

  @PostMapping("/CrossSiteScripting/attack1")
  @ResponseBody
  public AttackResult completed(
      @RequestParam(value = "checkboxAttack1", required = false) String checkboxValue) {
    if (checkboxValue != null) {
      return success(this).build();
    } else {
      return failed(this).feedback("xss.lesson1.failure").build();
    }
  }
}
