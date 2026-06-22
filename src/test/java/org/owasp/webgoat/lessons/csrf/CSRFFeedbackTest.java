/*
 * SPDX-FileCopyrightText: Copyright © 2017 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.csrf;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.servlet.http.Cookie;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.plugins.LessonTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class CSRFFeedbackTest extends LessonTest {

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  }

  @Test
  public void postingJsonMessageThroughWebGoatShouldWork() throws Exception {
    mockMvc
        .perform(
            post("/csrf/feedback/message")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"name\": \"Test\", \"email\": \"test1233@dfssdf.de\", \"subject\":"
                        + " \"service\", \"message\":\"dsaffd\"}"))
        .andExpect(status().isOk());
  }

  @Test
  public void csrfAttack() throws Exception {
    mockMvc
        .perform(
            post("/csrf/feedback/message")
                .contentType(MediaType.TEXT_PLAIN)
                .cookie(new Cookie("JSESSIONID", "test"))
                .header("host", "localhost:8080")
                .header("referer", "webgoat.org")
                .content(
                    "{\"name\": \"Test\", \"email\": \"test1233@dfssdf.de\", \"subject\":"
                        + " \"service\", \"message\":\"dsaffd\"}"))
        .andExpect(jsonPath("lessonCompleted", is(true)))
        .andExpect(jsonPath("feedback", StringContains.containsString("the flag is: ")));
  }
}
