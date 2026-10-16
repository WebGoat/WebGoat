/*
 * SPDX-FileCopyrightText: Copyright © 2018 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.sqlinjection.introduction;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.plugins.LessonTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class SqlInjectionLesson9Test extends LessonTest {

  private final String completedError = "JSON path \"lessonCompleted\"";

  @Test
  public void malformedQueryReturnsError() throws Exception {
    try {
      mockMvc
          .perform(
              MockMvcRequestBuilders.post("/SqlInjection/attack9")
                  .param("name", "Smith")
                  .param("auth_tan", "3SL99A' OR '1' = '1'"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("lessonCompleted", is(false)))
          .andExpect(jsonPath("$.output", containsString("feedback-negative")));
    } catch (AssertionError e) {
      if (!e.getMessage().contains(completedError)) throw e;

      mockMvc
          .perform(
              MockMvcRequestBuilders.post("/SqlInjection/attack9")
                  .param("name", "Smith")
                  .param("auth_tan", "3SL99A' OR '1' = '1'"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("lessonCompleted", is(true)))
          .andExpect(jsonPath("$.feedback", is(messages.getMessage("sql-injection.9.success"))))
          .andExpect(jsonPath("$.output", containsString("feedback-negative")));
    }
  }

  @Test
  public void SmithIsNotMostEarning() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/SqlInjection/attack9")
                .param("name", "Smith")
                .param(
                    "auth_tan",
                    "3SL99A'; UPDATE employees SET salary = 9999 WHERE last_name = 'Smith"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("lessonCompleted", is(false)))
        .andExpect(jsonPath("$.feedback", is(messages.getMessage("sql-injection.9.one"))));
  }

  @Test
  public void OnlySmithSalaryMustBeUpdated() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/SqlInjection/attack9")
                .param("name", "Smith")
                .param("auth_tan", "3SL99A'; UPDATE employees SET salary = 9999 -- "))
        .andExpect(status().isOk())
        .andExpect(jsonPath("lessonCompleted", is(false)))
        .andExpect(jsonPath("$.feedback", is(messages.getMessage("sql-injection.9.one"))));
  }

  @Test
  public void OnlySmithMustMostEarning() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/SqlInjection/attack9")
                .param("name", "'; UPDATE employees SET salary = 999999 -- ")
                .param("auth_tan", ""))
        .andExpect(status().isOk())
        .andExpect(jsonPath("lessonCompleted", is(false)))
        .andExpect(jsonPath("$.feedback", is(messages.getMessage("sql-injection.9.one"))));
  }

  @Test
  public void SmithIsMostEarningCompletesAssignment() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/SqlInjection/attack9")
                .param("name", "Smith")
                .param(
                    "auth_tan",
                    "3SL99A'; UPDATE employees SET salary = '300000' WHERE last_name = 'Smith"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("lessonCompleted", is(true)))
        .andExpect(jsonPath("$.feedback", is(messages.getMessage("sql-injection.9.success"))))
        .andExpect(jsonPath("$.output", containsString("300000")));
  }
}
