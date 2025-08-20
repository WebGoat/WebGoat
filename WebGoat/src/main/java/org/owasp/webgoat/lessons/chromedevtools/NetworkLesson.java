/*
 * SPDX-FileCopyrightText: Copyright © 2018 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.chromedevtools;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Assignment where the user has to look through an HTTP Request using the Developer Tools and find
 * a specific number.
 */
@RestController
@AssignmentHints({"networkHint1", "networkHint2"})
public class NetworkLesson implements AssignmentEndpoint {

  @PostMapping(
      value = "/ChromeDevTools/network",
      params = {"network_num", "number"})
  @ResponseBody
  public AttackResult completed(@RequestParam String network_num, @RequestParam String number) {
    if (network_num.equals(number)) {
      return success(this).feedback("network.success").output("").build();
    } else {
      return failed(this).feedback("network.failed").build();
    }
  }

  @PostMapping(path = "/ChromeDevTools/network", params = "networkNum")
  @ResponseBody
  public ResponseEntity<?> ok(@RequestParam String networkNum) {
    return ResponseEntity.ok().build();
  }
}
