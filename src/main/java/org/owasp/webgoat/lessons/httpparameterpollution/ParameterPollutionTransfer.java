/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.httpparameterpollution;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import jakarta.servlet.http.HttpServletRequest;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AssignmentHints({
  "http-parameter-pollution.hints.transfer.1",
  "http-parameter-pollution.hints.transfer.2",
  "http-parameter-pollution.hints.transfer.3",
  "http-parameter-pollution.hints.transfer.4"
})
public class ParameterPollutionTransfer implements AssignmentEndpoint {

  @PostMapping("/HttpParameterPollution/transfer")
  @ResponseBody
  public AttackResult transfer(HttpServletRequest request) {
    String[] recipients = request.getParameterValues("recipient");
    if (recipients == null || recipients.length == 0 || recipients[0].isBlank()) {
      return failed(this)
          .feedback("http-parameter-pollution.transfer.missing-recipient")
          .build();
    }

    Integer amount = readAmount(request.getParameterValues("amount"));
    if (amount == null) {
      return failed(this).feedback("http-parameter-pollution.transfer.invalid-amount").build();
    }

    String validatedRecipient = recipients[0];
    String actualRecipient = recipients[recipients.length - 1];

    if (!"alice".equals(validatedRecipient)) {
      return failed(this).feedback("http-parameter-pollution.transfer.unauthorized").build();
    }
    if ("attacker".equals(actualRecipient) && amount == 100) {
      return success(this).feedback("http-parameter-pollution.transfer.success").build();
    }
    if ("alice".equals(actualRecipient) && amount == 100) {
      return failed(this).feedback("http-parameter-pollution.transfer.allowed").build();
    }
    return failed(this).feedback("http-parameter-pollution.transfer.invalid-recipient").build();
  }

  private Integer readAmount(String[] amounts) {
    if (amounts == null || amounts.length != 1) {
      return null;
    }
    try {
      return Integer.valueOf(amounts[0]);
    } catch (NumberFormatException e) {
      return null;
    }
  }
}
