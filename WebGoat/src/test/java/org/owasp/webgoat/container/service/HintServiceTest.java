/*
 * SPDX-FileCopyrightText: Copyright © 2017 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.service;

import static org.owasp.webgoat.container.service.HintService.URL_HINTS_MVC;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.List;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.owasp.webgoat.container.lessons.Assignment;
import org.owasp.webgoat.container.lessons.Lesson;
import org.owasp.webgoat.container.session.Course;
import org.owasp.webgoat.lessons.httpbasics.HttpBasics;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(MockitoExtension.class)
public class HintServiceTest {

  private MockMvc mockMvc;

  @BeforeEach
  void setup() {
    Lesson lesson = new HttpBasics();
    lesson.addAssignment(
        new Assignment("test", "/HttpBasics/attack1", List.of("hint 1", "hint 2")));
    Course course = new Course(List.of(lesson));
    this.mockMvc = standaloneSetup(new HintService(course)).build();
  }

  @Test
  void hintsPerAssignment() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get(URL_HINTS_MVC))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].hint", CoreMatchers.is("hint 1")))
        .andExpect(jsonPath("$[0].assignmentPath", CoreMatchers.is("/HttpBasics/attack1")));
  }
}
