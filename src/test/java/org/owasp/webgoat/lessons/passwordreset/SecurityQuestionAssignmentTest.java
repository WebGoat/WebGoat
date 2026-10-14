/*
 * SPDX-FileCopyrightText: Copyright © 2019 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.passwordreset;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.plugins.LessonTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class SecurityQuestionAssignmentTest extends LessonTest {

  private MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  }

  @Test
  public void oneQuestionShouldNotSolveTheAssignment() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/PasswordReset/SecurityQuestions")
                .param("question", "What is your favorite animal?"))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath(
                "$.feedback",
                CoreMatchers.is(messages.getMessage("password-questions-one-successful"))))
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)))
        .andExpect(jsonPath("$.output", CoreMatchers.notNullValue()));
  }

  @Test
  public void twoQuestionsShouldSolveTheAssignment() throws Exception {
    MockHttpSession mocksession = new MockHttpSession();
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/PasswordReset/SecurityQuestions")
                .param("question", "What is your favorite animal?")
                .session(mocksession))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/PasswordReset/SecurityQuestions")
                .param("question", "In what year was your mother born?")
                .session(mocksession))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("assignment.solved"))))
        .andExpect(jsonPath("$.output", CoreMatchers.notNullValue()))
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));
  }

  @Test
  public void answeringSameQuestionTwiceShouldNotSolveAssignment() throws Exception {
    MockHttpSession mocksession = new MockHttpSession();
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/PasswordReset/SecurityQuestions")
                .param("question", "What is your favorite animal?")
                .session(mocksession))
        .andExpect(status().isOk());
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/PasswordReset/SecurityQuestions")
                .param("question", "What is your favorite animal?")
                .session(mocksession))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath(
                "$.feedback",
                CoreMatchers.is(messages.getMessage("password-questions-one-successful"))))
        .andExpect(jsonPath("$.output", CoreMatchers.notNullValue()))
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
  }

  @Test
  public void solvingForOneUserDoesNotSolveForOtherUser() throws Exception {
    MockHttpSession mocksession = new MockHttpSession();
    mockMvc.perform(
        MockMvcRequestBuilders.post("/PasswordReset/SecurityQuestions")
            .param("question", "What is your favorite animal?")
            .session(mocksession));
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/PasswordReset/SecurityQuestions")
                .param("question", "In what year was your mother born?")
                .session(mocksession))
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));

    MockHttpSession mocksession2 = new MockHttpSession();
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/PasswordReset/SecurityQuestions")
                .param("question", "What is your favorite animal?")
                .session(mocksession2))
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
  }
}
