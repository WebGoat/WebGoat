/*
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 2019 Bruce Mayhew
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * Getting Source ==============
 *
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software projects.
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
