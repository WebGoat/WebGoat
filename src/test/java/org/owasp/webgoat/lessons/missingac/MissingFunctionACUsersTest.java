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

package org.owasp.webgoat.lessons.missingac;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.plugins.LessonTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class MissingFunctionACUsersTest extends LessonTest {

  @BeforeEach
  void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  }

  @Test
  void getUsers() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/access-control/users")
                .header("Content-type", "application/json"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].username", CoreMatchers.is("Tom")))
        .andExpect(
            jsonPath(
                "$[0].userHash", CoreMatchers.is("Mydnhcy00j2b0m6SjmPz6PUxF9WIeO7tzm665GiZWCo=")))
        .andExpect(jsonPath("$[0].admin", CoreMatchers.is(false)));
  }

  @Test
  void addUser() throws Exception {
    var user =
        """
                {"username":"newUser","password":"newUser12","admin": "true"}
                """;
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/access-control/users")
                .header("Content-type", "application/json")
                .content(user))
        .andExpect(status().isOk());

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/access-control/users")
                .header("Content-type", "application/json"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()", is(4)));
  }
}
