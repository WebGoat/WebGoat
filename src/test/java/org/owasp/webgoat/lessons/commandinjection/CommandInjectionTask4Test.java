/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.commandinjection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.container.users.WebGoatUser;
import org.springframework.web.server.ResponseStatusException;

class CommandInjectionTask4Test {

  private CommandInjectionTask4Service service;
  private CommandInjectionTask4Search searchController;
  private CommandInjectionTask4Key keyController;
  private WebGoatUser user;

  @BeforeEach
  void setUp() {
    service =
        new CommandInjectionTask4Service(
            "./target/webgoat-test",
            new CommandInjectionCatService(),
            new CommandExecutionService());
    searchController = new CommandInjectionTask4Search(service);
    keyController = new CommandInjectionTask4Key(service);
    user = new WebGoatUser("bob", "password");
    service.initialize(user);
  }

  @Test
  void shouldRejectEmptyTitle() {
    assertThatThrownBy(() -> searchController.search(user, ""))
        .isInstanceOf(ResponseStatusException.class)
        .hasMessageContaining("commandinjection.task4.failure.payload");
  }

  @Test
  void shouldMentionFilterWhenBlacklistHit() {
    CommandInjectionTask4Service.SearchResponse response =
        searchController.search(user, "luna; cat api-key.txt");

    assertThat(response.console()).contains("[Filter] Removed characters");
    assertThat(response.command()).doesNotContain(";").doesNotContain("&&").doesNotContain("|");
  }

  @Test
  void shouldLeakApiKeyWithSubstitutionInjection() {
    CommandInjectionTask4Service.SearchResponse response =
        searchController.search(user, "$(cat api-key.txt >&2)");

    String apiKey = extractApiKey(response.console());
    AttackResult result = keyController.submitKey(user, apiKey);
    assertThat(result.assignmentSolved()).isTrue();
  }

  private String extractApiKey(String console) {
    return console
        .lines()
        .filter(line -> line.startsWith("API_KEY="))
        .findFirst()
        .orElseThrow();
  }
}
