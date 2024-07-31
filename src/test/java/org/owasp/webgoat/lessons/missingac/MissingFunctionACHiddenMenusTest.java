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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.owasp.webgoat.container.assignments.AssignmentEndpointTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(MockitoExtension.class)
public class MissingFunctionACHiddenMenusTest extends AssignmentEndpointTest {

  private MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    MissingFunctionACHiddenMenus hiddenMenus = new MissingFunctionACHiddenMenus();
    init(hiddenMenus);
    this.mockMvc = standaloneSetup(hiddenMenus).build();
  }

  @Test
  public void HiddenMenusSuccess() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/access-control/hidden-menu")
                .param("hiddenMenu1", "Users")
                .param("hiddenMenu2", "Config"))
        .andExpect(
            jsonPath(
                "$.feedback",
                CoreMatchers.is(pluginMessages.getMessage("access-control.hidden-menus.success"))))
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));
  }

  @Test
  public void HiddenMenusClose() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/access-control/hidden-menu")
                .param("hiddenMenu1", "Config")
                .param("hiddenMenu2", "Users"))
        .andExpect(
            jsonPath(
                "$.feedback",
                CoreMatchers.is(pluginMessages.getMessage("access-control.hidden-menus.close"))))
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
  }

  @Test
  public void HiddenMenusFailure() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/access-control/hidden-menu")
                .param("hiddenMenu1", "Foo")
                .param("hiddenMenu2", "Bar"))
        .andExpect(
            jsonPath(
                "$.feedback",
                CoreMatchers.is(pluginMessages.getMessage("access-control.hidden-menus.failure"))))
        .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
  }
}
