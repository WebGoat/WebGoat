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
@AssignmentHints({"password-storage.stage3.hint1", "password-storage.stage3.hint2"})
public class Stage3Argon2Assignment implements AssignmentEndpoint {

  private final PasswordStorageService passwordStorageService;

  public Stage3Argon2Assignment(PasswordStorageService passwordStorageService) {
    this.passwordStorageService = passwordStorageService;
  }

  @PostMapping("/PasswordStorage/stage3")
  public AttackResult submit(
      @RequestParam(defaultValue = "") String algorithm,
      @RequestParam(defaultValue = "-1") int version,
      @RequestParam(defaultValue = "-1") int memory,
      @RequestParam(defaultValue = "-1") int iterations,
      @RequestParam(defaultValue = "-1") int parallelism,
      @RequestParam(defaultValue = "") String salt,
      @RequestParam(defaultValue = "") String comparison) {
    boolean parametersCorrect =
        "argon2id".equalsIgnoreCase(algorithm)
            && version == PasswordStorageService.ARGON_VERSION
            && memory == PasswordStorageService.ARGON_MEMORY_KIB
            && iterations == PasswordStorageService.ARGON_ITERATIONS
            && parallelism == PasswordStorageService.ARGON_PARALLELISM
            && passwordStorageService.argonRecord().salt().equals(salt);
    if (parametersCorrect && "argon2id-costs-more-per-guess".equals(comparison)) {
      return success(this).feedback("password-storage.stage3.success").build();
    }
    return failed(this).feedback("password-storage.stage3.failure").build();
  }
}
