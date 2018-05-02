package org.owasp.webgoat.service;

import org.assertj.core.util.Maps;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.lessons.Assignment;
import org.owasp.webgoat.session.WebSession;
import org.owasp.webgoat.users.LessonTracker;
import org.owasp.webgoat.users.UserTracker;
import org.owasp.webgoat.users.UserTrackerRepository;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * ************************************************************************************************
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * <p>
 * Copyright (c) 2002 - 20014 Bruce Mayhew
 * <p>
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 * <p>
 * Getting Source ==============
 * <p>
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software
 * projects.
 * <p>
 *
 * @author nbaars
 * @version $Id: $Id
 * @since November 25, 2016
 */
@RunWith(MockitoJUnitRunner.class)
public class LessonProgressServiceTest {

    private MockMvc mockMvc;

    @Mock
    private AbstractLesson lesson;
    @Mock
    private UserTracker userTracker;
    @Mock
    private LessonTracker lessonTracker;
    @Mock
    private UserTrackerRepository userTrackerRepository;
    @Mock
    private WebSession websession;

    @Before
    public void setup() {
        Assignment assignment = new Assignment("test", "test");
        when(userTrackerRepository.findByUser(anyString())).thenReturn(userTracker);
        when(userTracker.getLessonTracker(any(AbstractLesson.class))).thenReturn(lessonTracker);
        when(websession.getCurrentLesson()).thenReturn(lesson);
        when(lessonTracker.getLessonOverview()).thenReturn(Maps.newHashMap(assignment, true));
        this.mockMvc = MockMvcBuilders.standaloneSetup(new LessonProgressService(userTrackerRepository, websession)).build();
    }

    @Test
    public void jsonLessonOverview() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/service/lessonoverview.mvc").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].assignment.name", is("test")))
                .andExpect(jsonPath("$[0].solved", is(true)));
    }

}