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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.owasp.webgoat.sql_injection.SqlLessonTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author nbaars
 * @since 6/15/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class SqlInjectionLesson6aTest extends SqlLessonTest {

    @Test
    public void wrongSolution() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjectionAdvanced/attack6a")
                .param("userid_6a", "John"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonCompleted", is(false)));
    }

    @Test
    public void wrongNumberOfColumns() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjectionAdvanced/attack6a")
                .param("userid_6a", "Smith' union select userid,user_name, password,cookie from user_system_data --"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonCompleted", is(false)))
                .andExpect(jsonPath("$.output", containsString("column number mismatch detected in rows of UNION, INTERSECT, EXCEPT, or VALUES operation")));
    }

    @Test
    public void wrongDataTypeOfColumns() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjectionAdvanced/attack6a")
                .param("userid_6a", "Smith' union select 1,password, 1,'2','3', '4',1 from user_system_data --"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonCompleted", is(false)))
                .andExpect(jsonPath("$.output", containsString("incompatible data types in combination")));
    }

    @Test
    public void correctSolution() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjectionAdvanced/attack6a")
                .param("userid_6a", "Smith'; SELECT * from user_system_data; --"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonCompleted", is(true)))
                .andExpect(jsonPath("$.feedback", containsString("passW0rD")));
    }

    @Test
    public void noResultsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjectionAdvanced/attack6a")
                .param("userid_6a", "Smith' and 1 = 2 --"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonCompleted", is(false)))
                .andExpect(jsonPath("$.feedback", is(SqlInjectionLesson8Test.modifySpan(messages.getMessage("sql-injection.6a.no.results")))));
    }

    @Test
    public void noUnionUsed() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjectionAdvanced/attack6a")
                .param("userid_6a", "S'; Select * from user_system_data; --"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonCompleted", is(true)))
                .andExpect(jsonPath("$.feedback", containsString("UNION")));
    }
}