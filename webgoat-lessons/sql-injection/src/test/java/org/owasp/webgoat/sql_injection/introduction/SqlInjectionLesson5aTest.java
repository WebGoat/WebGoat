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

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.owasp.webgoat.sql_injection.SqlLessonTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
public class SqlInjectionLesson5aTest extends SqlLessonTest {

    @Test
    public void knownAccountShouldDisplayData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/assignment5a")
                .param("account", "Smith")
                .param("operator", "")
                .param("injection", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("lessonCompleted", is(false)))
                .andExpect(jsonPath("$.feedback", is(messages.getMessage("assignment.not.solved"))))
                .andExpect(jsonPath("$.output", containsString("<p>USERID, FIRST_NAME")));
    }

    @Disabled
    @Test
    public void unknownAccount() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/assignment5a")
                .param("account", "Smith")
                .param("operator", "").param("injection", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("lessonCompleted", is(false)))
                .andExpect(jsonPath("$.feedback", is(messages.getMessage("NoResultsMatched"))))
                .andExpect(jsonPath("$.output").doesNotExist());
    }

    @Test
    public void sqlInjection() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/assignment5a")
                .param("account", "'")
                .param("operator", "OR")
                .param("injection", "'1' = '1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("lessonCompleted", is(true)))
                .andExpect(jsonPath("$.feedback", containsString("You have succeed")))
                .andExpect(jsonPath("$.output").exists());
    }

    @Test
    public void sqlInjectionWrongShouldDisplayError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/assignment5a")
                .param("account", "Smith'")
                .param("operator", "OR")
                .param("injection", "'1' = '1'"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("lessonCompleted", is(false)))
                .andExpect(jsonPath("$.feedback", containsString(messages.getMessage("assignment.not.solved"))))
                .andExpect(jsonPath("$.output", is("malformed string: '1''<br> Your query was: SELECT * FROM user_data WHERE" +
                        " first_name = 'John' and last_name = 'Smith' OR '1' = '1''")));
    }
}