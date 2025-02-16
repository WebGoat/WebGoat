/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.service;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.owasp.webgoat.container.lessons.Assignment;
import org.owasp.webgoat.container.lessons.Lesson;
import org.owasp.webgoat.container.session.Course;
import org.owasp.webgoat.container.users.AssignmentProgress;
import org.owasp.webgoat.container.users.LessonProgress;
import org.owasp.webgoat.container.users.UserProgress;
import org.owasp.webgoat.container.users.UserProgressRepository;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class LessonProgressServiceTest {

  private MockMvc mockMvc;

  @Mock private Lesson lesson;
  @Mock private UserProgress userProgress;
  @Mock private LessonProgress lessonTracker;
  @Mock private UserProgressRepository userProgressRepository;
  @Mock private Course course;

  @BeforeEach
  void setup() {
    Assignment assignment = new Assignment("test", "test", List.of());
    AssignmentProgress assignmentProgress = new AssignmentProgress(assignment);
    when(userProgressRepository.findByUser(any())).thenReturn(userProgress);
    when(userProgress.getLessonProgress(any(Lesson.class))).thenReturn(lessonTracker);
    when(course.getLessonByName(any())).thenReturn(lesson);
    when(lessonTracker.getLessonOverview()).thenReturn(Maps.newHashMap(assignmentProgress, true));
    this.mockMvc =
        MockMvcBuilders.standaloneSetup(new LessonProgressService(userProgressRepository, course))
            .build();
  }

  @Test
  void jsonLessonOverview() throws Exception {
    this.mockMvc
        .perform(
            MockMvcRequestBuilders.get("/service/lessonoverview.mvc/test.lesson")
                .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].assignment.name", is("test")))
        .andExpect(jsonPath("$[0].solved", is(true)));
  }
}
