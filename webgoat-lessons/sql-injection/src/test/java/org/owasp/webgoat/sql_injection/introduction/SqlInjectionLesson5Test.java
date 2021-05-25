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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.owasp.webgoat.LessonDataSource;
import org.owasp.webgoat.sql_injection.SqlLessonTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.SQLException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
public class SqlInjectionLesson5Test extends SqlLessonTest {

    @Autowired
    private LessonDataSource dataSource;

    @AfterEach
    public void removeGrant() throws SQLException {
        dataSource.getConnection().prepareStatement("revoke select on grant_rights from unauthorized_user cascade").execute();
    }

    @Test
    public void grantSolution() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack5")
                .param("query", "grant select on grant_rights to unauthorized_user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));
    }

    @Test
    public void differentTableShouldNotSolveIt() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack5")
                .param("query", "grant select on users to unauthorized_user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
    }

    @Test
    public void noGrantShouldNotSolveIt() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack5")
                .param("query", "select * from grant_rights"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
    }
}