/*
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 2021 Bruce Mayhew
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
 * Getting Source
 * ==============
 *
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software projects.
 */

package org.owasp.webgoat.lessons.xss;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.owasp.webgoat.container.assignments.AssignmentEndpointTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 *
 * @author Angel Olle Blazquez
 *
 */

@ExtendWith(MockitoExtension.class)
class CrossSiteScriptingLesson1Test extends AssignmentEndpointTest {

    private static final String CONTEXT_PATH = "/CrossSiteScripting/attack1";

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        CrossSiteScriptingLesson1 crossSiteScriptingLesson1 = new CrossSiteScriptingLesson1();
        init(crossSiteScriptingLesson1);
        mockMvc = standaloneSetup(crossSiteScriptingLesson1).build();
    }

    @Test
    void success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(CONTEXT_PATH)
            .param("checkboxAttack1", "value"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));
    }

    @Test
    void failure() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(CONTEXT_PATH))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
    }

}
