/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.httpproxies;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.owasp.webgoat.container.plugins.LessonTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(MockitoExtension.class)
public class HttpBasicsInterceptRequestTest extends LessonTest {

  @Test
  void success() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/HttpProxies/intercept-request")
                .header("x-request-intercepted", "true")
                .param("changeMe", "Requests are tampered easily"))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath(
                "$.feedback",
                CoreMatchers.is(messages.getMessage("http-proxies.intercept.success"))))
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));
  }

  @Test
  void failure() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/HttpProxies/intercept-request")
                .header("x-request-intercepted", "false")
                .param("changeMe", "Requests are tampered easily"))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath(
                "$.feedback",
                CoreMatchers.is(messages.getMessage("http-proxies.intercept.failure"))))
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
  }

  @Test
  void missingParam() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/HttpProxies/intercept-request")
                .header("x-request-intercepted", "false"))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath(
                "$.feedback",
                CoreMatchers.is(messages.getMessage("http-proxies.intercept.failure"))))
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
  }

  @Test
  void missingHeader() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/HttpProxies/intercept-request")
                .param("changeMe", "Requests are tampered easily"))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath(
                "$.feedback",
                CoreMatchers.is(messages.getMessage("http-proxies.intercept.failure"))))
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
  }

  @Test
  void whenPostAssignmentShouldNotPass() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/HttpProxies/intercept-request")
                .header("x-request-intercepted", "true")
                .param("changeMe", "Requests are tampered easily"))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath(
                "$.feedback",
                CoreMatchers.is(messages.getMessage("http-proxies.intercept.failure"))))
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
  }
}
