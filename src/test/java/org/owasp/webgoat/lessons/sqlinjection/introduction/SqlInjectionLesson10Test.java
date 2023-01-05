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

package org.owasp.webgoat.lessons.sqlinjection.introduction;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.owasp.webgoat.lessons.sqlinjection.SqlLessonTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * @author Benedikt Stuhrmann
 * @since 11/07/18.
 */
public class SqlInjectionLesson10Test extends SqlLessonTest {

  private String completedError = "JSON path \"lessonCompleted\"";

  @Test
  public void tableExistsIsFailure() throws Exception {
    try {
      mockMvc
          .perform(MockMvcRequestBuilders.post("/SqlInjection/attack10").param("action_string", ""))
          .andExpect(status().isOk())
          .andExpect(jsonPath("lessonCompleted", is(false)))
          .andExpect(jsonPath("$.feedback", is(messages.getMessage("sql-injection.10.entries"))));
    } catch (AssertionError e) {
      if (!e.getMessage().contains(completedError)) throw e;

      mockMvc
          .perform(MockMvcRequestBuilders.post("/SqlInjection/attack10").param("action_string", ""))
          .andExpect(status().isOk())
          .andExpect(jsonPath("lessonCompleted", is(true)))
          .andExpect(jsonPath("$.feedback", is(messages.getMessage("sql-injection.10.success"))));
    }
  }

  @Test
  public void tableMissingIsSuccess() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/SqlInjection/attack10")
                .param("action_string", "%'; DROP TABLE access_log;--"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("lessonCompleted", is(true)))
        .andExpect(jsonPath("$.feedback", is(messages.getMessage("sql-injection.10.success"))));
  }
}
