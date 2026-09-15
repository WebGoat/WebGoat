/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.commandinjection;

import org.owasp.webgoat.container.CurrentUser;
import org.owasp.webgoat.container.lessons.Initializable;
import org.owasp.webgoat.container.users.WebGoatUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class CommandInjectionTask4Search implements Initializable {

  private final CommandInjectionTask4Service service;

  public CommandInjectionTask4Search(CommandInjectionTask4Service service) {
    this.service = service;
  }

  @PostMapping(
      value = "/CommandInjection/task4/search",
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public CommandInjectionTask4Service.SearchResponse search(
      @CurrentUser WebGoatUser user, @RequestParam("title") String title) {

    if (title == null || title.isBlank()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "commandinjection.task4.failure.payload");
    }
    return service.search(user, title);
  }

  @Override
  public void initialize(WebGoatUser user) {
    service.initialize(user);
  }
}
