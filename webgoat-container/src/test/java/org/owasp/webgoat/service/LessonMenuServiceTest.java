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
package org.owasp.webgoat.service;

import com.beust.jcommander.internal.Lists;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.lessons.Lesson;
import org.owasp.webgoat.session.Course;
import org.owasp.webgoat.session.WebSession;
import org.owasp.webgoat.users.LessonTracker;
import org.owasp.webgoat.users.UserTracker;
import org.owasp.webgoat.users.UserTrackerRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.owasp.webgoat.service.LessonMenuService.URL_LESSONMENU_MVC;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class LessonMenuServiceTest {

    @Mock(lenient=true)
    private LessonTracker lessonTracker;
    @Mock
    private Course course;
    @Mock
    private UserTracker userTracker;
    @Mock
    private UserTrackerRepository userTrackerRepository;
    @Mock
    private WebSession webSession;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = standaloneSetup(new LessonMenuService(course, webSession, userTrackerRepository)).build();
    }

    @Test
    public void lessonsShouldBeOrdered() throws Exception {
        Lesson l1 = Mockito.mock(Lesson.class);
        Lesson l2 = Mockito.mock(Lesson.class);
        when(l1.getTitle()).thenReturn("ZA");
        when(l2.getTitle()).thenReturn("AA");
        when(lessonTracker.isLessonSolved()).thenReturn(false);
        when(course.getLessons(any())).thenReturn(Lists.newArrayList(l1, l2));
        when(course.getCategories()).thenReturn(Lists.newArrayList(Category.ACCESS_CONTROL));
        when(userTracker.getLessonTracker(any(Lesson.class))).thenReturn(lessonTracker);
        when(userTrackerRepository.findByUser(any())).thenReturn(userTracker);

        mockMvc.perform(MockMvcRequestBuilders.get(URL_LESSONMENU_MVC))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].children[0].name", CoreMatchers.is("AA")))
                .andExpect(jsonPath("$[0].children[1].name", CoreMatchers.is("ZA")));
    }

    @Test
    public void lessonCompleted() throws Exception {
        Lesson l1 = Mockito.mock(Lesson.class);
        when(l1.getTitle()).thenReturn("ZA");
        when(lessonTracker.isLessonSolved()).thenReturn(true);
        when(course.getLessons(any())).thenReturn(Lists.newArrayList(l1));
        when(course.getCategories()).thenReturn(Lists.newArrayList(Category.ACCESS_CONTROL));
        when(userTracker.getLessonTracker(any(Lesson.class))).thenReturn(lessonTracker);
        when(userTrackerRepository.findByUser(any())).thenReturn(userTracker);

        mockMvc.perform(MockMvcRequestBuilders.get(URL_LESSONMENU_MVC))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$[0].children[0].complete", CoreMatchers.is(true)));
    }
}