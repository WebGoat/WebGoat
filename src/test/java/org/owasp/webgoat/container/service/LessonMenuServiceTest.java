/*
 * SPDX-FileCopyrightText: Copyright © 2017 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mock.Strictness.LENIENT;
import static org.mockito.Mockito.when;
import static org.owasp.webgoat.container.service.LessonMenuService.URL_LESSONMENU_MVC;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.Arrays;
import java.util.List;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.owasp.webgoat.container.lessons.Category;
import org.owasp.webgoat.container.lessons.Lesson;
import org.owasp.webgoat.container.session.Course;
import org.owasp.webgoat.container.users.LessonProgress;
import org.owasp.webgoat.container.users.UserProgress;
import org.owasp.webgoat.container.users.UserProgressRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(MockitoExtension.class)
public class LessonMenuServiceTest {

  @Mock(strictness = LENIENT)
  private LessonProgress lessonTracker;

  @Mock private Course course;
  @Mock private UserProgress userTracker;
  @Mock private UserProgressRepository userTrackerRepository;
  private MockMvc mockMvc;

  @BeforeEach
  void setup() {
    this.mockMvc =
        standaloneSetup(
                new LessonMenuService(
                    course, userTrackerRepository, Arrays.asList("none"), Arrays.asList("none")))
            .build();
  }

  @Test
  void lessonsShouldBeOrdered() throws Exception {
    Lesson l1 = Mockito.mock(Lesson.class);
    Lesson l2 = Mockito.mock(Lesson.class);
    when(l1.getTitle()).thenReturn("ZA");
    when(l2.getTitle()).thenReturn("AA");
    when(lessonTracker.isLessonSolved()).thenReturn(false);
    when(course.getLessons(any())).thenReturn(List.of(l1, l2));
    when(course.getCategories()).thenReturn(List.of(Category.A1));
    when(userTracker.getLessonProgress(any(Lesson.class))).thenReturn(lessonTracker);
    when(userTrackerRepository.findByUser(any())).thenReturn(userTracker);

    mockMvc
        .perform(MockMvcRequestBuilders.get(URL_LESSONMENU_MVC))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].children[0].name", CoreMatchers.is("AA")))
        .andExpect(jsonPath("$[0].children[1].name", CoreMatchers.is("ZA")));
  }

  @Test
  void lessonCompleted() throws Exception {
    Lesson l1 = Mockito.mock(Lesson.class);
    when(l1.getTitle()).thenReturn("ZA");
    when(lessonTracker.isLessonSolved()).thenReturn(true);
    when(course.getLessons(any())).thenReturn(List.of(l1));
    when(course.getCategories()).thenReturn(List.of(Category.A1));
    when(userTracker.getLessonProgress(any(Lesson.class))).thenReturn(lessonTracker);
    when(userTrackerRepository.findByUser(any())).thenReturn(userTracker);

    mockMvc
        .perform(MockMvcRequestBuilders.get(URL_LESSONMENU_MVC))
        .andExpect(status().isOk()) // .andDo(print())
        .andExpect(jsonPath("$[0].children[0].complete", CoreMatchers.is(true)));
  }
}
