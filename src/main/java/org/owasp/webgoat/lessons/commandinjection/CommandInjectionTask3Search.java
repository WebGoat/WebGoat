/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.commandinjection;

import org.owasp.webgoat.container.CurrentUser;
import org.owasp.webgoat.container.users.WebGoatUser;
import org.owasp.webgoat.lessons.commandinjection.CommandInjectionTask3Service.SearchResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class CommandInjectionTask3Search {

  private final CommandInjectionTask3Service service;
  private final CommandInjectionSafetyService safetyService;

  public CommandInjectionTask3Search(
      CommandInjectionTask3Service service, CommandInjectionSafetyService safetyService) {
    this.service = service;
    this.safetyService = safetyService;
  }

  @PostMapping(
      value = "/CommandInjection/task3/search",
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public SearchResponse search(
      @CurrentUser WebGoatUser user, @RequestParam("title") String title) {

    safetyService.requireAcknowledgement(user);
    if (title == null || title.isBlank()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "commandinjection.task3.failure.payload");
    }
    return service.search(user, title);
  }

}
