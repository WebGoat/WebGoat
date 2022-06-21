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

package org.owasp.webgoat.lessons.sql_injection.introduction;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.owasp.webgoat.lessons.sql_injection.SqlLessonTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Benedikt Stuhrmann
 * @since 11/07/18.
 */
public class SqlInjectionLesson9Test extends SqlLessonTest {

    private String completedError = "JSON path \"lessonCompleted\"";

    @Test
    public void oneAccount() throws Exception {
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack9")
                    .param("name", "Smith")
                    .param("auth_tan", "3SL99A"))

                    .andExpect(status().isOk())
                    .andExpect(jsonPath("lessonCompleted", is(false)))
                    .andExpect(jsonPath("$.feedback", is(messages.getMessage("sql-injection.9.one"))))
                    .andExpect(jsonPath("$.output", containsString("<table><tr><th>")));
        } catch (AssertionError e) {
            if (!e.getMessage().contains(completedError)) throw e;

            mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack9")
                    .param("name", "Smith")
                    .param("auth_tan", "3SL99A"))

                    .andExpect(status().isOk())
                    .andExpect(jsonPath("lessonCompleted", is(true)))
                    .andExpect(jsonPath("$.feedback", is(messages.getMessage("sql-injection.9.success"))))
                    .andExpect(jsonPath("$.output", containsString("<table><tr><th>")));
        }
    }

    @Test
    public void multipleAccounts() throws Exception {
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack9")
                    .param("name", "Smith")
                    .param("auth_tan", "3SL99A' OR '1' = '1"))

                    .andExpect(status().isOk())
                    .andExpect(jsonPath("lessonCompleted", is(false)))
                    .andExpect(jsonPath("$.feedback", is(messages.getMessage("sql-injection.9.one"))))
                    .andExpect(jsonPath("$.output", containsString("<tr><td>96134<\\/td><td>Bob<\\/td><td>Franco<\\/td><td>Marketing<\\/td><td>83700<\\/td><td>LO9S2V<\\/td><\\/tr>")));
        } catch (AssertionError e) {
            if (!e.getMessage().contains(completedError)) throw e;

            mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack9")
                    .param("name", "Smith")
                    .param("auth_tan", "3SL99A' OR '1' = '1"))

                    .andExpect(status().isOk())
                    .andExpect(jsonPath("lessonCompleted", is(true)))
                    .andExpect(jsonPath("$.feedback", is(messages.getMessage("sql-injection.9.success"))))
                    .andExpect(jsonPath("$.output", containsString("<tr><td>96134<\\/td><td>Bob<\\/td><td>Franco<\\/td><td>Marketing<\\/td><td>83700<\\/td><td>LO9S2V<\\/td><\\/tr>")));
        }
    }

    @Test
    public void wrongNameReturnsNoAccounts() throws Exception {
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack9")
                    .param("name", "Smithh")
                    .param("auth_tan", "3SL99A"))

                    .andExpect(status().isOk())
                    .andExpect(jsonPath("lessonCompleted", is(false)))
                    .andExpect(jsonPath("$.feedback", is(messages.getMessage("sql-injection.8.no.results"))))
                    .andExpect(jsonPath("$.output").doesNotExist());
        } catch (AssertionError e) {
            if (!e.getMessage().contains(completedError)) throw e;

            mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack9")
                    .param("name", "Smithh")
                    .param("auth_tan", "3SL99A"))

                    .andExpect(status().isOk())
                    .andExpect(jsonPath("lessonCompleted", is(true)))
                    .andExpect(jsonPath("$.feedback", is(messages.getMessage("sql-injection.8.no.success"))))
                    .andExpect(jsonPath("$.output").doesNotExist());
        }
    }

    @Test
    public void wrongTANReturnsNoAccounts() throws Exception {
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack9")
                    .param("name", "Smithh")
                    .param("auth_tan", ""))

                    .andExpect(status().isOk())
                    .andExpect(jsonPath("lessonCompleted", is(false)))
                    .andExpect(jsonPath("$.feedback", is(messages.getMessage("sql-injection.8.no.results"))))
                    .andExpect(jsonPath("$.output").doesNotExist());
        } catch (AssertionError e) {
            if (!e.getMessage().contains(completedError)) throw e;

            mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack9")
                    .param("name", "Smithh")
                    .param("auth_tan", ""))

                    .andExpect(status().isOk())
                    .andExpect(jsonPath("lessonCompleted", is(true)))
                    .andExpect(jsonPath("$.feedback", is(messages.getMessage("sql-injection.9.success"))))
                    .andExpect(jsonPath("$.output").doesNotExist());
        }
    }

    @Test
    public void malformedQueryReturnsError() throws Exception {
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack9")
                    .param("name", "Smith")
                    .param("auth_tan", "3SL99A' OR '1' = '1'"))

                    .andExpect(status().isOk())
                    .andExpect(jsonPath("lessonCompleted", is(false)))
                    .andExpect(jsonPath("$.output", containsString("feedback-negative")));
        } catch (AssertionError e) {
            if (!e.getMessage().contains(completedError)) throw e;

            mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack9")
                    .param("name", "Smith")
                    .param("auth_tan", "3SL99A' OR '1' = '1'"))

                    .andExpect(status().isOk())
                    .andExpect(jsonPath("lessonCompleted", is(true)))
                    .andExpect(jsonPath("$.feedback", is(messages.getMessage("sql-injection.9.success"))))
                    .andExpect(jsonPath("$.output", containsString("feedback-negative")));
        }
    }

    @Test
    public void SmithIsMostEarningCompletesAssignment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/SqlInjection/attack9")
                .param("name", "Smith")
                .param("auth_tan", "3SL99A'; UPDATE employees SET salary = '300000' WHERE last_name = 'Smith"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("lessonCompleted", is(true)))
                .andExpect(jsonPath("$.feedback", is(messages.getMessage("sql-injection.9.success"))))
                .andExpect(jsonPath("$.output", containsString("300000")));
    }
}
