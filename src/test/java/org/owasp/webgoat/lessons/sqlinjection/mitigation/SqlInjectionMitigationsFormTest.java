/*
 * SPDX-FileCopyrightText: Copyright © 2026 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.sqlinjection.mitigation;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.jsoup.Jsoup;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.owasp.webgoat.container.plugins.LessonTest;

class SqlInjectionMitigationsFormTest extends LessonTest {

  @ParameterizedTest
  @CsvSource(
      delimiter = '|',
      value = {
        "userid_sql_only_input_validation|Smith';SELECT/**/*/**/from/**/user_system_data;--",
        "userid_sql_only_input_validation_on_keywords|Smith';SESELECTLECT/**/*/**/FRFROMOM/**/user_system_data;--"
      })
  void inputValidationFormsSubmitToWorkingEndpoints(String inputName, String solution)
      throws Exception {
    var response =
        mockMvc
            .perform(get("/WebGoat/SqlInjectionMitigations.lesson").contextPath("/WebGoat"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();
    var form =
        Jsoup.parse(response.getContentAsString())
            .selectFirst("form:has(input[name=" + inputName + "])");
    assertNotNull(form, "The lesson must contain the input validation form");
    assertEquals("POST", form.attr("method").toUpperCase(java.util.Locale.ROOT));

    mockMvc
        .perform(post(form.attr("action")).contextPath("/WebGoat").param(inputName, solution))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted", is(true)));
  }
}
