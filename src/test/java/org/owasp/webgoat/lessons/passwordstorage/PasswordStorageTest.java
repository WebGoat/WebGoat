/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.passwordstorage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.plugins.LessonTest;
import org.springframework.beans.factory.annotation.Autowired;

class PasswordStorageTest extends LessonTest {

  @Autowired private PasswordStorageService passwordStorageService;

  @Test
  void lessonPageRendersTheIntroductionAndAllFiveStages() throws Exception {
    mockMvc
        .perform(get("/PasswordStorage.lesson"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Passwords are difficult to store safely")))
        .andExpect(content().string(containsString("Stage 1")))
        .andExpect(content().string(containsString("Stage 5")));
  }

  @Test
  void unsaltedHashesRevealPasswordEquality() {
    var records = passwordStorageService.stage1Records();

    assertThat(records.get(0).hash()).isEqualTo(records.get(1).hash());
    assertThat(records.get(2).hash())
        .isEqualTo(PasswordStorageService.sha256(PasswordStorageService.AUTHOR_PASSWORD));
  }

  @Test
  void uniqueSaltsHidePasswordEquality() {
    var records = passwordStorageService.stage2Records();

    assertThat(records.get(0).salt()).isNotEqualTo(records.get(1).salt());
    assertThat(records.get(0).hash()).isNotEqualTo(records.get(1).hash());
  }

  @Test
  void stage1AcceptsOnlyTheRecoveredPassword() throws Exception {
    mockMvc
        .perform(post("/PasswordStorage/stage1").param("password", "wrong"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted").value(false));

    mockMvc
        .perform(
            post("/PasswordStorage/stage1")
                .param("password", PasswordStorageService.AUTHOR_PASSWORD))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted").value(true));
  }

  @Test
  void stage2RequiresTheSaltInferenceAndPassword() throws Exception {
    mockMvc
        .perform(
            post("/PasswordStorage/stage2")
                .param("inference", "different-passwords")
                .param("password", PasswordStorageService.AUTHOR_PASSWORD))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted").value(false));

    mockMvc
        .perform(
            post("/PasswordStorage/stage2")
                .param("inference", "different-salts-hide-equality")
                .param("password", PasswordStorageService.AUTHOR_PASSWORD))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted").value(true));
  }

  @Test
  void stage3RequiresEveryPhcFieldAndTheCorrectCostComparison() throws Exception {
    mockMvc
        .perform(
            post("/PasswordStorage/stage3")
                .param("algorithm", "argon2id")
                .param("version", Integer.toString(PasswordStorageService.ARGON_VERSION))
                .param("memory", Integer.toString(PasswordStorageService.ARGON_MEMORY_KIB))
                .param("iterations", Integer.toString(PasswordStorageService.ARGON_ITERATIONS))
                .param("parallelism", Integer.toString(PasswordStorageService.ARGON_PARALLELISM))
                .param("salt", passwordStorageService.argonRecord().salt())
                .param("comparison", "argon2id-costs-more-per-guess"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted").value(true));
  }

  @Test
  void stage4RequiresTheMissingPepperExplanation() throws Exception {
    mockMvc
        .perform(
            post("/PasswordStorage/stage4")
                .param("databaseSufficient", "no")
                .param("missingValue", "pepper"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted").value(true));
  }

  @Test
  void configurationDisclosureExposesThePepper() throws Exception {
    mockMvc
        .perform(get("/PasswordStorage/blog-backup/application.properties"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("blog.password.pepper=" + PasswordStorageService.PEPPER)));
  }

  @Test
  void boundedAttackNeedsThePepperAndFindsTheTrainingPassword() throws Exception {
    mockMvc
        .perform(get("/PasswordStorage/api/stage5/crack").param("pepper", "wrong"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.matched").value(false))
        .andExpect(jsonPath("$.candidatesTested").value(0));

    mockMvc
        .perform(
            get("/PasswordStorage/api/stage5/crack").param("pepper", PasswordStorageService.PEPPER))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.matched").value(true))
        .andExpect(jsonPath("$.password").value(PasswordStorageService.AUTHOR_PASSWORD))
        .andExpect(jsonPath("$.candidatesTested").value(4));
  }

  @Test
  void stage5CompletesWithTheRecoveredPassword() throws Exception {
    mockMvc
        .perform(
            post("/PasswordStorage/stage5")
                .param("password", PasswordStorageService.AUTHOR_PASSWORD))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted").value(true));
  }
}
