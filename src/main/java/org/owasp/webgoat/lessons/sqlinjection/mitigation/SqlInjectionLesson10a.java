/*
 * SPDX-FileCopyrightText: Copyright © 2017 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.sqlinjection.mitigation;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import lombok.extern.slf4j.Slf4j;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@AssignmentHints(
    value = {"SqlStringInjectionHint-mitigation-10a-1", "SqlStringInjectionHint-mitigation-10a-2"})
public class SqlInjectionLesson10a implements AssignmentEndpoint {

  private static final String[] results = {
    "getConnection", "PreparedStatement", "prepareStatement", "?", "?", "setString", "setString"
  };

  @PostMapping("/SqlInjectionMitigations/attack10a")
  @ResponseBody
  public AttackResult completed(
      @RequestParam String field1,
      @RequestParam String field2,
      @RequestParam String field3,
      @RequestParam String field4,
      @RequestParam String field5,
      @RequestParam String field6,
      @RequestParam String field7) {
    String[] userInput = {field1, field2, field3, field4, field5, field6, field7};
    int position = 0;
    boolean completed = false;
    for (String input : userInput) {
      if (input.toLowerCase().contains(this.results[position].toLowerCase())) {
        completed = true;
      } else {
        return failed(this).build();
      }
      position++;
    }
    if (completed) {
      return success(this).build();
    }
    return failed(this).build();
  }
}
