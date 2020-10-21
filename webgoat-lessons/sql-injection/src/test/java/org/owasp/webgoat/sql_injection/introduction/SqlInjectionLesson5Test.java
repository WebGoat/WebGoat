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

package org.owasp.webgoat.sql_injection.introduction;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.owasp.webgoat.assignments.AssignmentEndpointTest;
import org.owasp.webgoat.sql_injection.SqlLessonTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringJUnit4ClassRunner.class)
public class SqlInjectionLesson5Test extends SqlLessonTest {

    @Test
    public void grantSolution() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack5")
                .param("query","grant alter table to unauthorizedUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.output", CoreMatchers.containsString("grant")))
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));
    }

    @Test
    public void grantSolutionWithSingleQuotes() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack5")
                .param("query","grant alter table to 'unauthorizedUser';"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.output", CoreMatchers.containsString("grant")))
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));
    }

    @Test
    public void grantSolutionWrong() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack5")
                .param("query","grant alter table to me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
    }
}