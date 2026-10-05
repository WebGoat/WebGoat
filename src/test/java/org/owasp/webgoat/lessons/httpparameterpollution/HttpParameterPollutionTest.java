/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.httpparameterpollution;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.plugins.LessonTest;

class HttpParameterPollutionTest extends LessonTest {

  @Test
  void singleParameterShowsBothInterpretationsWithoutCompletingTheAssignment() throws Exception {
    mockMvc
        .perform(get("/HttpParameterPollution/parameters").param("name", "WebGoat"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted").value(false))
        .andExpect(jsonPath("$.output").value(containsString("request contains <strong>1")))
        .andExpect(jsonPath("$.output").value(containsString("WebGoat")))
        .andExpect(jsonPath("$.output").value(containsString("[WebGoat]")));
  }

  @Test
  void differentDuplicateParametersCompleteTheObservationAssignment() throws Exception {
    mockMvc
        .perform(
            get("/HttpParameterPollution/parameters")
                .param("name", "WebGoat")
                .param("name", "OWASP"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted").value(true))
        .andExpect(jsonPath("$.output").value(containsString("request contains <strong>2")))
        .andExpect(jsonPath("$.output").value(containsString("one value reads: <code>WebGoat")))
        .andExpect(jsonPath("$.output").value(containsString("[WebGoat, OWASP]")));
  }

  @Test
  void equalDuplicateParametersDoNotCompleteTheObservationAssignment() throws Exception {
    mockMvc
        .perform(
            get("/HttpParameterPollution/parameters")
                .param("name", "WebGoat")
                .param("name", "WebGoat"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted").value(false));
  }

  @Test
  void allowedTransferDoesNotCompleteTheExploitAssignment() throws Exception {
    mockMvc
        .perform(
            post("/HttpParameterPollution/transfer")
                .param("recipient", "alice")
                .param("amount", "100"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted").value(false))
        .andExpect(jsonPath("$.feedback").value(containsString("transfer to alice succeeded")));
  }

  @Test
  void directTransferToAttackerIsBlocked() throws Exception {
    mockMvc
        .perform(
            post("/HttpParameterPollution/transfer")
                .param("recipient", "attacker")
                .param("amount", "100"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted").value(false));
  }

  @Test
  void duplicateRecipientCanExploitTheInterpretationMismatch() throws Exception {
    mockMvc
        .perform(
            post("/HttpParameterPollution/transfer")
                .param("recipient", "alice")
                .param("recipient", "attacker")
                .param("amount", "100"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted").value(true));
  }

  @Test
  void reverseRecipientOrderFailsValidation() throws Exception {
    mockMvc
        .perform(
            post("/HttpParameterPollution/transfer")
                .param("recipient", "attacker")
                .param("recipient", "alice")
                .param("amount", "100"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted").value(false));
  }

  @Test
  void lastOfThreeRecipientValuesControlsTheTransfer() throws Exception {
    mockMvc
        .perform(
            post("/HttpParameterPollution/transfer")
                .param("recipient", "alice")
                .param("recipient", "attacker")
                .param("recipient", "bob")
                .param("amount", "100"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted").value(false));

    mockMvc
        .perform(
            post("/HttpParameterPollution/transfer")
                .param("recipient", "alice")
                .param("recipient", "bob")
                .param("recipient", "attacker")
                .param("amount", "100"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted").value(true));
  }

  @Test
  void missingRecipientFailsCleanly() throws Exception {
    mockMvc
        .perform(post("/HttpParameterPollution/transfer").param("amount", "100"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted").value(false));
  }

  @Test
  void missingAmountFailsCleanly() throws Exception {
    mockMvc
        .perform(post("/HttpParameterPollution/transfer").param("recipient", "alice"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted").value(false));
  }

  @Test
  void duplicateAmountFailsCleanly() throws Exception {
    mockMvc
        .perform(
            post("/HttpParameterPollution/transfer")
                .param("recipient", "alice")
                .param("recipient", "attacker")
                .param("amount", "100")
                .param("amount", "100"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted").value(false));
  }

  @Test
  void mitigationAcceptsExactlyOneRecipient() {
    assertEquals("alice", ParameterPollutionMitigation.requireSingleValue(new String[] {"alice"}));
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(
            () ->
                ParameterPollutionMitigation.requireSingleValue(
                    new String[] {"alice", "attacker"}));
  }

}
