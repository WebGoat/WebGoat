/*
 * SPDX-FileCopyrightText: Copyright © 2017 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.xxe;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.WithWebGoatUser;
import org.owasp.webgoat.container.plugins.LessonTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@WithWebGoatUser
class ContentTypeAssignmentTest extends LessonTest {

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  }

  @Test
  void sendingXmlButContentTypeIsJson() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/xxe/content-type")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "<?xml version=\"1.0\" standalone=\"yes\" ?><!DOCTYPE user [<!ENTITY root"
                        + " SYSTEM \"file:///\"> ]><comment><text>&root;</text></comment>"))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath(
                "$.feedback",
                CoreMatchers.is(messages.getMessage("xxe.content.type.feedback.json"))));
  }

  @Test
  void workingAttack() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/xxe/content-type")
                .contentType(MediaType.APPLICATION_XML)
                .content(
                    "<?xml version=\"1.0\" standalone=\"yes\" ?><!DOCTYPE user [<!ENTITY root"
                        + " SYSTEM \"file:///\"> ]><comment><text>&root;</text></comment>"))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("assignment.solved"))));
  }

  @Test
  void postingJsonShouldAddComment() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/xxe/content-type")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{  \"text\" : \"Hello World\"}"))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath(
                "$.feedback",
                CoreMatchers.is(messages.getMessage("xxe.content.type.feedback.json"))));

    mockMvc
        .perform(get("/xxe/comments").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[*].text").value(Matchers.hasItem("Hello World")));
  }

  private int countComments() throws Exception {
    var response =
        mockMvc
            .perform(get("/xxe/comments").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
    return new ObjectMapper().reader().readTree(response.getResponse().getContentAsString()).size();
  }

  @Test
  void postingInvalidJsonShouldNotAddComment() throws Exception {
    var numberOfComments = countComments();
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/xxe/content-type")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{  'text' : 'Wrong'"))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath(
                "$.feedback",
                CoreMatchers.is(messages.getMessage("xxe.content.type.feedback.json"))));

    mockMvc
        .perform(get("/xxe/comments").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        // .andDo(MockMvcResultHandlers.print())
        .andExpect(jsonPath("$.[*]").value(Matchers.hasSize(numberOfComments)));
  }
}
