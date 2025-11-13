/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.commandinjection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.container.users.WebGoatUser;
import org.springframework.web.server.ResponseStatusException;

class CommandInjectionTask3Test {

  private CommandInjectionTask3Service service;
  private CommandInjectionTask3Search searchController;
  private CommandInjectionTask3Flag flagController;
  private WebGoatUser user;

  @BeforeEach
  void setUp() {
    service =
        new CommandInjectionTask3Service(
            "./target/webgoat-test",
            new CommandInjectionCatService(),
            new CommandExecutionService());
    searchController = new CommandInjectionTask3Search(service);
    flagController = new CommandInjectionTask3Flag(service);
    user = new WebGoatUser("alice", "password");
    service.initialize(user);
  }

  @Test
  void shouldFailWhenTitleMissing() {
    assertThatThrownBy(() -> searchController.search(user, ""))
        .isInstanceOf(ResponseStatusException.class)
        .hasMessageContaining("commandinjection.task3.failure.payload");
  }

  @Test
  void shouldFailWhenFlagMissing() {
    AttackResult result = flagController.submitFlag(user, "");
    assertThat(result.assignmentSolved()).isFalse();
    assertThat(result.getFeedback()).isEqualTo("commandinjection.task3.failure.blank");
  }

  @Test
  void shouldSucceedWhenFlagMatches() {
    CommandInjectionTask3Service.SearchResponse firstRun =
        searchController.search(user, injectionPayload());
    Matcher matcher =
        Pattern.compile(
                "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}")
            .matcher(firstRun.stdout());
    assertThat(matcher.find()).isTrue();
    String flag = matcher.group();

    AttackResult result = flagController.submitFlag(user, flag);

    assertThat(result.assignmentSolved()).isTrue();
    assertThat(result.getFeedback()).isEqualTo("commandinjection.task3.success");
  }

  private String injectionPayload() {
    boolean windows =
        System.getProperty("os.name", "").toLowerCase(Locale.US).contains("win");
    return windows ? "luna images/* & type flag.txt & rem" : "luna images/*; cat flag.txt; #";
  }
}
