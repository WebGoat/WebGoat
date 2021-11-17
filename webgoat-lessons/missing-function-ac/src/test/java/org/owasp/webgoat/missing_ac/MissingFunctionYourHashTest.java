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

package org.owasp.webgoat.missing_ac;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.plugins.LessonTest;
import org.owasp.webgoat.users.WebGoatUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.owasp.webgoat.missing_ac.MissingFunctionAC.PASSWORD_SALT_SIMPLE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MissingFunctionYourHashTest extends LessonTest {
    @Autowired
    private MissingFunctionAC ac;

    @BeforeEach
    public void setup() {
        when(webSession.getCurrentLesson()).thenReturn(ac);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    void hashDoesNotMatch() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/access-control/user-hash")
                .param("userHash", "42"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
    }

    @Test
    void hashMatches() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/access-control/user-hash")
                .param("userHash", "SVtOlaa+ER+w2eoIIVE5/77umvhcsh5V8UyDLUa1Itg="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(true)));
    }
}
