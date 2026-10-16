/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.passwordstorage;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PasswordStorageDataController {

  private final PasswordStorageService passwordStorageService;

  public PasswordStorageDataController(PasswordStorageService passwordStorageService) {
    this.passwordStorageService = passwordStorageService;
  }

  @GetMapping(value = "/PasswordStorage/api/stage1", produces = MediaType.APPLICATION_JSON_VALUE)
  public Map<String, Object> stage1() {
    return Map.of(
        "scheme", "SHA-256(UTF-8(password))",
        "records", passwordStorageService.stage1Records(),
        "wordlist", PasswordStorageService.WORDLIST);
  }

  @GetMapping(value = "/PasswordStorage/api/stage2", produces = MediaType.APPLICATION_JSON_VALUE)
  public Map<String, Object> stage2() {
    return Map.of(
        "scheme", "SHA-256(UTF-8(password) || saltBytes)",
        "records", passwordStorageService.stage2Records(),
        "wordlist", PasswordStorageService.WORDLIST);
  }

  @GetMapping(value = "/PasswordStorage/api/stage3", produces = MediaType.APPLICATION_JSON_VALUE)
  public PasswordStorageService.ArgonRecord stage3() {
    return passwordStorageService.argonRecord();
  }

  @GetMapping(value = "/PasswordStorage/api/stage4", produces = MediaType.APPLICATION_JSON_VALUE)
  public PasswordStorageService.PepperedRecord stage4() {
    return passwordStorageService.pepperedRecord();
  }

  @GetMapping(
      value = "/PasswordStorage/blog-backup/application.properties",
      produces = MediaType.TEXT_PLAIN_VALUE)
  public String disclosedConfiguration() {
    return "blog.password.pepper="
        + PasswordStorageService.PEPPER
        + "\nblog.datasource.url=jdbc:hsqldb:mem:blog\n";
  }

  @GetMapping(
      value = "/PasswordStorage/api/stage5/crack",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Map<String, Object> crack(@RequestParam(defaultValue = "") String pepper) {
    PasswordStorageService.CrackResult result = passwordStorageService.crackWithPepper(pepper);
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("candidatesTested", result.candidatesTested());
    result.password().ifPresentOrElse(
        password -> {
          response.put("matched", true);
          response.put("password", password);
        },
        () -> {
          response.put("matched", false);
          response.put("message", "The supplied pepper cannot verify any training candidate.");
        });
    return response;
  }
}
