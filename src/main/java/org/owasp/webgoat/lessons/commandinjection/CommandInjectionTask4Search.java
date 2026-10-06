/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.commandinjection;

import org.owasp.webgoat.container.CurrentUser;
import org.owasp.webgoat.container.users.WebGoatUser;
import org.owasp.webgoat.lessons.commandinjection.CommandInjectionTask4Service.SearchResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class CommandInjectionTask4Search {

  private final CommandInjectionTask4Service service;
  private final CommandInjectionSafetyService safetyService;

  public CommandInjectionTask4Search(
      CommandInjectionTask4Service service, CommandInjectionSafetyService safetyService) {
    this.service = service;
    this.safetyService = safetyService;
  }

  @PostMapping(
      value = "/CommandInjection/task4/search",
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public SearchResponse search(
      @CurrentUser WebGoatUser user, @RequestParam("title") String title) {

    safetyService.requireAcknowledgement(user);
    if (title == null || title.isBlank()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "commandinjection.task4.failure.payload");
    }
    return service.search(user, title);
  }

}
