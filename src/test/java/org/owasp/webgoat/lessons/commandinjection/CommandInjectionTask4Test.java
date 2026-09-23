/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.commandinjection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.container.users.WebGoatUser;
import org.owasp.webgoat.lessons.commandinjection.CommandInjectionTask4Service.SearchResponse;
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
    user = new WebGoatUser("bob", "password");
    var safetyService = new CommandInjectionSafetyService();
    safetyService.acknowledge(user);
    searchController = new CommandInjectionTask4Search(service, safetyService);
    keyController = new CommandInjectionTask4Key(service);
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
    SearchResponse response = searchController.search(user, "luna; cat api-key.txt");

    assertThat(response.console()).contains("[Filter] Removed characters");
    assertThat(response.command()).doesNotContain(";").doesNotContain("&&").doesNotContain("|");
  }

  @Test
  void shouldLeakApiKeyWithSubstitutionInjection() {
    boolean windows =
        System.getProperty("os.name", "").toLowerCase(Locale.US).contains("win");
    String payload =
        windows ? "luna images\\*.txt & type api-key.txt & rem" : "$(cat api-key.txt >&2)";
    SearchResponse response = searchController.search(user, payload);

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
