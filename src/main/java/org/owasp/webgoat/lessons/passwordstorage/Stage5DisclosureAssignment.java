/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.passwordstorage;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AssignmentHints({"password-storage.stage5.hint1", "password-storage.stage5.hint2"})
public class Stage5DisclosureAssignment implements AssignmentEndpoint {

  @PostMapping("/PasswordStorage/stage5")
  public AttackResult submit(@RequestParam(defaultValue = "") String password) {
    if (PasswordStorageService.AUTHOR_PASSWORD.equals(password)) {
      return success(this).feedback("password-storage.stage5.success").build();
    }
    return failed(this).feedback("password-storage.stage5.failure").build();
  }
}
