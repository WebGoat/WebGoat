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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.owasp.webgoat.assignments.AssignmentEndpointTest;
import org.owasp.webgoat.users.UserService;
import org.owasp.webgoat.users.WebGoatUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.Silent.class)
public class MissingFunctionYourHashTest extends AssignmentEndpointTest {
    private MockMvc mockMvc;
    private DisplayUser mockDisplayUser;

    @Mock
    protected UserService userService;

    @Before
    public void setUp() {
        MissingFunctionACYourHash yourHashTest = new MissingFunctionACYourHash();
        init(yourHashTest);
        this.mockMvc = standaloneSetup(yourHashTest).build();
        this.mockDisplayUser = new DisplayUser(new WebGoatUser("user", "userPass"));
        ReflectionTestUtils.setField(yourHashTest, "userService", userService);
        when(userService.loadUserByUsername(any())).thenReturn(new WebGoatUser("user", "userPass"));
    }

    @Test
    public void HashDoesNotMatch() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/access-control/user-hash")
                .param("userHash", "42"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.containsString("Keep trying, this one may take several attempts")))
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
    }

    @Test
    public void hashMatches() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/access-control/user-hash")
                .param("userHash", "2340928sadfajsdalsNfwrBla="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback", CoreMatchers.containsString("Keep trying, this one may take several attempts")))
                .andExpect(jsonPath("$.lessonCompleted", CoreMatchers.is(false)));
    }
}
